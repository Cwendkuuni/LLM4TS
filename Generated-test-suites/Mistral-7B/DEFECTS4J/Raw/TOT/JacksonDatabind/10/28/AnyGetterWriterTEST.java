import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.BeanProperty;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private ObjectMapper objectMapper;
 private AnyGetterWriter anyGetterWriter;
 private AnnotatedMember annotatedMember;
 private BeanProperty property;
 private MapSerializer mapSerializer;
 private SerializerProvider serializerProvider;
 private JsonGenerator jsonGenerator;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 annotatedMember = mock(AnnotatedMember.class);
 property = new BeanProperty("testProperty", AnnotatedMember.Visibility.NONE);
 mapSerializer = new MapSerializer(new StandardSerializer(Map.class), null, null, null, Collections.emptyList(), NameTransformer.IDENTITY);
 anyGetterWriter = new AnyGetterWriter(property, annotatedMember, mapSerializer);
 serializerProvider = mock(SerializerProvider.class);
 jsonGenerator = mock(JsonGenerator.class);
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 Object testBean = new Object();
 when(annotatedMember.getValue(testBean)).thenReturn(null);
 anyGetterWriter.getAndSerialize(testBean, jsonGenerator, serializerProvider);
 verify(jsonGenerator, never()).writeStartObject();
 }

 @Test
 public void testGetAndSerializeNonMapValue() throws Exception {
 Object testBean = new Object();
 when(annotatedMember.getValue(testBean)).thenReturn("nonMapValue");
 anyGetterWriter.getAndSerialize(testBean, jsonGenerator, serializerProvider);
 assertThrows(JsonMappingException.class, () -> {
 anyGetterWriter.getAndSerialize(testBean, jsonGenerator, serializerProvider);
 });
 }

 @Test
 public void testGetAndSerializeMapValue() throws Exception {
 Object testBean = new Object();
 Map<String, String> testMap = new HashMap<>();
 testMap.put("key1", "value1");
 when(annotatedMember.getValue(testBean)).thenReturn(testMap);
 anyGetterWriter.getAndSerialize(testBean, jsonGenerator, serializerProvider);
 verify(jsonGenerator, times(1)).writeStartObject();
 verify(jsonGenerator, times(2)).writeStringField(anyString(), anyString());
 verify(jsonGenerator, times(1)).writeEndObject();
 }

 @Test
 public void testGetAndFilterNullValue() throws Exception {
 Object testBean = new Object();
 when(annotatedMember.getValue(testBean)).thenReturn(null);
 anyGetterWriter.getAndFilter(testBean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
 verify(jsonGenerator, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterNonMapValue() throws Exception {
 Object testBean = new Object();
 when(annotatedMember.getValue(testBean)).thenReturn("nonMapValue");
 anyGetterWriter.getAndFilter(testBean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
 assertThrows(JsonMappingException.class, () -> {
 anyGetterWriter.getAndFilter(testBean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
 });
 }

 @Test
 public void testGetAndFilterEmptyMapValue() throws Exception {
 Object testBean = new Object();
 Map<String, String> testMap = new HashMap<>();
 when(annotatedMember.getValue(testBean)).thenReturn(testMap);
 anyGetterWriter.getAndFilter(testBean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
 verify(jsonGenerator, never()).writeStartObject();
 }

 @Test
 public void testGetAndFilterMapValueWithFiltering() throws Exception {
 Object testBean = new Object();
 Map<String, String> testMap = new HashMap<>();
 testMap.put("key1", "value1");
 testMap.put("key2", "value2");
 when(annotatedMember.getValue(testBean)).thenReturn(testMap);
 anyGetterWriter.getAndFilter(testBean, jsonGenerator, serializerProvider, mock(PropertyFilter.class));
 verify(jsonGenerator, times(1)).writeStartObject();
 verify(jsonGenerator, times(2)).writeStringField(anyString(), anyString());
 verify(jsonGenerator, times(1)).writeEndObject();
 }

 @Test
 public void testResolve() throws Exception {
 MapSerializer mapSerializer = mock(MapSerializer.class);
 when(serializerProvider.handlePrimaryContextualization(mapSerializer, property)).thenReturn(mapSerializer);
 anyGetterWriter.resolve(serializerProvider);
 verify(mapSerializer, times(1)).resolve(serializerProvider);
 }
}