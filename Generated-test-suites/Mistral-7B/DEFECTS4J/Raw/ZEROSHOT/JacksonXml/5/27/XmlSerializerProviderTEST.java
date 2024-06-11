package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {

 private XmlMapper mapper;
 private XmlSerializerProvider provider;
 private ObjectMapper baseMapper;
 private SerializationConfig config;
 private QName rootNameForNull;
 private XmlRootNameLookup rootNameLookup;
 private SerializerProvider baseProvider;

 @Before
 public void setup() {
 mapper = new XmlMapper();
 provider = mapper.getXmlSerializerProvider();
 baseMapper = new ObjectMapper();
 baseProvider = baseMapper.getSerializerProvider();
 config = baseMapper.getSerializationConfig();
 rootNameForNull = XmlSerializerProvider.ROOT_NAME_FOR_NULL;
 rootNameLookup = mock(XmlRootNameLookup.class);
 }

 @Test
 public void testXmlSerializerProviderCopy() {
 XmlSerializerProvider original = new XmlSerializerProvider(rootNameLookup);
 XmlSerializerProvider copy = original.copy();
 assertEquals(original, copy);
 }

 @Test
 public void testXmlSerializerProviderCreateInstance() {
 XmlSerializerProvider original = new XmlSerializerProvider(rootNameLookup);
 XmlSerializationConfig config = mock(XmlSerializationConfig.class);
 SerializerFactory factory = mock(SerializerFactory.class);
 XmlSerializerProvider instance = new XmlSerializerProvider(original, config, factory);
 assertEquals(original, instance);
 }

 @Test
 public void testXmlSerializerProviderInitWithRootName() throws IOException {
 QName rootName = new QName("rootElement");
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(true);
 provider._initWithRootName(xgen, rootName);
 verify(xgen).setNextName(rootName);
 }

 @Test
 public void testXmlSerializerProviderInitWithRootNameNoSetNextName() throws IOException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 when(xgen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
 provider._initWithRootName(xgen, new QName("rootElement"));
 assertNull(xgen.getName());
 }

 @Test
 public void testXmlSerializerProviderStartRootArray() throws IOException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 provider._startRootArray(xgen, new QName("rootElement"));
 verify(xgen).writeStartObject();
 verify(xgen).writeFieldName("item");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithNull() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 provider._serializeXmlNull(gen);
 verify(gen, times(1)).writeStartObject();
 verify(gen, times(1)).writeFieldName("null");
 verify(gen, times(1)).writeEndObject();
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithRootNameFromConfig() throws IOException {
 Object value = new Object();
 QName rootName = new QName("rootElement");
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(rootName);
 provider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("rootElement");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithRootNameFromConfigNull() throws IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(null);
 provider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithRootNameFromConfigEmptyString() throws IOException {
 Object value = new Object();
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(new QName(""));
 provider.serializeValue(gen, value);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithCustomRootName() throws IOException {
 Object value = new Object();
 QName rootName = new QName("rootElement");
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(null);
 when(gen.setNextName(any(QName.class))).thenReturn(true);
 provider.serializeValue(gen, value, rootName);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("rootElement");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithRootName() throws IOException {
 Object value = new Object();
 QName rootName = new QName("rootElement");
 JavaType rootType = mapper.getTypeFactory().constructType(Object[].class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(rootName);
 provider.serializeValue(gen, value, rootType, null);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("rootElement");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithRootNameNull() throws IOException {
 Object value = new Object();
 JavaType rootType = mapper.getTypeFactory().constructType(Object[].class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(null);
 provider.serializeValue(gen, value, rootType, null);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithNullSerializer() throws IOException {
 Object value = new Object();
 JavaType rootType = mapper.getTypeFactory().constructType(Object[].class);
 JsonSerializer<Object> nullSerializer = null;
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(new QName("rootElement"));
 provider.serializeValue(gen, value, rootType, nullSerializer);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("item");
 }

 @Test
 public void testXmlSerializerProvider_serializeValueWithSerializer() throws IOException {
 Object value = new Object();
 JavaType rootType = mapper.getTypeFactory().constructType(Object[].class);
 StdSerializer<Object> serializer = new StdSerializer<Object>(Object.class);
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(new QName("rootElement"));
 when(baseProvider.findValueSerializer(eq(Object.class), eq(true), any()).thenReturn(serializer));
 provider.serializeValue(gen, value, rootType, null);
 verify(gen).writeStartObject();
 verify(gen).writeFieldName("rootElement");
 verify(serializer).serialize(value, gen, provider);
 }

 @Test
 public void testXmlSerializerProvider_wrapAsIOE() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 IOException ioException = new IOException("Test IO exception");
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(new QName("rootElement"));
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getXmlSerializerProvider();
 assertEquals("Test IO exception", provider._wrapAsIOE(gen, ioException).getMessage());
 }

 @Test(expected = JsonMappingException.class)
 public void testXmlSerializerProvider_wrapAsIOE_JsonMappingException() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 JsonMappingException jsonMappingException = new JsonMappingException(gen, "", null);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 when(gen.getSerializationConfig()).thenReturn(config);
 when(rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(new QName("rootElement"));
 ObjectMapper mapper = new ObjectMapper();
 SerializerProvider provider = mapper.getXmlSerializerProvider();
 provider._wrapAsIOE(gen, jsonMappingException);
 }

 @Test
 public void testXmlSerializerProvider_asXmlGenerator() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(mock(ToXmlGenerator.class));
 ToXmlGenerator toXmlGenerator = provider._asXmlGenerator(gen);
 assertEquals(toXmlGenerator, toXmlGenerator);
 }

 @Test
 public void testXmlSerializerProvider_asXmlGenerator_Null() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(null);
 ToXmlGenerator toXmlGenerator = provider._asXmlGenerator(gen);
 assertNull(toXmlGenerator);
 }
}