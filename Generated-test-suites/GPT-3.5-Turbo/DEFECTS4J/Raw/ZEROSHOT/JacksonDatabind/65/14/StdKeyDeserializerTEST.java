package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StdKeyDeserializerTest {

	@Mock
	private ObjectMapper objectMapper;
	
	private StdKeyDeserializer stdKeyDeserializer;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		stdKeyDeserializer = new StdKeyDeserializer(TYPE_INT, Integer.class);
	}
	
	@Test
	public void testDeserializeKeyWithValidKey() throws Exception {
		String key = "123";
		DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
		Mockito.when(deserializationContext.getParser()).thenReturn(objectMapper);
		
		Object result = stdKeyDeserializer.deserializeKey(key, deserializationContext);
		
		Mockito.verify(deserializationContext).getParser();
		Mockito.verifyNoMoreInteractions(deserializationContext);
	}
	
	@Test
	public void testDeserializeKeyWithNullKey() throws Exception {
		String key = null;
		DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
		Mockito.when(deserializationContext.getParser()).thenReturn(objectMapper);
		
		Object result = stdKeyDeserializer.deserializeKey(key, deserializationContext);
		
		Mockito.verify(deserializationContext).getParser();
		Mockito.verifyNoMoreInteractions(deserializationContext);
	}
	
	@Test
	public void testDeserializeKeyWithException() throws Exception {
		String key = "abc";
		DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
		Mockito.when(deserializationContext.getParser()).thenReturn(objectMapper);
		
		Object result = stdKeyDeserializer.deserializeKey(key, deserializationContext);
		
		Mockito.verify(deserializationContext).getParser();
		Mockito.verifyNoMoreInteractions(deserializationContext);
	}
}