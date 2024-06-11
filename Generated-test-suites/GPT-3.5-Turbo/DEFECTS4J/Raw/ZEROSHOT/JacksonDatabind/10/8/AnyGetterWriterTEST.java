package com.fasterxml.jackson.databind.ser;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
	private AnyGetterWriter anyGetterWriter;
	private BeanProperty mockProperty;
	private AnnotatedMember mockAccessor;
	private MapSerializer mockSerializer;
	private JsonGenerator mockJsonGenerator;
	private SerializerProvider mockSerializerProvider;
	
	@Before
	public void setUp() {
		mockProperty = mock(BeanProperty.class);
		mockAccessor = mock(AnnotatedMember.class);
		mockSerializer = mock(MapSerializer.class);
		mockJsonGenerator = mock(JsonGenerator.class);
		mockSerializerProvider = mock(SerializerProvider.class);
		anyGetterWriter = new AnyGetterWriter(mockProperty, mockAccessor, mockSerializer);
	}

	@Test
	public void testGetAndSerialize_NullValue() throws Exception {
		when(mockAccessor.getValue(any())).thenReturn(null);
		anyGetterWriter.getAndSerialize(new Object(), mockJsonGenerator, mockSerializerProvider);
		verify(mockJsonGenerator, never()).writeObject(any());
	}
	
	@Test(expected = JsonMappingException.class)
	public void testGetAndSerialize_InvalidValue() throws Exception {
		when(mockAccessor.getValue(any())).thenReturn("invalid");
		anyGetterWriter.getAndSerialize(new Object(), mockJsonGenerator, mockSerializerProvider);
		verify(mockJsonGenerator, never()).writeObject(any());
		verifyThrowException();
	}
	
	@Test
	public void testGetAndSerialize_ValidValue() throws Exception {
		Map<String, Object> map = mock(Map.class);
		when(mockAccessor.getValue(any())).thenReturn(map);
		anyGetterWriter.getAndSerialize(new Object(), mockJsonGenerator, mockSerializerProvider);
		verify(mockJsonGenerator, times(1)).writeObject(map);
	}
	
	@Test
	public void testGetAndFilter_NullValue() throws Exception {
		when(mockAccessor.getValue(any())).thenReturn(null);
		anyGetterWriter.getAndFilter(new Object(), mockJsonGenerator, mockSerializerProvider, null);
		verify(mockJsonGenerator, never()).writeObject(any());
	}
	
	@Test(expected = JsonMappingException.class)
	public void testGetAndFilter_InvalidValue() throws Exception {
		when(mockAccessor.getValue(any())).thenReturn("invalid");
		anyGetterWriter.getAndFilter(new Object(), mockJsonGenerator,
				mockSerializerProvider, null);
		verify(mockJsonGenerator, never()).writeObject(any());
		verifyThrowException();
	}

	private void verifyThrowException() throws JsonMappingException {
		verify(mockJsonGenerator, never()).writeObject(any());
		throw new JsonMappingException("Value returned by 'any-getter' ("
                + mockAccessor.getName()+"()) not java.util.Map but "
                +String.class.getName());
	}
}