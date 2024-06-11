package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

public class StdDelegatingSerializerTest {

    @Test
    public void testConstructors() {
        // Test no-args constructor
        Converter<Object, ?> converter = null;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertNull(serializer._delegateType);
        Assert.assertNull(serializer._delegateSerializer);

        // Test constructor with single argument
        Object delegateValue = new Object();
        Converter<Object, Object> converter1 = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = null;
        StdDelegatingSerializer serializer1 = new StdDelegatingSerializer(converter1, delegateType, delegateSerializer);
        Assert.assertEquals(delegateType, serializer1._delegateType);
        Assert.assertNull(serializer1._delegateSerializer);

        // Test constructor with three arguments
        Object delegateValue2 = new Object();
        Converter<Object, ?> converter2 = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue2;
            }
        };
        JavaType delegateType2 = JavaType.constructType(Object.class, String.class);
        JsonSerializer<Object> delegateSerializer2 = new StdSerializer<String>(String.class) {};
        StdDelegatingSerializer serializer2 = new StdDelegatingSerializer(converter2, delegateType2, delegateSerializer2);
        Assert.assertEquals(delegateType2, serializer2._delegateType);
        Assert.assertEquals(delegateSerializer2, serializer2._delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        SerializerProvider provider = new SerializerProvider();
        serializer.resolve(provider);
        JsonSerializer<Object> resolvedDelegateSerializer = serializer.getDelegatee();
        Assert.assertEquals(delegateSerializer, resolvedDelegateSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonSerializer<?> contextualSerializer = serializer.createContextual(provider, null);
        Assert.assertEquals(delegateSerializer, contextualSerializer);

        // Test with property
        BeanProperty property = new BeanProperty("test", Object.class);
        contextualSerializer = serializer.createContextual(provider, property);
        Assert.assertEquals(serializer, contextualSerializer);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("delegate");
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(null, gen, new SerializerProvider());
        Assert.assertEquals("delegate", gen.getOutputContent().toString());

        serializer.serialize(delegateValue, gen, new SerializerProvider());
        Assert.assertEquals("delegate", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, IOException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {
            @Override
            public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSerializer) throws IOException {
                gen.writeString("delegate");
                typeSerializer.writeTypePrefix(delegateType, gen);
                typeSerializer.writeTypeSuffix(gen);
            }
        };
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();
        serializer.serializeWithType(null, gen, new SerializerProvider(), typeSerializer);
        Assert.assertEquals("delegate", gen.getOutputContent().toString());
        Assert.assertEquals("java.lang.Object", gen.getOutputContent().toString().split(":")[1]);

        serializer.serializeWithType(delegateValue, gen, new SerializerProvider(), typeSerializer);
        Assert.assertEquals("delegate", gen.getOutputContent().toString());
        Assert.assertEquals("java.lang.Object", gen.getOutputContent().toString().split(":")[1]);
    }

    @Test
    public void testIsEmpty() {
        Object delegateValue1 = null;
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue1;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), null));

        delegateValue1 = new Object();
        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), delegateValue1));

        delegateValue1 = "";
        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), delegateValue1));

        delegateValue1 = new byte[0][];
        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), delegateValue1));

        delegateValue1 = new byte[0];
        Assert.assertFalse(serializer.isEmpty(null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), null));
        Assert.assertFalse(serializer.isEmpty(new SerializerProvider(), delegateValue1));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        SerializerProvider provider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(provider, null);
        Assert.assertNull(schema);

        schema = serializer.getSchema(provider, Object.class);
        Assert.assertNull(schema);

        schema = serializer.getSchema(provider, String.class);
        Assert.assertNull(schema);

        JsonSchema schema1 = new JsonSchema.StdSchema(Object.class);
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper() {
            @Override
            public void visitObjectSchema(JsonSchema schema) {
                serializer.getDelegateSerializer().acceptJsonFormatVisitor(this, schema.getType());
            }
        }, delegateType);
        Assert.assertEquals(delegateSerializer.getSchema(provider, delegateType), schema1);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Object delegateValue = new Object();
        Converter<Object, ?> converter = new Converter.StdConverter<>(Object.class, Object.class) {
            @Override
            public Object convert(Object src) {
                return delegateValue;
            }
        };
        JavaType delegateType = Object.class;
        JsonSerializer<Object> delegateSerializer = new StdSerializer<Object>(Object.class) {};
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper() {
            private JsonNode result;

            @Override
            public void visitObjectFormat(JsonObjectFormat visitor, JavaType typeHint) throws JsonMappingException {
                result = visitor.visitObjectSchema(new JsonSchema.StdSchema(typeHint));
            }
        };
        serializer.acceptJsonFormatVisitor(visitor, Object.class);
        Assert.assertNull(result);

        JsonSchema schema = new JsonSchema.StdSchema(Object.class);
        serializer.getDelegateSerializer().acceptJsonFormatVisitor(visitor, schema.getType());
        Assert.assertEquals(delegateSerializer.getSchema(new SerializerProvider(), schema.getType()), result);
    }
}