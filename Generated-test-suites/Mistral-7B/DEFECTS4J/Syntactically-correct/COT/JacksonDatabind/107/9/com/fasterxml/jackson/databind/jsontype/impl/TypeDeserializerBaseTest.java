package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private JsonDeserializer<Object> defaultImplDeserializer;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext context;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() throws IOException {
        typeIdResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        when(context.getDeserializationConfig()).thenReturn(new DeserializationConfig());
        when(context.getTypeFactory()).thenReturn(new SimpleTypeFactory());
        when(context.findContextualValueDeserializer(any(), any())).thenAnswer(invocation -> invocation.getArgument(1));

        defaultImplDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(defaultImplDeserializer);

        typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, "_propertyName", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };
    }

    @Test
    public void testToString() throws IOException {
        assertEquals("[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@1b6d748]",
                typeDeserializerBase.toString());
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializer result = typeDeserializerBase.forProperty(property);

        assertEquals(typeDeserializerBase, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testForPropertyWithoutProperty() throws IOException {
        typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetBaseType() {
        JavaType baseType = new SimpleType(Object.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "_propertyName", false, null);

        assertEquals(baseType, typeDeserializerBase.baseType());
    }

    @Test
    public void testGetBaseTypeNull() {
        typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, "_propertyName", false, null);

        assertNull(typeDeserializerBase.baseType());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "com.example.TestClass";
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);

        when(typeIdResolver.typeFromId(context, typeId)).thenReturn(new SimpleType(TestClass.class));
        when(context.findContextualValueDeserializer(any(), any())).thenReturn(deserializer);

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);

        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializerNull() throws IOException {
        String typeId = "com.example.TestClass";

        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);

        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        String typeId = "java.lang.Void";

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertEquals(NullifyingDeserializer.instance, result);
    }

    @Test
    public void testFindDefaultImplDeserializerNull() throws IOException {
        typeDeserializerBase._defaultImpl = null;

        JsonDeserializer<Object> result
                = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "com.example.TestClass";

        typeDeserializerBase._idResolver = null;

        typeDeserializerBase._handleUnknownTypeId(context, typeId);
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        String extraDesc = "Error description";

        typeDeserializerBase._idResolver = null;

        JavaType result = typeDeserializerBase._handleMissingTypeId(context, extraDesc);

        assertEquals(Object.class, result.getRawClass());
    }
}