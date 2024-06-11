package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDescriptor;

import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerializeNumber() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        JsonGenerator jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(1, jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigDecimal("1.23"), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1.23", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigInteger("123"), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("123", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Integer(1), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Long(1), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Double(1.23), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1.23", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Float(1.23f), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1.23", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Byte((byte) 1), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Short((short) 1), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("1", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigDecimal("NaN"), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("NaN", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new BigInteger("NaN"), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("NaN", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Integer(Integer.MIN_VALUE), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("-2147483648", jsonGenerator.getOutputBuffer().toString());

        jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        numberSerializer.serialize(new Long(Long.MIN_VALUE), jsonGenerator, serializers);
        jsonGenerator.flush();
        assertEquals("-9223372036854775808", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonObjectIdVisitor();
        NumberSerializer numberSerializer = NumberSerializer.instance;

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Integer.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Long.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Double.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Float.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Byte.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(Short.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(BigInteger.class));
        assertNotNull(visitor.getNumberFormat());

        numberSerializer.acceptJsonFormatVisitor(visitor, TypeDescriptor.valueDesc(BigDecimal.class));
        assertNotNull(visitor.getNumberFormat());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        NumberSerializer numberSerializer = NumberSerializer.instance;

        assertEquals("number", numberSerializer.getSchema(null, null).getClass().getSimpleName());
        assertEquals("integer", numberSerializer.getSchema(null, TypeDescriptor.valueDesc(Integer.class)).getClass().getSimpleName());
    }
}