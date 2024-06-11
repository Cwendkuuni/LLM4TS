package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;
 private XmlRootNameLookup xmlRootNameLookup;
 private SerializerProvider xmlSerializerProvider;
 private JsonGenerator gen;
 private SerializationConfig config;
 private SerializerFactory serializerFactory;
 private TokenBuffer tokenBuffer;
 private QName rootName;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 xmlRootNameLookup = mock(XmlRootNameLookup.class);
 config = objectMapper.getSerializationConfig();
 serializerFactory = objectMapper.getSerializerFactory();
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
 tokenBuffer = new TokenBuffer();
 gen = mock(JsonGenerator.class);
 when(gen.getAsXmlGenerator()).thenReturn(tokenBuffer);
 rootName = new QName("test", "RootElement");
 }

 @Test
 public void testSerializeValue() throws Exception {
 Object value = "test value";
 JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
 when(jsonSerializer.serialize(value, gen, xmlSerializerProvider)).thenCallRealMethod();
 when(xmlSerializerProvider.findTypedValueSerializer(value.getClass(), true, null)).thenReturn(jsonSerializer);
 xmlSerializerProvider.serializeValue(gen, value);
 verifySerializerCall(value, "test value", Arrays.asList("test"), Arrays.asList("RootElement"));
 }

 @Test
 public void testSerializeValueWithRootType() throws Exception {
 JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.asList(String.class).getClass());
 JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
 when(jsonSerializer.serialize(value, gen, xmlSerializerProvider)).thenCallRealMethod();
 when(xmlSerializerProvider.findTypedValueSerializer(rootType, true, null)).thenReturn(jsonSerializer);
 xmlSerializerProvider.serializeValue(gen, value, rootType);
 verifySerializerCall(value, "test value", Arrays.asList("test"), Arrays.asList("RootElement"));
 }

 @Test
 public void testSerializeValueWithRootTypeAndSerializer() throws Exception {
 JavaType rootType = objectMapper.getTypeFactory().constructType(Arrays.asList(String.class).getClass());
 JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
 when(jsonSerializer.serialize(value, gen, xmlSerializerProvider)).thenCallRealMethod();
 when(xmlSerializerProvider.findTypedValueSerializer(rootType, true, jsonSerializer)).thenReturn(jsonSerializer);
 xmlSerializerProvider.serializeValue(gen, value, rootType, jsonSerializer);
 verifySerializerCall(value, "test value", Arrays.asList("test"), Arrays.asList("RootElement"));
 }

 @Test
 public void testSerializeValueNull() throws Exception {
 xmlSerializerProvider.serializeValue(gen, null);
 verify(gen).writeNull();
 }

 @Test
 public void testSerializeValueWithNullRootType() throws Exception {
 JavaType rootType = null;
 xmlSerializerProvider.serializeValue(gen, null, rootType);
 verify(gen).writeNull();
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeValueWithNullSerializer() throws Exception {
 JsonSerializer<Object> jsonSerializer = null;
 xmlSerializerProvider.serializeValue(gen, null, null, jsonSerializer);
 verify(gen).writeNull();
 }

 @Test
 public void testCreateInstance() throws Exception {
 SerializerProvider src = mock(SerializerProvider.class);
 SerializationConfig config = mock(SerializationConfig.class);
 SerializerFactory factory = mock(SerializerFactory.class);
 XmlSerializerProvider provider = new XmlSerializerProvider(xmlRootNameLookup);
 XmlSerializerProvider newProvider = provider.createInstance(config, factory);
 assertEquals(provider, newProvider);
 }

 @Test
 public void testSerializeValueWithRootName() throws Exception {
 String value = "test value";
 when(xmlRootNameLookup.findRootName(value.getClass(), config)).thenReturn(rootName);
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config, serializerFactory);
 xmlSerializerProvider.serializeValue(gen, value);
 verifySerializerCall(value, "test value", Arrays.asList("test"), Arrays.asList("RootElement"));
 }

 @Test
 public void testSerializeValueWithRootNameFromConfig() throws Exception {
 PropertyName rootNameProperty = new PropertyName("RootElement");
 config.setRootName(rootNameProperty);
 xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup, config, serializerFactory);
 xmlSerializerProvider.serializeValue(gen, value);
 verifySerializerCall(value, "test value", Arrays.asList("RootElement"), Arrays.asList("RootElement"));
 }

 @Test
 public void testStartRootArray() throws IOException, XMLStreamException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 xmlSerializerProvider._startRootArray(xgen, rootName);
 assertEquals(XmlSerializerProvider.RootNameForNull, xgen.getName());
 xgen.writeStartArray();
 verify(xgen).writeStartObject();
 verify(xgen).writeFieldName("item");
 }

 @Test
 public void testInitWithRootName() throws IOException, XMLStreamException {
 ToXmlGenerator xgen = mock(ToXmlGenerator.class);
 xmlSerializerProvider._initWithRootName(xgen, rootName);
 assertEquals(rootName, xgen.getName());
 xgen.initGenerator();
 StaxUtil staxUtil = mock(StaxUtil.class);
 when(xgen.getStaxWriter()).thenReturn(staxUtil);
 verify(staxUtil).setDefaultNamespace(rootName.getNamespaceURI());
 }

 @Test
 public void testRootNameFromConfig() throws Exception {
 PropertyName rootNameProperty = new PropertyName("RootElement");
 config.setRootName(rootNameProperty);
 assertEquals(new QName("RootElement"), xmlSerializerProvider._rootNameFromConfig());
 }

 @Test
 public void testAsXmlGenerator() throws Exception {
 JsonGenerator jsonGenerator = mock(JsonGenerator.class);
 when(jsonGenerator.isEnabled(JsonGenerator.Feature.WRITE_XML_NAMESPACE)).thenReturn(true);
 when(jsonGenerator.getAsXmlGenerator()).thenReturn(tokenBuffer);
 assertEquals(tokenBuffer, xmlSerializerProvider._asXmlGenerator(jsonGenerator));
 }

 @Test
 public void testAsXmlGeneratorNull() throws Exception {
 JsonGenerator jsonGenerator = null;
 assertNull(xmlSerializerProvider._asXmlGenerator(jsonGenerator));
 }

 private void verifySerializerCall(Object value, String actualValue, List<String> expectedTypes, List<String> expectedRootNames) throws IOException, JsonMappingException {
 JsonSerializer<Object> jsonSerializer = mock(JsonSerializer.class);
 when(jsonSerializer.serialize(value, gen, xmlSerializerProvider)).thenReturn(new Object());
 verify(jsonSerializer).serialize(value, gen, xmlSerializerProvider);
 assertEquals(actualValue, tokenBuffer.getRootValueAsString());
 assertEquals(expectedTypes, Arrays.asList(tokenBuffer.getRootValueAsString().split("\\.")[0].split(":")[0]));
 assertEquals(expectedRootNames, Arrays.asList(rootName.getLocalPart()));
 }

}