package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private static final ObjectNode EMPTY_OBJECT = JsonCreationUtil.createObjectMapper().createObjectNode();

 @Test
 public void testDefaultSerialization() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{ new XmlRootName("root") }));
 ObjectWriter objectWriter = Json.std(serializerProvider).writerForTree(EMPTY_OBJECT);
 obj.put("name", "value");
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<root>"));
 }

 @Test
 public void testSerializationWithRootName() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"root:myRootName"}));
 ObjectWriter objectWriter = Json.std(serializerProvider).writerForTree(EMPTY_OBJECT);
 obj.put("name", "value");
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<myRootName>"));
 }

 @Test
 public void testSerializationNullValue() throws Exception {
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"myRoot"})).writerForTree(EMPTY_OBJECT);
 objectWriter.writeNull();
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<myRoot name=\"null\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"));
 }

 @Test
 public void testSerializationArray() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 JsonSerializer<Object> serializer = new StdSerializer<Object>(Object.class) {
 @Override
 public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
 jgen.writeStartArray();
 jgen.writeStartObject();
 jgen.writeFieldName("name");
 jgen.writeValue("value");
 jgen.writeEndObject();
 jgen.writeEndArray();
 }
 };
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"root"})).writerForTree(obj);
 objectWriter.setSerializerProvider(new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"root:items"}), serializerProvider, null) {
 @Override
 public void serializeValue(JsonGenerator gen, Object value, JavaType rootType) throws IOException {
 if (value == null) {
 _serializeXmlNull(gen);
 return;
 }
 super.serializeValue(gen, value, rootType);
 }
 });
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<root>"));
 assertTrue(xml.contains("<items>"));
 assertTrue(xml.contains("<items>"));
 assertTrue(xml.contains("<items name=\"name\">value</items>"));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializationWithInvalidRootName() throws Exception {
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"root:root"})).writerForTree(EMPTY_OBJECT);
 objectWriter.writeValueAsString(EMPTY_OBJECT);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testSerializationWithEmptyRootName() throws Exception {
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{})).writerForTree(EMPTY_OBJECT);
 objectWriter.writeValueAsString(EMPTY_OBJECT);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<root>"));
 }

 @Test
 public void testSerializationWithRootNameFromConfig() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator(":");
 XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"myRoot"}), config);
 ObjectWriter objectWriter = Json.std(serializerProvider).writerForTree(obj);
 obj.put("name", "value");
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<myRoot>"));
 }

 @Test
 public void testSerializationWithRootNameFromConfigWithNull() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 SerializationConfig config = new SerializationConfig();
 config.setRootValueSeparator(":");
 XmlSerializerProvider serializerProvider = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"myRoot"}), config);
 ObjectWriter objectWriter = Json.std(serializerProvider).writerForTree(obj);
 obj.put("name", null);
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<myRoot name=\"null\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"));
 }

 @Test
 public void testSerializationWithCustomSerializer() throws Exception {
 ObjectNode obj = JsonCreationUtil.createObjectMapper().createObjectNode();
 SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name");
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"root"}), filter).writerForTree(obj);
 obj.put("name", "value");
 obj.put("age", 30);
 objectWriter.writeValueAsString(obj);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.startsWith("<root>"));
 assertFalse(xml.contains("<age>"));
 }

 @Test
 public void testSerializationWithEmptyObject() throws Exception {
 ObjectWriter objectWriter = new XmlSerializerProvider(new XmlRootNameLookup(new XmlRootName[]{"myRoot"})).writerForTree(EMPTY_OBJECT);
 objectWriter.writeValueAsString(EMPTY_OBJECT);
 String xml = objectWriter.getValueAsString();
 assertTrue(xml.equals(""));
 }
}