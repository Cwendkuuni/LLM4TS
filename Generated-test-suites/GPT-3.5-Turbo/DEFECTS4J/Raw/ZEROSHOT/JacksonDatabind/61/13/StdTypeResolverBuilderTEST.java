package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;

public class StdTypeResolverBuilderTest {

	private StdTypeResolverBuilder stdTypeResolverBuilder;
	private MapperConfig<?> config;
	
	@Before
	public void setUp() {
		stdTypeResolverBuilder = new StdTypeResolverBuilder();
		config = new MapperConfig<>();
	}
	
	@Test
	public void testInit() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
		assertNotNull(result);
		assertEquals(JsonTypeInfo.Id.CLASS, stdTypeResolverBuilder._idType);
		assertNull(stdTypeResolverBuilder._customIdResolver);
		assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInitWithNullIdType() {
		stdTypeResolverBuilder.init(null, null);
	}
	
	@Test
	public void testBuildTypeSerializerWithTypeAsNone() {
		stdTypeResolverBuilder._idType = JsonTypeInfo.Id.NONE;
		stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
		TypeSerializer result = stdTypeResolverBuilder.buildTypeSerializer(config, null, null);
		assertNull(result);
	}
	
	@Test
	public void testBuildTypeSerializerWithCustomIdResolver() {
		stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CUSTOM;
		stdTypeResolverBuilder._customIdResolver = new ClassNameIdResolver(null, null);
		stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
		
		TypeSerializer result = stdTypeResolverBuilder.buildTypeSerializer(config, null, null);
		assertNotNull(result);
	}
	
	@Test
	public void testBuildTypeDeserializerWithTypeAsNone() {
		stdTypeResolverBuilder._idType = JsonTypeInfo.Id.NONE;
		stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
		TypeDeserializer result = stdTypeResolverBuilder.buildTypeDeserializer(config, null, null);
		assertNull(result);
	}
	
	@Test
	public void testBuildTypeDeserializerWithCustomIdResolver() {
		stdTypeResolverBuilder._idType = JsonTypeInfo.Id.CUSTOM;
		stdTypeResolverBuilder._customIdResolver = new ClassNameIdResolver(null, null);
		stdTypeResolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
		
		TypeDeserializer result = stdTypeResolverBuilder.buildTypeDeserializer(config, null, null);
		assertNotNull(result);
	}
	
	@Test
	public void testInclusion() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
		assertNotNull(result);
		assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, stdTypeResolverBuilder._includeAs);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInclusionWithNullIncludeAs() {
		stdTypeResolverBuilder.inclusion(null);
	}
	
	@Test
	public void testTypeProperty() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.typeProperty("typeIdPropName");
		assertNotNull(result);
		assertEquals("typeIdPropName", stdTypeResolverBuilder._typeProperty);
	}
	
	@Test
	public void testTypePropertyWithNull() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.typeProperty(null);
		assertNotNull(result);
		assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
	}
	
	@Test
	public void testDefaultImpl() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.defaultImpl(Object.class);
		assertNotNull(result);
		assertEquals(Object.class, stdTypeResolverBuilder._defaultImpl);
	}
	
	@Test
	public void testTypeIdVisibility() {
		StdTypeResolverBuilder result = stdTypeResolverBuilder.typeIdVisibility(true);
		assertNotNull(result);
		assertTrue(stdTypeResolverBuilder._typeIdVisible);
	}
	
	@Test
	public void testGetDefaultImpl() {
		stdTypeResolverBuilder._defaultImpl = Object.class;
		assertEquals(Object.class, stdTypeResolverBuilder.getDefaultImpl());
	}
	
	@Test
	public void testGetTypeProperty() {
		stdTypeResolverBuilder._typeProperty = "typeIdPropName";
		assertEquals("typeIdPropName", stdTypeResolverBuilder.getTypeProperty());
	}
	
	@Test
	public void testIsTypeIdVisible() {
		stdTypeResolverBuilder._typeIdVisible = true;
		assertTrue(stdTypeResolverBuilder.isTypeIdVisible());
	}
	
	@Test
	public void testIdResolver() {
		StdTypeResolverBuilder spyBuilder = spy(stdTypeResolverBuilder);
		TypeIdResolver result = spyBuilder.idResolver(config, null, null, true, false);
		assertNull(result);
	}
	
	private static class ClassNameIdResolver implements TypeIdResolver {

		public ClassNameIdResolver(JavaType baseType, MapperConfig<?> config) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void init(JavaType baseType) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String idFromValue(Object value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String idFromValueAndType(Object value, Class<?> suggestedType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String idFromBaseType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Id getMechanism() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JavaType typeFromId(DatabindContext context, String id) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}