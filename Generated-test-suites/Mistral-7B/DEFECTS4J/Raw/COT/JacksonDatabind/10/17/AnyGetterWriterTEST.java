package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AnyGetterWriterTest extends TestBase {

 private AnyGetterWriter _anyGetterWriter;
 private ObjectMapper _mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 _mapper = new ObjectMapper();
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 BeanProperty property = mock(BeanProperty.class);
 MapSerializer mapSerializer = new MapSerializer(null, null, null);
 _anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 Object bean = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(bean, gen, provider);
 assertNull(gen.getOutputBuffer().toString());
 }

 @Test
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object bean = new Object();
 Object value = new Object();
 when(_accessor.getValue(bean)).thenReturn(value);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(bean, gen, provider);
 String output = gen.getOutputBuffer().toString();
 assertTrue("Unexpected output: " + output, output.isEmpty());
 verify(provider).handleUnexpectedValue(_accessor, value);
 }

 @Test
 public void testGetAndSerializeMapValue() throws Exception {
 Object bean = new Object();
 Object value = new HashMap<>();
 when(_accessor.getValue(bean)).thenReturn(value);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.getAndSerialize(bean, gen, provider);
 String output = gen.getOutputBuffer().toString();
 String expected = "{\"}";
 assertEquals("Unexpected output: " + output, expected, output);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeMapSerializerNull() throws Exception {
 Object bean = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter = new AnyGetterWriter(null, null, null);
 _anyGetterWriter.getAndSerialize(bean, gen, provider);
 }

 @Test
 public void testGetAndFilterNullValue() throws Exception {
 Object bean = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = mock(PropertyFilter.class);
 _anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 assertNull(gen.getOutputBuffer().toString());
 }

 @Test
 public void testGetAndFilterNonMapValue() throws Exception {
 Object bean = new Object();
 Object value = new Object();
 when(_accessor.getValue(bean)).thenReturn(value);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = mock(PropertyFilter.class);
 _anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 String output = gen.getOutputBuffer().toString();
 assertTrue("Unexpected output: " + output, output.isEmpty());
 verify(provider).handleUnexpectedValue(_accessor, value);
 }

 @Test
 public void testGetAndFilterMapValue() throws Exception {
 Object bean = new Object();
 Object value = new HashMap<>();
 when(_accessor.getValue(bean)).thenReturn(value);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 PropertyFilter filter = mock(PropertyFilter.class);
 _anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 String output = gen.getOutputBuffer().toString();
 String expected = "{\"key1\":null,\"key2\":null}"; // empty map for simplicity
 assertEquals("Unexpected output: " + output, expected, output);
 }

 @Test
 public void testResolveNullSerializerProvider() throws Exception {
 SerializerProvider provider = null;
 _anyGetterWriter.resolve(provider);
 }

 @Test
 public void testResolveNonMapSerializer() throws Exception {
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.resolve(provider);
 assertNotNull(_anyGetterWriter.getMapSerializer());
 }

 @Test
 public void testGetAndSerializeWithMapSerializer() throws Exception {
 Object bean = new Object();
 Object value = new HashMap<>();
 when(_accessor.getValue(bean)).thenReturn(value);
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = createSerializerProvider();
 _anyGetterWriter.resolve(provider);
 _anyGetterWriter.getAndSerialize(bean, gen, provider);
 String output = gen.getOutputBuffer().toString();
 String expected = "{\"key1\":null,\"key2\":null}";
 assertEquals("Unexpected output: " + output, expected, output);
 }
}