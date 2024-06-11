package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToTestInstance;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConverter() throws IOException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object value) {
                return (Integer) value;
            }

            @Override
            public Object convertAndSet(Object value, Integer newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object idFromValue(Object value) {
                return value;
            }

            @Override
            public Object idFromSerializationContext(SerializationConfig config,
                    BeanProperty property, JavaType type) {
                return null;
            }
        };

        StdDelegatingSerializer<Integer> serializer = new StdDelegatingSerializer<Integer>(converter);

        assertEquals(Integer.class, serializer.getType());
        assertEquals(converter, serializer.getConverter());
        assertNull(serializer.getDelegateSerializer());
        assertNull(serializer.getDelegateType());

        Integer intValue = 1;
        Object delegateValue = serializer.convertValue(intValue);
        assertEquals(intValue, delegateValue);
        JsonGenerator gen = new TestJsonGenerator();
        serializer.serialize(intValue, gen, new SerializerProvider());
        assertTrue(gen.isCurrentlyActive());
        gen.close();
    }

    @Test
    public void testSerialization() throws IOException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object value) {
                return (Integer) value;
            }

            @Override
            public Object convertAndSet(Object value, Integer newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object idFromValue(Object value) {
                return value;
            }

            @Override
            public Object idFromSerializationContext(SerializationConfig config,
                    BeanProperty property, JavaType type) {
                return null;
            }
        };

        JavaType delegateType = JavaType.construct(Integer.class);
        JsonSerializer<Integer> delegateSerializer = new StdSerializer<Integer>(Integer.class) {};

        StdDelegatingSerializer<Object> serializer = new StdDelegatingSerializer<Object>(converter, delegateType, delegateSerializer);

        assertEquals(Object.class, serializer.getType());
        assertEquals(converter, serializer.getConverter());
        assertEquals(delegateSerializer, serializer.getDelegateSerializer());
        assertEquals(delegateType, serializer.getDelegateType());

        Integer intValue = 1;
        Object delegateValue = serializer.convertValue(intValue);
        assertEquals(intValue, delegateValue);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(intValue);
        Object readValue = mapper.readValue(json, Object.class);
        assertEquals(intValue, readValue);

        JsonGenerator gen = new TestJsonGenerator();
        serializer.serialize(intValue, gen, new SerializerProvider());
        assertTrue(gen.isCurrentlyActive());
        gen.close();
    }

    @Test
    public void testResolve() throws IOException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object value) {
                return (Integer) value;
            }

            @Override
            public Object convertAndSet(Object value, Integer newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object idFromValue(Object value) {
                return value;
            }

            @Override
            public Object idFromSerializationContext(SerializationConfig config,
                    BeanProperty property, JavaType type) {
                return null;
            }
        };

        StdDelegatingSerializer<Integer> serializer = new StdDelegatingSerializer<Integer>(converter);
        SerializerProvider provider = new SerializerProvider() {};
        serializer.resolve(provider);
        assertEquals(serializer.getDelegateSerializer(), provider.findValueSerializer(Integer.class));
    }

    @Test
    public void testCreateContextual() throws IOException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object value) {
                return (Integer) value;
            }

            @Override
            public Object convertAndSet(Object value, Integer newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object idFromValue(Object value) {
                return value;
            }

            @Override
            public Object idFromSerializationContext(SerializationConfig config,
                    BeanProperty property, JavaType type) {
                return null;
            }
        };

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Integer> delegateSerializer = mapper.findValueSerializer(Integer.class);

        StdDelegatingSerializer<Integer> serializer = new StdDelegatingSerializer<Integer>(converter);
        Object serializerInstance = serializer.createContextual(new SerializerProvider(), new AnnotatedClass<Integer>(Integer.class));
        assertEquals(serializer, serializerInstance);

        StdDelegatingSerializer<Integer> subSerializer = serializer.withDelegate(converter, delegateType, delegateSerializer);
        Object subSerializerInstance = subSerializer.createContextual(new SerializerProvider(), new AnnotatedClass<Integer>(Integer.class));
        assertEquals(subSerializer, subSerializerInstance);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        Converter<Object, Integer> converter = new Converter.StdConverter<Object, Integer>() {
            @Override
            public Integer convert(Object value) {
                return (Integer) value;
            }

            @Override
            public Object convertAndSet(Object value, Integer newValue) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Object idFromValue(Object value) {
                return value;
            }

            @Override
            public Object idFromSerializationContext(SerializationConfig config,
                    BeanProperty property, JavaType type) {
                return null;
            }
        };

        StdDelegatingSerializer<Integer> serializer = new StdDelegatingSerializer<Integer>(converter);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonObjectCodec(), null);
        serializer.acceptJsonFormatVisitor(visitor, JavaType.construct(Integer.class));
        assertNotNull(visitor.getSchemaProvider());
        assertNull(visitor.getSerializerProvider());
        JsonNode schema = visitor.getSchemaProvider().getSchema(new SerializerProvider(), JavaType.construct(Integer.class));
        assertNotNull(schema);
        assertTrue(schema.isObject());
        SchemaAware schemaAware = (SchemaAware) serializer.getDelegateSerializer();
        JsonNode schemaFromDelegate = schemaAware.getSchema(new SerializerProvider(), JavaType.construct(Integer.class));
        assertEquals(schema, schemaFromDelegate);
    }
}

class TestJsonGenerator extends JsonGenerator {
    private boolean active = false;

    public TestJsonGenerator() {
        super(new TestOutputFactory());
    }

    @Override
    public void flush() throws IOException {
        active = false;
        super.flush();
    }

    @Override
    public void close() throws IOException {
        active = false;
        super.close();
    }

    @Override
    public void writeStartObject() throws IOException {
        active = true;
        super.writeStartObject();
    }

    @Override
    public void writeEndObject() throws IOException {
        active = false;
        super.writeEndObject();
    }

    @Override
    public void writeStartArray() throws IOException {
        active = true;
        super.writeStartArray();
    }

    @Override
    public void writeEndArray() throws IOException {
        active = false;
        super.writeEndArray();
    }

    public boolean isCurrentlyActive() {
        return active;
    }
}

class TestOutputFactory extends OutputFactory {
    @Override
    public JsonGenerator createGenerator(String featureName, JsonGenerator.Feature feature) {
        return new TestJsonGenerator();
    }
}