package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest extends TestBase {

    @Test
    public void testEmptyConstructor() throws Exception {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        assertNull(serializer.getDelegateSerializer());
        assertNull(serializer.getDelegateType());
    }

    @Test
    public void testObjectConstructor() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(Object.class, converter, javaType, mapper.findValueSerializer(String.class));
        assertEquals(String.class, serializer.getDelegateType().getRawClass());
        assertEquals(mapper.findValueSerializer(String.class), serializer.getDelegateSerializer());
    }

    @Test
    public void testDelegateConstructor() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        assertEquals(String.class, serializer.getDelegateType().getRawClass());
        assertEquals(delegateSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testResolve() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        SerializerProvider provider = mapper.getSerializerProvider();
        serializer.resolve(provider);
        assertEquals(delegateSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testCreateContextual() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        assertEquals(delegateSerializer, serializer.createContextual(provider, null));
    }

    @Test
    public void testSerialize() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        ObjectMapper mapper = new ObjectMapper();
        Object value = "test";
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        serializer.serialize(value, gen, provider);
        assertEquals("\"test\"", gen.getOutputContentAsString());
        gen.close();
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        ObjectMapper mapper = new ObjectMapper();
        Object value = "test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonSerializer<Object> delegateSerializerWithType = new DelegatingSerializerWithType(serializer, mapper.getTypeFactory().constructType(String.class));
        delegateSerializerWithType.serialize(value, gen, provider, mapper.getPolymorphicSerializer(javaType));
        assertEquals("\"test\"", gen.getOutputContentAsString());
        gen.close();
    }

    @Test
    public void testGetConverter() {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        assertEquals(converter, serializer.getConverter());
    }

    @Test
    public void testGetDelegatee() {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        assertEquals(delegateSerializer, serializer.getDelegatee());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        JsonFormatVisitorWrapper visitor = mapper.getPolymorphicSerializerProvider().getSchemaFactory().getJsonSchemaModelParser().getSchemaVisitor(Object.class, null, null, null);
        serializer.acceptJsonFormatVisitor(visitor, javaType);
    }

    @Test
    public void testSchemaAware() throws Exception {
        Converter<Object, String> converter = mapper.constructorForType(Converter.class);
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        JsonSerializer<Object> delegateSerializer = mapper.findValueSerializer(String.class);
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, javaType, delegateSerializer);
        SchemaAware delegateSerializerWithSchema = new SchemaAwareSerializer(delegateSerializer);
        assertEquals(delegateSerializerWithSchema.getSchema(provider, javaType), serializer.getSchema(provider, javaType));
    }

    static class DelegatingSerializerWithType extends StdDelegatingSerializer {
        public DelegatingSerializerWithType(StdDelegatingSerializer serializer, JavaType delegateType) {
            super(serializer.getConverter(), null, serializer, delegateType);
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
            super.serialize(value, gen, provider);
        }
    }

    static class SchemaAwareSerializer extends JsonSerializer<String> implements SchemaAware {
        private final JsonSerializer<String> delegateSerializer;

        public SchemaAwareSerializer(JsonSerializer<String> delegateSerializer) {
            this.delegateSerializer = delegateSerializer;
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            delegateSerializer.serialize(value, gen, provider);
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
            return null;
        }

        @Override
        public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional) throws JsonMappingException {
            return null;
        }
    }
}