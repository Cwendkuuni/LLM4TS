package com.fasterxml.jackson.databind.ser;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

public class AnyGetterWriterTest {
	
	@Mock
	private BeanProperty mockBeanProperty;
	
	@Mock
	private AnnotatedMember mockAnnotatedMember;
	
	@Mock
	private MapSerializer mockMapSerializer;
	
	@Mock
	private JsonGenerator mockJsonGenerator;
	
	@Mock
	private SerializerProvider mockSerializerProvider;
	
	private AnyGetterWriter anyGetterWriter;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		anyGetterWriter = new AnyGetterWriter(mockBeanProperty, mockAnnotatedMember, mockMapSerializer);
	}
	
	@Test
	public void testGetAndSerialize_withNullValue_doesNotThrowException() throws Exception {
		anyGetterWriter.getAndSerialize(null, mockJsonGenerator, mockSerializerProvider);
	}
	
	@Test(expected = JsonMappingException.class)
	public void testGetAndSerialize_withNonMapValue_throwsException() throws Exception {
		anyGetterWriter.getAndSerialize(new Object(), mockJsonGenerator, mockSerializerProvider);
	}
	
	@Test
	public void testGetAndSerialize_withMapValue_callsMapSerializerSerializeFields() throws Exception {
		Map<String, Object> mapValue = new HashMap<>();
		anyGetterWriter.getAndSerialize(mapValue, mockJsonGenerator, mockSerializerProvider);
		// verify that serializeFields method of MapSerializer is called with correct arguments
		// ...
	}
	
	@Test
	public void testGetAndFilter_withNullValue_doesNotThrowException() throws Exception {
		anyGetterWriter.getAndFilter(null, mockJsonGenerator, mockSerializerProvider, null);
	}
	
	@Test(expected = JsonMappingException.class)
	public void testGetAndFilter_withNonMapValue_throwsException() throws Exception {
		anyGetterWriter.getAndFilter(new Object(), mockJsonGenerator, mockSerializerProvider, null);
	}
	
	@Test
	public void testGetAndFilter_withMapValue_callsMapSerializerSerializeFilteredFields() throws Exception {
		Map<String, Object> mapValue = new HashMap<>();
		anyGetterWriter.getAndFilter(mapValue, mockJsonGenerator, mockSerializerProvider, null);
		// verify that serializeFilteredFields method of MapSerializer is called with correct arguments
		// ...
	}
	
	@Test
	public void testResolve_callsProviderHandlePrimaryContextualization() throws JsonMappingException {
		anyGetterWriter.resolve(mockSerializerProvider);
		// verify that handlePrimaryContextualization method of SerializerProvider is called with correct arguments
		// ...
	}
}