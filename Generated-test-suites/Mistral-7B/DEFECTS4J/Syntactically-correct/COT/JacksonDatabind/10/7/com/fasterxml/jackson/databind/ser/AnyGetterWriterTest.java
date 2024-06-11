import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private MapSerializer mapSerializer;
 private Object bean;

 @Before
 public void setup() {
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 mapSerializer = mock(MapSerializer.class);
 bean = new Object() {};
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer);
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 // Test when value is null
 anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 // No exception is thrown
 }

 @Test
 public void testGetAndSerializeNonMapValue() throws Exception {
 // Test when value is not a Map
 Object nonMapValue = new Object() {};
 doReturn(nonMapValue).when(accessor).getValue(bean);
 try {
 anyGetterWriter.getAndSerialize(bean, mock(JsonGenerator.class), mock(SerializerProvider.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but " + nonMapValue.getClass().getName(), e.getMessage());
 }
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 // Test when value is a Map
 Object mapValue = new HashMap<>();
 doReturn(mapValue).when(accessor).getValue(bean);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 // Map is serialized
 verify(mapSerializer, times(1)).serializeFields((Map<?,?>) mapValue, gen, provider);
 }

 @Test
 public void testGetAndFilterNullValue() throws Exception {
 // Test when value is null
 anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), mock(PropertyFilter.class));
 // No operation is performed
 }

 @Test
 public void testGetAndFilterNonMapValue() throws Exception {
 // Test when value is not a Map
 Object nonMapValue = new Object() {};
 doReturn(nonMapValue).when(accessor).getValue(bean);
 try {
 anyGetterWriter.getAndFilter(bean, mock(JsonGenerator.class), mock(SerializerProvider.class), mock(PropertyFilter.class));
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 assertEquals("Value returned by 'any-getter' (" + accessor.getName() + "()) not java.util.Map but " + nonMapValue.getClass().getName(), e.getMessage());
 }
 }

 @Test
 public void testGetAndFilter() throws Exception {
 // Test when value is a Map
 Object mapValue = new HashMap<>();
 doReturn(mapValue).when(accessor).getValue(bean);
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 // Map is filtered and serialized
 verify(mapSerializer, times(1)).serializeFilteredFields((Map<?,?>) mapValue, gen, provider, filter, null);
 }

 @Test
 public void testResolve() throws JsonMappingException {
 // Test resolving MapSerializer
 SerializerProvider provider = mock(SerializerProvider.class);
 anyGetterWriter.resolve(provider);
 // MapSerializer is resolved
 verify(provider, times(1)).handlePrimaryContextualization(anyGetterWriter._mapSerializer, property);
 }
}