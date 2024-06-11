package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotate.JsonDeserialize;
import com.fasterxml.jackson.databind.annotate.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@JsonSerialize(using = NumberSerializer.class)
@JsonDeserialize(using = NumberSerializer.class)
static class TestNumber {
    BigDecimal bd;
    BigInteger bi;
    Double d;
    Float f;
    Integer i;
    Long l;
    Short s;
    Number n;
    String str;
}

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestNumber testNumber = new TestNumber();
        testNumber.bd = new BigDecimal("123.456");
        testNumber.bi = new BigInteger("12345");
        testNumber.d = 123.456;
        testNumber.f = 123.456f;
        testNumber.i = 123;
        testNumber.l = 123L;
        testNumber.n = 123;
        testNumber.s = (short) 123;
        testNumber.str = "123";

        String json = mapper.writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.bd, parsedObject.bd);
        assertEquals(testNumber.bi, parsedObject.bi);
        assertEquals(testNumber.d, parsedObject.d);
        assertEquals(testNumber.f, parsedObject.f);
        assertEquals(testNumber.i, parsedObject.i);
        assertEquals(testNumber.l, parsedObject.l);
        assertEquals(testNumber.s, parsedObject.s);
        assertEquals(testNumber.n, parsedObject.n);
        assertEquals(testNumber.str, parsedObject.str);
    }

    @Test
    public void testSerializationOfBigDecimalAndBigInteger() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();
        testNumber.bd = new BigDecimal("12345678901234567890");
        testNumber.bi = new BigInteger("12345678901234567890");

        String json = mapper.writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.bd.toString(), parsedObject.bd.toString());
        assertEquals(testNumber.bi.toString(), parsedObject.bi.toString());
    }

    @Test
    public void testSerializationOfLongAndDouble() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();
        testNumber.d = Double.MAX_VALUE;
        testNumber.l = Long.MAX_VALUE;

        String json = mapper.writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.d, parsedObject.d);
        assertEquals(testNumber.l, parsedObject.l);
    }

    @Test
    public void testSerializationOfJsonFormat() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;

        testNumber.bd = new BigDecimal("12345678901234567890");
        testNumber.bi = new BigInteger("12345678901234567890");

        testNumber.bd = new BigDecimal("123.456");
        testNumber.bi = new BigInteger("123");

        testNumber.d = 123.456;
        testNumber.f = 123.456f;
        testNumber.i = 123;
        testNumber.l = 123L;
        testNumber.n = 123;
        testNumber.s = (short) 123;
        testNumber.str = "123";

        testNumber.bd = new BigDecimal("12345678901234567890");
        testNumber.bi = new BigInteger("12345678901234567890");

        testNumber.bd = new BigDecimal("123.456");
        testNumber.bi = new BigInteger("123");

        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(shape);

        testNumber.setBd(jsonFormat.withPrefix("prefix_bd"));
        testNumber.setBi(jsonFormat.withPrefix("prefix_bi"));

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.bd.toString(), parsedObject.getBd().toString());
        assertEquals(testNumber.bi.toString(), parsedObject.getBi().toString());
    }

    @Test
    public void testSerializationOfContextualSerializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();
        testNumber.i = 123;

        JsonSerializer<Number> serializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();

        testNumber.setI(serializer.createContextual(provider, null));

        String json = mapper.writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.i, parsedObject.i);
    }

    @Test
    public void testSerializationOfContextualSerializerWithJsonFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();
        testNumber.i = 123;

        JsonSerializer<Number> serializer = NumberSerializer.instance;
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.shape(JsonFormat.Shape.STRING);

        testNumber.setI(serializer.createContextual(mapper.getSerializerProvider(), null));
        JsonSerializer<Number> contextualSerializer = testNumber.getI();

        contextualSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper() {
            @Override
            public void visitBooleanFormat(JsonFormatVisitorWrapper visitor, JavaType javaType) throws JsonMappingException {
                // should not be called
            }

            @Override
            public void visitNumberFormat(JsonFormatVisitorWrapper visitor, JavaType javaType)  throws JsonMappingException {
                assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.expectNumberFormat(javaType).getNumberType());
            }

            @Override
            public void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType javaType) throws JsonMappingException {
                // should not be called
            }

            @Override
            public void acceptNullFormat(JsonFormatVisitorWrapper visitor, JavaType javaType)  throws JsonMappingException {
                // should not be called
            }

            @Override
            public void visitDateFormat(JsonFormatVisitorWrapper visitor, JavaType javaType) throws JsonMappingException {
                // should not be called
            }

            @Override
            public void visitDateTimeFormat(JsonFormatVisitorWrapper visitor, JavaType javaType) throws JsonMappingException {
                // should not be called
            }

            @Override
            public void visitObjectFormat(JsonFormatVisitorWrapper visitor, JavaType javaType) throws JsonMappingException {
                // should not be called
            }
        }, null);

        String json = mapper.writeValueAsString(testNumber);

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(testNumber.i, parsedObject.i);
    }

    @Test
    public void testDeserializationOfJsonFormatString() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":\"12345678901234567890\",\"prefix_bi\":\"12345678901234567890\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(new BigDecimal("12345678901234567890"), parsedObject.getBd());
        assertEquals(new BigInteger("12345678901234567890"), parsedObject.getBi());
    }

    @Test
    public void testDeserializationOfJsonFormatNumber() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":12345678901234567890,\"prefix_bi\":12345678901234567890,\"i\":\"123\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(new BigDecimal("12345678901234567890"), parsedObject.getBd());
        assertEquals(new BigInteger("12345678901234567890"), parsedObject.getBi());
        assertEquals(123, parsedObject.i.intValue());
    }

    @Test
    public void testDeserializationOfJsonFormatBigDecimal() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":\"123.456\",\"prefix_bi\":123,\"i\":\"123\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(new BigDecimal("123.456"), parsedObject.getBd());
        assertEquals(new BigInteger("123"), parsedObject.getBi());
        assertEquals(123, parsedObject.i.intValue());
    }

    @Test
    public void testDeserializationOfJsonFormatBigInteger() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":\"123.456\",\"prefix_bi\":\"12345678901234567890\",\"i\":\"123\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);

        assertEquals(new BigDecimal("123.456"), parsedObject.getBd());
        assertEquals(new BigInteger("12345678901234567890"), parsedObject.getBi());
        assertEquals(123, parsedObject.i.intValue());
    }

    @Test(expected = IOException.class)
    public void testDeserializationOfJsonFormatInvalidNumber() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":\"123.456\",\"prefix_bi\":\"12345678901234567891\",\"i\":\"123\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializationOfJsonFormatInvalidJsonFormat() throws Exception {
        ObjectMapper mapper = JsonMapper.builder().build();
        TestNumber testNumber = new TestNumber();

        String json = "{\"prefix_bd\":\"123.456\",\"prefix_bi\":\"12345678901234567890\",\"i\":\"invalid\"}";

        JsonParser parser = mapper.getFactory().createParser(json);
        TestNumber parsedObject = mapper.readValue(parser, TestNumber.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestNumber testNumber = new TestNumber();

        JsonFormat jsonFormat = new JsonFormat();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();

        testNumber.bd = new BigDecimal("123.456");
        testNumber.bi = new BigInteger("123");

        testNumber.setBd(jsonFormat.withPrefix("prefix_bd"));
        testNumber.setBi(jsonFormat.withPrefix("prefix_bi"));

        testNumber.acceptJsonFormatVisitor(visitor, null);

        assertEquals(JsonNodeType.NUMBER, visitor.getCurrentNodeType());
        assertEquals("prefix_bd", visitor.getCurrentNodeName());
        assertEquals(JsonParser.NumberType.BIG_DECIMAL, visitor.getNumberType());

        visitor.resetCurrentNode();

        testNumber.acceptJsonFormatVisitor(visitor, null);

        assertEquals(JsonNodeType.NUMBER, visitor.getCurrentNodeType());
        assertEquals("prefix_bi", visitor.getCurrentNodeName());
        assertEquals(JsonParser.NumberType.BIG_INTEGER, visitor.getNumberType());
    }
}