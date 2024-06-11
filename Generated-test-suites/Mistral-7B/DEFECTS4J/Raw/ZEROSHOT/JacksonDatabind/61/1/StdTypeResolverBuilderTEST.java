package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
        mapper = new ObjectMapper();
    }

    @Test
    public void testDefaultConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInitWithNullIdType() {
        try {
            builder.init(null, null);
            fail("Expected IllegalArgumentException with null idType");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInitWithNullIdRes() {
        try {
            builder.init(JsonTypeInfo.Id.CLASS, null);
            fail("Expected IllegalArgumentException with null idRes");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testInitWithValidArguments() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertSame(JsonTypeInfo.Id.CLASS, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testBuildTypeSerializerWithNullConfig() {
        try {
            builder.buildTypeSerializer(null, JavaType.valueOf(String.class), null);
            fail("Expected IllegalArgumentException with null config");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuildTypeSerializerWithValidArguments() {
        JsonTypeInfo info = new JsonTypeInfo.Builder()
                .usage(JsonTypeInfo.Id.CLASS)
                .build();
        MapperConfig config = new MapperConfig(mapper(), info);
        JavaType baseType = JavaType.valueOf(List.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeSerializer typeSerializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithIdTypeNone() {
        builder.init(JsonTypeInfo.Id.NONE, null);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(null, JavaType.valueOf(String.class), null);
        assertNull(typeSerializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNullConfig() {
        try {
            builder.buildTypeDeserializer(null, JavaType.valueOf(String.class), null);
            fail("Expected IllegalArgumentException with null config");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testBuildTypeDeserializerWithValidArguments() {
        JsonTypeInfo info = new JsonTypeInfo.Builder()
                .usage(JsonTypeInfo.As.PROPERTY)
                .build();
        DeserializationConfig config = mapper.getConfig();
        JavaType baseType = JavaType.valueOf(List.class);
        Collection<NamedType> subtypes = new ArrayList<>();

        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testTypeProperty() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeProperty("myTypeProperty");
        assertEquals("myTypeProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.defaultImpl(String.class);
        assertEquals(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testInclusion() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
    }

    @Test
    public void testGetters() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
        assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
        assertEquals("", builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }
}

class MyCustomIdResolver implements TypeIdResolver {
    @Override
    public TypeId idFromValue(Object value) {
        return null;
    }

    @Override
    public TypeId idFromValueAndType(Object value, JavaType type) {
        return null;
    }
}

class MyConverter extends StdConverter<String, Integer> {
    @Override
    public Integer convert(String text) {
        return Integer.parseInt(text);
    }
}

class MyMapperFactory extends SimpleTypeSerializerFactory {
    @Override
    public TypeSerializer findValueSerializer(SerializerProvider provider, JavaType type) {
        return null;
    }

    @Override
    public TypeSerializer findContentSerializer(SerializerProvider provider, JavaType type) {
        return null;
    }

    @Override
    public TypeSerializer findSerializer(SerializerProvider provider, JavaType type) {
        return new SimpleTypeSerializer(provider.getDefaultInstanceFactory(), type);
    }

    @Override
    public TypeSerializer findValueDeserializer(DeserializationConfig config, JavaType type) {
        return new MinimalStdValueDeserializer(config, type);
    }

    @Override
    public JsonSerializer<?> findValueSerializer(JsonSerializerProvider serializers, JavaType type,
            BeanProperty property, TypeSerializer valueSerializer) {
        return new MyConverter();
    }
}

class MyMapper extends ObjectMapper {
    @Override
    protected List<SimpleSerializerFactory> getDefaultSerializerFactories() {
        return new ArrayList<>(Arrays.asList(new MyMapperFactory()));
    }

    @Override
    protected List<SimpleDeserializerFactory> getDefaultDeserializerFactories() {
        return new ArrayList<>(Arrays.asList(new MyMapperFactory()));
    }

    public static ObjectMapper mapper() {
        return new MyMapper();
    }
}