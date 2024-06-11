 package com.example.testing; import org.junit.*; import org.mockito.*; import org.mockito.junit.*; import com.fasterxml.jackson.core.*; import com.fasterxml.jackson.databind.*; import com.fasterxml.jackson.databind.introspect.AnnotatedMember; import com.fasterxml.jackson.databind.ser.std.MapSerializer;

@RunWith(MockitoJUnitRunner.class) public class AnyGetterWriterTest { private AnyGetterWriter _anyGetterWriter;

 @Before public void setUp() { BeanProperty property = Mockito.mock(BeanProperty.class); AnnotatedMember accessor = Mockito.mock(AnnotatedMember.class); MapSerializer mapSerializer = Mockito.mock(MapSerializer.class); _anyGetterWriter = new AnyGetterWriter(property, accessor, mapSerializer); }

 @Test public void testConstructor() { assertNull(_anyGetterWriter._mapSerializer); }

 @Test public void testConstructorWithNullMapSerializer() { _anyGetterWriter = new AnyGetterWriter(property, accessor, null); }

 @Test public void testGetAndSerializeWithValidJsonGenerator() throws Exception { Object bean = Mockito.mock(Object.class); JsonGenerator gen = Mockito.mock(JsonGenerator.class); SerializerProvider provider = Mockito.mock(SerializerProvider.class); Map<String, Object> map = new HashMap<>(); _anyGetterWriter.getAndSerialize(bean, gen, provider); Mockito.verify(gen).writeStartObject(); Mockito.verify(gen).writeFieldName("fieldName"); assertEquals(map.size(), gen.getOutputSize()); }

 @Test(expected = JsonMappingException.class) public void testGetAndSerializeWithNullBean() throws Exception { _anyGetterWriter.getAndSerialize(null, gen, provider); }

 @Test(expected = JsonMappingException.class) public void testGetAndSerializeWithNullMap() throws Exception { Object bean = Mockito.mock(Object.class); JsonGenerator gen = Mockito.mock(JsonGenerator.class); SerializerProvider provider = Mockito.mock(SerializerProvider.class); _anyGetterWriter._accessor.setValue(bean, null); _anyGetterWriter.getAndSerialize(bean, gen, provider); }

 @Test public void testGetAndFilterWithValidJsonGenerator() throws Exception { Object bean = Mockito.mock(Object.class); JsonGenerator gen = Mockito.mock(JsonGenerator.class); SerializerProvider provider = Mockito.mock(SerializerProvider.class); PropertyFilter filter = Mockito.mock(PropertyFilter.class); Map<String, Object> map = new HashMap<>(); _anyGetterWriter.getAndFilter(bean, gen, provider, filter); Mockito.verify(gen).writeStartObject(); Mockito.verify(gen).writeFieldName("fieldName"); Mockito.verify(filter).include("includeKey"); assertEquals(map.size() - 1, gen.getOutputSize()); }

 @Test(expected = JsonMappingException.class) public void testGetAndFilterWithNullBean() throws Exception { _anyGetterWriter.getAndFilter(null, gen, provider, filter); }

 @Test(expected = JsonMappingException.class) public void testGetAndFilterWithNullMap() throws Exception { Object bean = Mockito.mock(Object.class); JsonGenerator gen = Mockito.mock(JsonGenerator.class); SerializerProvider provider = Mockito.mock(SerializerProvider.class); PropertyFilter filter = Mockito.mock(PropertyFilter.class); _anyGetterWriter._accessor.setValue(bean, null); _anyGetterWriter.getAndFilter(bean, gen, provider, filter); }

 @Test(expected = JsonMappingException.class) public void testGetAndFilterWithNullPropertyFilter() throws Exception { _anyGetterWriter.getAndFilter(bean, gen, provider, null); }

 @Test public void testResolve() throws JsonMappingException { SerializerProvider provider = Mockito.mock(SerializerProvider.class); _anyGetterWriter.resolve(provider); assertNotNull(_anyGetterWriter._mapSerializer); }

 @Test(expected = JsonMappingException.class) public void testResolveWithNullSerializerProvider() throws JsonMappingException { _anyGetterWriter.resolve(null); }
}