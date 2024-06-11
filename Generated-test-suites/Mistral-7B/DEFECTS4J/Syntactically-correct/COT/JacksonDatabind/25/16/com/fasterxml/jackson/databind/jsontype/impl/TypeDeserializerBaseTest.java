package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    

    @Test
    public void testTypeDeserializerBaseConstructor() {
        // Test constructor with required arguments
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        assertNotNull(typeDeserializerBase);

        // Test constructor with all arguments
        baseType = null;
        idRes = null;
        typePropertyName = null;
        typeIdVisible = false;
        defaultImpl = null;
        BeanProperty property = null;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, property);
        assertNotNull(typeDeserializerBase2);
    }

    @Test
    public void testForProperty() {
        // Test forProperty method with an empty BeanProperty argument
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        assertNull(typeDeserializerBase.forProperty(null));

        // Test forProperty method with a valid BeanProperty argument
        BeanProperty beanProperty = new BeanProperty(null, "testProperty");
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullBeanProperty() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        // Test getTypeInclusion method returning null
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        JsonTypeInfo.As typeInclude = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclude);

        // Test getTypeInclusion method returning AS_EXISTING_PROPERTY
        JsonTypeInfo.As expectedTypeInclude = JsonTypeInfo.As.EXISTING_PROPERTY;
        TypeIdResolver idRes = null;
        String typePropertyName = "testProperty";
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, idRes, typePropertyName, false, null);
        JsonTypeInfo.As actualTypeInclude = typeDeserializerBase2.getTypeInclusion();
        assertEquals(expectedTypeInclude, actualTypeInclude);
    }

    @Test
    public void testBaseTypeName() {
        // Test baseTypeName method with null baseType argument
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        String result = typeDeserializerBase.baseTypeName();
        assertNull(result);

        // Test baseTypeName method with a valid baseType argument
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        String expectedResult = "java.lang.Object";
        String result2 = typeDeserializerBase2.baseTypeName();
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testGetPropertyName() {
        // Test getPropertyName method returning null propertyName
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        String result = typeDeserializerBase.getPropertyName();
        assertNull(result);

        // Test getPropertyName method returning a valid propertyName
        String typePropertyName = "testProperty";
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, null, typePropertyName, false, null);
        String expectedResult = typePropertyName;
        String result2 = typeDeserializerBase2.getPropertyName();
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testGetTypeIdResolver() {
        // Test getTypeIdResolver method returning null idResolver
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNull(result);

        // Test getTypeIdResolver method returning a valid idResolver
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, idRes, typePropertyName, typeIdVisible, defaultImpl);
        TypeIdResolver expectedResult = idRes;
        TypeIdResolver result2 = typeDeserializerBase2.getTypeIdResolver();
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testGetDefaultImpl() {
        // Test getDefaultImpl method returning null defaultImpl
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);

        // Test getDefaultImpl method returning a valid defaultImpl
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = null;
        boolean typeIdVisible = false;
        Class<?> defaultImpl = String.class;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        Class<?> expectedResult = String.class;
        Class<?> result2 = typeDeserializerBase2.getDefaultImpl();
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testToString() {
        // Test toString method returning an empty String
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null);
        String result = typeDeserializerBase.toString();
        assertEquals("[]", result);

        // Test toString method returning a valid String
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typePropertyName = "testProperty";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        String expectedResult = "[TypeDeserializerBase; base-type:null; id-resolver: null;]";
        String result2 = typeDeserializerBase2.toString();
        assertEquals(expectedResult, result2);
    }

    @Test
    public void testFindDeserializerNullContext() throws IOException {
        // Test _findDeserializer method with null context
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typeId = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(null, typeId);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerValidContext() throws IOException {
        // Test _findDeserializer method with a valid context
        JsonDeserializer<Object> deserializer = new StandardSerializer<>(String.class);
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typeId = "testTypeId";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        context.putContextualValueDeserializer(String.class, deserializer);
        JsonDeserializer<Object> expectedResult = deserializer;
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        // Test _findDefaultImplDeserializer method with null context
        JavaType baseType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(null);
        assertNull(result);
    }

    @Test
    public void testFindDefaultImplDeserializerValidContext() throws IOException {
        // Test _findDefaultImplDeserializer method with a valid context
        JsonDeserializer<Object> deserializer = new StandardSerializer<>(String.class);
        JavaType baseType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        DeserializationContext context = new TestDeserializationContext();
        context.putContextualValueDeserializer(String.class, deserializer);
        JsonDeserializer<Object> expectedResult = deserializer;
        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        // Test _deserializeWithNativeTypeId method with null context
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdValidContext() throws IOException {
        // Test _deserializeWithNativeTypeId method with a valid context and typeId
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jp = objectMapper.getFactory().createParser("{\"@type\": \"com.test.TestClass\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, TestClass.class);
        Object expectedResult = new TestClass();
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "com.test.TestClass");
        assertEquals(expectedResult, result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        // Test _handleUnknownTypeId method with null context
        JavaType baseType = null;
        TypeIdResolver idRes = null;
        String typeId = "testTypeId";
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, null, false, null);
        typeDeserializerBase._handleUnknownTypeId(null, typeId, idRes, baseType);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdValidContext() throws IOException {
        // Test _handleUnknownTypeId method with a valid context
        JavaType baseType = null;
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);
        TypeIdResolver idRes = new TestTypeIdResolver();
        String typeId = "testTypeId";
        try {
            typeDeserializerBase._handleUnknownTypeId(new TestDeserializationContext(), typeId, idRes, baseType);
            fail();
        } catch (JsonMappingException e) {
            // Expected exception
        }
    }

    

    static class TestDeserializationContext extends DeserializationContext {
        @Override
        public boolean isEnabled(DeserializationFeature feature) {
            return true;
        }

        @Override
        public JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty property) throws IOException {
            return new StandardSerializer<>(String.class);
        }
    }

    static class TestTypeIdResolver implements TypeIdResolver {
        @Override
        public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
            return null;
        }

        @Override
        public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return "";
        }
    }

    static class TestClass {
        // Empty class
    }
}