package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;

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
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;

public class XmlSerializerProviderTest {

 private static final ObjectMapper mapper = new ObjectMapper(new XMLFactory());

 @Test
 public void testSerializeNullValue() throws Exception {
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return null;
 }
 });

 JavaType nullType = mapper.getTypeFactory().constructValueType(null, String.class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 provider.serializeValue(tokenBuffer, null, nullType, null);
 String xml = sw.toString();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeSimpleBean() throws Exception {
 Person person = new Person("John Doe", 30);
 JavaType personType = mapper.getTypeFactory().constructType(Person.class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return new QName("root");
 }
 });
 provider.getConfig().setRootValueSeparator(":");
 provider.serializeValue(tokenBuffer, person, personType, null);
 String xml = sw.toString();
 assertEquals("<root><person><name>John Doe</name><age>30</age></person></root>", xml);
 }

 @Test
 public void testSerializeArray() throws Exception {
 int[] ints = new int[] { 1, 2, 3 };
 JavaType intsType = mapper.getTypeFactory().constructType(int[].class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return new QName("root");
 }
 });
 provider.getConfig().setRootValueSeparator(":");
 provider.serializeValue(tokenBuffer, ints, intsType, null);
 String xml = sw.toString();
 assertEquals("<root><item type=\"java.lang.int[]\">" +
 "<int>1</int><int>2</int><int>3</int></item></root>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomRootName() throws Exception {
 Person person = new Person("John Doe", 30);
 JavaType personType = mapper.getTypeFactory().constructType(Person.class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return new QName("customRoot");
 }
 }, mapper.getSerializationConfig(), new DefaultSerializerFactory());
 provider.serializeValue(tokenBuffer, person, personType, null);
 String xml = sw.toString();
 assertEquals("<customRoot><person><name>John Doe</name><age>30</age></person></customRoot>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomSerializer() throws Exception {
 Person person = new Person("John Doe", 30);
 JavaType personType = mapper.getTypeFactory().constructType(Person.class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
 JsonSerializer<Person> personSerializer = new StdSerializer<Person>(Person.class) {
 @Override
 public void serialize(Person person, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("id");
 jsonGenerator.writeNumber(person.getId());
 jsonGenerator.writeFieldName("name");
 jsonGenerator.writeString(person.getName());
 jsonGenerator.writeEndObject();
 }
 };
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 return null;
 }
 }, mapper.getSerializationConfig(), new DefaultSerializerFactory(), filter, personSerializer);
 provider.serializeValue(tokenBuffer, person, personType, null);
 String xml = sw.toString();
 assertEquals("<person><id>0</id><name>John Doe</name></person>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeException() throws Exception {
 JavaType intsType = mapper.getTypeFactory().constructType(int[].class);
 StringWriter sw = new StringWriter();
 TokenBuffer tokenBuffer = new TokenBuffer(sw);
 SerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup() {
 @Override
 public QName findRootName(Class<?> clazz, SerializationConfig config) {
 throw new RuntimeException("Test failure");
 }
 });
 try {
 provider.serializeValue(tokenBuffer, null, intsType, null);
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Failed to serialize value of type: [B to XML Stream, problem: com.fasterxml.jackson.core.JsonProcessingException: No root name found for type [B", e.getMessage());
 }
 }

}

class Person {
 private int id;
 private String name;
 private int age;

 public Person() {}

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 this.id = 0;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }

 public int getId() {
 return id;
 }
}