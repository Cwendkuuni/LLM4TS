
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        assertNotNull(allDeserializers.get(Integer.class.getName()));
        assertNotNull(allDeserializers.get(Integer.TYPE.getName()));
        assertNotNull(allDeserializers.get(Long.class.getName()));
        assertNotNull(allDeserializers.get(Long.TYPE.getName()));
        assertNotNull(allDeserializers.get(Byte.class.getName()));
        assertNotNull(allDeserializers.get(Byte.TYPE.getName()));
        assertNotNull(allDeserializers.get(Short.class.getName()));
        assertNotNull(allDeserializers.get(Short.TYPE.getName()));
        assertNotNull(allDeserializers.get(Double.class.getName()));
        assertNotNull(allDeserializers.get(Double.TYPE.getName()));
        assertNotNull(allDeserializers.get(Float.class.getName()));
        assertNotNull(allDeserializers.get(Float.TYPE.getName()));
    }

    @Test
    public void testBaseGetSchema() {
        NumberSerializers.Base base = new NumberSerializers.Base(Object.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing
            }
        };

        JsonNode schema = base.getSchema(null, null);
        assertNotNull(schema);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testBaseAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializers.Base baseInt = new NumberSerializers.Base(Object.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing
            }
        };

        NumberSerializers.Base baseFloat = new NumberSerializers.Base(Object.class, JsonParser.NumberType.FLOAT, "number") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing
            }
        };

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper.Base();
        baseInt.acceptJsonFormatVisitor(visitor, null);
        baseFloat.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testBaseContextual() throws JsonMappingException {
        NumberSerializers.Base base = new NumberSerializers.Base(Object.class, JsonParser.NumberType.INT, "integer") {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // Do nothing
            }
        };

        SerializerProvider prov = new JsonSerializerProvider(new ObjectMapper());
        BeanProperty property = null;
        JsonSerializer<?> serializer = base.createContextual(prov, property);
        assertNotNull(serializer);
    }

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize((short) 123, gen, provider);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize(123, gen, provider);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize(123, gen, provider);
    }

    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize(123L, gen, provider);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize(3.14f, gen, provider);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        SerializerProvider provider = new JsonSerializerProvider(new ObjectMapper());
        serializer.serialize(3.14, gen, provider);
    }
}
