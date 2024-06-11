package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.StandardSerializers;
import com.fasterxml.jackson.databind.ser.TestSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@RunWith(Parameterized.class)
public class NumberSerializerTest {

    private Class<? extends Number> numberType;
    private ObjectMapper mapper;
    private NumberSerializer serializer;

    public NumberSerializerTest(Class<? extends Number> numberType) {
        this.numberType = numberType;
    }

    @Parameterized.Parameters(name = "{index}: Testing NumberSerializer for {0}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {Byte.class},
                {Short.class},
                {Integer.class},
                {Long.class},
                {Float.class},
                {Double.class},
                {BigInteger.class},
                {BigDecimal.class},
                {BigInteger.class},
                {BigDecimal.class},
                {new TestNumberClass()}
        });
    }

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        serializer = NumberSerializer.instance;
    }

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        Number number = (Number) mapper.readValue("123", numberType);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(number);
        assertEquals("123", json);
    }

    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException, IOException {
        BigDecimal decimal = new BigDecimal("123.45");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(decimal);
        assertEquals("123.45", json);
    }

    @Test
    public void testSerializeBigInteger() throws JsonGenerationException, IOException {
        BigInteger integer = new BigInteger("123456789012345678901234567890");
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(integer);
        assertEquals("123456789012345678901234567890", json);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        when(visitor.expectNumberFormat(isA(JavaType.class))).thenReturn(visitor);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), null);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testSerializeCustomClass() throws JsonGenerationException, IOException {
        TestNumberClass customNumber = new TestNumberClass();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customNumber);
        assertEquals("123", json);
    }
}

class TestNumberClass {
    @JsonSerialize(using = NumberSerializer.class)
    private int value;
}