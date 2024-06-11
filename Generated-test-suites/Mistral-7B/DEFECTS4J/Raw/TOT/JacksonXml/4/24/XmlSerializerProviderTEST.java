package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class XmlSerializerProviderTest extends BaseMapTest {

 private XmlMapper mapper;
 private XmlSerializerProvider provider;

 @BeforeClass
 public void setUp() throws Exception {
 mapper = new XmlMapper(new JacksonXmlAnnotationIntrospector());
 provider = mapper.getSerializerProvider().findValueSerializerProviderInstance(XmlSerializerProvider.class);
 }

 @Test
 public void testSerializeNull() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, (Object) null);
 String xml = result.getValueAsString();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeEmptyString() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, "");
 String xml = result.getValueAsString();
 assertEquals("<></>", xml);
 }

 @Test
 public void testSerializeObject() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new TestObject());
 String xml = result.getValueAsString();
 assertEquals("<testObject><name>John Doe</name><age>30</age></testObject>", xml);
 }

 @Test
 public void testSerializeArray() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new int[] { 1, 2, 3 });
 String xml = result.getValueAsString();
 assertEquals("<intArray><int>1</int><int>2</int><int>3</int></intArray>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomRootName() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new TestObjectWithCustomRootName("MyRoot"));
 String xml = result.getValueAsString();
 assertEquals("<MyRoot><name>John Doe</name><age>30</age></MyRoot>", xml);
 }

 @Test
 public void testSerializeObjectWithRootNameFromConfig() throws Exception {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("rootName", "MyRoot");
 XmlMapper configMapper = new XmlMapper(new JacksonXmlAnnotationIntrospector());
 configMapper.setRootValueSerializer(new StdSerializer<ObjectNode>(ObjectNode.class) {
 @Override
 public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("rootName");
 gen.writeValue(value.get("rootName").asText());
 gen.writeObjectFieldStart("data");
 provider.defaultSerializeValue(value, gen, this);
 gen.writeEndObject();
 }
 });
 XmlSerializerProvider configProvider = configMapper.getSerializerProvider().findValueSerializerProviderInstance(XmlSerializerProvider.class);
 ObjectNode result = configMapper.writeValueAsTree(configProvider, new TestObject());
 String xml = result.getValueAsString();
 assertEquals("<MyRoot><data><name>John Doe</name><age>30</age></data></MyRoot>", xml);
 }

 @Test
 public void testSerializeArrayWithRootNameFromConfig() throws Exception {
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.put("rootName", "MyRoot");
 XmlMapper configMapper = new XmlMapper(new JacksonXmlAnnotationIntrospector());
 configMapper.setRootValueSerializer(new StdSerializer<ObjectNode>(ObjectNode.class) {
 @Override
 public void serialize(ObjectNode value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("rootName");
 gen.writeValue(value.get("rootName").asText());
 gen.writeFieldName("items");
 provider.defaultSerializeValue(value.get("items"), gen, this);
 gen.writeEndObject();
 }
 });
 XmlSerializerProvider configProvider = configMapper.getSerializerProvider().findValueSerializerProviderInstance(XmlSerializerProvider.class);
 ObjectNode result = configMapper.writeValueAsTree(configProvider, new int[] { 1, 2, 3 });
 String xml = result.getValueAsString();
 assertEquals("<MyRoot><items><int>1</int><int>2</int><int>3</int></items></MyRoot>", xml);
 }

 @Test
 public void testSerializeObjectWithCustomRootNameAndCustomSerialization() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new TestObjectWithCustomRootNameAndCustomSerialization("MyRoot", new TestObjectSerializer()));
 String xml = result.getValueAsString();
 assertEquals("<MyRoot><name>John Doe</name><age>30</age></MyRoot>", xml);
 }

 @Test
 public void testSerializeArrayWithCustomRootNameAndCustomSerialization() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new int[] { 1, 2, 3 }, new TestArraySerializer("MyRoot"));
 String xml = result.getValueAsString();
 assertEquals("<MyRoot><item><int>1</int></item><item><int>2</int></item><item><int>3</int></item></MyRoot>", xml);
 }

 @Test(expectedExceptions = JsonMappingException.class)
 public void testSerializeObjectWithInvalidRootName() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new TestObjectWithInvalidRootName());
 fail("Expected JsonMappingException but no exception was thrown");
 }

 @Test(expectedExceptions = JsonMappingException.class)
 public void testSerializeArrayWithInvalidRootName() throws Exception {
 ObjectNode result = mapper.writeValueAsTree(provider, new int[] { 1, 2, 3 }, new TestArraySerializerWithInvalidRootName());
 fail("Expected JsonMappingException but no exception was thrown");
 }
}

class TestObject {
 private String name;
 private int age;

 public TestObject() {
 }

 public TestObject(String name, int age) {
 this.name = name;
 this.age = age;
 }

 public String getName() {
 return name;
 }

 public int getAge() {
 return age;
 }
}

class TestObjectWithCustomRootName {
 private String rootName;
 private TestObject obj;

 public TestObjectWithCustomRootName() {
 this("MyRoot", new TestObject());
 }

 public TestObjectWithCustomRootName(String rootName, TestObject obj) {
 this.rootName = rootName;
 this.obj = obj;
 }

 public String getRootName() {
 return rootName;
 }

 public TestObject getObject() {
 return obj;
 }
}

class TestObjectSerializer extends StdSerializer<TestObject> {
 protected TestObjectSerializer() {
 this(null);
 }

 protected TestObjectSerializer(Class<TestObject> t) {
 super(t);
 }

 @Override
 public void serialize(TestObject value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("name");
 gen.writeValue(value.getName());
 gen.writeFieldName("age");
 gen.writeValue(value.getAge());
 gen.writeEndObject();
 }
}

class TestArraySerializer extends StdSerializer<int[]> {
 private String rootName;

 protected TestArraySerializer() {
 this(null);
 }

 protected TestArraySerializer(Class<int[]> t, String rootName) {
 super(t);
 this.rootName = rootName;
 }

 @Override
 public void serialize(int[] values, JsonGenerator gen, SerializerProvider provider) throws IOException {
 gen.writeStartObject();
 gen.writeFieldName("rootName");
 gen.writeValue(rootName);
 gen.writeFieldName("items");
 provider.defaultSerializeValue(values, gen, this);
 gen.writeEndObject();
 }
}

class TestArraySerializerWithInvalidRootName extends TestArraySerializer {
 public TestArraySerializerWithInvalidRootName() {
 super(int[].class, "InvalidRoot");
 }
}

class TestObjectWithInvalidRootName extends TestObjectWithCustomRootName {
 public TestObjectWithInvalidRootName() {
 super("InvalidRoot", new TestObject());
 }
}