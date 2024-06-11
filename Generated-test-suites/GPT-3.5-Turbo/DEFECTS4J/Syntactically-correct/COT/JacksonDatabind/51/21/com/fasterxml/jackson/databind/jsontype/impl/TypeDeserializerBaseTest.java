
package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Assert;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    // Test for _findDeserializer method
    @Test
    public void testFindDeserializer() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        // Test case 1: Deserializer is already present in the map
        JsonDeserializer<Object> deserializer1 = new TypeDeserializerBaseTestDeserializer();
        typeDeserializerBase._deserializers.put("testType1", deserializer1);
        Assert.assertEquals(deserializer1, typeDeserializerBase._findDeserializer(null, "testType1"));

        // Test case 2: Deserializer needs to be created and put in the map
        JsonDeserializer<Object> deserializer2 = typeDeserializerBase._findDeserializer(null, "testType2");
        Assert.assertNotNull(deserializer2);
        Assert.assertEquals(deserializer2, typeDeserializerBase._deserializers.get("testType2"));
    }

    // Test for _findDefaultImplDeserializer method
    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        // Test case 1: default implementation is null
        TypeDeserializerBase typeDeserializerBase1 = new TypeDeserializerBaseTestTypeDeserializer(null);
        JsonDeserializer<Object> deserializer1 = typeDeserializerBase1._findDefaultImplDeserializer(null);
        Assert.assertEquals(NullifyingDeserializer.instance, deserializer1);

        // Test case 2: default implementation is not null and is available in the map
        JsonDeserializer<Object> deserializer2 = new TypeDeserializerBaseTestDeserializer();
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBaseTestTypeDeserializer(deserializer2);
        JsonDeserializer<Object> deserializer3 = typeDeserializerBase2._findDefaultImplDeserializer(null);
        Assert.assertEquals(deserializer2, deserializer3);

        // Test case 3: default implementation is not null but not available in the map
        TypeDeserializerBase typeDeserializerBase3 = new TypeDeserializerBaseTestTypeDeserializer(null);
        JsonDeserializer<Object> deserializer4 = typeDeserializerBase3._findDefaultImplDeserializer(null);
        Assert.assertNotNull(deserializer4);
        Assert.assertEquals(deserializer4, typeDeserializerBase3._defaultImplDeserializer);
    }

    // Test for _deserializeWithNativeTypeId method
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseTestTypeDeserializer();
        JsonParser jsonParser = new TypeDeserializerBaseTestJsonParser();
        DeserializationContext deserializationContext = new TypeDeserializerBaseTestDeserializationContext();

        // Test case 1: typeId is null
        Object result1 = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext);
        Assert.assertNull(result1);

        // Test case 2: typeId is not null and corresponding deserializer is available
        typeDeserializerBase._deserializers.put("testType1", new TypeDeserializerBaseTestDeserializer());
        Object result2 = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "testType1");
        Assert.assertNotNull(result2);

        // Test case 3: typeId is not null but corresponding deserializer is not available
        Object result3 = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, deserializationContext, "testType2");
        Assert.assertNull(result3);
    }

    // Test for _handleUnknownTypeId method
    @Test
    public void testHandleUnknownTypeId() throws Exception {
        // Test case 1: idResolver is instance of TypeIdResolverBase
        TypeDeserializerBase typeDeserializerBase1 = new TypeDeserializerBaseTestTypeDeserializer();
        String result1 = typeDeserializerBase1._handleUnknownTypeId(null, "testId", new TypeDeserializerBaseTestTypeIdResolver(), null).getMessage();
        Assert.assertEquals("Unknown type id: testId (known type ids = testId1, testId2)", result1);

        // Test case 2: idResolver is not instance of TypeIdResolverBase
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBaseTestTypeDeserializer();
        String result2 = typeDeserializerBase2._handleUnknownTypeId(null, "testId", new TypeDeserializerBaseTestTypeIdResolverNonBase(), null).getMessage();
        Assert.assertEquals("Unknown type id: testId", result2);
    }
    
    private static class TypeDeserializerBaseTestTypeDeserializer extends TypeDeserializerBase {

        private static final long serialVersionUID = 1L;
        private JsonDeserializer<Object> deserializer;

        public TypeDeserializerBaseTestTypeDeserializer() {
            super(null, null, null, false, null);
        }

        public TypeDeserializerBaseTestTypeDeserializer(JsonDeserializer<Object> deserializer) {
            super(null, null, null, false, null);
            this.deserializer = deserializer;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

    }

    private static class TypeDeserializerBaseTestDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return null;
        }

    }

    private static class TypeDeserializerBaseTestJsonParser extends JsonParser {

        @Override
        public ObjectCodec getCodec() {
            return null;
        }

        @Override
        public void setCodec(ObjectCodec c) {
        }

        @Override
        public Version version() {
            return null;
        }

    }

    private static class TypeDeserializerBaseTestDeserializationContext extends DeserializationContext {

        @Override
        public DeserializationConfig getConfig() {
            return null;
        }
    }

    private static class TypeDeserializerBaseTestTypeIdResolver extends TypeIdResolverBase {

        private static final String[] IDS = { "testId1", "testId2" };

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) {
            return null;
        }

        @Override
        public Id getMechanism() {
            return null;
        }

        public TypeDeserializerBaseTestTypeIdResolver() {
            this.baseType = null;
            this._idType = null;
            this._typeFactory = null;
        }

        public TypeDeserializerBaseTestTypeIdResolver(JavaType baseType, TypeFactory typeFactory) {
            super(baseType, typeFactory);
        }

        @Override
        public String getDescForKnownTypeIds() {
            return "testId1, testId2";
        }
    }

    private static class TypeDeserializerBaseTestTypeIdResolverNonBase implements TypeIdResolver {

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {
            return null;
        }
        
    }
}
