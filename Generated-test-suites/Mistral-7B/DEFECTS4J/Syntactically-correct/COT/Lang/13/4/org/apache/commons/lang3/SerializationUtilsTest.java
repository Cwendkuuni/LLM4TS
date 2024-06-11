import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.SerializationException;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

 private Serializable objectToSerialize;
 private byte[] serializedObject;

 @Before
 public void setUp() {
 objectToSerialize = new HashMap<>();
 serializedObject = SerializationUtils.serialize(objectToSerialize);
 }

 @After
 public void tearDown() {
 // Add any tear down logic here
 }

 // Test for clone method
 @Test
 public void testClone() {
 @SuppressWarnings("unchecked") // We know the type of the object
 HashMap<String, String> clonedObject = (HashMap<String, String>) SerializationUtils.clone(objectToSerialize);
 assertEquals("The cloned object should be equal to the original.", objectToSerialize, clonedObject);
 }

 // Test for serialize method with OutputStream
 @Test
 public void testSerializeWithOutputStream() throws IOException {
 OutputStream outputStream = new ByteArrayOutputStream();
 SerializationUtils.serialize(objectToSerialize, outputStream);
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 @SuppressWarnings("unchecked") // We know the type of the object
 HashMap<String, String> deserializedObject = (HashMap<String, String>) SerializationUtils.deserialize(inputStream);
 assertEquals("The deserialized object should be equal to the original.", objectToSerialize, deserializedObject);
 }

 // Test for serialize method with byte[]
 @Test
 public void testSerializeWithByteArray() throws IOException, SerializationException {
 HashMap<String, String> deserializedObject = (HashMap<String, String>) SerializationUtils.deserialize(serializedObject);
 assertEquals("The deserialized object should be equal to the original.", objectToSerialize, deserializedObject);
 }

 // Test for deserialize method with InputStream
 @Test
 public void testDeserializeWithInputStream() throws IOException, SerializationException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedObject);
 @SuppressWarnings("unchecked") // We know the type of the object
 HashMap<String, String> deserializedObject = (HashMap<String, String>) SerializationUtils.deserialize(inputStream);
 assertEquals("The deserialized object should be equal to the original.", objectToSerialize, deserializedObject);
 }

 // Test for deserialize method with null InputStream
 @Test(expected = IllegalArgumentException.class)
 public void testDeserializeWithNullInputStream() {
 SerializationUtils.deserialize(null);
 }

 // Test for serialize method with null OutputStream
 @Test(expected = IllegalArgumentException.class)
 public void testSerializeWithNullOutputStream() {
 SerializationUtils.serialize(null, null);
 }

 // Test for clone method with null object
 @Test(expected = NullPointerException.class)
 public void testCloneWithNullObject() {
 SerializationUtils.clone(null);
 }

 // Test for serialize method with null object
 @Test(expected = NullPointerException.class)
 public void testSerializeWithNullObject() {
 SerializationUtils.serialize(null, new ByteArrayOutputStream());
 }

 // Test for deserialize method with null byte[]
 @Test(expected = NullPointerException.class)
 public void testDeserializeWithNullByteArray() {
 SerializationUtils.deserialize(null);
 }
}