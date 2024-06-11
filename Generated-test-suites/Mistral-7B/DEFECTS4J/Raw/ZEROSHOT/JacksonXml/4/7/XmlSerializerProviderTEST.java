package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

 private XmlSerializerProvider serializerProvider;
 private XmlRootNameLookup rootNameLookup;

 @Before
 public void setup() {
 SerializationConfig config = new SerializationConfig();
 rootNameLookup = new XmlRootNameLookup();
 serializerProvider = new XmlSerializerProvider(rootNameLookup, config, new DefaultSerializerFactory());
 }

 @Test
 public void testDefaultSerializationBehavior() throws JsonMappingException, IOException {
 // Test serializing various types
 ObjectMapper mapper = new ObjectMapper(serializerProvider);
 String xml = mapper.writeValueAsString(new Integer(1));
 assertEquals("<integer>1</integer>", xml);

 xml = mapper.writeValueAsString(new Long(2));
 assertEquals("<long>2</long>", xml);

 xml = mapper.writeValueAsString(new Double(3.0));
 assertEquals("<double>3.0</double>", xml);

 xml = mapper.writeValueAsString("Hello");
 assertEquals("<string>Hello</string>", xml);

 xml = mapper.writeValueAsString(new Boolean(true));
 assertEquals("<boolean>true</boolean>", xml);

 xml = mapper.writeValueAsString(null);
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializationWithCustomRootName() throws JsonMappingException, IOException {
 // Create ObjectMapper with custom root name
 SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator("__");
 rootNameLookup = new XmlRootNameLookup();
 rootNameLookup.setRootNameForClass("MyClass", "myRoot");
 serializerProvider = new XmlSerializerProvider(rootNameLookup, config, new DefaultSerializerFactory());
 ObjectMapper mapper = new ObjectMapper(serializerProvider);

 // Test serializing an instance of MyClass
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setValue(123);
 String xml = mapper.writeValueAsString(obj);
 assertEquals("__myRoot><name>Test</name><value>123</value>", xml);
 }

 @Test
 public void testSerializationWithCustomRootSerializer() throws JsonMappingException, IOException {
 // Test serializing an instance of MyClass with a custom root serializer
 MyRootSerializer rootSerializer = new MyRootSerializer();
 SerializationConfig config = new SerializationConfig();
 config.setRootValueSerializer(rootSerializer);
 rootNameLookup = new XmlRootNameLookup();
 rootNameLookup.setRootNameForClass("MyClass", "myRoot");
 serializerProvider = new XmlSerializerProvider(rootNameLookup, config, new DefaultSerializerFactory());
 ObjectMapper mapper = new ObjectMapper(serializerProvider);

 // Test serializing an instance of MyClass
 JavaType javaType = mapper.getTypeFactory().constructType(MyClass.class);
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setValue(123);
 String xml = mapper.writeValueAsString(obj);
 assertEquals("<root><myRoot><name>Test</name><value>123</value></myRoot></root>", xml);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testFindTypedValueSerializer() throws JsonMappingException, IOException {
 // Test finding typed value serializer for String type
 JsonSerializer<String> stringSerializer = serializerProvider.findTypedValueSerializer(String.class, true, null);
 assertNotNull(stringSerializer);
 assertTrue(stringSerializer instanceof StandardSerializer);

 // Test finding typed value serializer for Integer type
 JsonSerializer<Integer> integerSerializer = (JsonSerializer<Integer>) serializerProvider.findTypedValueSerializer(Integer.class, true, null);
 assertNotNull(integerSerializer);
 assertTrue(integerSerializer instanceof StandardSerializer);
 }

 @Test
 public void testSerializationWithCustomSerializer() throws JsonMappingException, IOException {
 // Create a custom serializer for MyClass
 MyClassSerializer myClassSerializer = new MyClassSerializer();
 serializerProvider.setSerializerInstance(MyClass.class, myClassSerializer);

 // Test serializing an instance of MyClass
 MyClass obj = new MyClass();
 obj.setName("Test");
 obj.setValue(123);
 String xml = serializerProvider.getSerializerProvider().serializeValueAsString(obj);
 assertEquals("<MyClass><name>Test</name><value>123</value></MyClass>", xml);
 }

 class MyClass {
 private String name;
 private int value;

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getValue() {
 return value;
 }

 public void setValue(int value) {
 this.value = value;
 }
 }

 class MyRootSerializer extends JsonSerializer<Object> {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("root");
 gen.writeObject(value);
 gen.writeEndObject();
 }
}

 class MyClassSerializer extends JsonSerializer<MyClass> {
 @Override
 public void serialize(MyClass value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("MyClass");
 gen.writeFieldName("name");
 gen.writeString(value.getName());
 gen.writeFieldName("value");
 gen.writeNumber(value.getValue());
 gen.writeEndObject();
 }
}
}