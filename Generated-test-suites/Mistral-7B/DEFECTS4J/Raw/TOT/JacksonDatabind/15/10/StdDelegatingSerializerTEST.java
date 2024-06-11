import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

 private ObjectMapper mapper;
 private StdDelegatingSerializer delegateSerializer;
 private Converter<String, Integer> converter;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 converter = mapper.getTypeFactory().constructConverter(String.class, Integer.class);
 }

 @Test
 public void testDelegateSerializationIntString() throws JsonProcessingException, JsonGenerationException {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("intValue", "42");

 delegateSerializer = new StdDelegatingSerializer(converter, Object.class.getType(int[].class), mapper.findValueSerializer(int[].class));
 Object intArray = delegateSerializer.deserialize(sourceNode, Object[].class, mapper);
 assertEquals(42, intArray[0]);

 ObjectNode resultNode = mapper.createObjectNode();
 delegateSerializer = new StdDelegatingSerializer(converter, int[].class, mapper.findValueSerializer(int[].class));
 resultNode = (ObjectNode) delegateSerializer.serializeWithType(new int[]{42}, resultNode, mapper, mapper.getTypeSerializer());
 assertEquals("42", resultNode.get("intValue").asText());
 }

 @Test
 public void testDelegateSerializationNull() throws JsonProcessingException, JsonGenerationException {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.putNull("intValue");

 delegateSerializer = new StdDelegatingSerializer(converter, Object.class.getType(int[].class), mapper.findValueSerializer(int[].class));
 Object intArray = delegateSerializer.deserialize(sourceNode, Object[].class, mapper);
 assertNull(intArray);

 ObjectNode resultNode = mapper.createObjectNode();
 delegateSerializer = new StdDelegatingSerializer(converter, int[].class, mapper.findValueSerializer(int[].class));
 resultNode = (ObjectNode) delegateSerializer.serializeWithType(null, resultNode, mapper, mapper.getTypeSerializer());
 assertTrue(resultNode.isNull());
 }

 @Test(expected = IllegalStateException.class)
 public void testDelegateSerializationSelfReference() throws JsonProcessingException, JsonGenerationException {
 ObjectNode sourceNode = mapper.createObjectNode();
 sourceNode.put("intValue", "42");
 sourceNode.put("serializer", this);

 delegateSerializer = new StdDelegatingSerializer(converter, Object.class.getType(int[].class), mapper.findValueSerializer(int[].class));
 Object intArray = delegateSerializer.deserialize(sourceNode, Object[].class, mapper);
 fail("Should throw IllegalStateException for self reference");
 }
}