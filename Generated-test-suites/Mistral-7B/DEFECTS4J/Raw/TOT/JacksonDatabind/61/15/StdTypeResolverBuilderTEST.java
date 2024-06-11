package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * Test class for {@link StdTypeResolverBuilder}.
 */
public class StdTypeResolverBuilderTest {

    /**
     * Test method for {@link StdTypeResolverBuilder#noTypeInfoBuilder()}.
     */
    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getCustomIdResolver());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#init(JsonTypeInfo.Id, TypeIdResolver)}.
     */
    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(String.class, new MapperFactory(new ObjectMapper()));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder.getIdType());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertEquals(JsonTypeInfo.Property.STANDARD_BEAN, builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#buildTypeSerializer(SerializationConfig, JavaType, Collection)}.
     */
    @Test
    public void testBuildTypeSerializer() throws JsonProcessingException {
        MapperFactory mapperFactory = new MapperFactory(new ObjectMapper());
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String[].class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleNamedType(String.class, String.class.getName()));
        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapperFactory.getSerializationConfig(), javaType, subtypes);
        assertNotNull(typeSerializer);
        ObjectNode node = objectMapper.createObjectNode();
        node.putPOJO("foo", new String[]{"one", "two"});
        ObjectNode serializedNode = objectMapper.writeValueAsTree(node.deepClone());
        ObjectNode deserializedNode = objectMapper.readValue(serializedNode, String[].class);
        assertArrayEquals(new String[]{"one", "two"}, deserializedNode);
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#buildTypeDeserializer(DeserializationConfig, JavaType, Collection)}.
     */
    @Test
    public void testBuildTypeDeserializer() throws JsonProcessingException {
        MapperFactory mapperFactory = new MapperFactory(new ObjectMapper());
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleNamedType(String.class, String.class.getName()));
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapperFactory.getDeserializationConfig(), javaType, subtypes);
        assertNotNull(typeDeserializer);
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", "String");
        node.put("value", "one");
        ObjectNode deserializedNode = objectMapper.readValue(node, String.class);
        assertEquals("one", deserializedNode);
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#inclusion(JsonTypeInfo.As)}.
     */
    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
        builder.inclusion(includeAs);
        assertEquals("PROPERTY", builder.getIncludeAs().name());
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#typeProperty(String)}.
     */
    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeProperty = "type";
        builder.typeProperty(typeProperty);
        assertEquals(typeProperty, builder.getTypeProperty());
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#defaultImpl(Class)}.
     */
    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    /**
     * Test method for {@link StdTypeResolverBuilder#typeIdVisibility(boolean)}.
     */
    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);
        assertEquals(isVisible, builder.isTypeIdVisible());
    }

    /**
     * Test method for custom id resolver.
     */
    @Test
    public void testCustomIdResolver() throws JsonProcessingException {
        MapperFactory mapperFactory = new MapperFactory(new ObjectMapper());
        ObjectMapper objectMapper = mapperFactory.getObjectMapper();
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.NAME;
        String customTypeId = "customType";
        TypeIdResolver idRes = new CustomTypeIdResolver();
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(
            new SimpleNamedType(String.class, customTypeId));
        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapperFactory.getSerializationConfig(), javaType, subtypes);
        ObjectNode node = objectMapper.createObjectNode();
        node.putPOJO("foo", new CustomBean());
        ObjectNode serializedNode = objectMapper.writeValueAsTree(node.deepClone());
        ObjectNode deserializedNode = objectMapper.readValue(serializedNode, String.class);
        assertEquals("customType", deserializedNode);
    }
}

class CustomBean {

    @JsonTypeName("customType")
    private String name;

    @JsonDeserialize(using = CustomDeserializer.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class CustomDeserializer extends StdDeserializer<String> {

    public CustomDeserializer() {
        this(null);
    }

    protected CustomDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        String name = jp.getText();
        return name;
    }
}

class CustomTypeIdResolver extends TypeIdResolverBase {

    @Override
    public JavaType idFromValue(SerializerProvider serializers, TypeResolverProvider typeResolvers, Object value)
            throws JsonMappingException {
        return ClassIdType.fromValue(value.getClass());
    }
}