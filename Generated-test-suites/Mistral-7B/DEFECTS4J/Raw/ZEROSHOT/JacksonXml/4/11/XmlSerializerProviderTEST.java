package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;

public class XmlSerializerProviderTest {

 private XmlMapper mapper;
 private XmlSerializerProvider xmlProvider;

 @BeforeClass
 public void setup() {
 mapper = new XmlMapper();
 xmlProvider = mapper.getXMLSerializerProvider();
 }

 @Test
 public void testNullSerialization() throws IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, null);
 gen.close();
 }

 @Test
 public void testEmptyClassSerialization() throws IOException {
 Object value = new Object();
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, value);
 gen.close();
 }

 @Test
 public void testStringSerialization() throws IOException {
 String testString = "Test String";
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, testString);
 gen.close();
 }

 @Test
 public void testIntegerSerialization() throws IOException {
 int testInt = 1;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, testInt);
 gen.close();
 }

 @Test
 public void testBooleanSerialization() throws IOException {
 boolean testBoolean = true;
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, testBoolean);
 gen.close();
 }

 @Test
 public void testCustomRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonSerializer<int[]> serializer = mapper.findValueSerializer(int[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, javaType.getValue(), javaType, serializer);
 gen.close();
 }

 @Test
 public void testCustomRootNameWithConfiguredRootName() throws IOException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparator("_");
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonSerializer<int[]> serializer = mapper.findValueSerializer(int[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider = new XmlSerializerProvider(xmlProvider, config, null);
 xmlProvider.serializeValue(gen, javaType.getValue(), javaType, serializer);
 gen.close();
 }

 @Test
 public void testArraySerialization() throws IOException {
 int[] testArray = {1, 2, 3};
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, testArray);
 gen.close();
 }

 @Test
 public void testArraySerializationWithCustomRootName() throws IOException {
 int[] testArray = {1, 2, 3};
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonSerializer<int[]> serializer = mapper.findValueSerializer(int[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider = new XmlSerializerProvider(xmlProvider, null, null);
 xmlProvider.serializeValue(gen, testArray, javaType);
 gen.close();
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonSerializer<int[]> serializer = mapper.findValueSerializer(int[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, javaType.getValue(), javaType, serializer, new QName("Test", "InvalidRootName"));
 gen.close();
 }

 @Test
 public void testNullRootName() throws IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, null, null);
 gen.close();
 }

 @Test
 public void testNullRootNameFromConfig() throws IOException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparator(null);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider = new XmlSerializerProvider(xmlProvider, config, null);
 xmlProvider.serializeValue(gen, null, null);
 gen.close();
 }

 @Test
 public void testRootNameFromConfig() throws IOException {
 SerializationConfig config = mapper.getSerializationConfig();
 config.setRootValueSeparator("_");
 config.setRootValueSeparatorSeparator('.');
 config.setRootValueSeparatorElements(Arrays.asList("root", "element"));
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider = new XmlSerializerProvider(xmlProvider, config, null);
 xmlProvider.serializeValue(gen, null, null);
 gen.close();
 }

 @Test
 public void testNullAsArray() throws IOException {
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider.serializeValue(gen, null, JavaType.constructType(int[].class), null);
 gen.close();
 }

 @Test
 public void testNullAsArrayWithCustomRootName() throws IOException {
 JavaType javaType = mapper.getTypeFactory().constructType(int[].class);
 JsonSerializer<int[]> serializer = mapper.findValueSerializer(int[].class);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 xmlProvider = new XmlSerializerProvider(xmlProvider, null, null);
 xmlProvider.serializeValue(gen, null, javaType);
 gen.close();
 }
}