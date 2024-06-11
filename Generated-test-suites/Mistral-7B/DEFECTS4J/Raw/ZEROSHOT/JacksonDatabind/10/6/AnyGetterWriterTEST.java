package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AnyGetterWriterTest extends BaseMapTest {

 private static final Object MAP_VALUE = new Object() {
 @SuppressWarnings("unused")
 private final Map<String, Object> delegate = new HashMap<>();
 };

 @SuppressWarnings("unchecked")
 private AnyGetterWriter createAnyGetterWriter(BeanProperty property, AnnotatedMember accessor, MapSerializer serializer) {
 return new AnyGetterWriter(property, accessor, serializer);
 }

 @org.junit.Before
 public void setUp() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 _objectMapper = mapper;
 }

 @org.junit.Test
 public void testGetAndSerialize() throws Exception {
 BeanProperty property = new BeanProperty(null, "mapProperty", null, null, null, null);
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("mapProperty");
 when(accessor.getValue(anyObject())).thenReturn(MAP_VALUE);

 MapSerializer mapSerializer = new MapSerializer(ObjectMap.class, _objectMapper.getConfig());
 AnyGetterWriter anyGetterWriter = createAnyGetterWriter(property, accessor, mapSerializer);

 Object testObject = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 provider.setCurrentValue(_objectMapper.createObjectContext().setRootValue(testObject));
 provider.setSerializerProviderContext(new EmptyContext());

 anyGetterWriter.getAndSerialize(testObject, gen, provider);

 String result = gen.getOutputStream().toString();
 String expected = "{\"mapProperty\":{}}";
 assertEquals(expected, result);

 gen.close();
 provider.close();
 }

 @org.junit.Test
 public void testGetAndSerializeNullValue() throws Exception {
 BeanProperty property = new BeanProperty(null, "mapProperty", null, null, null, null);
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("mapProperty");
 when(accessor.getValue(anyObject())).thenReturn(null);

 MapSerializer mapSerializer = new MapSerializer(ObjectMap.class, _objectMapper.getConfig());
 AnyGetterWriter anyGetterWriter = createAnyGetterWriter(property, accessor, mapSerializer);

 Object testObject = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 provider.setCurrentValue(_objectMapper.createObjectContext().setRootValue(testObject));
 provider.setSerializerProviderContext(new EmptyContext());

 anyGetterWriter.getAndSerialize(testObject, gen, provider);

 String result = gen.getOutputStream().toString();
 String expected = "{}";
 assertEquals(expected, result);

 gen.close();
 provider.close();
 }

 @org.junit.Test(expected = JsonMappingException.class)
 public void testGetAndSerializeNonMapValue() throws Exception {
 BeanProperty property = new BeanProperty(null, "mapProperty", null, null, null, null);
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("mapProperty");
 when(accessor.getValue(anyObject()))
 .thenReturn(new Integer(1));

 MapSerializer mapSerializer = new MapSerializer(ObjectMap.class, _objectMapper.getConfig());
 AnyGetterWriter anyGetterWriter = createAnyGetterWriter(property, accessor, mapSerializer);

 Object testObject = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 provider.setCurrentValue(_objectMapper.createObjectContext().setRootValue(testObject));
 provider.setSerializerProviderContext(new EmptyContext());

 anyGetterWriter.getAndSerialize(testObject, gen, provider);
 }

 @org.junit.Test
 public void testGetAndFilter() throws Exception {
 BeanProperty property = new BeanProperty(null, "mapProperty", null, null, null, null);
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("mapProperty");
 when(accessor.getValue(anyObject())).thenReturn(MAP_VALUE);

 MapSerializer mapSerializer = new MapSerializer(ObjectMap.class, _objectMapper.getConfig());
 AnyGetterWriter anyGetterWriter = createAnyGetterWriter(property, accessor, mapSerializer);

 Object testObject = new Object();
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new SerializerProvider();
 provider.setCurrentValue(_objectMapper.createObjectContext().setRootValue(testObject));
 provider.setSerializerProviderContext(new EmptyContext());
 PropertyFilter filter = mock(PropertyFilter.class);

 anyGetterWriter.getAndFilter(testObject, gen, provider, filter);

 String result = gen.getOutputStream().toString();
 String expected = "{\"mapProperty\":{}}";
 assertEquals(expected, result);

 gen.close();
 provider.close();
 }

 @org.junit.Test
 public void testResolve() throws Exception {
 BeanProperty property = new BeanProperty(null, "mapProperty", null, null, null, null);
 AnnotatedMember accessor = mock(AnnotatedMember.class);
 when(accessor.getName()).thenReturn("mapProperty");
 when(accessor.getValue(anyObject())).thenReturn(MAP_VALUE);

 MapSerializer mapSerializer = new MapSerializer(ObjectMap.class, _objectMapper.getConfig());
 AnyGetterWriter anyGetterWriter = createAnyGetterWriter(property, accessor, mapSerializer);
 ObjectMapper providerMapper = new ObjectMapper();
 ProviderSerializerProvider provider = new ProviderSerializerProvider(providerMapper);

 anyGetterWriter.resolve(provider);
 assertNotNull(anyGetterWriter._mapSerializer);
 equal(mapSerializer, anyGetterWriter._mapSerializer);
 }
}