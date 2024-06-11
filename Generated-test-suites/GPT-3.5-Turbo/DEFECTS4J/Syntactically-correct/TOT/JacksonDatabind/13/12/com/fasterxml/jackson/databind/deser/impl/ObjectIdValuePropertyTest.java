package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.ObjectIdReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

	@Test
	public void testDeserializeAndSet() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "{\"value\": \"abc\"}";
		JsonParser parser = objectMapper.getFactory().createParser(json);
		DeserializationContext context = objectMapper.getDeserializationContext();
		ObjectNode objectNode = objectMapper.readTree(json);

		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, ClassUtil.createObjectIdInfo(objectMapper.getSerializationConfig().getTypeFactory(),
				SettableBeanProperty.class, SettableBeanProperty.class, null, null), new ObjectIdValueProperty(new ObjectIdReader(objectMapper,
						new ObjectIdInfo(objectMapper.getSerializationConfig().getTypeFactory(), Object.class, null, null)), null), null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

		Object instance = new Object();

		property.deserializeAndSet(parser, context, instance);

		assertEquals(objectNode.get("value").asText(), instance);
	}

	@Test
	public void testSet() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonDeserializer<?> deserializer = objectMapper.getDeserializerProvider().findTypedValueDeserializer(objectMapper.getConstructType(Object.class));
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		Object instance = new Object();
		property.set(instance, "abc");
		Object expected = deserializer.deserialize(null, objectMapper.getDeserializationContext());
		assertEquals(expected, property.setAndReturn(instance, "abc"));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSetWithoutIdProperty() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonDeserializer<?> deserializer = objectMapper.getDeserializerProvider().findTypedValueDeserializer(objectMapper.getConstructType(Object.class));
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		Object instance = new Object();
		property.setAndReturn(instance, "abc");
	}

	@Test
	public void testGetName() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		assertEquals(objectIdReader.propertyName, property.getFullName());
	}

	@Test
	public void testGetIdType() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		assertEquals(objectIdReader.getIdType(), property.getType());
	}

	@Test
	public void testWithNewName() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		PropertyName newName = new PropertyName("testName");
		ObjectIdValueProperty newProperty = property.withName(newName);
		assertEquals(newName, newProperty.getFullName());
		assertEquals(property.getType(), newProperty.getType());
	}

	@Test
	public void testWithValueDeserializer() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonDeserializer<?> deserializer = objectMapper.getDeserializerProvider().findTypedValueDeserializer(objectMapper.getConstructType(Object.class));
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		ObjectIdValueProperty newProperty = property.withValueDeserializer(deserializer);
		assertEquals(property.getFullName(), newProperty.getFullName());
		assertEquals(deserializer, newProperty.getValueDeserializer());
	}

	@Test
	public void testGetAnnotation() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		assertNull(property.getAnnotation(Annotation.class));
	}

	@Test
	public void testGetMember() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectIdReader objectIdReader = new ObjectIdReader(objectMapper, null, null, null, null);
		ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
		assertNull(property.getMember());
	}
}