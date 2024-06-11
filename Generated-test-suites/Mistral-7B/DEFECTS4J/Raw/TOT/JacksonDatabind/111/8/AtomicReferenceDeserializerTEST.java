import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper objectMapper;
 private AtomicReference<String> atomicReference;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testAtomicReferenceDeserialization() throws Exception {
 ObjectReadOnlyTest objectReadOnlyTest = new ObjectReadOnlyTest("testValue");
 AtomicReference<ObjectReadOnlyTest> expectedAtomicReference = new AtomicReference<>(objectReadOnlyTest);

 ObjectWriter objectWriter = objectMapper.writerWithValueTypeSerializer(new TypeReference<AtomicReference<ObjectReadOnlyTest>>(){}.getType());
 String jsonString = objectWriter.writeValueAsString(expectedAtomicReference);

 AtomicReference<ObjectReadOnlyTest> deserializedAtomicReference = objectMapper.readValue(jsonString, AtomicReference.class);
 Object deserializedValue = deserializedAtomicReference.get();
 assertEquals(objectReadOnlyTest, deserializedValue);
 }

 @Test
 public void testNullValueDeserialization() throws Exception {
 AtomicReference<Object> nullAtomicReference = AtomicReferenceDeserializer.getNullValue(null);
 assertNull(nullAtomicReference.get());
 }

 @Test
 public void testEmptyValueDeserialization() throws Exception {
 AtomicReference<Object> emptyAtomicReference = AtomicReferenceDeserializer.getEmptyValue(null);
 assertNull(emptyAtomicReference.get());
 }

 @Test
 public void testReferenceValueDeserialization() throws Exception {
 ObjectReadOnlyTest objectReadOnlyTest = new ObjectReadOnlyTest("testValue");
 AtomicReference<ObjectReadOnlyTest> expectedAtomicReference = new AtomicReference<>(objectReadOnlyTest);

 ObjectWriter objectWriter = objectMapper.writerWithValueTypeSerializer(new TypeReference<AtomicReference<ObjectReadOnlyTest>>() {}.getType());
 String jsonString = objectWriter.writeValueAsString(expectedAtomicReference);

 AtomicReference<ObjectReadOnlyTest> deserializedAtomicReference = objectMapper.readValue(jsonString, AtomicReference.class);
 AtomicReference<ObjectReadOnlyTest> referenceValue = AtomicReferenceDeserializer.referenceValue(objectReadOnlyTest);
 assertEquals(deserializedAtomicReference, AtomicReferenceDeserializer.withResolved(null, null).referenceValue(referenceValue));
 }

 @Test
 public void testGetReferenced() throws Exception {
 ObjectReadOnlyTest objectReadOnlyTest = new ObjectReadOnlyTest("testValue");
 AtomicReference<ObjectReadOnlyTest> atomicReference = new AtomicReference<>(objectReadOnlyTest);
 assertEquals(objectReadOnlyTest, AtomicReferenceDeserializer.getReferenced(atomicReference));
 }

 @Test
 public void testUpdateReference() throws Exception {
 ObjectReadOnlyTest oldObjectReadOnlyTest = new ObjectReadOnlyTest("oldValue");
 ObjectReadOnlyTest newObjectReadOnlyTest = new ObjectReadOnlyTest("newValue");
 AtomicReference<ObjectReadOnlyTest> atomicReference = new AtomicReference<>(oldObjectReadOnlyTest);

 ObjectWriter objectWriter = objectMapper.writerWithValueTypeSerializer(new TypeReference<AtomicReference<ObjectReadOnlyTest>>() {}.getType());
 String jsonString = objectWriter.writeValueAsString(new AtomicReference<>(newObjectReadOnlyTest));

 AtomicReference<ObjectReadOnlyTest> updatedAtomicReference = objectMapper.readValue(jsonString, AtomicReference.class);
 AtomicReference<ObjectReadOnlyTest> updatedReference = AtomicReferenceDeserializer.withResolved(null, null).updateReference(atomicReference, newObjectReadOnlyTest);
 assertEquals(updatedReference, updatedAtomicReference);
 }

 @Test
 public void testSupportsUpdate() throws Exception {
 assertTrue(AtomicReferenceDeserializer.supportsUpdate(null));
 }
}

class ObjectReadOnlyTest {
 private final String value;

 public ObjectReadOnlyTest(String value) {
 this.value = value;
 }
}