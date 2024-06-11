package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void test_createContextual() throws JsonMappingException {
        SerializerProvider prov = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;

        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(prov, property);

        Assert.assertTrue(jsonSerializer instanceof NumberSerializer);
        Assert.assertEquals(numberSerializer, jsonSerializer);
    }

    @Test
    public void test_serialize_BigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("123.45");
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_BigInteger() throws IOException {
        BigInteger value = new BigInteger("123");
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Long() throws IOException {
        Long value = 123L;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Double() throws IOException {
        Double value = 123.45;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Float() throws IOException {
        Float value = 123.45f;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Integer() throws IOException {
        Integer value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Byte() throws IOException {
        Byte value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_Short() throws IOException {
        Short value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_serialize_UnknownNumberType() throws IOException {
        Number value = 123;
        JsonGenerator g = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        numberSerializer.serialize(value, g, provider);

        g.flush();
    }

    @Test
    public void test_getSchema() {
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Type typeHint = null;

        JsonNode schema = numberSerializer.getSchema(provider, typeHint);

        Assert.assertTrue(schema.isObject());
    }

    @Test
    public void test_acceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().getFactory()
                .getCodec().visitorWrapper();
        JavaType typeHint = null;

        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

}