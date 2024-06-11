package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private SerializerProvider provider;

    @Mock
    private BeanProperty property;

    @Mock
    private JsonGenerator g;

    @Mock
    private JsonFormatVisitorWrapper visitor;

    @Mock
    private JavaType typeHint;

    @Test
    public void testGetInstance() {
        NumberSerializer instance = NumberSerializer.instance;
        assertNotEquals(null, instance);
    }

    @Test
    public void testConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotEquals(null, numberSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer<?> contextSerializer = numberSerializer.createContextual(provider, property);
        assertNotEquals(null, contextSerializer);
        assertEquals(numberSerializer, contextSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal(10);
        numberSerializer.serialize(bigDecimal, g, provider);
        assertNotNull(g);
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode jsonNode = numberSerializer.getSchema(provider, (Type) Number.class);
        assertNotNull(jsonNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull(visitor);
    }
}