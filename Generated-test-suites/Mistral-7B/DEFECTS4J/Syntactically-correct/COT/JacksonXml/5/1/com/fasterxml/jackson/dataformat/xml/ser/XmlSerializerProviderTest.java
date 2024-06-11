package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 @Mock
 private JsonGenerator gen;
 @Mock
 private SerializationConfig config;
 @Mock
 private SerializerFactory factory;
 @Mock
 private XmlRootNameLookup rootNameLookup;
 @Mock
 private PropertyName rootName;
 @Mock
 private JavaType javaType;
 @Mock
 private JsonSerializer<Object> jsonSerializer;
 @Mock
 private ToXmlGenerator toXmlGenerator;

 @Before
 public void setup() {
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 when(config.getFullRootName()).thenReturn(rootName);
 when(config.getRootValueSerializer(SimpleBeanPropertySerializer.class)).thenReturn(jsonSerializer);
 when(factory.findValueSerializer(SimpleBeanPropertySerializer.class, null, javaType, config, null)).thenReturn(jsonSerializer);
 when(factory.findValueSerializer(SimpleBeanPropertySerializer.class, null, ClassUtil.emptyClassName(), config, null)).thenReturn(jsonSerializer);
 when(factory.createGenerator(config, gen)).thenReturn(toXmlGenerator);
 when(toXmlGenerator.getStaxWriter()).thenReturn(mock(javax.xml.stream.XMLStreamWriter.class));
 }

 @Test
 public void testCopy() {
 XmlSerializerProvider provider1 = xmlSerializerProvider;
 XmlSerializerProvider provider2 = provider1.copy();
 assertEquals(provider1, provider2);
 }

 @Test
 public void testCreateInstance() throws JsonMappingException, IOException {
 XmlSerializerProvider provider1 = xmlSerializerProvider;
 SerializerProvider result = xmlSerializerProvider.createInstance(config, factory);
 assertEquals(provider1, result);
 }

 @Test
 public void testCreateInstanceWithRootNameLookup() {
 XmlSerializerProvider src = mock(XmlSerializerProvider.class);
 XmlSerializerProvider result = new XmlSerializerProvider(src, config, factory);
 assertEquals(src, result._rootNameLookup);
 }

 @Test
 public void testCreateInstanceWithEmptyRootName() {
 XmlSerializerProvider src = mock(XmlSerializerProvider.class);
 XmlSerializerProvider result = new XmlSerializerProvider(src, config, factory);
 assertNull(result._rootNameLookup);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullRootName() throws JsonMappingException, IOException {
 xmlSerializerProvider.serializeValue(gen, null);
 }

 @Test
 public void testSerializeValue() throws JsonMappingException, IOException {
 String expectedRootName = "root";
 when(rootName.getSimpleName()).thenReturn(expectedRootName);
 when(rootNameLookup.findRootName(Object.class, config)).thenReturn(rootName);
 when(jsonSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn("");
 xmlSerializerProvider.serializeValue(gen, new Object(), javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName(expectedRootName);
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithCustomRootName() throws JsonMappingException, IOException {
 String customRootName = "customRoot";
 when(rootName.getSimpleName()).thenReturn(customRootName);
 when(config.getRootValueSerializer(SimpleBeanPropertySerializer.class)).thenReturn(null);
 when(rootNameLookup.findRootName(Object.class, config)).thenReturn(null);
 when(rootNameLookup.findRootName(Object.class, config, customRootName)).thenReturn(new QName(customRootName));
 when(jsonSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn("");
 xmlSerializerProvider.serializeValue(gen, new Object(), javaType);
 verify(gen).writeStartElement(customRootName);
 verify(gen).writeEndElement();
 }

 @Test
 public void testSerializeValueWithArray() throws JsonMappingException, IOException {
 String expectedRootName = "root";
 when(rootName.getSimpleName()).thenReturn(expectedRootName);
 when(rootNameLookup.findRootName(Object[].class, config)).thenReturn(rootName);
 when(jsonSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn("");
 xmlSerializerProvider.serializeValue(gen, new Object[]{}, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName(expectedRootName);
 verify(gen).writeStartArray();
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithArrayAndCustomRootName() throws JsonMappingException, IOException {
 String expectedRootName = "customRoot";
 String arrayFieldName = "items";
 when(rootName.getSimpleName()).thenReturn(expectedRootName);
 when(config.getRootValueSerializer(SimpleBeanPropertySerializer.class)).thenReturn(null);
 when(rootNameLookup.findRootName(Object[].class, config)).thenReturn(null);
 when(rootNameLookup.findRootName(Object[].class, config, expectedRootName)).thenReturn(new QName(expectedRootName, "items"));
 when(jsonSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn("");
 xmlSerializerProvider.serializeValue(gen, new Object[]{}, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName(expectedRootName);
 verify(gen).writeStartArray();
 verify(gen).writeFieldName(arrayFieldName);
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithEmptyObject() throws JsonMappingException, IOException {
 when(jsonSerializer.serialize(anyObject(), anyObject(), anyObject())).thenReturn("");
 xmlSerializerProvider.serializeValue(gen, new Object(), javaType);
 verify(gen).writeStartObject();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithEmptyList() throws JsonMappingException, IOException {
 JavaType javaType = mapType(Collections.class, Map.class);
 xmlSerializerProvider.serializeValue(gen, Collections.emptyList(), javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 verify(gen).writeStartArray();
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithEmptyMap() throws JsonMappingException, IOException {
 JavaType javaType = mapType(HashMap.class, Map.class);
 xmlSerializerProvider.serializeValue(gen, Collections.emptyMap(), javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 verify(gen).writeStartArray();
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithMap() throws JsonMappingException, IOException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", "value2");
 JavaType javaType = mapType(map.getClass(), Map.class);
 when(jsonSerializer.serializeValueAsString(map.get("key1"))).thenReturn("value1");
 when(jsonSerializer.serializeValueAsString(map.get("key2"))).thenReturn("value2");
 xmlSerializerProvider.serializeValue(gen, map, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("key1");
 verify(gen).writeString("value1");
 verify(gen).writeFieldName("key2");
 verify(gen).writeString("value2");
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithNullMapEntry() throws JsonMappingException, IOException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", "value1");
 map.put("key2", null);
 JavaType javaType = mapType(map.getClass(), Map.class);
 xmlSerializerProvider.serializeValue(gen, map, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("key1");
 verify(gen).writeString("value1");
 verify(gen).writeNullField("key2");
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithNestedMap() throws JsonMappingException, IOException {
 Map<String, Object> outerMap = new HashMap<>();
 Map<String, Object> innerMap = new HashMap<>();
 innerMap.put("key1", "value1");
 innerMap.put("key2", "value2");
 outerMap.put("key1", innerMap);
 JavaType javaType = mapType(outerMap.getClass(), Map.class);
 when(jsonSerializer.serializeValueAsString(innerMap.get("key1"))).thenReturn("value1");
 when(jsonSerializer.serializeValueAsString(innerMap.get("key2"))).thenReturn("value2");
 xmlSerializerProvider.serializeValue(gen, outerMap, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("key1");
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("key1");
 verify(gen).writeString("value1");
 verify(gen).writeFieldName("key2");
 verify(gen).writeString("value2");
 verify(gen).writeEndObject();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithNestedList() throws JsonMappingException, IOException {
 List<List<Object>> list = new ArrayList<>();
 List<Object> innerList1 = Arrays.asList("value1", "value2");
 List<Object> innerList2 = Arrays.asList("value3", "value4");
 list.add(innerList1);
 list.add(innerList2);
 JavaType javaType = listType(List.class, List.class);
 xmlSerializerProvider.serializeValue(gen, list, javaType);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 verify(gen).writeStartArray();
 verify(gen).writeStartArray();
 verify(gen).writeString("value1");
 verify(gen).writeString("value2");
 verify(gen).writeEndArray();
 verify(gen).writeStartArray();
 verify(gen).writeString("value3");
 verify(gen).writeString("value4");
 verify(gen).writeEndArray();
 verify(gen).writeEndArray();
 verify(gen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithNullValue() throws JsonMappingException, IOException {
 xmlSerializerProvider.serializeValue(gen, null, javaType);
 verify(gen).writeNull();
 }

 private JavaType mapType(Class<?> keyType, Class<?> valueType) {
 PropertyName rootName = new PropertyName("rootName");
 return new TypeFactory().constructType(Map.class, keyType, valueType, rootName);
 }

 private JavaType listType(Class<?> elementType, Class<?> containerType) {
 PropertyName rootName = new PropertyName("items");
 return new TypeFactory().constructType(List.class, elementType, rootName);
 }
}