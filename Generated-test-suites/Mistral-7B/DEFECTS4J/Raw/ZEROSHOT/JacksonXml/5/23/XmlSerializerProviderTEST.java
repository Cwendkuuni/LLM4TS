package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.impl.ToXmlGenerator;

public class XmlSerializerProviderTest {

 private static final ObjectMapper MAPPER = new XmlMapper();
 private static final SerializationConfig config = MAPPER.getSerializationConfig();
 private static final XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
 private static final XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNameLookup);

 @Test
 public void testDefaultSerialization() throws JsonProcessingException, IOException {
 String expectedXml = "<root><value>Hello World</value></root>";
 Object input = "Hello World";
 QName rootName = new QName("root");
 ToXmlGenerator xgen = _createToXmlGenerator(rootName);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithRootNameFromConfig() throws JsonProcessingException, IOException {
 String expectedXml = "<config><value>Hello World</value></config>";
 config.setRootValueSeparator("<", "/");
 config.setRootValueSeparatorSeparator('/');
 config.setRootName("config");
 String input = "Hello World";
 ToXmlGenerator xgen = _createToXmlGenerator(null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithRootNameFromRootNameLookup() throws JsonProcessingException, IOException {
 String expectedXml = "<rootElement><value>Hello World</value></rootElement>";
 String input = "Hello World";
 ToXmlGenerator xgen = _createToXmlGenerator(new QName("rootElement"));
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithNullValue() throws JsonProcessingException, IOException {
 String expectedXml = "<null/>";
 ToXmlGenerator xgen = _createToXmlGenerator(null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);
 jsonSerializer.serialize(null, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidRootName() throws JsonProcessingException, IOException {
 String expectedXml = "<value>Hello World</value>";
 config.setRootName("value");
 ToXmlGenerator xgen = _createToXmlGenerator(null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(String.class, null);
 jsonSerializer.serialize("Hello World", xgen, this);
 fail("Expected JsonMappingException: root name clashes with property name");
 }

 @Test
 public void testSerializationWithArray() throws JsonProcessingException, IOException {
 String[] input = {"Hello", "World"};
 String expectedXml = "<arrayOfString><item><value>Hello</value></item><item><value>World</value></item></arrayOfString>";
 ToXmlGenerator xgen = _createToXmlGenerator(new QName("arrayOfString"));
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(serializerProvider, config, null);
 JsonSerializer<Object> jsonSerializer = xmlSerializerProvider.findValueSerializer(input.getClass(), null);
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithCustomSerializer() throws JsonProcessingException, IOException {
 String input = "Hello World";
 StdSerializer<String> customSerializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("Custom: " + value);
 }
 };
 config.setSerializerProvider(new DefaultSerializerProvider());
 config.setSerializerProvider(serializerProvider);
 JsonSerializer<String> jsonSerializer = MAPPER.findValueSerializer(String.class, null);
 jsonSerializer = jsonSerializer instanceof XmlSerializer ? ((XmlSerializer) jsonSerializer).withSerializerProvider(serializerProvider) : jsonSerializer;
 jsonSerializer = (jsonSerializer == null) ? serializerProvider.findValueSerializer(String.class, null) : jsonSerializer;
 ToXmlGenerator xgen = _createToXmlGenerator(null);
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals("<value>Custom: Hello World</value>", actualXml);
 }

 @Test
 public void testCopy() {
 XmlSerializerProvider original = new XmlSerializerProvider(serializerProvider, config, null);
 XmlSerializerProvider copy = original.copy();
 assertNotSame(original, copy);
 assertEquals(original.getClass(), copy.getClass());
 assertEquals(original._rootNameLookup, copy._rootNameLookup);
 }

 @Test
 public void testCreateInstance() {
 XmlSerializerProvider original = new XmlSerializerProvider(serializerProvider, config, null);
 XmlSerializerProvider instance = XmlSerializerProvider.createInstance(config, null);
 assertNotSame(original, instance);
 assertEquals(original.getClass(), instance.getClass());
 assertEquals(original._rootNameLookup, instance._rootNameLookup);
 }

 @Test
 public void testCreateInstanceWithRootNameLookup() {
 XmlSerializerProvider original = new XmlSerializerProvider(serializerProvider, config, null);
 XmlSerializerProvider instance = XmlSerializerProvider.createInstance(config, rootNameLookup);
 assertNotSame(original, instance);
 assertEquals(original.getClass(), instance.getClass());
 assertEquals(original._rootNameLookup, instance._rootNameLookup);
 }

 @Test
 public void testCreateInstanceWithNullSerializationConfig() {
 assertThrows(NullPointerException.class, () -> XmlSerializerProvider.createInstance(null, null));
 }

 @Test
 public void testCreateInstanceWithNullSerializerFactory() {
 XmlSerializerProvider config = new XmlSerializerProvider(serializerProvider, null);
 assertThrows(NullPointerException.class, () -> XmlSerializerProvider.createInstance(config, null));
 }

 @Test
 public void testCreateInstanceWithNullRootNameLookup() {
 XmlSerializerProvider config = new XmlSerializerProvider(serializerProvider, config);
 assertThrows(NullPointerException.class, () -> XmlSerializerProvider.createInstance(config, null));
 }

 @Test
 public void testSerializationWithArrayConverter() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new XmlMapper();
 mapper.registerModule(new AbstractModule() {
 @Override
 protected void setupModule(SerializationConfig config) throws Exception {
 config.setSerializerProvider(new DefaultSerializerProvider());
 config.setSerializerProvider(new XmlSerializerProvider(serializerProvider, config, null));
 config.setRootValueSeparator("<", "/");
 config.setRootValueSeparatorSeparator('/');
 config.setRootName("array");
 config.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 config.setConverter(new Converter.Std(Collections.class) {
 @Override
 public Object convert(Object value, TypeSerializer ts) {
 return new Object[] { ((Object[]) value)[0], ((Object[]) value)[1] };
 }

 @Override
 public Object convertBack(Object value, TypeDescriptor descriptor) {
 return new Object[] { (String) value[0], (String) value[1] };
 }
 });
 }
 });
 String[] input = {"Hello", "World"};
 String expectedXml = "<array><item><value>Hello</value></item><item><value>World</value></item></array>";
 ObjectMapper xmlMapper = mapper;
 JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object[].class, null);
 ToXmlGenerator xgen = _createToXmlGenerator(new QName("array"));
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithArrayConverterSerializationInclusion() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new XmlMapper();
 mapper.registerModule(new AbstractModule() {
 @Override
 protected void setupModule(SerializationConfig config) throws Exception {
 config.setSerializerProvider(new DefaultSerializerProvider());
 config.setSerializerProvider(new XmlSerializerProvider(serializerProvider, config, null));
 config.setRootValueSeparator("<", "/");
 config.setRootValueSeparatorSeparator('/');
 config.setRootName("array");
 config.setSerializationInclusion(JsonInclude.Include.ALWAYS);
 config.setConverter(new Converter.Std(Collections.class) {
 @Override
 public Object convert(Object value, TypeSerializer ts) {
 return new Object[] { ((Object[]) value)[0], ((Object[]) value)[1] };
 }

 @Override
 public Object convertBack(Object value, TypeDescriptor descriptor) {
 return new Object[] { (String) value[0], (String) value[1] };
 }
 });
 }
 });
 String[] input = {null, "World"};
 String expectedXml = "<array><item><value></value></item><item><value>World</value></item></array>";
 ObjectMapper xmlMapper = mapper;
 JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object[].class, null);
 ToXmlGenerator xgen = _createToXmlGenerator(new QName("array"));
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializationWithArrayConverterNullInput() throws JsonProcessingException, IOException {
 ObjectMapper mapper = new XmlMapper();
 mapper.registerModule(new AbstractModule() {
 @Override
 protected void setupModule(SerializationConfig config) throws Exception {
 config.setSerializerProvider(new DefaultSerializerProvider());
 config.setSerializerProvider(new XmlSerializerProvider(serializerProvider, config, null));
 config.setRootValueSeparator("<", "/");
 config.setRootValueSeparatorSeparator('/');
 config.setRootName("array");
 config.setConverter(new Converter.Std(Collections.class) {
 @Override
 public Object convert(Object value, TypeSerializer ts) {
 return new Object[] { ((Object[]) value)[0], ((Object[]) value)[1] };
 }

 @Override
 public Object convertBack(Object value, TypeDescriptor descriptor) {
 return new Object[] { (String) value[0], (String) value[1] };
 }
 });
 }
 });
 String[] input = null;
 String expectedXml = "<array></array>";
 ObjectMapper xmlMapper = mapper;
 JsonSerializer<Object> jsonSerializer = xmlMapper.findValueSerializer(Object[].class, null);
 ToXmlGenerator xgen = _createToXmlGenerator(new QName("array"));
 jsonSerializer.serialize(input, xgen, this);
 String actualXml = xgen.getWriter().toString();
 assertEquals(expectedXml, actualXml);
 }

 private ToXmlGenerator _createToXmlGenerator(QName rootName) throws IOException {
 TokenBuffer tokenBuffer = new TokenBuffer(new ObjectOutputStream() {
 @Override
 public void write(int bytes, int off, int len) throws IOException {
 // Do nothing
 }

 @Override
 public void writeBytes(byte[] bytes, int off, int len) throws IOException {
 // Do nothing
 }

 @Override
 public void writeObject(Object obj) throws IOException {
 throw new IOException("Not used");
 }
 });
 ToXmlGenerator xgen = StaxUtil.createToXmlGenerator(tokenBuffer, null, null, null, null, false, false);
 if (rootName != null) {
 xgen.setRootName(rootName);
 }
 return xgen;
 }

}