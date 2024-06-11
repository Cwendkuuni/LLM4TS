import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.TestBeanAnyGetterWriter;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializer;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private JsonGenerator jsonGeneratorMock;
 private SerializerProvider serializerProviderMock;
 private AnnotatedMember annotatedMemberMock;
 private BeanProperty beanPropertyMock;
 private MapSerializer mapSerializerMock;
 private AnyGetterWriter anyGetterWriter;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 JsonMapper jsonMapper = (JsonMapper) objectMapper;
 jsonGeneratorMock = mock(JsonGenerator.class);
 serializerProviderMock = mock(SerializerProvider.class);
 annotatedMemberMock = mock(AnnotatedMember.class);
 beanPropertyMock = mock(BeanProperty.class);
 mapSerializerMock = mock(MapSerializer.class);
 when(objectMapper.findValueSerializer(new TypeReference<Map<String, Object>>(){}, serializerProviderMock)).thenReturn(mapSerializerMock);
 anyGetterWriter = new AnyGetterWriter(beanPropertyMock, annotatedMemberMock, mapSerializerMock);
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object bean = new TestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 doReturn(map).when(annotatedMemberMock).getValue(bean);

 doNothing().when(jsonGeneratorMock).writeStartObject();
 doNothing().when(jsonGeneratorMock).writeFields(mapSerializerMock);
 doNothing().when(jsonGeneratorMock).writeEndObject();
 doNothing().when(objectMapper).writeValue(jsonGeneratorMock, map);

 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProviderMock);

 verify(jsonGeneratorMock, times(1)).writeStartObject();
 verify(jsonGeneratorMock, times(1)).writeFields(mapSerializerMock);
 verify(jsonGeneratorMock, times(1)).writeEndObject();
 verify(objectMapper, times(1)).writeValue(jsonGeneratorMock, map);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNullValue() throws Exception {
 Object bean = new TestBean();
 doReturn(null).when(annotatedMemberMock).getValue(bean);
 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProviderMock);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndSerializeWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 doReturn(map).when(annotatedMemberMock).getValue(bean);
 doReturn(new String[]{"nonMapValue"}).when(mapSerializerMock).getSerializerProvidedProperties(mapSerializerMock, map, serializerProviderMock, null, null);
 anyGetterWriter.getAndSerialize(bean, jsonGeneratorMock, serializerProviderMock);
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object bean = new TestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 doReturn(map).when(annotatedMemberMock).getValue(bean);
 PropertyFilter filter = mock(PropertyFilter.class);
 doNothing().when(mapSerializerMock).serializeFilteredFields(mapSerializerMock, jsonGeneratorMock, serializerProviderMock, filter, null);
 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProviderMock, filter);
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNullValue() throws Exception {
 Object bean = new TestBean();
 doReturn(null).when(annotatedMemberMock).getValue(bean);
 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProviderMock, mock(PropertyFilter.class));
 }

 @Test(expected = JsonMappingException.class)
 public void testGetAndFilterWithNonMapValue() throws Exception {
 Object bean = new TestBean();
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 doReturn(map).when(annotatedMemberMock).getValue(bean);
 doReturn(new String[]{"nonMapValue"}).when(mapSerializerMock).getSerializerProvidedProperties(mapSerializerMock, map, serializerProviderMock, null, null);
 anyGetterWriter.getAndFilter(bean, jsonGeneratorMock, serializerProviderMock, mock(PropertyFilter.class));
 }

 @Test
 public void testResolve() throws Exception {
 SerializerProvider serializerProvider = mock(SerializerProvider.class);
 MapSerializer mapSerializer = new MapSerializer(objectMapper.getFactory(), new TypeReference<Map<String, Object>>(){}, null, null, null, null, null, null, Collections.emptyList(), null);
 when(serializerProviderMock.findValueSerializer(new TypeReference<Map<String, Object>>(){}, serializerProvider)).thenReturn(mapSerializer);
 anyGetterWriter.resolve(serializerProviderMock);
 verify(serializerProviderMock, times(1)).handlePrimaryContextualization(mapSerializer, beanPropertyMock);
 }
}

class TestBean {
 private Map<String, Object> map;

 public Map<String, Object> getMap() {
 return map;
 }
}