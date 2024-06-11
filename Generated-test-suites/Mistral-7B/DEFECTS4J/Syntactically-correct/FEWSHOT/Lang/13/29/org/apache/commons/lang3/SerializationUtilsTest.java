package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class SerializationUtilsTest {

 @Test
 public void testClone() {
 Map<Integer, String> originalMap = new HashMap<>();
 originalMap.put(1, "one");
 originalMap.put(2, "two");

 Map<Integer, String> clonedMap = SerializationUtils.clone(originalMap);

 assertNotNull(clonedMap);
 assertNotNull(clonedMap.get(1));
 assertNotNull(clonedMap.get(2));
 assertEquals("one", clonedMap.get(1));
 assertEquals("two", clonedMap.get(2));
 }

 @Test
 public void testSerializeAndDeserialize() throws IOException, ClassNotFoundException {
 Integer originalInt = 1;
 Integer deserializedInt = null;

 // Serialize
 byte[] serializedInt = SerializationUtils.serialize(originalInt);

 // Deserialize
 deserializedInt = (Integer) SerializationUtils.deserialize(new ByteArrayInputStream(serializedInt));

 assertEquals(originalInt, deserializedInt);
 }

 @Test
 public void testSerializeAndDeserializeNullObject() throws IOException, ClassNotFoundException {
 Serializable nullObject = null;
 byte[] serializedNullObject = SerializationUtils.serialize(nullObject);
 Object deserializedNullObject = SerializationUtils.deserialize(new ByteArrayInputStream(serializedNullObject));

 assertNull(deserializedNullObject);
 }

 @Test(expected = SerializationException.class)
 public void testSerializationException() throws IOException {
 SerializationUtils.serialize(new Object() {
 @Override
 public String toString() {
 throw new RuntimeException();
 }
 });
 }

 @Test(expected = SerializationException.class)
 public void testDeserializeException() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Object deserializedObject = SerializationUtils.deserialize(inputStream);
 }

 @Test(expected = StreamCorruptedException.class)
 public void testStreamCorruptedException() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
 objectOutputStream.writeInt(1);
 objectOutputStream.close();

 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Object deserializedObject = SerializationUtils.deserialize(inputStream);
 }

}