package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private SerializationConfig config;
 private XmlRootNameLookup rootNameLookup;
 private SerializerProvider serializerProvider;
 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectNode rootNode;

 @Before
 public void setup() throws Exception {
 objectMapper = new ObjectMapper();
 config = objectMapper.getSerializationConfig();
 rootNameLookup = Mockito.spy(new XmlRootNameLookup());
 serializerProvider = new DefaultSerializerProvider();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testCreateInstance() throws Exception {
 SerializerProvider expected = new XmlSerializerProvider(xmlSerializerProvider, config, null);
 assertEquals(expected.getClass(), XmlSerializerProvider.class);
 assertEquals(xmlSerializerProvider, expected._rootNameLookup);
 }

 @Test
 public void testSerializeValue_Null() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 XMLStreamException xmlStreamException = Mockito.mock(XMLStreamException.class);
 StaxUtil.throwXmlAsIOException(xmlStreamException);
 when(gen.getStaxWriter()).thenThrow(xmlStreamException);
 Mockito.doThrow(xmlStreamException).when(xmlSerializerProvider)._serializeXmlNull(gen);

 Object actual = xmlSerializerProvider.serializeValue(gen, null);
 assertNull(actual);
 }

 @Test
 public void testSerializeValue_Object() throws IOException {
 String value = "test";
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 when(xgen.setNextNameIfMissing(Mockito.any(QName.class))).thenReturn(true);
 when(xgen.inRoot()).thenReturn(true);
 when(xgen.initGenerator()).thenReturn(true);
 when(xgen.getStaxWriter()).thenReturn(Mockito.mock(javax.xml.stream.XMLStreamWriter.class));

 QName rootName = new QName("testRoot");
 when(rootNameLookup.findRootName(Mockito.any(Class.class), Mockito.eq(config))).thenReturn(rootName);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(xgen);
 when(xgen.writeStartObject()).thenReturn(true);
 when(xgen.writeFieldName("value")).thenReturn(true);
 when(xgen.writeStringValue(value)).thenReturn(true);
 when(xgen.writeEndObject()).thenReturn(true);

 Object actual = xmlSerializerProvider.serializeValue(gen, value);
 assertEquals(value, actual);
 }

 @Test
 public void testSerializeValue_JavaTypeRootType() throws IOException {
 String value = "test";
 JavaType rootType = TypeFactory.defaultInstance().constructType(String.class);

 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 when(xgen.setNextNameIfMissing(Mockito.any(QName.class))).thenReturn(true);
 when(xgen.inRoot()).thenReturn(true);
 when(xgen.initGenerator()).thenReturn(true);
 when(xgen.getStaxWriter()).thenReturn(Mockito.mock(javax.xml.stream.XMLStreamWriter.class));

 QName rootName = new QName("testRoot");
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(rootName);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(xgen);
 when(xgen.writeStartObject()).thenReturn(true);
 when(xgen.writeFieldName("value")).thenReturn(true);
 when(xgen.writeStringValue(value)).thenReturn(true);
 when(xgen.writeEndObject()).thenReturn(true);

 Object actual = xmlSerializerProvider.serializeValue(gen, value, rootType);
 assertEquals(value, actual);
 }

 @Test
 public void testSerializeValue_JavaTypeRootType_JsonSerializer() throws IOException {
 String value = "test";
 JavaType rootType = TypeFactory.defaultInstance().constructType(String.class);
 JsonSerializer<String> jsonSerializer = Mockito.mock(JsonSerializer.class);

 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 when(xgen.setNextNameIfMissing(Mockito.any(QName.class))).thenReturn(true);
 when(xgen.inRoot()).thenReturn(true);
 when(xgen.initGenerator()).thenReturn(true);
 when(xgen.getStaxWriter()).thenReturn(Mockito.mock(javax.xml.stream.XMLStreamWriter.class));

 QName rootName = new QName("testRoot");
 when(rootNameLookup.findRootName(rootType, config)).thenReturn(rootName);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(xgen);
 when(xgen.writeStartObject()).thenReturn(true);
 when(xgen.writeFieldName("value")).thenReturn(true);
 when(xgen.writeStringValue(value)).thenReturn(true);
 when(xgen.writeEndObject()).thenReturn(true);
 when(jsonSerializer.serialize(Mockito.anyObject(), Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class))).thenReturn(true);

 Object actual = xmlSerializerProvider.serializeValue(gen, value, rootType, jsonSerializer);
 assertEquals(value, actual);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValue_JsonMappingException() throws IOException {
 String value = "test";
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 JsonMappingException jsonMappingException = Mockito.mock(JsonMappingException.class);

 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(null);
 when(gen.writeStartObject()).thenReturn(true);
 when(gen.writeStringValue(value)).thenReturn(true);
 when(gen.writeEndObject()).thenReturn(true);
 when(jsonSerializerProvider.findTypedValueSerializer(Mockito.any(Class.class), Mockito.anyBoolean(), Mockito.any(PropertyName.class))).thenThrow(jsonMappingException);

 Object actual = xmlSerializerProvider.serializeValue(gen, value);
 fail();
 }

 @Test(expected = IOException.class)
 public void testSerializeValue_IOException() throws IOException {
 String value = "test";
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 IOException ioException = Mockito.mock(IOException.class);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(null);
 when(gen.writeStartObject()).thenReturn(true);
 when(gen.writeStringValue(value)).thenReturn(true);
 when(gen.writeEndObject()).thenReturn(true);
 when(jsonSerializerProvider.findTypedValueSerializer(Mockito.any(Class.class), Mockito.anyBoolean(), Mockito.any(PropertyName.class))).thenReturn(null);
 when(gen.writeStartObject()).thenThrow(ioException);

 Object actual = xmlSerializerProvider.serializeValue(gen, value);
 fail();
 }

 @Test
 public void testStartRootArray() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);

 when(xgen.writeStartObject()).thenReturn(true);
 when(xgen.writeFieldName("item")).thenReturn(true);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(xgen);
 xmlSerializerProvider.serializeValue(gen, null);
 xmlSerializerProvider.serializeValue(gen, null);
 xmlSerializerProvider.serializeValue(gen, null);
 xmlSerializerProvider.serializeValue(gen, null);
 assertEquals(4, Arrays.asList(gen.getEvents()).size());
 }

 @Test
 public void testInitWithRootName() throws IOException {
 JsonGenerator gen = Mockito.mock(JsonGenerator.class);
 ToXmlGenerator xgen = Mockito.mock(ToXmlGenerator.class);
 when(xgen.writeStartObject()).thenReturn(true);
 when(xgen.writeFieldName("root")).thenReturn(true);
 when(xmlSerializerProvider._asXmlGenerator(gen)).thenReturn(xgen);

 xmlSerializerProvider.initWithRootName(gen, new QName("root"));
 assertEquals(2, Arrays.asList(gen.getEvents()).size());
 }
}