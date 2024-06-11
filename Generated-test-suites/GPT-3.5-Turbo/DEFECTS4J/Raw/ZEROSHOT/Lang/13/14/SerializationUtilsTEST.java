import org.junit.Test;
import static org.junit.Assert.*;

public class SerializationUtilsTest {

  @Test
  public void testClone_nullObject_returnsNull() {
    Object obj = null;
    Object cloneObj = SerializationUtils.clone(obj);
    assertNull(cloneObj);
  }

  @Test
  public void testClone_serializableObject_clonesObject() {
    TestClass obj = new TestClass("test");
    TestClass cloneObj = SerializationUtils.clone(obj);
    assertEquals(obj, cloneObj);
    assertNotSame(obj, cloneObj);
  }

  @Test
  public void testClone_deserializationFails_throwsSerializationException() {
    TestClass obj = new TestClass("test");
    byte[] objData = SerializationUtils.serialize(obj);
    objData[0] = (byte) (objData[0] + 1); // Corrupting the serialized data
    assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(objData));
  }

  @Test
  public void testSerialize_nullObject_throwsIllegalArgumentException() {
    OutputStream outputStream = new ByteArrayOutputStream();
    assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null, outputStream));
  }

  @Test
  public void testSerialize_serializableObject_serializesObject() {
    TestClass obj = new TestClass("test");
    OutputStream outputStream = new ByteArrayOutputStream();
    SerializationUtils.serialize(obj, outputStream);
    byte[] objData = ((ByteArrayOutputStream) outputStream).toByteArray();
    assertNotNull(objData);
    assertTrue(objData.length > 0);
  }

  @Test
  public void testDeserialize_nullInputStream_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
  }

  @Test
  public void testDeserialize_serializedObject_deserializesObject() {
    TestClass obj = new TestClass("test");
    byte[] objData = SerializationUtils.serialize(obj);
    Object deserializedObj = SerializationUtils.deserialize(objData);
    assertNotNull(deserializedObj);
    assertEquals(obj, deserializedObj);
  }

  private static class TestClass implements Serializable {
    private String name;

    public TestClass(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      TestClass other = (TestClass) obj;
      return name.equals(other.name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }
  }
}