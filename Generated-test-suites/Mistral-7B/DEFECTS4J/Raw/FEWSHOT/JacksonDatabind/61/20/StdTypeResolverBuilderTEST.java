package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        builder.init(idType, idRes);
        assertEquals(idType, builder._idType);
        assertEquals(idRes, builder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, TypeIdResolver.noIdResolver());
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);

        builder.init(idType, idRes);
        TypeSerializer serializer = builder.buildTypeSerializer(new MapperConfig(), baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, TypeIdResolver.noIdResolver());
        builder.buildTypeSerializer(new MapperConfig(), JavaType.construct(int[].class), null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        builder.init(idType, idRes);
        builder.buildTypeSerializer(new MapperConfig(), null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        builder.init(idType, idRes);
        builder.buildTypeSerializer(new MapperConfig(), baseType, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnsupportedInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);
        builder.init(idType, idRes);
        builder.buildTypeSerializer(new MapperConfig(), baseType, subtypes);
        builder._includeAs = JsonTypeInfo.As.CUSTOM;
        builder.buildTypeSerializer(new MapperConfig(), baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);

        builder.init(idType, idRes);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(new MapperConfig(), baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, TypeIdResolver.noIdResolver());
        builder.buildTypeDeserializer(new MapperConfig(), JavaType.construct(int[].class), null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        builder.init(idType, idRes);
        builder.buildTypeDeserializer(new MapperConfig(), null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithNullSubtypes() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        builder.init(idType, idRes);
        builder.buildTypeDeserializer(new MapperConfig(), baseType, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnsupportedInclusionType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
        TypeIdResolver idRes = TypeIdResolver.noIdResolver();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);
        builder.init(idType, idRes);
        builder.buildTypeDeserializer(new MapperConfig(), baseType, subtypes);
        builder._includeAs = JsonTypeInfo.As.CUSTOM;
        builder.buildTypeDeserializer(new MapperConfig(), baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        builder.inclusion(includeAs);
        assertEquals(includeAs, builder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "typeId";
        builder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, builder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);
        TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, true);
        assertEquals(TypeIdResolver.noIdResolver(), idRes);

        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        builder.init(idType, null);
        idRes = builder.idResolver(config, baseType, subtypes, true, true);
        assertNotNull(idRes);
    }

    @Test
    public void testBuildTypeSerializerWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);
        TypeIdResolver idRes = new CustomTypeIdResolver();
        builder.init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializerWithCustomIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig config = new MapperConfig();
        JavaType baseType = JavaType.construct(int[].class);
        Collection<NamedType> subtypes = JavaType.constructCollectionType(List.class, int.class);
        TypeIdResolver idRes = new CustomTypeIdResolver();
        builder.init(JsonTypeInfo.Id.PROPERTY, idRes);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @JsonSerialize(using = ToStringSerializer.class)
    static class CustomType {
        private int value;

        public CustomType() {}

        public CustomType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    @JsonDeserialize(using = CustomTypeDeserializer.class)
    static class CustomTypeDeserializer extends JsonDeserializer<CustomType> {
        @Override
        public CustomType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            int value = jp.getIntValue();
            CustomType customType = new CustomType();
            customType.setValue(value);
            return customType;
        }
    }

    static class CustomTypeIdResolver extends TypeIdResolverBase {
        @Override
        public TypeId idFromValue(Object value, JavaType baseType) {
            if (value instanceof CustomType) {
                return new TypeIdImpl(JsonTypeInfo.Id.CUSTOM, "CustomType");
            }
            return null;
        }
    }
}

class CustomTypeSerializer extends StdSerializer<CustomType> {
    public CustomTypeSerializer() {
        this(null);
    }

    public CustomTypeSerializer(Class<CustomType> t, AnnotationIntrospector ai) {
        super(t, ai);
    }

    @Override
    public void serialize(CustomType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeNumber(value.getValue());
        }
    }
}

class CustomTypeDeserializer extends JsonDeserializer<CustomType> {
    @Override
    public CustomType deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        int value = jp.getIntValue();
        CustomType customType = new CustomType();
        customType.setValue(value);
        return customType;
    }
}