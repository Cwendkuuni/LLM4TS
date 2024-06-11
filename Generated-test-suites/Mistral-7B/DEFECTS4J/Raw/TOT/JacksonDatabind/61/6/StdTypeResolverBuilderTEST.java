package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializerImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        MapperFactory factory = new MapperFactory();
        factory.findAndRegisterModules();
        mapper = factory.getObjectMapper();
    }

    @Test
    public void testStdTypeResolverBuilderDefault() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        StdTypeResolver resolver = builder.build();
        assertNull(resolver.getDefaultImpl());
        assertNull(resolver.getTypeIdPropertyName());
        assertFalse(resolver.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilderCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(Id.CUSTOM, new MyTypeIdResolver())
                .build();
        assertNotNull(builder.getIdResolver());
    }

    @Test
    public void testStdTypeResolverBuilderTypeIdProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .typeProperty("customProperty")
                .build();
        assertEquals("customProperty", builder.getTypeIdPropertyName());
    }

    @Test
    public void testStdTypeResolverBuilderDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .defaultImpl(MyClass.class)
                .build();
        assertEquals(MyClass.class, builder.getDefaultImpl());
    }

    @Test
    public void testStdTypeResolverBuilderTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .typeIdVisibility(true)
                .build();
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(As.PROPERTY)
                .build();
        assertSame(TypeSerializer.PropertyBasedTypeSerializer.class,
                builder.getTypeSerializer(createConfig(), MyClass.class, null).getClass());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsWrapperObject() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(As.WRAPPER_OBJECT)
                .build();
        assertSame(TypeSerializerWrapper.class,
                builder.getTypeSerializer(createConfig(), MyClass.class, null).getClass());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsWrapperArray() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(As.WRAPPER_ARRAY)
                .build();
        assertSame(TypeSerializerWrapperArray.class,
                builder.getTypeSerializer(createConfig(), MyClass[].class, null).getClass());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsExternalProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(As.EXTERNAL_PROPERTY)
                .build();
        assertSame(TypeSerializerExternalProperty.class,
                builder.getTypeSerializer(createConfig(), MyClass.class, null).getClass());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsExistingProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .inclusion(As.EXISTING_PROPERTY)
                .build();
        assertSame(TypeSerializerExistingProperty.class,
                builder.getTypeSerializer(createConfig(), MyClass.class, null).getClass());
    }

    @Test
    public void testStdTypeResolverBuilderIncludeAsNone() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(Id.NONE, null)
                .build();
        assertNull(builder.getTypeSerializer(createConfig(), MyClass.class, null));
    }

    @Test
    public void testStdTypeResolverBuilderTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .typeIdProperty("typeId")
                .defaultImpl(MyClass.class)
                .build();
        TypeDeserializer deserializer = builder.getTypeDeserializer(createConfig(),
                MyClass.class, null);
        assertEquals(MyClass.class, deserializer.deserializeTypedValue(mapper.readTree("{typeId: MyClass}"), null).getValueClass());
    }

    private SerializationConfig createConfig() {
        return mapper.getSerializationConfig();
    }

    static class MyClass {}

    static class MyTypeIdResolver implements TypeIdResolver {
        @Override
        public String idFromValue(Object value) {
            return "MyCustomValue";
        }

        @Override
        public Class<?> idFromValueAndType(Class<?> cls, Object value) {
            return cls;
        }
    }

    static class MyTypeSerializerWrapper implements TypeSerializer {
        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                TypeSerializer containerSerializer, JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            // no-op
        }

        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            // no-op
        }

        @Override
        public JsonSerializer<Object> createContextSerializer(SerializerProvider provider) {
            return null;
        }

        @Override
        public void serializeContents(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            generator.writeStartObject();
            generator.writeFieldName("typeId");
            generator.writeValue("MyWrapperObject");
            generator.writeEndObject();
        }
    }

    static class MyTypeSerializerWrapperArray implements TypeSerializer {
        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                TypeSerializer containerSerializer, JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            // no-op
        }

        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            generator.writeStartArray();
            generator.writeStartObject();
            generator.writeFieldName("typeId");
            generator.writeValue("MyWrapperArray");
            generator.writeEndObject();
            generator.writeEndArray();
        }

        @Override
        public JsonSerializer<Object> createContextSerializer(SerializerProvider provider) {
            return null;
        }
    }

    static class MyTypeSerializerExternalProperty implements TypeSerializer {
        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                TypeSerializer containerSerializer, JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            // no-op
        }

        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            generator.writeFieldName("typeId");
            generator.writeValue("MyExternalProperty");
        }

        @Override
        public JsonSerializer<Object> createContextSerializer(SerializerProvider provider) {
            return null;
        }
    }

    static class MyTypeSerializerExistingProperty implements TypeSerializer {
        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                TypeSerializer containerSerializer, JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            // no-op
        }

        @Override
        public void serializeValue(SerializerProvider provider, Object value, TypeSerializer valueSerializer,
                JsonGenerator generator, Serializers.SerializerProvider serializers) throws IOException {
            generator.writeFieldName("typeId");
            generator.writeValue("MyExistingProperty");
        }

        @Override
        public JsonSerializer<Object> createContextSerializer(SerializerProvider provider) {
            return null;
        }
    }
}