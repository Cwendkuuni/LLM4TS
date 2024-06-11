import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.ObjectInputStream;
import org.apache.commons.lang3.ObjectOutputStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

 @Test
 public void testClone() {
 Serializable obj = new HashMap<>();
 Serializable clonedObj = SerializationUtils.clone(obj);
 assertEquals(obj, clonedObj);
 }

 @Test
 public void testSerializeAndDeserializeObject() throws IOException, SerializationException {
 Serializable obj = new HashMap<>();
 byte[] objectData = SerializationUtils.serialize(obj);
 Serializable deserializedObj = SerializationUtils.deserialize(objectData);
 assertEquals(obj, deserializedObj);
 }

 @Test
 public void testSerializeAndDeserializeByteArray() throws IOException, SerializationException {
 Serializable obj = new HashMap<>();
 byte[] objectData = SerializationUtils.serialize(obj);
 Serializable deserializedObj = SerializationUtils.deserialize(objectData);
 assertEquals(obj, deserializedObj);
 }

 @Test(expected = SerializationException.class)
 public void testSerializeNullObject() {
 SerializationUtils.serialize(null, new ByteArrayOutputStream(512));
 }

 @Test(expected = SerializationException.class)
 public void testDeserializeNullStream() {
 SerializationUtils.deserialize(null);
 }

 @Test
 public void testDeserializeClassNotFound() throws IOException, SerializationException {
 Serializable obj = new HashMap<>();
 byte[] objectData = SerializationUtils.serialize(obj);
 ObjectStreamClass desc = new ObjectStreamClass("nonExistentClass", false);
 try (ByteArrayInputStream bais = new ByteArrayInputStream(objectData);
 ObjectInputStream in = new ObjectLoaderAwareObjectInputStream(bais, null)) {
 Object readObject = in.readObject();
 fail("Expected SerializationException but got " + readObject.getClass());
 }
 }

 @Test
 public void testDeserializeIOException() throws IOException, SerializationException {
 Serializable obj = new HashMap<>();
 byte[] objectData = SerializationUtils.serialize(obj);
 try (ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{})) {
 Object readObject = SerializationUtils.deserialize(bais);
 fail("Expected IOException but got " + readObject.getClass());
 }
 }

}