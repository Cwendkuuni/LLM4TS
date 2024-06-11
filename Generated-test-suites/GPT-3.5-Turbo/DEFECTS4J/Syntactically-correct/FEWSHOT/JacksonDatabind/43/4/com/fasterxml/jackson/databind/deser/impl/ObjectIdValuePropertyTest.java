package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;

public class ObjectIdValuePropertyTest {
	
	private ObjectIdValueProperty property;
	private ObjectIdReader objectIdReader;
	private PropertyName propertyName;
	private PropertyMetadata metadata;
	private JsonDeserializer<?> deserializer;
	
	@Before
	public void setup() {
		objectIdReader = mock(ObjectIdReader.class);
		propertyName = mock(PropertyName.class);
		metadata = mock(PropertyMetadata.class);
		deserializer = mock(JsonDeserializer.class);
		property = new ObjectIdValueProperty(objectIdReader, metadata);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(objectIdReader.propertyName, property.getName());
		assertEquals(objectIdReader.getIdType(), property.getType());
		assertEquals(metadata, property.getMetadata());
		assertEquals(objectIdReader.getDeserializer(), property.getValueDeserializer());
		assertEquals(objectIdReader, property._objectIdReader);
	}
	
	@Test
	public void testCopyConstructorWithDeserializer() {
		ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
		@SuppressWarnings("unchecked")
		JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
		when(src.getValueDeserializer()).thenReturn(deser);
		
		ObjectIdValueProperty newProperty = new ObjectIdValueProperty(src, deser);
		
		assertEquals(src, newProperty.getOriginalProperty());
		assertEquals(src.getName(), newProperty.getName());
		assertEquals(src.getType(), newProperty.getType());
		assertEquals(src.getMetadata(), newProperty.getMetadata());
		assertEquals(deser, newProperty.getValueDeserializer());
		assertEquals(src._objectIdReader, newProperty._objectIdReader);
	}
	
	@Test
	public void testCopyConstructorWithName() {
		ObjectIdValueProperty src = mock(ObjectIdValueProperty.class);
		PropertyName newName = mock(PropertyName.class);
		when(src.getName()).thenReturn(newName);
		
		ObjectIdValueProperty newProperty = new ObjectIdValueProperty(src, newName);
		
		assertEquals(src, newProperty.getOriginalProperty());
		assertEquals(newName, newProperty.getName());
		assertEquals(src.getType(), newProperty.getType());
		assertEquals(src.getMetadata(), newProperty.getMetadata());
		assertEquals(src.getValueDeserializer(), newProperty.getValueDeserializer());
		assertEquals(src._objectIdReader, newProperty._objectIdReader);
	}
	
	@Test
	public void testWithName() {
		PropertyName newName = mock(PropertyName.class);
		ObjectIdValueProperty newProperty = property.withName(newName);
		
		assertEquals(property, newProperty.getOriginalProperty());
		assertEquals(newName, newProperty.getName());
		assertEquals(property.getType(), newProperty.getType());
		assertEquals(property.getMetadata(), newProperty.getMetadata());
		assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
		assertEquals(property._objectIdReader, newProperty._objectIdReader);
	}
	
	@Test
	public void testWithValueDeserializer() {
		@SuppressWarnings("unchecked")
		JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
		ObjectIdValueProperty newProperty = property.withValueDeserializer(deser);
		
		assertEquals(property, newProperty.getOriginalProperty());
		assertEquals(property.getName(), newProperty.getName());
		assertEquals(property.getType(), newProperty.getType());
		assertEquals(property.getMetadata(), newProperty.getMetadata());
		assertEquals(deser, newProperty.getValueDeserializer());
		assertEquals(property._objectIdReader, newProperty._objectIdReader);
	}
	
	@Test
	public void testDeserializeAndSet() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		property.deserializeAndSet(p, ctxt, instance);
		
		verify(property).deserializeSetAndReturn(p, ctxt, instance);
	}
	
	@Test
	public void testDeserializeSetAndReturnWithNullId() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		when(property._valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(null);
		
		Object result = property.deserializeSetAndReturn(p, ctxt, instance);
		
		assertNull(result);
		verify(ctxt).findObjectId(null, property._objectIdReader.generator, property._objectIdReader.resolver);
		verify(property._objectIdReader.generator).bindItem(instance);
	}
	
	@Test
	public void testDeserializeSetAndReturn() throws IOException {
		JsonParser p = mock(JsonParser.class);
		DeserializationContext ctxt = mock(DeserializationContext.class);
		Object instance = mock(Object.class);
		Object id = mock(Object.class);
		Object result = mock(Object.class);
		ReadableObjectId roid = mock(ReadableObjectId.class);
		property._objectIdReader.idProperty = mock(SettableBeanProperty.class);
		
		when(property._valueDeserializer.deserialize(p, ctxt)).thenReturn(id);
		when(ctxt.findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver)).thenReturn(roid);
		when(property._objectIdReader.idProperty.setAndReturn(instance, id)).thenReturn(result);
		
		Object actualResult = property.deserializeSetAndReturn(p, ctxt, instance);
		
		assertEquals(result, actualResult);
		verify(ctxt).findObjectId(id, property._objectIdReader.generator, property._objectIdReader.resolver);
		verify(roid).bindItem(instance);
		verify(property._objectIdReader.idProperty).setAndReturn(instance, id);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testSetAndReturnWithoutIdProperty() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);
		property._objectIdReader.idProperty = null;
		
		property.setAndReturn(instance, value);
	}
	
	@Test
	public void testSetAndReturnWithIdProperty() throws IOException {
		Object instance = mock(Object.class);
		Object value = mock(Object.class);
		Object result = mock(Object.class);
		property._objectIdReader.idProperty = mock(SettableBeanProperty.class);
		when(property._objectIdReader.idProperty.setAndReturn(instance, value)).thenReturn(result);
		
		Object actualResult = property.setAndReturn(instance, value);
		
		assertEquals(result, actualResult);
		verify(property._objectIdReader.idProperty).setAndReturn(instance, value);
	}
}