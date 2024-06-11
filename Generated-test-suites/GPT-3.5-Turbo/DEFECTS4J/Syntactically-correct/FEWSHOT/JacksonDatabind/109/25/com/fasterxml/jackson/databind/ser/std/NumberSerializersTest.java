package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializersTest {
    @Test
    public void testShortSerializerSerialize() throws IOException {
        ShortSerializer serializer = new ShortSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Short value = 10;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testIntegerSerializerSerialize() throws IOException {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Integer value = 100;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testIntLikeSerializerSerialize() throws IOException {
        IntLikeSerializer serializer = new IntLikeSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Number value = 1000;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testLongSerializerSerialize() throws IOException {
        LongSerializer serializer = new LongSerializer(Long.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Long value = 10000L;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testFloatSerializerSerialize() throws IOException {
        FloatSerializer serializer = new FloatSerializer();
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Float value = 10.5F;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testDoubleSerializerSerialize() throws IOException {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);
        JsonGenerator generator = null;
        SerializerProvider provider = null;
        
        try {
            generator = null;
            provider = null;
            
            generator = new JsonGenerator(null, 0, null, null);
            generator.writeStartObject();
            
            Double value = 10.5678;
            serializer.serialize(value, generator, provider);
            
            generator.writeEndObject();
            generator.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        } finally {
            if (generator != null) {
                generator.close();
            }
        }
    }

    @Test
    public void testShortSerializerGetSchema() {
        ShortSerializer serializer = new ShortSerializer();

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testIntegerSerializerGetSchema() {
        IntegerSerializer serializer = new IntegerSerializer(Integer.class);

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testIntLikeSerializerGetSchema() {
        IntLikeSerializer serializer = new IntLikeSerializer();

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testLongSerializerGetSchema() {
        LongSerializer serializer = new LongSerializer(Long.class);

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testFloatSerializerGetSchema() {
        FloatSerializer serializer = new FloatSerializer();

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testDoubleSerializerGetSchema() {
        DoubleSerializer serializer = new DoubleSerializer(Double.class);

        SerializerProvider provider = null;
        Type typeHint = null;

        JsonNode schema = serializer.getSchema(provider, typeHint);

        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testValidateJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;

        Base<Object> serializer = new IntegerSerializer(Integer.class);

        try {
            visitor = null;
            typeHint = null;

            serializer.acceptJsonFormatVisitor(visitor, typeHint);

            visitor = null;
            typeHint = null;

            serializer = new FloatSerializer();

            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider prov = null;
        BeanProperty property = null;

        Base<Object> serializer = new IntegerSerializer(Integer.class);

        try {
            prov = null;
            property = null;

            serializer.createContextual(prov, property);

            prov = null;
            property = null;

            serializer = new FloatSerializer();

            serializer.createContextual(prov, property);
        } catch (JsonMappingException e) {
            fail("JsonMappingException should not be thrown");
        }
    }

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        assertEquals(8, allDeserializers.size());
        assertTrue(allDeserializers.containsKey(Integer.class.getName()));
        assertTrue(allDeserializers.containsKey(Integer.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Long.class.getName()));
        assertTrue(allDeserializers.containsKey(Long.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Byte.class.getName()));
        assertTrue(allDeserializers.containsKey(Byte.TYPE.getName()));
        assertTrue(allDeserializers.containsKey(Short.class.getName()));
        assertTrue(allDeserializers.containsKey(Short.TYPE.getName()));
    }
}