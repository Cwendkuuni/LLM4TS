package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

	@Test
	public void testGetClassAnnotations() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		Annotations annotations = propertyBuilder.getClassAnnotations();
		assertNotNull(annotations);
	}

	@Test
	public void testBuildWriter() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		SerializerProvider prov = new SerializerProvider();
		BeanPropertyDefinition propDef = new BeanPropertyDefinition();
		JavaType declaredType = new JavaType();
		JsonSerializer<?> ser = new JsonSerializer<>();
		TypeSerializer typeSer = new TypeSerializer();
		TypeSerializer contentTypeSer = new TypeSerializer();
		AnnotatedMember am = new AnnotatedMember();
		boolean defaultUseStaticTyping = true;
		try {
			BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer,
					contentTypeSer, am, defaultUseStaticTyping);
			assertNotNull(writer);
		} catch (JsonMappingException e) {
			fail("Failed to build writer");
		}
	}

	@Test
	public void testFindSerializationType() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		Annotated a = new Annotated();
		boolean useStaticTyping = true;
		JavaType declaredType = new JavaType();
		try {
			JavaType type = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);
			assertNull(type);
		} catch (JsonMappingException e) {
			fail("Failed to find serialization type");
		}
	}

	@Test
	public void testGetPropertyDefaultValue() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		String name = "property";
		AnnotatedMember member = new AnnotatedMember();
		JavaType type = new JavaType();
		Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);
		assertNotNull(defaultValue);
	}

	@Test
	public void testGetDefaultValue() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		JavaType type = new JavaType();
		Object defaultValue = propertyBuilder.getDefaultValue(type);
		assertNull(defaultValue);
	}

	@Test
	public void testThrowWrapped() {
		SerializationConfig config = new SerializationConfig();
		BeanDescription beanDesc = new BeanDescription();
		PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
		Exception e = new Exception();
		String propName = "property";
		Object defaultBean = new Object();
		try {
			Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
			fail("Failed to throw wrapped exception");
		} catch (IllegalArgumentException ex) {
			// Expected
		}
	}
}