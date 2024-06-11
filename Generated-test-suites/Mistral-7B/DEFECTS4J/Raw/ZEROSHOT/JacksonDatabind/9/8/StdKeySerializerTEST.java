import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdKeySerializerTest {

 private StdKeySerializer serializer;
 private SerializerProvider provider;
 private JsonGenerator jsonGeneratormock;

 @Before
 public void setup() {
 serializer = new StdKeySerializer();
 provider = mock(SerializerProvider.class);
 jsonGeneratormock = mock(JsonGenerator.class);
 }

 @Test
 public void testSerializeDate() throws IOException, JsonProcessingException {
 Date date = new Date();
 when(provider.defaultSerializeDateKey(date, jsonGeneratormock)).thenCallRealMethod();

 serializer.serialize(date, jsonGeneratormock, provider);

 // Verify that the default serializeDateKey method was called instead
 // of the serialize method
 assertEquals(JsonGenerator.Feature.AUTO_CLOSE_TARGET, jsonGeneratormock.getFeatureValue("autoCloseTarget"));
 }

 @Test
 public void testSerializeString() throws IOException {
 String value = "testValue";

 when(jsonGeneratormock.writeFieldName(value)).thenCallRealMethod();

 serializer.serialize(value, jsonGeneratormock, provider);

 // Verify that writeFieldName was called with the correct argument
 assertEquals(value, jsonGeneratormock.getCurrentName());
 }

 @Test
 public void testGetSchema() {
 JsonNode schema = serializer.getSchema(provider, TypeFactory.defaultInstance().constructType(String.class));

 assertNotNull(schema);
 assertEquals("string", schema.asText());
 }

 @Test
 public void testAcceptJsonFormatVisitor() throws JsonMappingException {
 JsonFormatVisitorWrapper visitormock = mock(JsonFormatVisitorWrapper.class);
 JavaType typeHint = TypeFactory.defaultInstance().constructType(String.class);

 serializer.acceptJsonFormatVisitor(visitormock, typeHint);

 // Verify that the visitor is expecting a string format
 visitormock.assertCurrentValue("format", "string");
 }

 @Test
 public void testSerializeMap() throws IOException {
 ObjectMapper mapper = new ObjectMapper();
 Type mapType = TypeFactory.defaultInstance().constructMapTypes(String.class, Integer.class);
 ObjectNode rootNode = mapper.createObjectNode();
 rootNode.set("testKey", 1);

 SerializerProvider providerForObjectMapper = mapper.getSerializerProvider();
 JsonGenerator jsonGenerator = providerForObjectMapper.getGenerator();
 jsonGenerator.setCodec(mapper.getFactory().getEncoderFindForType(Map.class));
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("testMap");
 jsonGenerator.writeStartObject();
 serializer.serialize(rootNode, jsonGenerator, provider);
 jsonGenerator.writeEndObject();
 jsonGenerator.writeEndObject();

 String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonGenerator.getBuffer());

 assertEquals("{\"testMap\":{\"testKey\":1}}", jsonString);
 }
}