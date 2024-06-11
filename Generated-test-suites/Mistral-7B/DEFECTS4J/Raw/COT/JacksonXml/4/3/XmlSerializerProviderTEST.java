package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderType;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderWithType;
import com.fasterxml.jackson.databind.SerializationConfig.Feature;
import com.fasterxml.jackson.databind.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testXmlSerializerProviderConstructor() {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 assertNotNull(xmlSerializerProvider);
 assertEquals(SerializerProviderType.BASE, xmlSerializerProvider.getProviderType());
 }

 @Test
 public void testXmlSerializerProviderWithConfiguration() throws JsonMappingException, IOException {
 XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup(new String[]{"root"});
 SerializationConfig config = new SerializationConfig();
 config.setRootValueSerializer(new XmlSerializerProvider(xmlRootNameLookup).getRootValueSerializer());
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(config, new DefaultSerializerProvider(), new SerializerFactory());
 assertNotNull(xmlSerializerProvider);
 assertEquals(SerializerProviderType.STANDARD, xmlSerializerProvider.getProviderType());
 }

 @Test
 public void testCreateInstanceMethod() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 SerializerProvider serializerProvider = xmlSerializerProvider.createInstance(objectMapper.getSerializationConfig(), new SerializerFactory());
 assertInstanceOf(XmlSerializerProvider.class, serializerProvider);
 }

 @Test
 public void testSerializeValueMethodWithNullValue() throws JsonMappingException, IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(gen, null);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<null/>", gen.getOutputText());
 }

 @Test
 public void testSerializeValueMethodWithObjectValue() throws JsonMappingException, IOException {
 Object value = new Person();
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(gen, value);
 assertNotNull(gen.getOutputText());
 }

 @Test
 public void testSerializeValueMethodWithJavaType() throws JsonMappingException, IOException {
 JavaType javaType = objectMapper.getTypeFactory().constructType(Person[].class);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(gen, Arrays.asList(new Person(), new Person()), javaType);
 assertNotNull(gen.getOutputText());
 }

 @Test
 public void testSerializeValueMethodWithJavaTypeAndJsonSerializer() throws JsonMappingException, IOException {
 Person person = new Person();
 JavaType javaType = objectMapper.getTypeFactory().constructType(Person.class);
 JsonSerializer<Person> jsonSerializer = objectMapper.findValueSerializer(Person.class);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}), new DefaultSerializerProvider(), new SerializerFactory());
 xmlSerializerProvider.setSerializerInstance(javaType, jsonSerializer);
 xmlSerializerProvider.serializeValue(gen, person, javaType, jsonSerializer);
 assertNotNull(gen.getOutputText());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueMethodWithNullJsonSerializer() throws JsonMappingException, IOException {
 Person person = new Person();
 JavaType javaType = objectMapper.getTypeFactory().constructType(Person.class);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(gen, person, javaType, null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueMethodWithNullJsonGenerator() throws JsonMappingException, IOException {
 Person person = new Person();
 JsonSerializer<Person> jsonSerializer = objectMapper.findValueSerializer(Person.class);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(null, person, null, jsonSerializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueMethodWithNullRootName() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[0]));
 xmlSerializerProvider.serializeValue(objectMapper.getFactory().createGenerator(null, null), null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueMethodWithNullRootNameAndValue() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[0]));
 xmlSerializerProvider.serializeValue(objectMapper.getFactory().createGenerator(null, null), null);
 }

 @Test
 public void testStartRootArrayMethod() throws JsonMappingException, IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.serializeValue(gen, new Person(), null);
 xmlSerializerProvider.startRootArray(gen, new QName("array"));
 xmlSerializerProvider.serializeValue(gen, new Person(), null);
 xmlSerializerProvider.serializeValue(gen, new Person(), null);
 xmlSerializerProvider.writeEndArray(gen);
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<array>\n<item>\n<name>John Doe</name>\n<age>25</age>\n</item>\n<item>\n<name>Jane Doe</name>\n<age>30</age>\n</item>\n</array>", gen.getOutputText());
 }

 @Test
 public void testInitWithRootNameMethod() throws IOException, JsonMappingException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.initWithRootName(toXmlGenerator, new QName("root"));
 assertEquals("root", toXmlGenerator.getCurrentName());
 }

 @Test
 public void testSetNextNameIfMissingMethod() throws IOException, JsonMappingException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.setNextNameIfMissing(toXmlGenerator, new QName("root"));
 assertFalse(xmlSerializerProvider.setNextNameIfMissing(toXmlGenerator, new QName("item")));
 }

 @Test
 public void testSetNextNameMethod() throws IOException, JsonMappingException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.setNextName(toXmlGenerator, new QName("item"));
 assertEquals("item", toXmlGenerator.getCurrentName());
 }

 @Test
 public void testInitGeneratorMethod() throws IOException, JsonMappingException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.initGenerator(toXmlGenerator);
 assertTrue(toXmlGenerator.isInitialized());
 }

 @Test
 public void testSetDefaultNamespaceMethod() throws IOException, JsonMappingException, XMLStreamException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 ToXmlGenerator toXmlGenerator = (ToXmlGenerator) gen;
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 xmlSerializerProvider.setDefaultNamespace(toXmlGenerator, "http://example.com");
 assertEquals("http://example.com", toXmlGenerator.getDefaultNamespace());
 }

 @Test
 public void testRootNameFromConfigMethod() throws JsonMappingException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new SerializationConfig());
 assertEquals(null, xmlSerializerProvider.rootNameFromConfig());
 }

 @Test
 public void testRootNameFromConfigWithRootValueSerializer() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new SerializationConfig().withRootValueSerializer(new XmlRootAnnotationSerializer()));
 assertEquals(new QName("root"), xmlSerializerProvider.rootNameFromConfig());
 }

 @Test
 public void testRootNameFromConfigWithRootNameProperty() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new SerializationConfig().withFeature(Feature.XML_ROOT_NAME, new JsonRootName("root")));
 assertEquals(new QName("root"), xmlSerializerProvider.rootNameFromConfig());
 }

 @Test
 public void testAsXmlGeneratorMethod() throws JsonMappingException, IOException {
 JsonGenerator gen = objectMapper.getFactory().createGenerator(null, null);
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 assertTrue(xmlSerializerProvider.asXmlGenerator(gen) instanceof ToXmlGenerator);
 }

 @Test
 public void testFindTypedValueSerializerMethod() throws JsonMappingException, IOException {
 XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new String[]{"root"}));
 JsonSerializer<Person> jsonSerializer = xmlSerializerProvider.findTypedValueSerializer(Person.class, true, null);
 assertInstanceOf(XmlSerializerProvider.PersonSerializer.class, jsonSerializer);
 }
}

 class XmlRootAnnotationSerializer extends StaxXmlSerializer {
 @JsonRootName(value = "root")
 static class Person {
 private String name;
 private int age;

 public Person() {
 }

 public Person(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }

 public int getAge() {
 return age;
 }

 public void setAge(int age) {
 this.age = age;
 }
 }

 public XmlRootAnnotationSerializer() {
 this(null);
 }

 public XmlRootAnnotationSerializer(Class<?> serializableType) {
 super(serializableType);
 }
 }