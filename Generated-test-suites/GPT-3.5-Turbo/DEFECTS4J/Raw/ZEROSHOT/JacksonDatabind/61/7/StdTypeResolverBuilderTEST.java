package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.TypeSerializer;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setUp() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        // given
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CUSTOM;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.class, MapperConfig.TYPE_MAPPINGS);
        
        // when
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.init(idType, idRes);

        // then
        assertNull(actual);
        assertEquals(idType, stdTypeResolverBuilder._idType);
        assertEquals(idRes, stdTypeResolverBuilder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }
    
    @Test
    public void testBuildTypeSerializer() {
        // given
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = JavaType.class;
        Collection<NamedType> subtypes = Collection.class;

        // when
        TypeSerializer actual = stdTypeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);

        // then
        assertNull(actual);
    }

    @Test
    public void testBuildTypeDeserializer() {
        // given
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = JavaType.class;
        Collection<NamedType> subtypes = Collection.class;

        // when
        TypeDeserializer actual = stdTypeResolverBuilder.buildTypeDeserializer(config, baseType, subtypes);

        // then
        assertNull(actual);
    }

    @Test
    public void testInclusion() {
        // given
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;

        // when
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.inclusion(includeAs);

        // then
        assertNull(actual);
        assertEquals(includeAs, stdTypeResolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        // given
        String typeIdPropName = "typeIdProp";

        // when
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.typeProperty(typeIdPropName);

        // then
        assertNull(actual);
        assertEquals(typeIdPropName, stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        // given
        Class<?> defaultImpl = Object.class;

        // when
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.defaultImpl(defaultImpl);

        // then
        assertNull(actual);
        assertEquals(defaultImpl, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        // given
        boolean isVisible = true;

        // when
        StdTypeResolverBuilder actual = stdTypeResolverBuilder.typeIdVisibility(isVisible);

        // then
        assertNull(actual);
        assertEquals(isVisible, stdTypeResolverBuilder._typeIdVisible);
    }
}