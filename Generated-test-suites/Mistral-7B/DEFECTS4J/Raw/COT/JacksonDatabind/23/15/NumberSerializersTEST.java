import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, JsonSerializer<?>> allDeserializers = mapper.getSerializationConfig().getDefaultSerializerProvider().getDeserializerInstance(Map.class);

        NumberSerializers.addAll(allDeserializers);

        Assert.assertEquals(10, allDeserializers.size());
        Assert.assertNotNull(allDeserializers.get(Integer.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Float.TYPE.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.class.getName()));
        Assert.assertNotNull(allDeserializers.get(Double.TYPE.getName()));
    }

    @Test
    public void testBaseClass_getSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ShortSerializer shortSerializer = NumberSerializers.getShortSerializer();
        ObjectNode node = mapper.treeToNode(shortSerializer);
        Assert.assertEquals("number", node.get("schemaType").asText());
    }

    @Test
    public void testBaseClass_acceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ShortSerializer shortSerializer = NumberSerializers.getShortSerializer();
        ObjectNode node = mapper.treeToNode(shortSerializer);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getSerializationConfig());
        shortSerializer.acceptJsonFormatVisitor(visitor, null);

        JsonNumberFormatVisitor v = visitor.findNumberFormatVisitor();
        Assert.assertNotNull(v);
        Assert.assertEquals(JsonParser.NumberType.INT, v.getNumberType());
    }

    @Test(expected = JsonMappingException.class)
    public void testBaseClass_createContextual_nullProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<?> serializer = NumberSerializers.getIntSerializer();
        serializer.createContextual(null, null);
    }

    @Test
    public void testIntegerSerializer_serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        IntegerSerializer integerSerializer = NumberSerializers.getIntSerializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 123);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        integerSerializer.serialize(123, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());

        Assert.assertEquals(123, gen.getOutputSize());
        Assert.assertEquals("123", gen.getOutputBuffer().toString());

        integerSerializer.serialize(node, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
        Assert.assertEquals(123, gen.getOutputSize());
        Assert.assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test(expected = IOException.class)
    public void testIntegerSerializer_serializeWithType_nullTypeSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        IntegerSerializer integerSerializer = NumberSerializers.getIntSerializer();
        integerSerializer.serializeWithType(123, null, mapper.getSerializationConfig().getDefaultSerializerProvider(), null);
    }

    @Test
    public void testIntLikeSerializer_serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        IntLikeSerializer intLikeSerializer = NumberSerializers.getIntLikeSerializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 123);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        intLikeSerializer.serialize(123, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());

        Assert.assertEquals(123, gen.getOutputSize());
        Assert.assertEquals("123", gen.getOutputBuffer().toString());

        intLikeSerializer.serialize(node, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
        Assert.assertEquals(123, gen.getOutputSize());
        Assert.assertEquals("123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testLongSerializer_serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        LongSerializer longSerializer = NumberSerializers.getLongSerializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 1234567890123L);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        longSerializer.serialize(1234567890123L, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());

        Assert.assertEquals(22, gen.getOutputSize());
        Assert.assertEquals("1234567890123", gen.getOutputBuffer().toString());

        longSerializer.serialize(node, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
        Assert.assertEquals(22, gen.getOutputSize());
        Assert.assertEquals("1234567890123", gen.getOutputBuffer().toString());
    }

    @Test
    public void testFloatSerializer_serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        FloatSerializer floatSerializer = NumberSerializers.getFloatSerializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 123.456f);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        floatSerializer.serialize(123.456f, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());

        Assert.assertEquals(8, gen.getOutputSize());
        Assert.assertEquals("123.456", gen.getOutputBuffer().toString());

        floatSerializer.serialize(node, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
        Assert.assertEquals(8, gen.getOutputSize());
        Assert.assertEquals("123.456", gen.getOutputBuffer().toString());
    }

    @Test
    public void testDoubleSerializer_serialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        DoubleSerializer doubleSerializer = NumberSerializers.getDoubleSerializer();
        ObjectNode node = mapper.createObjectNode();
        node.put("value", 123.45678910111213d);

        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        doubleSerializer.serialize(123.45678910111213d, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());

        Assert.assertEquals(24, gen.getOutputSize());
        Assert.assertEquals("123.45678910111213", gen.getOutputBuffer().toString());

        doubleSerializer.serialize(node, gen, mapper.getSerializationConfig().getDefaultSerializerProvider());
        Assert.assertEquals(24, gen.getOutputSize());
        Assert.assertEquals("123.45678910111213", gen.getOutputBuffer().toString());
    }

    @Test
    public void testJsonFormatAnnotation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        class TestClass {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private int intValue;

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        TestClass testClass = new TestClass();
        testClass.setIntValue(123);

        ObjectWriter writer = mapper.writer(JsonEncoding.ASCII);
        ObjectNode node = writer.writeValueAsTree(testClass);
        String json = writer.writeValueAsString(testClass);

        JsonParser parser = jsonFactory.getParser(json);
        TestClass parsedObject = mapper.readValue(parser, TestClass.class);

        Assert.assertEquals(123, parsedObject.getIntValue());

        JsonParser parserNode = jsonFactory.getParser(node.toString());
        TestClass parsedObjectNode = mapper.readValue(parserNode, TestClass.class);

        Assert.assertEquals(123, parsedObjectNode.getIntValue());
    }

    @Test(expected = JsonMappingException.class)
    public void testJsonFormatAnnotation_noGetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        class TestClass {
            @JsonFormat(shape = JsonFormat.Shape.STRING)
            private int intValue;

            public int getOtherIntValue() {
                return intValue;
            }
        }

        TestClass testClass = new TestClass();
        testClass.setIntValue(123);

        ObjectWriter writer = mapper.writer(JsonEncoding.ASCII);
        ObjectNode node = writer.writeValueAsTree(testClass);
        String json = writer.writeValueAsString(testClass);

        JsonParser parser = jsonFactory.getParser(json);
        TestClass parsedObject = mapper.readValue(parser, TestClass.class);

        Assert.assertNotEquals(123, parsedObject.getIntValue());
        Assert.assertNull(parsedObject.getOtherIntValue());
    }

    @Test
    public void testJsonFormatAnnotation_customShape() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();

        class TestClass {
            @JsonFormat(shape = JsonFormat.Shape.INT, order = JsonFormat.Feature.ORDER_KIDS_FIRST)
            private int intValue;

            public int getIntValue() {
                return intValue;
            }

            public void setIntValue(int intValue) {
                this.intValue = intValue;
            }
        }

        TestClass testClass = new TestClass();
        testClass.setIntValue(123);

        ObjectWriter writer = mapper.writer(JsonEncoding.ASCII);
        ObjectNode node = writer.writeValueAsTree(testClass);
        String json = writer.writeValueAsString(testClass);

        JsonParser parser = jsonFactory.getParser(json);
        TestClass parsedObject = mapper.readValue(parser, TestClass.class);

        Assert.assertEquals(123, parsedObject.getIntValue());

        JsonParser parserNode = jsonFactory.getParser(node.toString());
        TestClass parsedObjectNode = mapper.readValue(parserNode, TestClass.class);

        Assert.assertEquals(123, parsedObjectNode.getIntValue());

        ObjectNode updatedNode = node.deepCopy();
        ObjectWriter writerUpdated = mapper.writer(JsonEncoding.ASCII);
        updatedNode.put("@JsonFormat", mapper.valueToTree(new JsonFormat.Builder().shape(JsonFormat.Shape.STRING).build()));
        String jsonUpdated = writerUpdated.writeValueAsString(updatedNode);

        JsonParser parserUpdated = jsonFactory.getParser(jsonUpdated);
        TestClass parsedObjectUpdated = mapper.readValue(parserUpdated, TestClass.class);

        Assert.assertEquals("123", parsedObjectUpdated.getIntValue().toString());
    }
}