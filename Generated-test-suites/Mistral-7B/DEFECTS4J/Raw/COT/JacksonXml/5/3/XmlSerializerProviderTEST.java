package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 @Mock
 private SerializationConfig config;
 @Mock
 private SerializerFactory serializerFactory;
 @Mock
 private XmlRootNameLookup xmlRootNameLookup;
 @Mock
 private JsonSerializer<Object> jsonSerializer;
 @Mock
 private ToXmlGenerator toXmlGenerator;
 @Mock
 private JsonGenerator jsonGenerator;
 @Mock
 private ObjectNode objectNode;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 }

 @Test
 public void testEmptyConstructor() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
 assertEquals(XmlSerializerProvider.class, xmlSerializerProvider.getClass());
 }

 @Test
 public void testConstructorWithConfigAndFactory() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, serializerFactory);
 assertEquals(XmlSerializerProvider.class, xmlSerializerProvider.getClass());
 assertEquals(config, xmlSerializerProvider.getConfig());
 assertEquals(serializerFactory, xmlSerializerProvider.getFactory());
 }

 @Test
 public void testCopyConstructor() {
 XmlSerializerProvider originalXmlSerializerProvider = new XmlSerializerProvider();
 XmlSerializerProvider copiedXmlSerializerProvider = new XmlSerializerProvider(originalXmlSerializerProvider);
 assertEquals(XmlSerializerProvider.class, copiedXmlSerializerProvider.getClass());
 assertEquals(originalXmlSerializerProvider.getFactory(), copiedXmlSerializerProvider.getFactory());
 assertEquals(originalXmlSerializerProvider.getConfig(), copiedXmlSerializerProvider.getConfig());
 assertNotSame(originalXmlSerializerProvider, copiedXmlSerializerProvider);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 when(jsonGenerator.writeStartObject()).thenReturn(objectNode);
 when(jsonGenerator.writeEndObject()).thenReturn(objectNode);
 when(jsonGenerator.writeFieldName("item")).thenReturn(objectNode);

 xmlSerializerProvider.serializeValue(jsonGenerator, null);

 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 verify(jsonGenerator).writeEndObject();
 }

 @Test
 public void testSerializeValueObject() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 when(jsonGenerator.writeStartObject()).thenReturn(objectNode);
 when(jsonGenerator.writeEndObject()).thenReturn(objectNode);
 when(jsonGenerator.writeFieldName("item")).thenReturn(objectNode);
 when(jsonSerializer.serialize("test", jsonGenerator, xmlSerializerProvider)).thenReturn(objectNode);

 xmlSerializerProvider.serializeValue(jsonGenerator, "test", jsonSerializer);

 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 verify(jsonGenerator).writeEndObject();
 verify(jsonSerializer).serialize("test", jsonGenerator, xmlSerializerProvider);
 }

 @Test
 public void testSerializeValueObjectWithRootType() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 when(jsonGenerator.writeStartObject()).thenReturn(objectNode);
 when(jsonGenerator.writeEndObject()).thenReturn(objectNode);
 when(jsonGenerator.writeFieldName("item")).thenReturn(objectNode);
 when(jsonSerializer.serialize("test", jsonGenerator, xmlSerializerProvider)).thenReturn(objectNode);

 JavaType rootType = new TypeFactory().constructType(Object.class);
 xmlSerializerProvider.serializeValue(jsonGenerator, "test", rootType, jsonSerializer);

 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 verify(jsonGenerator).writeEndObject();
 verify(jsonSerializer).serialize("test", jsonGenerator, xmlSerializerProvider);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullGenerator() throws IOException {
 xmlSerializerProvider.serializeValue(null, "test", jsonSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullSerializer() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 xmlSerializerProvider.serializeValue(jsonGenerator, null, jsonSerializer);
 }

 @Test
 public void testRootNameFromConfig() {
 assertEquals(null, xmlSerializerProvider._rootNameFromConfig());
 }

 @Test
 public void testRootNameLookupFindRootName() {
 QName result = xmlSerializerProvider._rootNameLookup.findRootName(Object.class, config);
 assertEquals(new QName("Object"), result);
 }

 @Test
 public void testInitializeWithRootName() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 QName rootName = new QName("TestRootName");
 xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);
 verify(toXmlGenerator).setNextName(rootName);
 verify(toXmlGenerator).initGenerator();
 }

 @Test(expected = XMLStreamException.class)
 public void testInitializeWithRootNameWithInvalidNamespace() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 QName rootName = new QName("TestRootName", "InvalidNamespace");
 xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);
 }

 @Test
 public void testStartRootArray() throws IOException {
 when(toXmlGenerator.getGenerator()).thenReturn(jsonGenerator);
 xmlSerializerProvider._startRootArray(toXmlGenerator, new QName("Array"));
 verify(jsonGenerator).writeStartObject();
 verify(jsonGenerator).writeFieldName("item");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartRootArrayWithNullRootName() throws IOException {
 xmlSerializerProvider._startRootArray(toXmlGenerator, null);
 }

 @Test
 public void testCopy() throws IOException {
 XmlSerializerProvider originalXmlSerializerProvider = new XmlSerializerProvider();
 XmlSerializerProvider copiedXmlSerializerProvider = originalXmlSerializerProvider.copy();
 assertEquals(XmlSerializerProvider.class, copiedXmlSerializerProvider.getClass());
 assertEquals(originalXmlSerializerProvider.getFactory(), copiedXmlSerializerProvider.getFactory());
 assertEquals(originalXmlSerializerProvider.getConfig(), copiedXmlSerializerProvider.getConfig());
 assertNotSame(originalXmlSerializerProvider, copiedXmlSerializerProvider);
 }
}