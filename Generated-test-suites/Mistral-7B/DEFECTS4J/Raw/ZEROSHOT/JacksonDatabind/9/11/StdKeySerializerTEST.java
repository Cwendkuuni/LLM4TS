import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatvisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private JsonGenerator mockJsonGenerator;
 private SerializerProvider mockProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 mockJsonGenerator = mock(JsonGenerator.class);
 mockProvider = mock(SerializerProvider.class);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeDate() throws JsonProcessingException {
 Date date = new Date();
 serializer.serialize(date, mockJsonGenerator, mockProvider);

 verify(mockJsonGenerator, times(1)).writeFieldName("@JsonKey@"); // Jackson uses this as default serialization key for Date
 }

 @Test
 public void testSerializeString() throws JsonProcessingException {
 String str = "test";
 serializer.serialize(str, mockJsonGenerator, mockProvider);

 verify(mockJsonGenerator, times(1)).writeFieldName(str);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 JsonNode schema = serializer.getSchema(mockProvider, JavaType.constructParametricType(JsonNode.class, String.class));

 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = TypeFactory.constructParametricType(JsonNode.class, String.class);

 serializer.acceptJsonFormatVisitor(visitor, typeHint);

 verify(visitor, times(1)).expectStringFormat(typeHint);
 }

 @Test
 public void testSerializeMap() throws JsonProcessingException {
 Map<String, Object> map = new HashMap<>();
 map.put("key1", new Date());
 map.put("key2", "value2");

 JavaType mapType = TypeFactory.constructParametricType(Map.class, String.class, Object.class);
 JsonNode expectedJsonNode = new ObjectMapper().readValue(getClass().getResource("/expected_map.json"), JsonNode.class);

 serializer = new StdKeySerializer();
 JsonGenerator jsonGenerator = mockJsonGenerator;
 doAnswer(invocation -> {
 ObjectMapper mapper = invocation.getArgument(1);
 try {
 return mapper.writeValueAsString(expectedJsonNode);
 } catch (JsonProcessingException e) {
 throw new RuntimeException(e);
 }
 }).when(serializer).serialize(any(Map.class), eq(mockJsonGenerator), eq(mockProvider));

 String json = objectMapper.writeValueAsString(map);

 assertEquals(expectedJsonNode.toString(), json);
 }
}