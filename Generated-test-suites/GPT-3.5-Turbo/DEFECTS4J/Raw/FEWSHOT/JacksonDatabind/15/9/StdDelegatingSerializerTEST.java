package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {
	
	@Mock
	private Converter<Object, Object> mockConverter;
	@Mock
	private JavaType mockDelegateType;
	@Mock
	private JsonSerializer<Object> mockDelegateSerializer;
	@Mock
	private Object mockValue;
	@Mock
	private JsonGenerator mockJsonGenerator;
	@Mock
	private SerializerProvider mockProvider;
	@Mock
	private TypeSerializer mockTypeSer;
	
	private StdDelegatingSerializer stdDelegatingSerializer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		stdDelegatingSerializer = new StdDelegatingSerializer(mockConverter, mockDelegateType, mockDelegateSerializer);
	}
	
	@Test
	public void testSerialize_Success() throws IOException {
		when(mockConverter.convert(mockValue)).thenReturn(mockValue);
		when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
		stdDelegatingSerializer.serialize(mockValue, mockJsonGenerator, mockProvider);
		verify(mockDelegateSerializer).serialize(mockValue, mockJsonGenerator, mockProvider);
	}
	
	@Test
	public void testSerialize_NullValue() throws IOException {
		stdDelegatingSerializer.serialize(null, mockJsonGenerator, mockProvider);
		verify(mockProvider).defaultSerializeNull(mockJsonGenerator);
	}
	
	@Test
	public void testSerializeWithType() throws IOException {
		when(mockConverter.convert(mockValue)).thenReturn(mockValue);
		when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
		stdDelegatingSerializer.serializeWithType(mockValue, mockJsonGenerator, mockProvider, mockTypeSer);
		verify(mockDelegateSerializer).serializeWithType(mockValue, mockJsonGenerator, mockProvider, mockTypeSer);
	}
	
	@Test
	public void testIsEmpty_Success() {
		when(mockConverter.convert(mockValue)).thenReturn(mockValue);
		when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
		when(mockDelegateSerializer.isEmpty(mockValue)).thenReturn(false);
		stdDelegatingSerializer.isEmpty(mockValue);
		verify(mockDelegateSerializer).isEmpty(mockValue);
	}
	
	@Test
	public void testIsEmpty_NullValue() {
		when(mockConverter.convert(mockValue)).thenReturn(null);
		stdDelegatingSerializer.isEmpty(mockProvider, mockValue);
		verify(mockDelegateSerializer).isEmpty(mockValue);
	}
	
	@Test
	public void testResolve() throws JsonMappingException {
		when(mockDelegateSerializer instanceof ResolvableSerializer).thenReturn(true);
		stdDelegatingSerializer.resolve(mockProvider);
		verify((ResolvableSerializer) mockDelegateSerializer).resolve(mockProvider);
	}
	
	@Test
	public void testCreateContextual_Success() throws JsonMappingException {
		BeanProperty mockProperty = mock(BeanProperty.class);
		when(mockDelegateSerializer instanceof ContextualSerializer).thenReturn(true);
		when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
		stdDelegatingSerializer.createContextual(mockProvider, mockProperty);
		verify(mockDelegateSerializer).createContextual(mockProvider, mockProperty);
	}
	
	@Test
	public void testCreateContextual_NoContextualSerializer() throws JsonMappingException {
		BeanProperty mockProperty = mock(BeanProperty.class);
		when(mockDelegateSerializer instanceof ContextualSerializer).thenReturn(false);
		stdDelegatingSerializer.createContextual(mockProvider, mockProperty);
		verify(mockDelegateSerializer).createContextual(mockProvider, mockProperty);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testWithContext_ConcreteSubClass() {
		StdDelegatingSerializer subClass = new StubStdDelegatingSerializer(mockConverter);
		subClass.withDelegate(mockConverter, mockDelegateType, mockDelegateSerializer);
	}
	
	@Test
	public void testGetSchema() throws JsonMappingException {
		when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
		stdDelegatingSerializer.getSchema(mockProvider, mockDelegateType);
		verify((SchemaAware) mockDelegateSerializer).getSchema(mockProvider, mockDelegateType);
	}
	
	@Test
	public void testGetSchema_NoSchemaAware() throws JsonMappingException {
		stdDelegatingSerializer.getSchema(mockProvider, mockDelegateType);
		verify(mockDelegateSerializer).getSchema(mockProvider, mockDelegateType);
	}
	
	@Test
	public void testGetSchema_WithIsOptional() throws JsonMappingException {
		when(mockDelegateSerializer instanceof SchemaAware).thenReturn(true);
		stdDelegatingSerializer.getSchema(mockProvider, mockDelegateType, true);
		verify((SchemaAware) mockDelegateSerializer).getSchema(mockProvider, mockDelegateType, true);
	}
	
	@Test
	public void testGetSchema_NoSchemaAware_WithIsOptional() throws JsonMappingException {
		stdDelegatingSerializer.getSchema(mockProvider, mockDelegateType, true);
		verify(mockDelegateSerializer).getSchema(mockProvider, mockDelegateType, true);
	}
	
	@Test
	public void testAcceptJsonFormatVisitor() throws JsonMappingException {
		when(mockDelegateSerializer.getDelegatee()).thenReturn(mockDelegateSerializer);
		stdDelegatingSerializer.acceptJsonFormatVisitor(mockDelegateSerializer, mockDelegateType);
		verify(mockDelegateSerializer).acceptJsonFormatVisitor(mockDelegateSerializer, mockDelegateType);
	}
  
  private class StubStdDelegatingSerializer extends StdDelegatingSerializer {
  	
  	public StubStdDelegatingSerializer(Converter<Object, ?> converter) {
  		super(converter);
  	}
  	
  	public StubStdDelegatingSerializer(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
  		super(converter, delegateType, delegateSerializer);
  	}
  	
  	@Override
  	protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer) {
  		throw new IllegalStateException("Method should not have been called");
  	}
  }
}