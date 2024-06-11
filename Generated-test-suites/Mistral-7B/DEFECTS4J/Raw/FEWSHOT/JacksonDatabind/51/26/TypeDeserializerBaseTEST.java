package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    private DeserializationContext ctxt;
    private TypeIdResolver idResolver;
    private JavaType baseType;
    private TypeDeserializerBase typeDeserializer;

    @Before
    public void setup() {
        idResolver = mock(TypeIdResolver.class);
        baseType = mock(JavaType.class);
        ctxt = mock(DeserializationContext.class);
        when(ctxt.getTypeFactory()).thenReturn(new StandardSerializer.Base());
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(ctxt.findContextualValueDeserializer(eq(_defaultImpl), any())).thenReturn(NullifyingDeserializer.instance);
        when(ctxt.getTypeIdResolver()).thenReturn(idResolver);

        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) { return this; }

            @Override
            public JsonTypeInfo.As getTypeInclusion() { return JsonTypeInfo.As.WRAPPER_OBJECT; }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc)
                    throws IOException { throw new RuntimeException("Unknown type id: " + typeId); }

            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException { return null; }
        };
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer forProperty = typeDeserializer.forProperty(prop);
        assertSame(typeDeserializer, forProperty);
    }

    @Test
    public void testGetTypeInclusion() {
        assertSame(JsonTypeInfo.As.WRAPPER_OBJECT, typeDeserializer.getTypeInclusion());
    }

    @Test(expected = RuntimeException.class)
    public void testHandleUnknownTypeId() throws IOException {
        typeDeserializer._handleUnknownTypeId(ctxt, "typeId", idResolver, "");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        assertNull(typeDeserializer._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "typeId";

        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(null);

        JsonDeserializer<Object> deser = typeDeserializer._findDeserializer(ctxt, typeId);
        assertNull(deser);

        when(idResolver.typeFromId(ctxt, typeId)).thenReturn(mock(JavaType.class));

        JavaType type = mock(JavaType.class);
        when(ctxt.getTypeFactory().constructSpecializedType(eq(_baseType), type.getRawClass())).thenReturn(type);
        when(ctxt.findContextualValueDeserializer(type, _property)).thenReturn(mock(JsonDeserializer.class));

        deser = typeDeserializer._findDeserializer(ctxt, typeId);

        verify(ctxt).findContextualValueDeserializer(type, _property);
        verifyNoMoreInteractions(ctxt);
        assertSame(mock(JsonDeserializer.class), deser);
    }
}