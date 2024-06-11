package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testTypeDeserializerBaseConstruction() throws Exception {
        JavaType baseType = JavaType.constructType(String.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        assertEquals("[" + TypeDeserializerBase.class.getName() + "; base-type:" + baseType + "; id-resolver: " + idResolver + "]", typeDeserializerBase.toString());
    }

    @Test
    public void testForProperty() throws Exception {
        JavaType baseType = JavaType.constructType(String.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(Integer.class);

        BeanProperty property = new BeanProperty("propertyName", Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(property);

        assertEquals(TypeDeserializer.class, typeDeserializer.getClass());
        assertEquals(baseType, typeDeserializer.getType(null));
        assertEquals(defaultImpl, typeDeserializer.getDefaultValue());
        assertEquals(typePropertyName, typeDeserializer.getPropertyName());
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JavaType baseType = JavaType.constructType(String.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(Integer.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        JsonTypeInfo.As actualTypeInclusion = typeDeserializerBase.getTypeInclusion();

        assertEquals(expectedTypeInclusion, actualTypeInclusion);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        JavaType baseType = JavaType.constructType(String.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null);

        String expectedBaseTypeName = "java.lang.String";

        String actualBaseTypeName = typeDeserializerBase.baseTypeName();

        assertEquals(expectedBaseTypeName, actualBaseTypeName);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestingDeserializationContext();
        String typeId = "Map";

        JsonDeserializer<Object> expectedDeserializer = new MapDeserializer();

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(ctxt, typeId);

        assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestingDeserializationContext();

        typeDeserializerBase._findDeserializer(ctxt, null);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestingDeserializationContext();

        JsonDeserializer<Object> expectedDeserializer = new HashMapDeserializer();

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);

        assertEquals(expectedDeserializer.getClass(), actualDeserializer.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializerWithNullContext() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(null);

        assertNull(actualDeserializer);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonParser jp = new TestingJsonParser();
        DeserializationContext ctxt = new TestingDeserializationContext();

        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);

        assertNull(actualResult);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithNullContext() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonParser jp = new TestingJsonParser();

        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(jp, null);

        assertNull(actualResult);
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        JsonParser jp = new TestingJsonParser();
        DeserializationContext ctxt = new TestingDeserializationContext();

        String typeId = "Map";

        Object actualResult = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);

        assertNull(actualResult);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestingDeserializationContext();
        String typeId = "UnknownType";

        JavaType expectedResult = JavaType.constructType(Object.class);

        JavaType actualResult = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeIdWithNullContext() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = null;
        String typeId = "UnknownType";

        typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
    }

    @Test
    public void testHandleUnknownTypeIdWithKnownIdResolver() throws Exception {
        JavaType baseType = JavaType.constructType(Map.class);
        TypeIdResolver idResolver = new BaseTypeIdResolver();
        String typePropertyName = "typeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.constructType(HashMap.class);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        DeserializationContext ctxt = new TestingDeserializationContext();
        String typeId = "Map";

        JavaType expectedResult = JavaType.constructType(Map.class);

        JavaType actualResult = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        assertEquals(expectedResult, actualResult);
    }

    private static class BaseTypeIdResolver extends TypeIdResolverBase {
        @Override
        public Map<String, String> getDescForKnownTypeIds() {
            return null;
        }
    }

    private static class MapDeserializer extends JsonDeserializer<Map<Object, Object>> {
    }

    private static class HashMapDeserializer extends JsonDeserializer<HashMap<Object, Object>> {
    }

    private static class TestingDeserializationContext extends DeserializationContext {
        protected TestingDeserializationContext() { super(new TestingObjectCodec()); }
    }

    private static class TestingObjectCodec extends ObjectCodec {
        @Override
        public Converter<Object, String> findValueSerializer(SerializerProvider serProvider, TypeSerializer typeSerializer) throws IOException {
            return null;
        }

        @Override
        public Converter<String, Object> findValueDeserializer(DeserializationContext ctxt, TypeDeserializer deserializer) throws IOException {
            return null;
        }
    }

    private static class TestingJsonParser extends JsonParser {
        @Override
        public Object parseValue(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        public Object parseValueAs(DeserializationContext ctxt, Class<?> type) throws IOException {
            return null;
        }

        @Override
        public Object parseValueAs(DeserializationContext ctxt, TypeDeserializer deserializer) throws IOException {
            return null;
        }
    }
}