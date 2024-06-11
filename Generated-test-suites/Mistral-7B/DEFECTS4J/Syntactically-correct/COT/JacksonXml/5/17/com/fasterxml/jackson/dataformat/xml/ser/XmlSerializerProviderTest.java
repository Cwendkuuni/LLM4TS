package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
 }

 // Basic test cases for the constructor
 @Test
 public void testXmlSerializerProviderConstructorNull() {
 assertNull(new XmlSerializerProvider(null));
 }

 @Test
 public void testXmlSerializerProviderConstructorWithXmlRootNameLookup() {
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 SerializerFactory serFact = objectMapper.getSerializerProvider().getFactory();
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(), config, serFact);
 assertNotNull(xmlProvider);
 }

 @Test
 public void testXmlSerializerProviderConstructorWithXmlSerializerProvider() {
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 SerializerFactory serFact = objectMapper.getSerializerProvider().getFactory();
 XmlSerializerProvider xmlProviderSrc = new XmlSerializerProvider(new XmlRootNameLookup());
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(xmlProviderSrc, config, serFact);
 assertNotNull(xmlProvider);
 assertNotSame(xmlProvider, xmlProviderSrc);
 }

 @Test
 public void testXmlSerializerProviderConstructorWithXmlSerializerProviderCopy() {
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 SerializerFactory serFact = objectMapper.getSerializerProvider().getFactory();
 XmlSerializerProvider xmlProviderSrc = new XmlSerializerProvider(new XmlRootNameLookup());
 XmlSerializerProvider xmlProvider = new XmlSerializerProvider(xmlProviderSrc);
 assertNotNull(xmlProvider);
 assertNotSame(xmlProvider, xmlProviderSrc);
 }

 // Basic test cases for the copy method
 @Test
 public void testXmlSerializerProviderCopyNull() {
 assertNull(xmlSerializerProvider.copy());
 }

 @Test
 public void testXmlSerializerProviderCopy() {
 XmlSerializerProvider xmlProviderCopy = xmlSerializerProvider.copy();
 assertNotNull(xmlProviderCopy);
 assertSame(xmlSerializerProvider._rootNameLookup, xmlProviderCopy._rootNameLookup);
 }

 // Basic test cases for the createInstance method
 @Test
 public void testXmlSerializerProviderCreateInstanceNull() {
 assertNull(xmlSerializerProvider.createInstance(null, null));
 }

 @Test
 public void testXmlSerializerProviderCreateInstance() {
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 SerializerFactory serFact = objectMapper.getSerializerProvider().getFactory();
 XmlSerializerProvider xmlProvider = xmlSerializerProvider.createInstance(config, serFact);
 assertNotNull(xmlProvider);
 assertSame(xmlSerializerProvider._rootNameLookup, xmlProvider._rootNameLookup);
 }

 // Basic test cases for the createInstance method with copy constructor
 @Test
 public void testXmlSerializerProviderCreateInstanceCopy() {
 SerializationConfig config = new SerializationConfig(objectMapper.getConfig());
 SerializerFactory serFact = objectMapper.getSerializerProvider().getFactory();
 XmlSerializerProvider xmlProviderSrc = new XmlSerializerProvider(new XmlRootNameLookup());
 XmlSerializerProvider xmlProvider = xmlSerializerProvider.createInstance(config, serFact);
 assertNotNull(xmlProvider);
 assertNotSame(xmlProvider, xmlProviderSrc);
 }

 // Test cases for the serializeValue method with null value
 @Test
 public void testSerializeValueNullRootName() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider.serializeValue(gen, null);
 assertEquals("<null/>", gen.getOutputText().toString());
 }

 @Test
 public void testSerializeValueNullRootNameWithRootType() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 JavaType rootType = new JavaType(String.class);
 xmlSerializerProvider.serializeValue(gen, null, rootType, null);
 assertEquals("<null/>", gen.getOutputText().toString());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeValueNullRootNameWithJsonSerializer() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 JsonSerializer<Object> ser = new JsonSerializer<Object>() {
 @Override public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not be called");
 }
 };
 xmlSerializerProvider.serializeValue(gen, null, null, ser);
 }

 @Test
 public void testSerializeValueNullRootNameWithRootNameFromConfig() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider.serializeValue(gen, null, new QName("rootName"));
 assertEquals("<rootName/>", gen.getOutputText().toString());
 }

 // Test cases for the serializeValue method with non-null value
 @Test
 public void testSerializeValueString() throws Exception {
 String value = "test";
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider.serializeValue(gen, value);
 assertEquals("<test/>", gen.getOutputText().toString());
 }

 @Test
 public void testSerializeValueObject() throws Exception {
 Object value = new Object();
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider.serializeValue(gen, value);
 fail("Expected JsonGenerationException but got: "+gen.getOutputText().toString());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeValueObjectWithJsonSerializer() throws Exception {
 Object value = new Object();
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 JsonSerializer<Object> ser = new JsonSerializer<Object>() {
 @Override public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not be called");
 }
 };
 xmlSerializerProvider.serializeValue(gen, value, null, ser);
 }

 // Test cases for the serializeValue method with rootType and JsonSerializer
 @Test
 public void testSerializeValueObjectWithJsonSerializerAndRootType() throws Exception {
 Object value = new Object();
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 JavaType rootType = new JavaType(String.class);
 JsonSerializer<Object> ser = new JsonSerializer<Object>() {
 @Override public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString("test");
 }
 };
 xmlSerializerProvider.serializeValue(gen, value, rootType, ser);
 assertEquals("<test/>", gen.getOutputText().toString());
 }

 // Test cases for the _serializeXmlNull method
 @Test
 public void testSerializeXmlNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._serializeXmlNull(gen);
 assertEquals("<null/>", gen.getOutputText().toString());
 }

 @Test
 public void testSerializeXmlNullWithRootNameFromConfigNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._serializeXmlNull(gen);
 assertEquals("<null/>", gen.getOutputText().toString());
 }

 @Test
 public void testSerializeXmlNullWithRootNameFromConfig() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._serializeXmlNull(gen, new QName("rootName"));
 assertEquals("<rootName/>", gen.getOutputText().toString());
 }

 // Test cases for the _startRootArray method
 @Test(expected = JsonMappingException.class)
 public void testStartRootArrayNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._startRootArray(gen, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testStartRootArrayNullRootName() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._startRootArray(gen, new QName(null, "rootName"));
 }

 @Test
 public void testStartRootArrayEmptyString() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._startRootArray(gen, new QName(""));
 assertEquals("<root/>", gen.getOutputText().toString());
 }

 @Test
 public void testStartRootArrayWithRootName() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._startRootArray(gen, new QName("rootName"));
 assertEquals("<rootName/>", gen.getOutputText().toString());
 }

 // Test cases for the _initWithRootName method
 @Test
 public void testInitWithRootNameNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._initWithRootName(gen, null);
 fail("Expected IOException but got: "+gen.getOutputText().toString());
 }

 @Test
 public void testInitWithRootNameEmptyString() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._initWithRootName(gen, "");
 fail("Expected IOException but got: "+gen.getOutputText().toString());
 }

 @Test
 public void testInitWithRootName() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._initWithRootName(gen, "rootName");
 assertEquals("<rootName>", gen.getOutputText().toString());
 }

 @Test
 public void testInitWithRootNameWithNamespace() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 xmlSerializerProvider._initWithRootName(gen, new QName("namespace", "rootName"));
 assertEquals("<rootName xmlns:ns=\"namespace\">", gen.getOutputText().toString());
 }

 // Test cases for the _rootNameFromConfig method
 @Test
 public void testRootNameFromConfigNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 QName rootName = xmlSerializerProvider._rootNameFromConfig();
 assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, rootName);
 }

 @Test
 public void testRootNameFromConfig() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 PropertyName name = new PropertyName("rootName");
 xmlSerializerProvider._config.setRootValue(name);
 QName rootName = xmlSerializerProvider._rootNameFromConfig();
 assertEquals(new QName("rootName"), rootName);
 }

 // Test cases for the _asXmlGenerator method
 @Test
 public void testAsXmlGeneratorNull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(gen);
 assertNull(toXmlGenerator);
 }

 @Test(expected = JsonMappingException.class)
 public void testAsXmlGeneratorNotInstanceOfToXmlGenerator() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(gen);
 fail("Expected JsonMappingException but got: "+toXmlGenerator);
 }

 @Test
 public void testAsXmlGenerator() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(gen);
 assertNotNull(toXmlGenerator);
 }

 // Test cases for the _wrapAsIOE method
 @Test
 public void testWrapAsIOENull() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 IOException ioException = xmlSerializerProvider._wrapAsIOE(gen, null);
 assertNull(ioException);
 }

 @Test(expected = IOException.class)
 public void testWrapAsIOE() throws Exception {
 JsonGenerator gen = new StaxUtil.StaxGenerator();
 Exception e = new Exception("test");
 IOException ioException = xmlSerializerProvider._wrapAsIOE(gen, e);
 assertNotNull(ioException);
 assertEquals(e.getMessage(), ioException.getMessage());
 }

 // Test cases for the getRootNameLookup method
 @Test
 public void testGetRootNameLookup() {
 XmlRootNameLookup xmlRootNameLookup = xmlSerializerProvider.getRootNameLookup();
 assertNotNull(xmlRootNameLookup);
 }

}