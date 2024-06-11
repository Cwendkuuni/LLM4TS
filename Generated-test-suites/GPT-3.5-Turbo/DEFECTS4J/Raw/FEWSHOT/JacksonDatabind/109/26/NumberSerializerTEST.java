package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {
    
    @Test
    public void testConstructor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        assertEquals(Number.class, serializer.handledType());
        assertEquals(false, serializer.isEmpty(null));
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertNull(serializer.createContextual(provider, null));
    }
    
    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        
        serializer.serialize(new BigDecimal("1.23"), generator, provider);
        serializer.serialize(new BigInteger("123"), generator, provider);
        serializer.serialize(new Long(123), generator, provider);
        serializer.serialize(new Double(1.23), generator, provider);
        serializer.serialize(new Float(1.23), generator, provider);
        serializer.serialize(new Integer(123), generator, provider);
        serializer.serialize(new Byte((byte) 123), generator, provider);
        serializer.serialize(new Short((short) 123), generator, provider);
        serializer.serialize("123", generator, provider);
    }
    
    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        assertEquals("number", serializer.getSchema(provider, null).get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getFactory()
                .getCodec().visitorProvider().getProvider().expectStringFormat(Number.class);
        serializer.acceptJsonFormatVisitor(visitor, null);
    }
}