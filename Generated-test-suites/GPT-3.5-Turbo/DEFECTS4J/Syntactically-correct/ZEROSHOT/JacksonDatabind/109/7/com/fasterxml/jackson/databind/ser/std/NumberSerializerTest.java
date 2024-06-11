package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

	@Test
	public void testSerialize() {
		NumberSerializer serializer = new NumberSerializer(Number.class);
		JsonGeneratorMock jsonGeneratorMock = new JsonGeneratorMock();
		SerializerProviderMock serializerProviderMock = new SerializerProviderMock();
		
		try {
			serializer.serialize(10, jsonGeneratorMock, serializerProviderMock);
			assertEquals("10", jsonGeneratorMock.getLastWrittenValueAsString());
			
			serializer.serialize(10L, jsonGeneratorMock, serializerProviderMock);
			assertEquals("10", jsonGeneratorMock.getLastWrittenValueAsString());
			
			serializer.serialize(10.1f, jsonGeneratorMock, serializerProviderMock);
			assertEquals("10.1", jsonGeneratorMock.getLastWrittenValueAsString());
			
			serializer.serialize(10.1, jsonGeneratorMock, serializerProviderMock);
			assertEquals("10.1", jsonGeneratorMock.getLastWrittenValueAsString());
			
			serializer.serialize(new BigInteger("10"), jsonGeneratorMock, serializerProviderMock);
			assertEquals("10", jsonGeneratorMock.getLastWrittenValueAsString());
			
			serializer.serialize(new BigDecimal("10.1"), jsonGeneratorMock, serializerProviderMock);
			assertEquals("10.1", jsonGeneratorMock.getLastWrittenValueAsString());
			
		} catch (IOException e) {
			fail("IOException");
		}

	}

	@Test
	public void testCreateContextual() {
		NumberSerializer serializer = new NumberSerializer(Number.class);
		SerializerProviderMock serializerProviderMock = new SerializerProviderMock();
		BeanProperty property = new BeanPropertyMock();
		
		try {
			JsonSerializer<?> result = serializer.createContextual(serializerProviderMock, property);
			assertEquals(serializer, result);
			
		} catch (JsonMappingException e) {
			fail("JsonMappingException");
		}
		
	}

	@Test
	public void testGetSchema() {
		NumberSerializer serializer = new NumberSerializer(Number.class);
		SerializerProviderMock serializerProviderMock = new SerializerProviderMock();
		
		JsonNode result = serializer.getSchema(serializerProviderMock, Number.class);
		assertEquals("number", result.get("type").asText());
	}

	@Test
	public void testAcceptJsonFormatVisitor() {
		NumberSerializer serializer = new NumberSerializer(Number.class);
		JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
		JavaTypeMock typeHint = new JavaTypeMock();
		
		try {
			serializer.acceptJsonFormatVisitor(visitor, typeHint);
			assertEquals("number", visitor.getVisitedFormat().asText());
		} catch (JsonMappingException e) {
			fail("JsonMappingException");
		}
	}
}