import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.ser.*;
import static com.fasterxml.jackson.databind.ser.TestBase.mapSerializer;
import static org.junit.Assert.*;

public class AnyGetterWriterTest {

 private AnyGetterWriter anyGetterWriter;
 private BeanProperty property;
 private AnnotatedMember accessor;
 private Object bean;
 private SerializerProvider provider;
 private Object mapValue;
 private JsonGenerator gen;
 private PropertyFilter filter;

 @Before
 public void setup() {
 property = mock(BeanProperty.class);
 accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("anyGetter");
 when(accessor.getValue(anyInstance(Object.class))).thenCallRealMethod();
 bean = mock(Object.class);
 provider = mock(SerializerProvider.class);
 mapValue = new HashMap<>();
 mapValue.put("key1", "value1");
 mapValue.put("key2", "value2");
 gen = mock(JsonGenerator.class);
 filter = mock(PropertyFilter.class);
 when(provider.getSerializers()).thenReturn(new SimpleBeanPropertyFilterFinder(ObjectMapper.getDefaultInstance()));
 anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer(provider));
 }

 @Test
 public void testGetAndSerialize() throws Exception {
 Object value = mapValue;
 when(accessor.getValue(bean)).thenReturn(value);
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("anyGetter");
 anyObjectWriter_writeValue(gen, value);
 verify(gen, times(1)).writeEndObject();
 }

 @Test
 public void testGetAndSerializeNullValue() throws Exception {
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 verify(gen, never()).writeStartObject();
 }

 @Test
 public void testGetAndSerializeNonMapValueType() throws Exception {
 Object nonMapValue = new Object();
 when(accessor.getValue(bean)).thenReturn(nonMapValue);
 try {
 anyGetterWriter.getAndSerialize(bean, gen, provider);
 fail("Expected JsonMappingException to be thrown");
 } catch (JsonMappingException e) {
 assertEquals("Value returned by 'any-getter' (anyGetter()) not java.util.Map but "+nonMapValue.getClass().getName(), e.getMessage());
 }
 }

 @Test
 public void testGetAndFilter() throws Exception {
 Object value = mapValue;
 when(accessor.getValue(bean)).thenReturn(value);
 anyGetterWriter.getAndFilter(bean, gen, provider, filter);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("anyGetter");
 anyObjectWriter_writeValue(gen, value);
 //TODO: Add test for filtering
 }

 @Test
 public void testResolve() throws JsonMappingException {
 Serializer serializer = mock(Serializer.class);
 when(provider.findSerializers(BeanPropertyPropertyType.FIELD_SERIALIZER.getClass())).thenReturn(serializer);
 when(provider.findValueSerializer(MapSerializer.class)).thenReturn(null);
 anyGetterWriter.resolve(provider);
 verify(serializer).setParent(anyGetterWriter);
 }

 private void anyObjectWriter_writeValue(JsonGenerator gen, Object value) throws IOException {
 ObjectWriter writer = ObjectMapper.getDefaultInstance().writerWithDefaultPrettyPrinter();
 writer.writeValue(gen, value);
 }
}