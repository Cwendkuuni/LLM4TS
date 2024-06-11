package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaTypeFactory;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.As;
import com.fasterxml.jackson.databind.jsontype.ClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.IdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeNameIdResolver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

	private static ObjectMapper mapper;

	@Before
	public void setup() {
		mapper = new ObjectMapper();
	}

	@After
	public void teardown() {
		mapper = null;
	}

	@Test
	public void testDefaultInstance() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		assertNull(builder.getDefaultImpl());
		assertNull(builder.getTypeProperty());
		assertFalse(builder.isTypeIdVisible());
		assertNull(builder._customIdResolver);
	}

	@Test
	public void testInit() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		assertEquals(idType, builder._idType);
		assertEquals(idResolver, builder._customIdResolver);
		assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
		assertFalse(builder.isTypeIdVisible());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullIdType() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(null, null);
	}

	@Test
	public void testBuildTypeSerializerNullIdType() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(JsonTypeInfo.Id.NONE, null);
		assertNull(builder.buildTypeSerializer(null, null, Collections.emptyList()));
	}

	@Test
	public void testBuildTypeSerializerClassType() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, new ClassIdResolver.NumberId("string")) {});
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
	}

	@Test
	public void testBuildTypeSerializerNoneType() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(JsonTypeInfo.Id.NONE, null);
		assertNull(builder.buildTypeSerializer(null, null, Collections.emptyList()));
	}

	@Test(expected = IllegalStateException.class)
	public void testInvalidIncludeAs() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(JsonTypeInfo.Id.CLASS, null);
		builder.inclusion(JsonTypeInfo.As.INVALID);
		builder.buildTypeSerializer(null, null, Collections.emptyList());
	}

	@Test
	public void testBuildTypeSerializerWrapperArray() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String[].class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.inclusion(As.WRAPPER_ARRAY);
		JavaType baseType = JavaType.construct(String[].class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
		assertInstanceOf(AsArrayTypeSerializer.class, typeSerializer);
	}

	@Test
	public void testBuildTypeSerializerProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
		assertInstanceOf(AsPropertyTypeSerializer.class, typeSerializer);
	}

	@Test
	public void testBuildTypeSerializerWrapperObject() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.inclusion(As.WRAPPER_OBJECT);
		JavaType baseType = JavaType.construct(Map.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
		assertInstanceOf(AsWrapperTypeSerializer.class, typeSerializer);
	}

	@Test
	public void testBuildTypeSerializerExternalProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.EXTERNAL_PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
		assertInstanceOf(AsExternalTypeSerializer.class, typeSerializer);
	}

	@Test
	public void testBuildTypeSerializerExistingProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.EXISTING_PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeSerializer typeSerializer = builder.buildTypeSerializer(new SerializationConfig(), baseType, subtypes);
		assertNotNull(typeSerializer);
		assertInstanceOf(AsExistingPropertyTypeSerializer.class, typeSerializer);
	}

	@Test
	public void testBuildTypeDeserializerNullIdType() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(JsonTypeInfo.Id.NONE, null);
		assertNull(builder.buildTypeDeserializer(null, null, Collections.emptyList()));
	}

	@Test
	public void testBuildTypeDeserializerClassType() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, new ClassIdResolver.NumberId("int")) {});
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
	}

	@Test
	public void testBuildTypeDeserializerNoneType() {
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(JsonTypeInfo.Id.NONE, null);
		assertNull(builder.buildTypeDeserializer(null, null, Collections.emptyList()));
	}

	@Test
	public void testBuildTypeDeserializerWrapperArray() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String[].class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.inclusion(As.WRAPPER_ARRAY);
		JavaType baseType = JavaType.construct(String[].class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
		assertInstanceOf(AsArrayTypeDeserializer.class, typeDeserializer);
	}

	@Test
	public void testBuildTypeDeserializerProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
		assertInstanceOf(AsPropertyTypeDeserializer.class, typeDeserializer);
	}

	@Test
	public void testBuildTypeDeserializerWrapperObject() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.inclusion(As.WRAPPER_OBJECT);
		JavaType baseType = JavaType.construct(Map.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
		assertInstanceOf(AsWrapperTypeDeserializer.class, typeDeserializer);
	}

	@Test
	public void testBuildTypeDeserializerExternalProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.EXTERNAL_PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
		assertInstanceOf(AsExternalTypeDeserializer.class, typeDeserializer);
	}

	@Test
	public void testBuildTypeDeserializerExistingProperty() {
		JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
		TypeIdResolver idResolver = new ClassNameIdResolver(JavaType.construct(String.class), mapper.getTypeFactory());
		StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
		builder.init(idType, idResolver);
		builder.typeProperty("typeProperty");
		builder.inclusion(As.EXISTING_PROPERTY);
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Collections.emptyList();
		TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(new DeserializationConfig(), baseType, subtypes);
		assertNotNull(typeDeserializer);
		assertInstanceOf(AsExistingPropertyTypeDeserializer.class, typeDeserializer);
	}

	@Test
	public void testIdResolver() {
		MapperConfig<Object> config = new MapperConfig<>();
		JavaType baseType = JavaType.construct(String.class);
		Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, new ClassIdResolver.NumberId("int")) {});
		IdResolver idResolver = new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null).idResolver(config, baseType, subtypes, false, true);
		assertSame(new ClassNameIdResolver(baseType, config.getTypeFactory()), idResolver);
	}
}