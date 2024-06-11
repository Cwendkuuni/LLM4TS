import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;
 private AtomicReferenceDeserializer deserializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Integer>>(){},
 null,
 null,
 null);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = deserializer.getNullValue(mockContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Object> expected = new AtomicReference<Object>();
 AtomicReference<Object> actual = deserializer.getEmptyValue(mockContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 Object contents = new Integer(1);
 AtomicReference<Object> expected = new AtomicReference<Object>(contents);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Integer(1));
 Object actual = deserializer.getReferenced(reference);
 assertEquals(1, actual.intValue());
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Integer(1));
 Object newContents = new Integer(2);
 AtomicReference<Object> expected = new AtomicReference<Object>(newContents);
 assertNotSame(reference, expected);
 deserializer.updateReference(reference, newContents);
 AtomicReference<Object> actual = deserializer.getReferenced(reference);
 assertEquals(newContents, actual.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Integer(1));
 Object newContents = new Integer(2);
 deserializer.updateReference(reference, newContents);
 fail("Update should not be supported");
 }

 @Test
 public void testDeserializeNull() throws JsonProcessingException {
 String jsonNull = "null";
 JsonNode node = mapper.readTree(jsonNull);
 Object actual = mapper.readValue(node, AtomicReference.class, deserializer);
 assertTrue(actual instanceof AtomicReference);
 assertNull(((AtomicReference) actual).get());
 }

 @Test
 public void testDeserializeEmptyJson() throws JsonProcessingException {
 String jsonEmpty = "{}";
 JsonNode node = mapper.readTree(jsonEmpty);
 Object actual = mapper.readValue(node, AtomicReference.class, deserializer);
 assertTrue(actual instanceof AtomicReference);
 assertNull(((AtomicReference) actual).get());
 }

 @Test
 public void testDeserializeInteger() throws JsonProcessingException {
 String jsonInteger = "1";
 JsonNode node = mapper.readTree(jsonInteger);
 Object actual = mapper.readValue(node, AtomicReference.class, deserializer);
 assertTrue(actual instanceof AtomicReference);
 assertEquals(1, ((AtomicReference) actual).get().intValue());
 }

 @Test
 public void testDeserializeString() throws JsonProcessingException {
 String jsonString = "\"Hello World\"";
 JsonNode node = mapper.readTree(jsonString);
 Object actual = mapper.readValue(node, AtomicReference.class, deserializer);
 assertTrue(actual instanceof AtomicReference);
 assertEquals("Hello World", ((AtomicReference) actual).get());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeInvalidJson() throws JsonProcessingException {
 String jsonInvalid = "InvalidJson";
 JsonNode node = mapper.readTree(jsonInvalid);
 mapper.readValue(node, AtomicReference.class, deserializer);
 fail("Invalid JSON should not be deserialized");
 }

 private DeserializationContext mockContext() {
 return new TestContext();
 }

}