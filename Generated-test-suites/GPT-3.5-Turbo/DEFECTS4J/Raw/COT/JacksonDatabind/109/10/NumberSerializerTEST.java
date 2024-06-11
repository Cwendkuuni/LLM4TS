package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

	@Mock
	private Number value;
	
	@Mock
	private JsonGenerator g;
	
	@Mock
	private SerializerProvider provider;
	
	private NumberSerializer numberSerializer;
	
	@Before
	public void setup() {
		numberSerializer = new NumberSerializer(Number.class);
	}
	
	@Test
	public void testSerialize_WhenValueIsBigDecimal_ShouldCallWriteNumberWithBigDecimal() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(true);
		BigDecimal bigDecimalValue = BigDecimal.ONE;
		when((BigDecimal) value).thenReturn(bigDecimalValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(bigDecimalValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsBigInteger_ShouldCallWriteNumberWithBigInteger() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(true);
		BigInteger bigIntegerValue = new BigInteger("1");
		when((BigInteger) value).thenReturn(bigIntegerValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(bigIntegerValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsLong_ShouldCallWriteNumberWithLongValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(true);
		long longValue = 1L;
		when(value.longValue()).thenReturn(longValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(longValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsDouble_ShouldCallWriteNumberWithDoubleValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(true);
		double doubleValue = 1.0;
		when(value.doubleValue()).thenReturn(doubleValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(doubleValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsFloat_ShouldCallWriteNumberWithFloatValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(false);
		when(value instanceof Float).thenReturn(true);
		float floatValue = 1.0f;
		when(value.floatValue()).thenReturn(floatValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(floatValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsInteger_ShouldCallWriteNumberWithIntegerValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(false);
		when(value instanceof Float).thenReturn(false);
		when(value instanceof Integer).thenReturn(true);
		int intValue = 1;
		when(value.intValue()).thenReturn(intValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(intValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsByte_ShouldCallWriteNumberWithIntegerValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(false);
		when(value instanceof Float).thenReturn(false);
		when(value instanceof Integer).thenReturn(false);
		when(value instanceof Byte).thenReturn(true);
		byte byteValue = 1;
		when(value.intValue()).thenReturn((int) byteValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(byteValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsShort_ShouldCallWriteNumberWithIntegerValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(false);
		when(value instanceof Float).thenReturn(false);
		when(value instanceof Integer).thenReturn(false);
		when(value instanceof Byte).thenReturn(false);
		when(value instanceof Short).thenReturn(true);
		short shortValue = 1;
		when(value.intValue()).thenReturn((int) shortValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(shortValue);
	}
	
	@Test
	public void testSerialize_WhenValueIsNotBigDecimalNorBigIntegerNorLong_NorDoubleNorFloatNorIntegerNorByteNorShort_ShouldCallWriteNumberWithStringValue() throws IOException {
		// Arrange
		when(value instanceof BigDecimal).thenReturn(false);
		when(value instanceof BigInteger).thenReturn(false);
		when(value instanceof Long).thenReturn(false);
		when(value instanceof Double).thenReturn(false);
		when(value instanceof Float).thenReturn(false);
		when(value instanceof Integer).thenReturn(false);
		when(value instanceof Byte).thenReturn(false);
		when(value instanceof Short).thenReturn(false);
		String stringValue = "1";
		when(value.toString()).thenReturn(stringValue);
		
		// Act
		numberSerializer.serialize(value, g, provider);
		
		// Assert
		verify(g).writeNumber(stringValue);
	}
	
	@Test
	public void testCreateContextual_ShouldReturnThis() throws JsonMappingException {
		// Arrange
		BeanProperty property = mock(BeanProperty.class);
		
		// Act
		JsonSerializer<?> result = numberSerializer.createContextual(provider, property);
		
		// Assert
		assertEquals(numberSerializer, result);
	}
	
	@Test
	public void testGetSchema_ShouldReturnIntegerSchemaNode_WhenNumberTypeIsBigInteger() {
		// Arrange
		when(value instanceof BigInteger).thenReturn(true);
		
		// Act
		JsonNode result = numberSerializer.getSchema(provider, null);
		
		// Assert
		assertEquals("integer", result.get("type").asText());
	}
	
	@Test
	public void testGetSchema_ShouldReturnNumberSchemaNode_WhenNumberTypeIsNotBigInteger() {
		// Arrange
		when(value instanceof BigInteger).thenReturn(false);
		
		// Act
		JsonNode result = numberSerializer.getSchema(provider, null);
		
		// Assert
		assertEquals("number", result.get("type").asText());
	}
	
	@Test
	public void testAcceptJsonFormatVisitor_ShouldCallExpectNumberFormat_WhenNumberTypeIsNotBigIntegerNorBigDecimal() throws JsonMappingException {
		// Arrange
		JavaType typeHint = mock(JavaType.class);
		Class<?> handledType = BigDecimal.class;
		when(typeHint.getRawClass()).thenReturn(handledType);
		
		JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
		
		// Act
		numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
		
		// Assert
		verify(visitor).expectNumberFormat(typeHint);
	}

}