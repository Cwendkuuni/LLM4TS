import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 SimpleModule module = new SimpleModule();
 mapper = new ObjectMapper();
 mapper.registerModule(module);
 }

 @Test
 public void testGetNullValue() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> actual = deserializer.getNullValue(null);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testGetEmptyValue() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object actual = deserializer.getEmptyValue(null);
 assertTrue(actual instanceof AtomicReference);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testReferenceValue() throws JsonProcessingException {
 Object contents = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> actual = deserializer.referenceValue(contents);
 assertEquals(contents, actual.get());
 }

 @Test
 public void testGetReferenced() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 Object actual = deserializer.getReferenced(reference);
 assertEquals(reference.get(), actual);
 }

 @Test
 public void testUpdateReference() throws JsonProcessingException {
 AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
 Object newContents = new Object();
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newContents);
 assertEquals(newContents, updatedReference.get());
 reference.set(newContents);
 assertEquals(newContents, reference.get());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSupportsUpdateReturnsFalse() throws JsonProcessingException {
 AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
 new TypeReference<AtomicReference<Object>>() {}, null, null, null);
 try {
 deserializer.supportsUpdate(null);
 fail();
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }
}