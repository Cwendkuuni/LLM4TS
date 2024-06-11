import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetNullValue() throws Exception {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object nullValue = serializer.getNullValue(null);
 assertNull(nullValue);
 }

 @Test
 public void testGetEmptyValue() throws Exception {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object emptyValue = serializer.getEmptyValue(null);
 assertInstanceOf(AtomicReference.class, emptyValue);
 assertEquals(0, ((AtomicReference<Object>) emptyValue).size());
 }

 @Test
 public void testReferenceValue() throws Exception {
 Object contents = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 AtomicReference<Object> reference = serializer.referenceValue(contents);
 assertInstanceOf(AtomicReference.class, reference);
 assertEquals(contents, reference.get());
 }

 @Test
 public void testGetReferenced() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<>(new Object());
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 Object object = serializer.getReferenced(reference);
 assertEquals(reference.get(), object);
 }

 @Test
 public void testUpdateReference() throws Exception {
 AtomicReference<Object> reference = new AtomicReference<>(new Object());
 Object newContent = new Object();
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 serializer.updateReference(reference, newContent);
 assertEquals(newContent, reference.get());
 }

 @Test
 public void testSupportsUpdate() throws Exception {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, Object.class),
 null, null, null);
 assertTrue(serializer.supportsUpdate(mapper.getConfig()));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedUpdate() throws Exception {
 AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(
 mapper.getTypeFactory().constructType(AtomicReference.class, new TypeReference<String>() {}),
 null, null, new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
 throws IOException {
 throw new UnsupportedOperationException();
 }
 });
 serializer.updateReference(new AtomicReference<String>(), "test");
 }
}