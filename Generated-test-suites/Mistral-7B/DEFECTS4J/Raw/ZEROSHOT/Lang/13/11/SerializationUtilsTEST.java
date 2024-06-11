package org.apache.commons.lang3;

import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.Serializable;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializationUtilsTest {

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 @Test
 public void testClone() throws SerializationException, InstantiationException, IllegalAccessException {
 Map<String, Integer> original = new HashMap<>();
 original.put("test", 1);
 Map<String, Integer> cloned = SerializationUtils.clone(original);
 assertEquals(original, cloned);
 }

 @Test
 public void testSerializeDeserialize() throws SerializationException, IOException {
 Serializable original = new HashMap<String, Integer>() {{
 put("test", 1);
 }};
 ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
 SerializationUtils.serialize(original, byteOut);
 ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
 Object deserialized = SerializationUtils.deserialize(byteIn);
 assertEquals(original, deserialized);
 }

 @Test
 public void testSerializeNullInput() {
 assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, new ByteArrayOutputStream()));
 }

 @Test
 public void testDeserializeNullInput() {
 assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
 }

 @Test
 public void testCloneNullInput() {
 assertNull(SerializationUtils.clone(null));
 }

 @Test
 public void testSerializeException() {
 Serializable serializableObject = new Exception("Test Exception");
 try {
 SerializationUtils.serialize(serializableObject, new ByteArrayOutputStream());
 } catch (SerializationException e) {
 assertEquals("Failed to serialize: Test Exception", e.getMessage());
 }
 }
}