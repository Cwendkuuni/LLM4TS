package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlSerializerProviderTest {

 private XmlRootNameLookup rootNameLookup;
 private XmlSerializerProvider xmlSerializerProvider;
 private SerializationConfig config;
 private ObjectMapper objectMapper;
 private SerializerProvider jsonSerializerProvider;

 @Before
 public void setup() {
 rootNameLookup = new XmlRootNameLookup();
 config = new SerializationConfig();
 objectMapper = new XmlMapper();
 objectMapper.setSerializationConfig(config);
 jsonSerializerProvider = objectMapper.getSerializerProvider();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 }

 @Test
 public void testSerializeNull() throws JsonGenerationException, JsonMappingException, IOException {
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(gen, null);
 assertEquals("<null/>", StaxUtil.convertStaxToXmlString(gen.asXML()));
 }

 @Test
 public void testSerializeSimpleObject() throws JsonGenerationException, JsonMappingException, IOException {
 String expected = "<root><value>Hello World</value></root>";
 Object value = "Hello World";
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(gen, value);
 assertEquals(expected, StaxUtil.convertStaxToXmlString(gen.asXML()));
 }

 @Test
 public void testSerializeArrayObject() throws JsonGenerationException, JsonMappingException, IOException {
 String expected = "<root><item><value1>Hello World 1</value1></item>" +
         "<item><value2>Hello World 2</value2></item></root>";
 Object[] values = {"Hello World 1", "Hello World 2"};
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(gen, values);
 assertEquals(expected, StaxUtil.convertStaxToXmlString(gen.asXML()));
 }

 @Test
 public void testSerializeCustomRootName() throws JsonGenerationException, JsonMappingException, IOException {
 config.setRootValueSeparator("");
 config.setRootName("customRootName");
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, config, jsonSerializerProvider.getFactory());
 xmlSerializerProvider.serializeValue(gen, new Object());
 assertEquals("<customRootName/>", StaxUtil.convertStaxToXmlString(gen.asXML()));
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullRootName() throws JsonGenerationException, JsonMappingException, IOException {
 config.setRootValueSeparator("");
 config.setRootName(null);
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, config, jsonSerializerProvider.getFactory());
 xmlSerializerProvider.serializeValue(gen, new Object());
 }

 @Test(expected = JsonGenerationException.class)
 public void testSerializeSerializerFailure() throws JsonGenerationException, JsonMappingException, IOException {
 class DummySerializer extends JsonSerializer<Object> {
 @Override public void serialize(Object value, JsonGenerator gen, SerializerProvider p) throws IOException { throw new JsonGenerationException(); }
 }
 xmlSerializerProvider.setSerializerProvider(new DefaultSerializerProvider());
 xmlSerializerProvider.setSerializerInstance(DummySerializer.class, new DummySerializer());
 JsonGenerator gen = new TokenBuffer(null);
 xmlSerializerProvider.serializeValue(gen, new Object());
 }
}