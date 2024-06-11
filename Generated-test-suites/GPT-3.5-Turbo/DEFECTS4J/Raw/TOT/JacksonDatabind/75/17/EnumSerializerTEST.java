
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.std.EnumSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;
import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeEnumAsString() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(createEnumValues(), false);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerFactory serializerFactory = BeanSerializerFactory.instance
                .withConfig(objectMapper.getSerializationConfig())
                .withAdditionalSerializers(enumSerializer);
        objectMapper.setSerializerFactory(serializerFactory);

        TestClass testClass = new TestClass(TestEnum.CONSTANT1);
        String json = objectMapper.writeValueAsString(testClass);

        Assert.assertEquals("{\"value\":\"CONSTANT1\"}", json);
    }

    @Test
    public void testSerializeEnumAsIndex() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(createEnumValues(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerFactory serializerFactory = BeanSerializerFactory.instance
                .withConfig(objectMapper.getSerializationConfig())
                .withAdditionalSerializers(enumSerializer);
        objectMapper.setSerializerFactory(serializerFactory);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

        TestClass testClass = new TestClass(TestEnum.CONSTANT2);
        String json = objectMapper.writeValueAsString(testClass);

        Assert.assertEquals("{\"value\":1}", json);
    }

    @Test
    public void testSerializeEnumUsingToString() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(createEnumValues(), false);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerFactory serializerFactory = BeanSerializerFactory.instance
                .withConfig(objectMapper.getSerializationConfig())
                .withAdditionalSerializers(enumSerializer);
        objectMapper.setSerializerFactory(serializerFactory);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

        TestClass testClass = new TestClass(TestEnum.CONSTANT3);
        String json = objectMapper.writeValueAsString(testClass);

        Assert.assertEquals("{\"value\":\"valueOf3\"}", json);
    }

    @Test
    public void testGetSchemaWithEnumAsString() {
        EnumSerializer enumSerializer = new EnumSerializer(createEnumValues(), false);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerFactory serializerFactory = BeanSerializerFactory.instance
                .withConfig(objectMapper.getSerializationConfig())
                .withAdditionalSerializers(enumSerializer);
        objectMapper.setSerializerFactory(serializerFactory);

        TestClass testClass = new TestClass(TestEnum.CONSTANT1);
        String schema = objectMapper.generateJsonSchema(TestClass.class).toString();

        Assert.assertEquals("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"properties\":{\"value\":{\"type\":\"string\",\"enum\":[\"CONSTANT1\",\"CONSTANT2\",\"CONSTANT3\"]}},\"type\":\"object\"}", schema);
    }

    @Test
    public void testGetSchemaWithEnumAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(createEnumValues(), true);
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerFactory serializerFactory = BeanSerializerFactory.instance
                .withConfig(objectMapper.getSerializationConfig())
                .withAdditionalSerializers(enumSerializer);
        objectMapper.setSerializerFactory(serializerFactory);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);

        TestClass testClass = new TestClass(TestEnum.CONSTANT2);
        String schema = objectMapper.generateJsonSchema(TestClass.class).toString();

        Assert.assertEquals("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"properties\":{\"value\":{\"type\":\"integer\"}},\"type\":\"object\"}", schema);
    }

    private EnumValues createEnumValues() {
        EnumValues enumValues = new EnumValues(TestEnum.class, new String[] {"CONSTANT1", "CONSTANT2", "CONSTANT3"});
        return enumValues;
    }

    private enum TestEnum {
        CONSTANT1,
        CONSTANT2,
        CONSTANT3
    }

    private class TestClass {
        public TestEnum value;

        public TestClass(TestEnum value) {
            this.value = value;
        }
    }
}
