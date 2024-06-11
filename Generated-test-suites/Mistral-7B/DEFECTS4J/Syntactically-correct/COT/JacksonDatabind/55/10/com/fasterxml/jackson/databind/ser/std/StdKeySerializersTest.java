package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetStdKeySerializer() throws Exception {
        // Test with null rawKeyType
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> standardSerializer = StdKeySerializers.getStdKeySerializer(config, null, false);
        assertNotNull(standardSerializer);

        // Test with String class
        JsonSerializer<Object> stringSerializer = StdKeySerializers.getStdKeySerializer(config, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), stringSerializer.getClass());

        // Test with primitive type and Number class
        JsonSerializer<Object> numberSerializer = StdKeySerializers.getStdKeySerializer(config, int.class, false);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), numberSerializer.getClass());

        // Test with Class class
        JsonSerializer<Object> classSerializer = StdKeySerializers.getStdKeySerializer(config, Class.class, false);
        assertEquals(StdKeySerializers.Default.class, classSerializer.getClass());

        // Test with Date class
        JsonSerializer<Object> dateSerializer = StdKeySerializers.getStdKeySerializer(config, Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, dateSerializer._typeId);

        // Test with Calendar class
        JsonSerializer<Object> calendarSerializer = StdKeySerializers.getStdKeySerializer(config, Calendar.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, calendarSerializer._typeId);

        // Test with java.util.UUID class
        JsonSerializer<Object> uuidSerializer = StdKeySerializers.getStdKeySerializer(config, UUID.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, uuidSerializer._typeId);

        // Test with boolean
        JsonSerializer<Object> booleanSerializer = StdKeySerializers.getStdKeySerializer(config, boolean.class, false);
        assertNull(booleanSerializer);

        // Test with Enum class
        JsonSerializer<Object> enumSerializer = StdKeySerializers.getStdKeySerializer(config, Enum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, enumSerializer._typeId);

        // Test with custom class
        JsonSerializer<Object> customSerializer = StdKeySerializers.getStdKeySerializer(config, CustomClass.class, false);
        assertNull(customSerializer);
    }

    @Test
    public void testGetFallbackKeySerializer() throws Exception {
        // Test with null rawKeyType
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> standardSerializer = StdKeySerializers.getFallbackKeySerializer(config, null);
        assertNotNull(standardSerializer);

        // Test with String class
        JsonSerializer<Object> stringSerializer = StdKeySerializers.getFallbackKeySerializer(config, String.class);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), stringSerializer.getClass());

        // Test with Enum class
        JsonSerializer<Object> enumSerializer = StdKeySerializers.getFallbackKeySerializer(config, Enum.class);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, enumSerializer._typeId);

        // Test with custom class
        JsonSerializer<Object> customSerializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class);
        assertNull(customSerializer);
    }

    @Test
    public void testDefaultKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> defaultSerializer = StdKeySerializers.getDefault();
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), defaultSerializer.getClass());
    }

    @Test
    public void testDynamicKeySerializer() throws Exception {
        // Test serialization
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass.class);
        assertNull(dynamicSerializer);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", "value1");
        rootNode.put("key2", new CustomClass("CustomClass1"));
        rootNode.put("key3", new CustomClass("CustomClass2"));

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        ObjectNode deserializedNode = objectMapper.readValue(input, ObjectNode.class);

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        objectMapper.readTree(input).traverseDeep(new StdKeySerializers.Dynamic().serializeValues(generator, null));

        // Test deserialization
        CustomClass customClass1 = objectMapper.readValue(input, CustomClass.class);
        CustomClass customClass2 = objectMapper.readValue(input, CustomClass.class);
        assertEquals(customClass1.name, "CustomClass1");
        assertEquals(customClass2.name, "CustomClass2");
    }

    @Test
    public void testStringKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> stringSerializer = StdKeySerializers.getDefaultStringSerializer();
        assertEquals(StdKeySerializers.StringKeySerializer.class, stringSerializer.getClass());

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", "value1");
        rootNode.put("key2", new CustomClass("CustomClass1"));

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonSerializer<Object> stringKeySerializer = StdKeySerializers.getStringKeySerializer();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        stringKeySerializer.serialize(input, generator, null);
    }

    @Test
    public void testEnumKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        JsonSerializer<Object> enumSerializer = StdKeySerializers.getDefaultEnumSerializer();
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, enumSerializer._typeId);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", CustomEnum.ONE);
        rootNode.put("key2", CustomEnum.TWO);

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonSerializer<Object> enumKeySerializer = StdKeySerializers.getEnumKeySerializer();
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        enumKeySerializer.serialize(input, generator, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownKeySerializer() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization
        JsonSerializer<Object> unknownSerializer = StdKeySerializers.getStdKeySerializer(config, CustomClass2.class, false);
        assertNull(unknownSerializer);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", "value1");
        rootNode.put("key2", new CustomClass2("CustomClass2"));

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonSerializer<Object> unknownKeySerializer = StdKeySerializers.getStdKeySerializer(config, CustomClass2.class, false);
        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out, JsonEncoding.UTF8);
        unknownKeySerializer.serialize(input, generator, null);

        // Test deserialization
        CustomClass2 customClass2 = mapper.readValue(input, CustomClass2.class);
        fail();
    }

    @Test(expected = JsonMappingException.class)
    public void testDynamicKeySerializerMissingMap() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        ObjectMapper mapper = new ObjectMapper();

        JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass2.class);
        assertNull(dynamicSerializer);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", "value1");
        rootNode.put("key2", new CustomClass2("CustomClass2"));

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonSerializer<Object> dynamicKeySerializer = StdKeySerializers.getStdKeySerializer(config, CustomClass2.class, false);
        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(input.getBytes("UTF-8")));
        parser.setCodec(mapper);

        Dynamic dynamic = new Dynamic();
        try {
            dynamic.deserialize(parser, null);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testDynamicKeySerializerWithMap() throws Exception {
        SerializationConfig config = objectMapper.getSerializationConfig();
        ObjectMapper mapper = new ObjectMapper();
        PropertySerializerMap serializers = new PropertySerializerMap();

        JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getFallbackKeySerializer(config, CustomClass2.class);
        assertNull(dynamicSerializer);

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("key1", "value1");
        rootNode.put("key2", new CustomClass2("CustomClass2"));

        String input = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        JsonSerializer<Object> dynamicKeySerializer = StdKeySerializers.getStdKeySerializer(config, CustomClass2.class, false);
        JsonFactory factory = objectMapper.getFactory();
        JsonParser parser = factory.createParser(new ByteArrayInputStream(input.getBytes("UTF-8")));
        parser.setCodec(mapper);

        Dynamic dynamic = new Dynamic();
        dynamic.readFields(parser, serializers, null);

        CustomClass2 customClass2 = dynamic.getValue();
        assertEquals(customClass2.name, "CustomClass2");
    }

    static class CustomClass implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public CustomClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    enum CustomEnum {
        ONE, TWO
    }

    enum CustomEnum2 {
        ONE, TWO, THREE
    }

    static class CustomClass2 implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public CustomClass2(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}