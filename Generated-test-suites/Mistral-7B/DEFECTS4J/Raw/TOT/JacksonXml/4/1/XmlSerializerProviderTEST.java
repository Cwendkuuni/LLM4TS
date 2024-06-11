package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private ObjectMapper objectMapper;
 private SerializerProvider mockSerializerProvider;
 private QName rootName;
 private ToXmlGenerator toXmlGenerator;

 @Before
 public void setUp() {
 objectMapper = new XmlMapper();
 xmlSerializerProvider = new XmlSerializerProvider(objectMapper.getRootNameLookup());
 mockSerializerProvider = mock(SerializerProvider.class);
 toXmlGenerator = mock(ToXmlGenerator.class);
 rootName = new QName("testRoot");
 }

 @After
 public void tearDown() {
 reset(mockSerializerProvider);
 }

 @Test
 public void testSerializeNull() throws JsonGenerationException, JsonMappingException, XMLStreamException {
 given((ToXmlGenerator) xmlSerializerProvider.getFactory().createGenerator(null, mockSerializerProvider, null))
 .willReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(any(QName.class)))
 .thenReturn(true);
 when(toXmlGenerator.initGenerator()).thenReturn(true);

 xmlSerializerProvider.serializeValue(any(JsonGenerator.class), null);

 verify(toXmlGenerator, times(1)).writeStartObject();
 verify(toXmlGenerator, times(1)).writeFieldName("null");
 verify(toXmlGenerator, times(1)).writeEndObject();
 }

 @Test
 public void testSerializeSimpleObject() throws JsonGenerationException, JsonMappingException, XMLStreamException {
 ObjectNode objectNode = objectMapper.createObjectNode();
 objectNode.put("name", "John Doe");
 given((ToXmlGenerator) xmlSerializerProvider.getFactory().createGenerator(objectNode, mockSerializerProvider, null))
 .willReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(eq(rootName)))
 .thenReturn(false);
 when(toXmlGenerator.setNextNameIfMissing(any(QName.class)))
 .thenReturn(true);
 when(toXmlGenerator.initGenerator()).thenReturn(true);

 xmlSerializerProvider.serializeValue(any(JsonGenerator.class), objectNode, rootName);

 verify(toXmlGenerator, times(1)).writeStartObject();
 verify(toXmlGenerator, times(1)).writeFieldName("name");
 verify(toXmlGenerator, times(1)).writeValue(eq("John Doe"));
 verify(toXmlGenerator, times(1)).writeEndObject();
 }

 @Test
 public void testSerializeArray() throws JsonGenerationException, JsonMappingException, XMLStreamException {
 ObjectNode arrayNode = objectMapper.createObjectNode();
 arrayNode.put("items", objectMapper.createArrayNode());
 given((ToXmlGenerator) xmlSerializerProvider.getFactory().createGenerator(arrayNode, mockSerializerProvider, null))
 .willReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(eq(rootName)))
 .thenReturn(false);
 when(toXmlGenerator.setNextNameIfMissing(any(QName.class)))
 .thenReturn(true);
 when(toXmlGenerator.initGenerator()).thenReturn(true);

 xmlSerializerProvider.serializeValue(any(JsonGenerator.class), arrayNode, rootName);

 verify(toXmlGenerator, times(1)).writeStartObject();
 verify(toXmlGenerator, times(1)).writeFieldName("item");
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullSerializer() throws JsonGenerationException, JsonMappingException, XMLStreamException {
 given((ToXmlGenerator) xmlSerializerProvider.getFactory().createGenerator(null, mockSerializerProvider, null))
 .willReturn(toXmlGenerator);
 when(toXmlGenerator.setNextNameIfMissing(any(QName.class)))
 .thenReturn(true);
 when(toXmlGenerator.initGenerator()).thenReturn(true);

 xmlSerializerProvider.serializeValue(any(JsonGenerator.class), null, null);

 verify(toXmlGenerator, times(1)).writeStartObject();
 verify(toXmlGenerator, never()).writeFieldName("null");
 verify(toXmlGenerator, times(1)).writeEndObject();

 throw new JsonMappingException("Null serializer provided");
 }
}