package com.fasterxml.jackson.databind.ser.std; // import statements go here if needed

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new LinkedHashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertEquals(11, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Float.class.getName()));
        assertTrue(allDeserializers.containsKey(Float.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Double.class.getName()));
        assertTrue(allDeserializers.containsKey(Double.TYPE.getName()));
    }

    @Test
    public void testShortSerializer() throws JsonProcessingException {
        Short number = Short.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-128", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Short result = MAPPER.readValue(parser, Short.class);
        assertEquals(Short.MIN_VALUE, result);
    }

    @Test
    public void testIntegerSerializer() throws JsonProcessingException {
        Integer number = Integer.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-2147483648", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Integer result = MAPPER.readValue(parser, Integer.class);
        assertEquals(Integer.MIN_VALUE, result);
    }

    @Test
    public void testIntLikeSerializer() throws JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-9223372036854775808", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Number result = MAPPER.readValue(parser, Number.class);
        assertEquals(Long.MIN_VALUE, result.longValue());

        json = MAPPER.writeValueAsString((float) 3.14);
        parser = MAPPER.getFactory().createParser(json);
        result = MAPPER.readValue(parser, Number.class);
        assertEquals(3.14, result.doubleValue(), 0.001);
    }

    @Test
    public void testLongSerializer() throws JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-9223372036854775808", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Long result = MAPPER.readValue(parser, Long.class);
        assertEquals(Long.MIN_VALUE, result);
    }

    @Test
    public void testFloatSerializer() throws JsonProcessingException {
        Float number = Float.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-3.4028235E38", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Float result = MAPPER.readValue(parser, Float.class);
        assertEquals(Float.MIN_VALUE, result);
    }

    @Test
    public void testDoubleSerializer() throws JsonProcessingException {
        Double number = Double.MIN_VALUE;
        String json = MAPPER.writeValueAsString(number);
        assertEquals("-4.9406664646e-324", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Double result = MAPPER.readValue(parser, Double.class);
        assertEquals(Double.MIN_VALUE, result);
    }

    @Test
    public void testJsonFormatVisitorWrapper() {
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(null, null);
        JsonNumberFormatVisitor numberFormatVisitor = visitorWrapper.expectNumberFormat(Object.class);
        JsonIntegerFormatVisitor intFormatVisitor = visitorWrapper.expectIntegerFormat(Object.class);

        assertNull(numberFormatVisitor);
        assertNull(intFormatVisitor);
    }

    @Test
    public void testJsonFormatVisitorWrapperWithIntType() {
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(null, JsonParser.NumberType.INT);
        JsonIntegerFormatVisitor intFormatVisitor = visitorWrapper.expectIntegerFormat(Object.class);

        assertNotNull(intFormatVisitor);
    }

    @Test
    public void testJsonFormatVisitorWrapperWithDoubleType() {
        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(null, JsonParser.NumberType.DOUBLE);
        JsonNumberFormatVisitor numberFormatVisitor = visitorWrapper.expectNumberFormat(Object.class);

        assertNotNull(numberFormatVisitor);
    }

    @Test
    public void testContextualSerializer() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        class Test {
            private int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }

        Test testObject = new Test();
        testObject.setValue(42);

        String json = MAPPER.writeValueAsString(testObject);
        assertEquals("42", json);

        JsonParser parser = MAPPER.getFactory().createParser(json);
        Test result = MAPPER.readValue(parser, Test.class);
        assertEquals(42, result.getValue());

        testObject.setValue("foo");
        json = MAPPER.writeValueAsString(testObject);
        assertEquals("\"foo\"", json);

        parser = MAPPER.getFactory().createParser(json);
        result = MAPPER.readValue(parser, Test.class);
        assertEquals("foo", result.getValue());
    }
}