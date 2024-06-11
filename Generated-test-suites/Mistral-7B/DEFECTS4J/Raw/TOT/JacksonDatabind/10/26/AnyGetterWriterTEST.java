import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.Serializers;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnyGetterWriterTest {

 private AnyGetterWriter writer;
 private Object testObject;
 private MapSerializer mapSerializer;

 @Before
 public void setup() {
 testObject = new TestClass();
 mapSerializer = mock(MapSerializer.class);
 writer = new AnyGetterWriter(mock(BeanProperty.class), mock(AnnotatedMember.class), mapSerializer);
 }

 @Test
 public void testGetAndSerializeNullMap() throws IOException {
 Object value = null;
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 writer.getAndSerialize(testObject, gen, provider);
 verifyNoMoreInteractions(gen);
 }

 @Test
 public void testGetAndSerializeEmptyMap() throws IOException {
 Object value = Collections.emptyMap();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 writer.getAndSerialize(testObject, gen, provider);
 verify(mapSerializer).serializeFields(value, eq(gen), eq(provider));
 }

 @Test
 public void testGetAndSerializeNonMapType() throws IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 writer.getAndSerialize(testObject, gen, provider);
 verifyThrows(JsonMappingException.class, () -> writer.getAndSerialize(testObject, gen, provider));
 }

 @Test
 public void testGetAndFilterNullMap() throws IOException {
 Object value = null;
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 writer.getAndFilter(testObject, gen, provider, filter);
 verifyNoMoreInteractions(gen);
 }

 @Test
 public void testGetAndFilterEmptyMap() throws IOException {
 Object value = Collections.emptyMap();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 writer.getAndFilter(testObject, gen, provider, filter);
 verify(mapSerializer).serializeFilteredFields(value, eq(gen), eq(provider), eq(filter), any());
 }

 @Test
 public void testGetAndFilterNonMapType() throws IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 SerializerProvider provider = mock(SerializerProvider.class);
 PropertyFilter filter = mock(PropertyFilter.class);
 writer.getAndFilter(testObject, gen, provider, filter);
 verifyThrows(JsonMappingException.class, () -> writer.getAndFilter(testObject, gen, provider, filter));
 }

 @Test
 public void testResolveNullSerializerProvider() throws JsonMappingException {
 SerializerProvider provider = null;
 writer.resolve(provider);
 verifyNoMoreInteractions(provider);
 }

 @Test
 public void testResolveWithValidSerializerProvider() throws JsonMappingException {
 SerializerProvider provider = mock(SerializerProvider.class);
 writer.resolve(provider);
 verify(provider).handlePrimaryContextualization(eq(mapSerializer), any());
 }

 class TestClass {
 Map<String, String> mapProperty;
 }
}