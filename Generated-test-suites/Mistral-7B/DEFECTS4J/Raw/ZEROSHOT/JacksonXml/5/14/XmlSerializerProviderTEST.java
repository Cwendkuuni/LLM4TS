package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

 private ObjectMapper mapper;
 private XmlSerializerProvider xmlSerializerProvider;
 private Object testObject;
 private JavaType javaType;

 @BeforeClass
 public void setup() throws Exception {
 mapper = new XmlMapper();
 testObject = new TestClass();
 javaType = mapper.getTypeFactory().constructType(TestClass[].class);
 xmlSerializerProvider = mapper.getXmlSerializerProvider();
 }

 @Test
 public void testCopy() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializerProvider copy = original.copy();
 assertNotSame(original, copy);
 }

 @Test
 public void testCreateInstance() throws Exception {
 SerializationConfig config = mapper.getSerializationConfig();
 SerializerFactory factory = mapper.getSerializerFactory();
 SerializerProvider instance = xmlSerializerProvider.createInstance(config, factory);
 assertSame(xmlSerializerProvider.getClass(), instance.getClass());
 }

 @Test
 public void testCreateInstanceWithSameArgs() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializationConfig config = mapper.getSerializationConfig();
 SerializerFactory factory = mapper.getSerializerFactory();
 SerializerProvider instance = xmlSerializerProvider.createInstance(config, factory);
 assertSame(original, instance);
 }

 @Test
 public void testCreateInstanceWithDifferentArgs() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializationConfig config = mapper.getSerializationConfig();
 SerializerFactory factory = new SerializerFactory(); // Different factory
 SerializerProvider instance = xmlSerializerProvider.createInstance(config, factory);
 assertNotSame(original, instance);
 }

 @Test
 public void testCreateInstanceWithSameConfigAndFactory() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializationConfig config = mapper.getSerializationConfig();
 SerializerFactory factory = mapper.getSerializerFactory();
 SerializerProvider instance = new XmlSerializerProvider(original, config, factory);
 assertSame(original, instance);
 }

 @Test
 public void testCreateInstanceWithDifferentConfigAndFactory() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializationConfig config1 = mapper.getSerializationConfig();
 SerializationConfig config2 = new SerializationConfig(config1); // Different config
 SerializerFactory factory = mapper.getSerializerFactory();
 SerializerProvider instance = new XmlSerializerProvider(original, config2, factory);
 assertNotSame(original, instance);
 }

 @Test
 public void testCreateInstanceWithSameConfigAndDifferentFactory() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializationConfig config = mapper.getSerializationConfig();
 SerializerFactory factory1 = mapper.getSerializerFactory();
 SerializerFactory factory2 = new SerializerFactory(); // Different factory
 SerializerProvider instance = new XmlSerializerProvider(original, config, factory2);
 assertNotSame(original, instance);
 }

 @Test
 public void testOverriddenCopy() throws Exception {
 SerializerProvider original = xmlSerializerProvider;
 SerializerProvider copy = original.copy();
 XmlSerializerProvider overriddenCopy = new XmlSerializerProvider(copy);
 assertNotSame(original, overriddenCopy);
 }

 @Test
 public void testSerializeValueWithNull() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<null/>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithObject() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, testObject);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<testClass><string>value1</string><integer>1</integer></testClass>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithObjectAndRootName() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator("rootElement");
 xmlSerializerProvider.serializeValue(gen, testObject, null, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><testClass><string>value1</string><integer>1</integer></testClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithObjectAndRootNameSpecifiedInConfig() throws IOException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparators(new PropertyName("rootElement"));
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, testObject, null, null, config);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><testClass><string>value1</string><integer>1</integer></testClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithObjectAndRootNameLookup() throws IOException {
 XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
 rootNameLookup.setRootName("rootElement");
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, testObject, null, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><testClass><string>value1</string><integer>1</integer></testClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithArray() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, Arrays.asList(testObject, testObject).toArray(), javaType, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<arrayOfTestClass><testClass><string>value1</string><integer>1</integer></testClass><testClass><string>value1</string><integer>1</integer></testClass></arrayOfTestClass>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithArrayAndRootName() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator("rootElement");
 xmlSerializerProvider.serializeValue(gen, Arrays.asList(testObject, testObject).toArray(), javaType, null, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><arrayOfTestClass><testClass><string>value1</string><integer>1</integer></testClass><testClass><string>value1</string><integer>1</integer></testClass></arrayOfTestClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithArrayAndRootNameSpecifiedInConfig() throws IOException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparators(new PropertyName("rootElement"));
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, Arrays.asList(testObject, testObject).toArray(), javaType, null, config);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><arrayOfTestClass><testClass><string>value1</string><integer>1</integer></testClass><testClass><string>value1</string><integer>1</integer></testClass></arrayOfTestClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testSerializeValueWithArrayAndRootNameLookup() throws IOException {
 XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
 rootNameLookup.setRootName("rootElement");
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 xmlSerializerProvider.serializeValue(gen, Arrays.asList(testObject, testObject).toArray(), javaType, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<rootElement><arrayOfTestClass><testClass><string>value1</string><integer>1</integer></testClass><testClass><string>value1</string><integer>1</integer></testClass></arrayOfTestClass></rootElement>";
 assertEquals(expected, actual);
 }

 @Test
 public void testRootNameFromConfig() throws IOException {
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 Object testObjectWithRootName = new TestClassWithRootName();
 xmlSerializerProvider.serializeValue(gen, testObjectWithRootName, null, null);
 String actual = StaxUtil.convertToText(gen);
 String expected = "<TestClassWithRootName><testClass><string>value1</string><integer>1</integer></testClass></TestClassWithRootName>";
 assertEquals(expected, actual);
 }

 @Test
 public void testAsXmlGenerator() throws IOException {
 // Should return null when called with JsonGenerator
 JsonGenerator gen = StaxUtil.createRootXmlGenerator();
 assertNull(xmlSerializerProvider._asXmlGenerator(gen));
 // Should return ToXmlGenerator when called with ToXmlGenerator
 ToXmlGenerator toXmlGenerator = StaxUtil.createRootXmlGenerator().asXMLGenerator();
 assertSame(toXmlGenerator, xmlSerializerProvider._asXmlGenerator(toXmlGenerator));
 }
}

 class TestClass {
 private String string;
 private int integer;

 public String getString() {
 return string;
 }

 public void setString(String string) {
 this.string = string;
 }

 public int getInteger() {
 return integer;
 }

 public void setInteger(int integer) {
 this.integer = integer;
 }
 }

 class TestClassWithRootName {
 // Root element name is set explicitly
 @JsonRootName(value = "TestClassWithRootName")
 private TestClass testClass;

 public TestClass getTestClass() {
 return testClass;
 }

 public void setTestClass(TestClass testClass) {
 this.testClass = testClass;
 }
 }