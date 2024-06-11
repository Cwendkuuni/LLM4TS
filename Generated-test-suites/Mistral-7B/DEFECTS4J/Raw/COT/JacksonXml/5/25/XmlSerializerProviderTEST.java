package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 @InjectMocks
 private XmlSerializerProvider xmlSerializerProvider;

 @Mock
 private SerializationConfig config;

 @Mock
 private SerializerFactory serializerFactory;

 @Mock
 private JsonSerializer<Object> jsonSerializer;

 @Mock
 private ToXmlGenerator toXmlGenerator;

 @Mock
 private QName rootName;

 @Mock
 private JsonGenerator jsonGenerator;

 @Test
 public void testCopy() throws IOException {
 XmlSerializerProvider originalProvider = new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("rootName")));
 XmlSerializerProvider copiedProvider = xmlSerializerProvider.copy();
 assertEquals(originalProvider, copiedProvider);
 }

 @Test
 public void testCreateInstance() throws IOException {
 XmlSerializerProvider originalProvider = new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("rootName")), config, serializerFactory);
 XmlSerializerProvider newProvider = xmlSerializerProvider.createInstance(config, serializerFactory);
 assertEquals(originalProvider, newProvider);
 }

 @Test
 public void testCreateInstanceWithRootName() {
 XmlSerializerProvider originalProvider = new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("rootName")), config, serializerFactory);
 XmlSerializerProvider newProvider = new XmlSerializerProvider(originalProvider, config, serializerFactory);
 assertNotSame(originalProvider, newProvider);
 assertEquals(originalProvider._rootNameLookup, newProvider._rootNameLookup);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 Mockito.when(xmlSerializerProvider._rootNameLookup.findRootName(Object.class, config)).thenReturn(rootName);
 Mockito.when(toXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(false);
 Mockito.when(toXmlGenerator.inRoot()).thenReturn(false);

 xmlSerializerProvider.serializeValue(jsonGenerator, null);
 assertEquals("null", jsonGenerator.getXML().toString());
 }

 @Test
 public void testSerializeValueObject() throws IOException {
 Object value = new Object();
 Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(jsonSerializer);
 Mockito.when(jsonSerializer.serialize(value, jsonGenerator, xmlSerializerProvider)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 Mockito.verify(jsonSerializer, Mockito.times(1)).serialize(value, jsonGenerator, xmlSerializerProvider);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueObjectNullSerializer() throws IOException, JsonMappingException {
 Object value = new Object();
 Mockito.when(xmlSerializerProvider.findTypedValueSerializer(Object.class, true, null)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 }

 @Test
 public void testSerializeValueJavaType() throws IOException {
 JavaType javaType = new SimpleType(Object.class);
 Mockito.when(xmlSerializerProvider.findTypedValueSerializer(javaType, true, null)).thenReturn(jsonSerializer);
 Mockito.when(jsonSerializer.serialize(Mockito.any(), jsonGenerator, xmlSerializerProvider)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, null, javaType, jsonSerializer);
 Mockito.verify(jsonSerializer, Mockito.times(1)).serialize(Mockito.any(), jsonGenerator, xmlSerializerProvider);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueJavaTypeNullSerializer() throws IOException, JsonMappingException {
 JavaType javaType = new SimpleType(Object.class);
 Mockito.when(xmlSerializerProvider.findTypedValueSerializer(javaType, true, null)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, null, javaType, jsonSerializer);
 }

 @Test
 public void testSerializeValueWithRootName() throws IOException {
 Object value = new Object();
 Mockito.when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootName);
 Mockito.when(toXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(false);
 Mockito.when(toXmlGenerator.inRoot()).thenReturn(false);
 Mockito.when(jsonSerializer.serialize(value, jsonGenerator, xmlSerializerProvider)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 assertEquals("rootName", jsonGenerator.getXML().toString());
 }

 @Test
 public void testSerializeValueWithRootNameAndNamespace() throws IOException {
 Object value = new Object();
 String namespace = "testNamespace";
 Mockito.when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(new QName(namespace, "rootName"));
 Mockito.when(toXmlGenerator.setNextNameIfMissing(new QName(namespace, "rootName"))).thenReturn(false);
 Mockito.when(toXmlGenerator.initGenerator()).thenThrow(XMLStreamException.class);
 Mockito.when(toXmlGenerator.getStaxWriter()).thenReturn(Mockito.mock(StaxWriter.class));
 Mockito.when(toXmlGenerator.getStaxWriter().setDefaultNamespace(namespace)).thenReturn(null);
 Mockito.when(jsonSerializer.serialize(value, jsonGenerator, xmlSerializerProvider)).thenReturn(null);

 xmlSerializerProvider.serializeValue(jsonGenerator, value);
 assertEquals("<rootName xmlns:\""+namespace+"\">"+
                 "<item>"+
                 "null" +
                 "</item>"+
                 "</rootName>", jsonGenerator.getXML().toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializeWithNullRootNameLookup() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializeWithNullSerializationConfig() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("rootName")), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInitializeWithNullSerializerFactory() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new PropertyName("rootName")), config, null);
 }
}