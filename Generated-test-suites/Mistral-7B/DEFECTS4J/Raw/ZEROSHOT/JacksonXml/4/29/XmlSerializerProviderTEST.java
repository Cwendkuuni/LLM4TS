package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Unit tests for {@link XmlSerializerProvider}.
 */
public class XmlSerializerProviderTest {

 private ObjectMapper mapper;
 private XmlSerializerProvider provider;
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 provider = mapper.getXmlFactory().getSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws JsonMappingException, IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 provider.serializeValue(gen, null);
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<null/>", actual);
 }

 @Test
 public void testSerializeValue() throws JsonMappingException, IOException {
 Object value = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 provider.serializeValue(gen, value);
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 fail("Expected null value to be serialized as <null/> but got: " + actual);
 }

 @Test
 public void testSerializeValueWithRootName() throws JsonMappingException, IOException {
 String rootName = "testRoot";
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparator("");
 config.setRootValueSeparator("root");
 provider = new XmlSerializerProvider(config, mapper.getSerializerFactory());
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 provider.serializeValue(gen, value, new SimpleModule().setRootValueSeparator("root").addSerializer(Object.class, new StdSerializer<Object>() {}));
 gen.writeStartElement(rootName);
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root><" + value.getClass().getName() + "/></root>", actual);
 }

 @Test
 public void testSerializeValueWithRootType() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, rootType);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement("item");
 gen.writeEndElement();
 gen.writeEndObject();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<item></item>", actual);
 }

 @Test
 public void testSerializeValueWithRootTypeAndSerializer() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, rootType);
 provider.setSerializerInstance(Object.class, serializer);
 provider.serializeValue(gen, value, rootType, serializer);
 gen.writeStartElement("item");
 gen.writeEndElement();
 gen.writeEndObject();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<item></item>", actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGenerator() throws JsonMappingException, IOException {
 JsonGenerator gen = mapper.createXmlGenerator(System.out, null);
 provider.serializeValue(gen, value);
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 fail("Expected null value to be serialized as <null/> but got: " + actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGeneratorAndRootType() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider.serializeValue(gen, value, rootType);
 gen.writeEndObject();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<item></item>", actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGeneratorAndRootTypeAndSerializer() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider.setSerializerInstance(Object.class, serializer);
 provider.serializeValue(gen, value, rootType, serializer);
 gen.writeEndObject();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<item></item>", actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGeneratorAndRootName() throws JsonMappingException, IOException {
 JsonGenerator gen = mapper.createXmlGenerator(System.out, null);
 provider.setRootValueSeparator("root");
 provider.serializeValue(gen, value, null);
 gen.writeStartElement("root");
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root></root>", actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGeneratorAndRootNameAndRootType() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 provider.setRootValueSeparator("root");
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement("root");
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root></root>", actual);
 }

 @Test
 public void testSerializeValueWithAsXmlGeneratorAndRootNameAndRootTypeAndSerializer() throws JsonMappingException, IOException {
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 provider.setRootValueSeparator("root");
 JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {};
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider.setSerializerInstance(Object.class, serializer);
 provider.serializeValue(gen, value, rootType, serializer);
 gen.writeStartElement("root");
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root></root>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookup() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 provider = new XmlSerializerProvider(lookup);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 provider.serializeValue(gen, value);
 gen.writeStartElement("root");
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root></root>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookupAndRootType() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, rootType);
 provider = new XmlSerializerProvider(lookup);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement("item");
 gen.writeEndElement();
 gen.writeEndObject();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<item></item>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookupAndRootTypeAndSerializer() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 provider.setSerializerInstance(Object.class, new StdSerializer<Object>(Object.class) {});
 provider.setRootValueSeparator("root");
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider = new XmlSerializerProvider(lookup);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement("root");
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<root></root>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookupAndRootName() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 QName rootName = new QName("testRoot");
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 provider = new XmlSerializerProvider(lookup);
 provider.serializeValue(gen, value, rootName);
 gen.writeStartElement(rootName.getLocalPart());
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<testRoot></testRoot>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookupAndRootNameAndRootType() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 QName rootName = new QName("testRoot");
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, rootType);
 provider = new XmlSerializerProvider(lookup);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement(rootName.getLocalPart());
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<testRoot></testRoot>", actual);
 }

 @Test
 public void testSerializeValueWithXmlRootNameLookupAndRootNameAndRootTypeAndSerializer() throws JsonMappingException, IOException {
 XmlRootNameLookup lookup = new XmlRootNameLookup();
 lookup.setRootValueSeparator("root");
 QName rootName = new QName("testRoot");
 JavaType rootType = mapper.getTypeFactory().constructType(String[].class);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 provider.setSerializerInstance(Object.class, new StdSerializer<Object>(Object.class) {});
 provider.setRootValueSeparator("root");
 JsonGenerator gen = mapper.createXmlGenerator(System.out, rootType);
 provider = new XmlSerializerProvider(lookup);
 provider.serializeValue(gen, value, rootType);
 gen.writeStartElement(rootName.getLocalPart());
 gen.writeEndElement();
 gen.flush();
 String actual = mapper.readTree(System.out).getValueAsString();
 assertEquals("<testRoot></testRoot>", actual);
 }
}