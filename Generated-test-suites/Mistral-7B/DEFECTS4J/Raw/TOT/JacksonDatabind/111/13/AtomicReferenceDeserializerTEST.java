import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;
 private AtomicReferenceDeserializer serializer;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 serializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Integer>>() {}, null, null, null);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReference<Integer> expected = new AtomicReference<Integer>();
 AtomicReference<Integer> actual = serializer.getNullValue(mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testGetEmptyValue() {
 AtomicReference<Integer> expected = new AtomicReference<Integer>();
 AtomicReference<Integer> actual = serializer.getEmptyValue(mapper.getDeserializationContext());
 assertEquals(expected, actual);
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 Integer contents = 1;
 AtomicReference<Integer> expected = new AtomicReference<Integer>(contents);
 Object readValue = mapper.readValue("1", new TypeReference<AtomicReference<Integer>>() {});
 AtomicReference<Integer> actual = serializer.referenceValue(readValue);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Integer> reference = new AtomicReference<Integer>(42);
 Integer value = serializer.getReferenced(reference);
 assertEquals(42, value.intValue());
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Integer> reference = new AtomicReference<Integer>(1);
 Integer newValue = 42;
 Object updatedReference = serializer.updateReference(reference, newValue);
 assertEquals(newValue, reference.get());
 assertEquals(updatedReference, reference);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSupportsUpdateWithInvalidConfig() {
 Object config = new Object();
 assertFalse(serializer.supportsUpdate((DeserializationConfig) config));
 }

 @Test
 public void testSupportsUpdateWithValidConfig() {
 Object config = mapper.getDeserializationConfig();
 assertTrue(serializer.supportsUpdate((DeserializationConfig) config));
 }
}