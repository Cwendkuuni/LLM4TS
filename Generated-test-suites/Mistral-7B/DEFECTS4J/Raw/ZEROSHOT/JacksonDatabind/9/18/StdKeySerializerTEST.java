import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;

public class StdKeySerializerTest {

 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;
 private StdKeySerializer stdKeySerializer;

 @Before
 public void setup() {
 serializerProvider = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 stdKeySerializer = new StdKeySerializer();
 }

 @Test
 public void testSerializeDate() throws IOException {
 Date date = new Date();
 JsonGenerator jgen = mock(JsonGenerator.class);
 stdKeySerializer.serialize(date, jgen, serializerProvider);
 Mockito.verify(serializerProvider, Mockito.times(1)).defaultSerializeDateKey(date, jgen);
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "testString";
 JsonGenerator jgen = mock(JsonGenerator.class);
 stdKeySerializer.serialize(value, jgen, serializerProvider);
 Mockito.verify(jgen, Mockito.times(1)).writeFieldName(value);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JavaType typeHint = mock(JavaType.class);
 ObjectNode schemaNode = JsonNodeFactory.instance.objectNode();
 stdKeySerializer.getSchema(serializerProvider, typeHint);
 when(typeHint.getTypeName()).thenReturn("string");
 when(serializerProvider.getSchemaProvider().findSchemaNode("string")).thenReturn(schemaNode);
 assertEquals(schemaNode, stdKeySerializer.getSchema(serializerProvider, typeHint));
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JavaType typeHint = mock(JavaType.class);
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 stdKeySerializer.acceptJsonFormatVisitor(visitor, typeHint);
 when(visitor.expectStringFormat(typeHint)).thenReturn(null);
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNull() throws IOException {
 Object value = null;
 JsonGenerator jgen = mock(JsonGenerator.class);
 stdKeySerializer.serialize(value, jgen, serializerProvider);
 }

 @Test
 public void testSerializeMap() throws IOException, JsonProcessingException {
 Map<String, Object> map = Map.of("key1", "value1", "key2", "value2");
 ObjectNode rootNode = objectMapper.createObjectNode();
 ObjectNode mapNode = objectMapper.createObjectNode();
 rootNode.set("map", mapNode);
 objectMapper.writerWithRootValue(rootNode).writeValue(System.out, map);
 stdKeySerializer.serialize(map, rootNode.get("map"), serializerProvider);
 assertEquals("{\"map\":{\"key1\":\"value1\",\"key2\":\"value2\"}}", root.getValueAsString());
 }
}