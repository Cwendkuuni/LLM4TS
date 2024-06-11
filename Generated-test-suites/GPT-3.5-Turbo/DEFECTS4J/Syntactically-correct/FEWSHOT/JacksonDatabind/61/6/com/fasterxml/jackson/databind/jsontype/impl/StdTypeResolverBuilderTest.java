package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver_Basic;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder resolverBuilder;

    @Before
    public void setUp() {
        resolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        TypeResolverBuilder<StdTypeResolverBuilder> initResult = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(initResult);

        JsonTypeInfo.Id idType = ClassUtil.findEnumType(JsonTypeInfo.Id.class,
                JsonTypeInfo.Id.CLASS.name());
        assertNotNull(idType);
        assertEquals(idType, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);

        String defaultPropertyName = idType.getDefaultPropertyName();
        assertNotNull(defaultPropertyName);
        assertEquals(defaultPropertyName, resolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        resolverBuilder.init(null, null);
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        TypeSerializer typeSerializer = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .buildTypeSerializer(config, baseType, subtypes);

        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);
    }

    @Test
    public void testBuildTypeSerializerWithNoneIdType() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        TypeSerializer typeSerializer = resolverBuilder.init(JsonTypeInfo.Id.NONE, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_OBJECT)
                .buildTypeSerializer(config, baseType, subtypes);

        assertNull(typeSerializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithUnknownIncludeAs() {
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXISTING_PROPERTY)
                .buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        TypeDeserializer typeDeserializer = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .buildTypeDeserializer(config, baseType, subtypes);

        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);
    }

    @Test
    public void testBuildTypeDeserializerWithNoneIdType() {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        TypeDeserializer typeDeserializer = resolverBuilder.init(JsonTypeInfo.Id.NONE, null)
                .inclusion(JsonTypeInfo.As.PROPERTY)
                .buildTypeDeserializer(config, baseType, subtypes);

        assertNull(typeDeserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithUnknownIncludeAs() {
        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.EXISTING_PROPERTY)
                .buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        TypeResolverBuilder<StdTypeResolverBuilder> inclusionResult = resolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertNotNull(inclusionResult);

        JsonTypeInfo.As includeAs = ClassUtil.findEnumType(JsonTypeInfo.As.class,
                JsonTypeInfo.As.EXISTING_PROPERTY.name());
        assertNotNull(includeAs);
        assertEquals(includeAs, resolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        resolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "testType";
        TypeResolverBuilder<StdTypeResolverBuilder> typePropertyResult = resolverBuilder.typeProperty(typeIdPropName);
        assertNotNull(typePropertyResult);
        assertEquals(typeIdPropName, resolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithDefaultImpl() {
        TypeFactory typeFactory = TypeFactory.defaultInstance();
        JavaType baseType = SimpleType.construct(Integer.class);
        Class<?> defaultImpl = String.class;
        JavaType expectedDefaultImpl = typeFactory.constructSpecializedType(baseType, defaultImpl);

        resolverBuilder.defaultImpl(defaultImpl);

        DeserializationConfig config = new ObjectMapper().getDeserializationConfig();
        Collection<NamedType> subtypes = Collections.emptyList();
        TypeDeserializer typeDeserializer = resolverBuilder.init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY)
                .buildTypeDeserializer(config, baseType, subtypes);

        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
        AsWrapperTypeDeserializer wrapperTypeDeserializer = (AsWrapperTypeDeserializer) typeDeserializer;
        assertEquals(expectedDefaultImpl, wrapperTypeDeserializer.getDefaultImpl());
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;
        TypeResolverBuilder<StdTypeResolverBuilder> defaultImplResult = resolverBuilder.defaultImpl(defaultImpl);
        assertNotNull(defaultImplResult);
        assertEquals(defaultImpl, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        TypeResolverBuilder<StdTypeResolverBuilder> typeIdVisibilityResult = resolverBuilder.typeIdVisibility(isVisible);
        assertNotNull(typeIdVisibilityResult);
        assertEquals(isVisible, resolverBuilder._typeIdVisible);
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        TypeIdResolver typeIdResolver = resolverBuilder.idResolver(config, baseType, subtypes, false, true);

        assertTrue(typeIdResolver instanceof ClassNameIdResolver);
    }

    @Test
    public void testIdResolverWithNoneIdType() {
        MapperConfig<?> config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        resolverBuilder.init(JsonTypeInfo.Id.NONE, null);
        TypeIdResolver typeIdResolver = resolverBuilder.idResolver(config, baseType, subtypes, false, true);

        assertNull(typeIdResolver);
    }

    @Test(expected = IllegalStateException.class)
    public void testIdResolverWithUnknownIdType() {
        MapperConfig<?> config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = SimpleType.construct(Integer.class);
        Collection<NamedType> subtypes = Collections.emptyList();

        resolverBuilder.init(JsonTypeInfo.Id.CUSTOM, null);
        resolverBuilder.idResolver(config, baseType, subtypes, false, true);
    }
}