package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup.RootName;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlRootNameLookup rootNameLookup;
 private XmlSerializerProvider xmlSerializerProvider;
 private SerializerProvider defaultSerializerProvider;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 rootNameLookup = new XmlRootNameLookup();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 defaultSerializerProvider = new DefaultSerializerProvider();
 }

 @Test
 public void testCreateInstance() throws JsonProcessingException {
 XmlSerializerProvider newInstance = xmlSerializerProvider.createInstance(objectMapper.getConfiguration(), objectMapper.getSerializerFactory());
 assertEquals(xmlSerializerProvider, newInstance);
 }

 @Test
 public void testSerializeValueNull() throws IOException {
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, null);
 String expectedXml = "<null/>";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueObject() throws IOException {
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, new Object());
 fail("Expected a specific exception");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueObjectWithException() throws IOException {
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, new Exception("Test Exception"));
 }

 @Test
 public void testSerializeValueJavaTypeNull() throws IOException {
 JavaType javaType = null;
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType);
 String expectedXml = "<null/>";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueJavaTypeEmptyArray() throws IOException {
 JavaType javaType = new JavaType(Collections.class);
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType, defaultSerializerProvider, null);
 String expectedXml = "<item xsi:type=\"array\">\n</item>\n";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueJavaTypeArray() throws IOException {
 JavaType javaType = new JavaType(new int[3].getClass());
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType, defaultSerializerProvider, null);
 String expectedXml = "<item xsi:type=\"array\">\n<item></item>\n<item></item>\n<item></item>\n</item>\n";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueJavaTypeObject() throws IOException {
 JavaType javaType = new JavaType(String.class);
 String value = "Test Value";
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType, defaultSerializerProvider, value);
 String expectedXml = "<TestValue><![CDATA[Test Value]]></TestValue>";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testSerializeValueJavaTypeObjectWithRootName() throws IOException {
 JavaType javaType = new JavaType(String.class, RootName.TEST);
 String value = "Test Value";
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType, defaultSerializerProvider, value);
 String expectedXml = "<test:TestValue><![CDATA[Test Value]]></test:TestValue>";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testFindTypedValueSerializerNull() throws IOException {
 Class<?> cls = null;
 JsonSerializer<Object> serializer = xmlSerializerProvider.findTypedValueSerializer(cls, true, null);
 assertNull(serializer);
 }

 @Test
 public void testFindTypedValueSerializerPrimitiveTypes() throws IOException {
 Class<Integer> intType = Integer.TYPE;
 JsonSerializer<Object> serializer = xmlSerializerProvider.findTypedValueSerializer(intType, true, null);
 assertNotNull(serializer);
 }

 @Test
 public void testFindTypedValueSerializerCustomSerializer() throws IOException {
 String rootName = "TestRoot";
 Class<String> stringClass = String.class;
 JsonSerializer<String> customSerializer = new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeString(value.toUpperCase());
 }
 };
 JavaType javaType = new JavaType(stringClass, RootName.TEST);
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, customSerializer);
 String value = "Test Value";
 JsonGenerator gen = StaxUtil.createRootGenerator(true);
 xmlSerializerProvider.serializeValue(gen, javaType, defaultSerializerProvider, value);
 String expectedXml = "<TestRoot><TESTVALUE>TEST VALUE</TESTVALUE></TestRoot>";
 String actualXml = StaxUtil.toString(gen);
 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindTypedValueSerializerNullSerializer() throws IOException {
 Class<String> stringClass = String.class;
 JavaType javaType = new JavaType(stringClass, RootName.TEST);
 xmlSerializerProvider.findTypedValueSerializer(javaType, true, null);
 fail("Expected a specific exception");
 }
}