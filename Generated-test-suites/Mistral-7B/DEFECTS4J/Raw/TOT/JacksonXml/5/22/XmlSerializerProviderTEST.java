package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private XmlMapper mapper;

 @Before
 public void setUp() {
 mapper = new XmlMapper();
 }

 @Test
 public void testSerializeValueNull() throws JsonProcessingException {
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 ObjectNode rootNode = mapper.createObjectNode();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);
 provider.serializeValue(gen, null);
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<null/>", xml);
 }

 @Test
 public void testSerializeValuePrimitiveTypes() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);

 provider.serializeValue(gen, 42);
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<int>42</int>", xml);

 provider.serializeValue(gen, (byte) 5);
 gen.close();
 xml = rootNode.toString();
 assertEquals("<byte>5</byte>", xml);

 provider.serializeValue(gen, (short) 127);
 gen.close();
 xml = rootNode.toString();
 assertEquals("<short>127</short>", xml);

 provider.serializeValue(gen, (char) 'A');
 gen.close();
 xml = rootNode.toString();
 assertEquals("<char>A</char>", xml);

 provider.serializeValue(gen, (boolean) true);
 gen.close();
 xml = rootNode.toString();
 assertEquals("<boolean>true</boolean>", xml);
 }

 @Test
 public void testSerializeValueCustomRootName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);

 provider.setRootName("MyRootElement");
 provider.serializeValue(gen, 42);
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<MyRootElement><int>42</int></MyRootElement>", xml);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueInvalidRootName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);

 provider.setRootName("  ");
 provider.serializeValue(gen, 42);
 }

 @Test
 public void testSerializeValueArray() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);
 provider.writeStartArray(gen);

 provider.serializeValue(gen, 1);
 provider.serializeValue(gen, 2);
 provider.serializeValue(gen, 3);
 provider.writeEndArray();
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<int>[1,2,3]</int>", xml);
 }

 @Test
 public void testSerializeValueArrayCustomRootName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);
 provider.setRootName("MyRootElement");
 provider.writeStartArray(gen, "MyRootElement");

 provider.serializeValue(gen, 1);
 provider.serializeValue(gen, 2);
 provider.serializeValue(gen, 3);
 provider.writeEndArray();
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<MyRootElement><int>[1,2,3]</int></MyRootElement>", xml);
 }

 @Test
 public void testSerializeValueNullArray() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);
 provider.writeStartArray(gen, "MyRootElement");
 provider.writeNull();
 provider.writeNull();
 provider.writeNull();
 provider.writeEndArray();
 gen.close();
 String xml = rootNode.toString();
 assertEquals("<MyRootElement><int>null</int><int>null</int><int>null</int></MyRootElement>", xml);
 }

 @Test
 public void testSerializeValueArrayInvalidRootName() throws JsonProcessingException {
 ObjectNode rootNode = mapper.createObjectNode();
 XmlSerializerProvider provider = mapper.getSerializerProvider();
 JsonGenerator gen = provider.getRootValueSerializer(ObjectNode.class).getGenerator(rootNode, provider, null);
 provider = new XmlSerializerProvider(provider, mapper.getSerializationConfig(), mapper.getSerializerFactory());
 provider.setRootName("  ");
 provider.writeStartArray(gen, "MyRootElement");
 provider.serializeValue(gen, 1);
 provider.serializeValue(gen, 2);
 provider.serializeValue(gen, 3);
 provider.writeEndArray();
 }
}