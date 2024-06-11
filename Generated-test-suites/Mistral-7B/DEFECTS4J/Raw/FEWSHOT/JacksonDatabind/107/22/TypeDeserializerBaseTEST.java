package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ConcurrentHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

/**
 * Test cases for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest extends TestBase {

    @SuppressWarnings("unchecked")
    private static final TypeDeserializerBase TESTEE = new TestTypeDeserializerBase(Object.class, null, null, false, null);

    @Test
    public void testNewInstance() {
        assertEquals(Object.class, TESTEE.baseType());
        assertNull(TESTEE.property());
        assertNotNull(TESTEE.typeIdResolver());
        assertFalse(TESTEE.isTypeIdVisible());
        assertEquals(16, TESTEE.deserializers().size());
        assertNull(TESTEE.defaultImplDeserializer());
    }

    @Test
    public void testForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase newInstance = new TestTypeDeserializerBase(Object.class, null, null, false, null);
        TypeDeserializerBase copy = new TestTypeDeserializerBase(newInstance, property);
        assertEquals(newInstance.baseType(), copy.baseType());
        assertEquals(newInstance.typeIdResolver(), copy.typeIdResolver());
        assertEquals(newInstance.typePropertyName(), copy.typePropertyName());
        assertEquals(newInstance.isTypeIdVisible(), copy.isTypeIdVisible());
        assertNotSame(newInstance.deserializers(), copy.deserializers());
        assertSame(newInstance.property(), copy.property());
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, TESTEE.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", TESTEE.baseTypeName());
    }

    @Test
    public void testToString() {
        String expected = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:com.fasterxml.jackson.databind.util.DefaultTypeIdResolver@6d0f9f5b]";
        assertEquals(expected, TESTEE.toString());
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "TestType";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(context, typeId)).thenReturn(TestType.class);
        TypeDeserializerBase deserializerBase = spy(TESTEE); // for @Mockito.verify call
        when(deserializerBase.typeIdResolver()).thenReturn(idResolver);

        JsonDeserializer<Object> deser = deserializerBase._findDeserializer(context, typeId);
        verify(deserializerBase).typeIdResolver();
        assertEquals(TestType.class, deser.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        TypeDeserializerBase deserializerBase = spy(TESTEE); // for @Mockito.verify call
        when(deserializerBase.typeIdResolver()).thenReturn(mock(TypeIdResolver.class));

        JsonDeserializer<Object> deser = deserializerBase._findDefaultImplDeserializer(context);
        assertNull(deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "TestType";
        TypeDeserializerBase deserializerBase = spy(TESTEE); // for @Mockito.verify call
        when(deserializerBase.typeIdResolver()).thenReturn(mock(TypeIdResolver.class));

        JsonType actual = deserializerBase._handleUnknownTypeId(context, typeId);
        assertNull(actual);
        verify(context).handleUnknownTypeId(eq(TESTEE.baseType()), eq(typeId), any(), anyString());
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdThrowException() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        String typeId = "TestType";
        TypeDeserializerBase deserializerBase = spy(TESTEE); // for @Mockito.verify call
        when(deserializerBase.typeIdResolver()).thenReturn(mock(TypeIdResolver.class));

        TestType actual = deserializerBase._handleUnknownTypeId(context, typeId);
        fail("Should throw an IOException");
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        DeserializationContext context = mock(DeserializationContext.class);
        JsonParser parser = mock(JsonParser.class);
        Object typeId = "TestType";
        when(parser.getTypeId()).thenReturn(typeId);
        TypeDeserializerBase deserializerBase = spy(TESTEE); // for @Mockito.verify call
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);

        when(deserializerBase.typeIdResolver()).thenReturn(mock(TypeIdResolver.class));
        when(deserializerBase._findDeserializer(context, typeId.toString())).thenReturn(deser);

        Object result = deserializerBase._deserializeWithNativeTypeId(parser, context);
        verify(parser).nextToken();
        verify(deserializerBase).typeIdResolver();
        verify(deserializerBase)._findDeserializer(context, typeId.toString());
        verify(deser).deserialize(parser, context);
        assertEquals(deser.getDefaultValue(), result);
    }

    private static class TestTypeDeserializerBase extends TypeDeserializerBase {
        TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        }

        TestTypeDeserializerBase(TestTypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        protected TypeDeserializer forProperty(BeanProperty prop) {
            fail("Should not be called");
            return null;
        }
    }

    private static class TestType {
    }
}