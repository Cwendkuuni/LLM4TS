package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider provider;
 @Mock
 private JsonGenerator gen;
 @Mock
 private SerializationConfig config;
 @Mock
 private SerializerFactory factory;
 @Mock
 private XmlRootNameLookup rootNameLookup;
 @Mock
 private JsonSerializer<Object> serializer;
 @Mock
 private JavaType rootType;
 @Mock
 private ToXmlGenerator xmlGen;

 @Before
 public void setup() {
 provider = new XmlSerializerProvider(rootNameLookup);
 when(rootNameLookup.findRootName(any(Class.class), eq(config)))
 .thenReturn(new QName("rootName"));
 when(factory.createValueSerializer(any(Class.class), anyBoolean(), any()))
 .thenReturn(serializer);
 when(gen.writeStartObject()).thenReturn(xmlGen);
 }

 @Test
 public void testSerializeValue() throws IOException {
 Object value = new Object();
 provider.serializeValue(gen, value);

 verify(xmlGen).setNextName("rootName");
 verify(xmlGen).writeStartObject();
 verify(serializer).serialize(value, xmlGen, eq(provider));
 verify(xmlGen).writeEndObject();
 }

 @Test
 public void testSerializeValueWithRootType() throws IOException {
 Object value = new Object();
 JavaType rootType = new JavaType(String.class,Arrays.asList(new TypeReference<List<String>>(){}) );
 provider.serializeValue(gen, value, rootType, serializer);

 verify(xmlGen).setNextName("rootName");
 verify(xmlGen).writeStartObject();
 verify(serializer).serialize(value, xmlGen, eq(provider));
 verify(xmlGen).writeEndObject();
 }

 @Test
 public void testSerializeNull() throws IOException {
 provider.serializeValue(gen, null);

 verify(xmlGen).setNextName("null");
 verify(xmlGen).writeNull();
 }

 @Test
 public void testSerializeValueWithRootNameFromConfig() throws IOException {
 PropertyName rootName = new PropertyName("rootNameFromConfig");
 when(config.getFullRootName()).thenReturn(rootName);
 provider = new XmlSerializerProvider(rootNameLookup);

 provider.serializeValue(gen, new Object());

 verify(xmlGen).setNextName("rootNameFromConfig");
 verify(xmlGen).writeStartObject();
 verifyNoMoreInteractions(xmlGen);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithInvalidGenerator() throws IOException {
 provider.serializeValue(new Object(), new Object());
 }

 @Test
 public void testInitializeWithRootName() throws IOException {
 when(xmlGen.setNextNameIfMissing(any(QName.class))).thenReturn(false);
 provider.serializeValue(gen, new Object());

 verify(xmlGen).setNextName("rootName");
 verify(xmlGen).writeStartObject();
 }

 @Test(expected = JsonMappingException.class)
 public void testInitializeWithNullRootName() throws IOException {
 provider.serializeValue(gen, new Object(), null);
 }

 @Test
 public void testInitializeWithEmptyRootName() throws IOException {
 provider.serializeValue(gen, new Object(), new QName(""));
 }

 @Test
 public void testInitializeWithRootNameWithNamespace() throws IOException {
 QName rootName = new QName("http://example.com/rootName");
 provider.serializeValue(gen, new Object(), rootName);

 verify(xmlGen).setNextName(rootName);
 verify(xmlGen).writeStartObject();
 }

 @Test
 public void testSerializeValueWithArray() throws IOException {
 Object[] values = new Object[]{"value1", "value2"};
 provider = new XmlSerializerProvider(rootNameLookup);
 provider.serializeValue(gen, values);

 verify(xmlGen).writeStartObject();
 verify(xmlGen).writeFieldName("item");
 verify(xmlGen, times(values.length)).writeStartObject();
 for (Object value : values) {
 verify(xmlGen).writeNull();
 }
 for (Object value : values) {
 verify(xmlGen, times(1)).writeEndObject();
 }
 verify(xmlGen).writeEndObject();
 }
}