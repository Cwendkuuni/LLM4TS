package com.fasterxml.jackson.databind.jsontype.impl; // package import

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class TypeDeserializerBaseTest {

    

    @org.junit.Before
    public void setUp() {
        // Setup mocks
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty property = mock(BeanProperty.class);
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));
        when(idResolver.getIdResolverBasis()).thenReturn(context);
        when(idResolver.typeFromId(context, "typeId")).thenReturn(baseType);
        when(baseType.getRawClass()).thenReturn(Object.class);
        when(baseType.getRawClass()).thenReturn(Map.class);
        when(idResolver.typeFromId(context, "anotherTypeId")).thenReturn(baseType);
    }

    @org.junit.Test
    public void testAbstractForProperty() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
        assertNull(typeDeserializerBase.forProperty(property));
    }

    @org.junit.Test
    public void testAbstractGetTypeInclusion() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @org.junit.Test
    public void testBaseTypeName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @org.junit.Test
    public void testGetPropertyName() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "name", false, null);
        assertEquals("name", typeDeserializerBase.getPropertyName());
    }

    @org.junit.Test
    public void testGetTypeIdResolver() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertSame(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @org.junit.Test
    public void testGetDefaultImpl() throws Exception {
        TypeDeserializerBase typeDeserializerBase1 = new TypeDeserializerBase(baseType, idResolver, null, false, Object.class);
        assertEquals(Object.class, typeDeserializerBase1.getDefaultImpl());

        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        assertNull(typeDeserializerBase2.getDefaultImpl());
    }

    @org.junit.Test
    public void testToString() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.Object; id-resolver:org.mockito.mockitools.Mockito$1";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @org.junit.Test
    public void testFindDeserializer() throws Exception {
        when(context.findContextualValueDeserializer(baseType, property)).thenReturn(NullifyingDeserializer.instance);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(context, "typeId");
        assertSame(NullifyingDeserializer.instance, deser);
    }

    @org.junit.Test
    public void testFindDefaultImplDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(deser);
    }

    @org.junit.Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(baseType, property)).thenReturn(mock(JsonDeserializer.class));

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(mock(JsonParser.class), context, "typeId");
        assertNotNull(result);
    }

    @org.junit.Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testHandleUnknownTypeId() throws Exception {
        when(idResolver.typeFromId(context, "unknownTypeId")).thenReturn(baseType);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(context, "unknownTypeId", idResolver, baseType);
    }

    
}