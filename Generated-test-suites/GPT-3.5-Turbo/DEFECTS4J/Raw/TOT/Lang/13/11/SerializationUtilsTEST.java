import static org.junit.Assert.*;

import org.junit.Test;

public class SerializationUtilsTest {

  @Test
  public void testClone_WithNullObject_ReturnNull() {
    assertNull(SerializationUtils.clone(null));
  }

  @Test
  public void testClone_WithSerializableObject_ReturnClonedObject() {
    DummyObject dummy = new DummyObject();
    dummy.setName("Test");
    DummyObject clonedDummy = SerializationUtils.clone(dummy);
    assertEquals(dummy.getName(), clonedDummy.getName());
  }

  @Test
  public void testClone_WithNonSerializableObject_ThrowsSerializationException() {
    NonSerializableObject nonSerializable = new NonSerializableObject();
    assertThrows(SerializationException.class, () -> SerializationUtils.clone(nonSerializable));
  }

  @Test
  public void testSerialize_WithNullObject_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SerializationUtils.serialize(null));
  }

  @Test
  public void testSerialize_WithSerializableObject_ReturnSerializedByteArray() {
    DummyObject dummy = new DummyObject();
    dummy.setName("Test");
    byte[] serialized = SerializationUtils.serialize(dummy);
    assertNotNull(serialized);
  }

  @Test
  public void testDeserialize_WithNullInputStream_ThrowsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> SerializationUtils.deserialize(null));
  }

  @Test
  public void testDeserialize_WithInvalidSerializedByteArray_ThrowsSerializationException() {
    byte[] invalidSerialized = new byte[] { 1, 2, 3 };
    assertThrows(SerializationException.class, () -> SerializationUtils.deserialize(invalidSerialized));
  }

  @Test
  public void testDeserialize_WithValidSerializedByteArray_ReturnDeserializedObject() {
    DummyObject dummy = new DummyObject();
    dummy.setName("Test");
    byte[] serialized = SerializationUtils.serialize(dummy);
    DummyObject deserializedDummy = (DummyObject) SerializationUtils.deserialize(serialized);
    assertEquals(dummy.getName(), deserializedDummy.getName());
  }

  private class DummyObject implements Serializable {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  private class NonSerializableObject {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}