import org.junit.Test;
import org.junit.Assert;
import java.io.*;
import org.apache.commons.lang3.SerializationUtils.*;

public class SerializationUtilsTest {

  // Test case for clone()
  @Test
  public void testClone() {
    // Typical use case
    MyClass object = new MyClass(1, "abc");
    MyClass clonedObject = SerializationUtils.clone(object);
    Assert.assertEquals(object, clonedObject);

    // Edge case: Null object
    MyClass nullObject = null;
    MyClass clonedNullObject = SerializationUtils.clone(nullObject);
    Assert.assertNull(clonedNullObject);

    // Edge case: Empty object
    MyClass emptyObject = new MyClass();
    MyClass clonedEmptyObject = SerializationUtils.clone(emptyObject);
    Assert.assertEquals(emptyObject, clonedEmptyObject);
  }

  // Test case for serialize(Serializable obj, OutputStream outputStream)
  @Test
  public void testSerializeWithOutputStream() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);

    // Typical use case
    MyClass object = new MyClass(1, "abc");
    SerializationUtils.serialize(object, oos);

    // Check if object is serialized correctly
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    MyClass deserializedObject = (MyClass) ois.readObject();
    Assert.assertEquals(object, deserializedObject);

    oos.close();
    ois.close();

    // Edge case: Null object
    MyClass nullObject = null;
    ByteArrayOutputStream nullBaos = new ByteArrayOutputStream();
    ObjectOutputStream nullOos = new ObjectOutputStream(nullBaos);
    SerializationUtils.serialize(nullObject, nullOos);
    Assert.assertEquals(0, nullBaos.toByteArray().length);

    nullOos.close();

    // Edge case: Empty object
    MyClass emptyObject = new MyClass();
    ByteArrayOutputStream emptyBaos = new ByteArrayOutputStream();
    ObjectOutputStream emptyOos = new ObjectOutputStream(emptyBaos);
    SerializationUtils.serialize(emptyObject, emptyOos);
    Assert.assertEquals(0, emptyBaos.toByteArray().length);

    emptyOos.close();
  }

  // Test case for serialize(Serializable obj)
  @Test
  public void testSerialize() {
    // Typical use case
    MyClass object = new MyClass(1, "abc");
    byte[] serializedObject = SerializationUtils.serialize(object);

    // Check if object is serialized correctly
    ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
    ObjectInputStream ois = new ObjectInputStream(bais);
    MyClass deserializedObject = (MyClass) ois.readObject();
    Assert.assertEquals(object, deserializedObject);

    ois.close();

    // Edge case: Null object
    MyClass nullObject = null;
    byte[] serializedNullObject = SerializationUtils.serialize(nullObject);
    Assert.assertEquals(0, serializedNullObject.length);

    // Edge case: Empty object
    MyClass emptyObject = new MyClass();
    byte[] serializedEmptyObject = SerializationUtils.serialize(emptyObject);
    Assert.assertEquals(0, serializedEmptyObject.length);
  }

  // Test case for deserialize(InputStream inputStream)
  @Test
  public void testDeserializeWithInputStream() throws IOException {
    // Typical use case
    MyClass object = new MyClass(1, "abc");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(object);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    MyClass deserializedObject = (MyClass) SerializationUtils.deserialize(bais);
    Assert.assertEquals(object, deserializedObject);

    oos.close();

    // Edge case: Null input stream
    InputStream nullInputStream = null;
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      SerializationUtils.deserialize(nullInputStream);
    });

    // Edge case: Empty input stream
    InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
    Assert.assertThrows(EOFException.class, () -> {
      SerializationUtils.deserialize(emptyInputStream);
    });
  }

  // Test case for deserialize(byte[] objectData)
  @Test
  public void testDeserialize() {
    // Typical use case
    MyClass object = new MyClass(1, "abc");
    byte[] serializedObject = SerializationUtils.serialize(object);
    MyClass deserializedObject = (MyClass) SerializationUtils.deserialize(serializedObject);
    Assert.assertEquals(object, deserializedObject);

    // Edge case: Null objectData
    byte[] serializedNullObject = null;
    Assert.assertThrows(IllegalArgumentException.class, () -> {
      SerializationUtils.deserialize(serializedNullObject);
    });

    // Edge case: Empty objectData
    byte[] serializedEmptyObject = new byte[0];
    Assert.assertThrows(EOFException.class, () -> {
      SerializationUtils.deserialize(serializedEmptyObject);
    });
  }

  // Test case for ClassLoaderAwareObjectInputStream
  @Test
  public void testCustomObjectInputStream() throws IOException, ClassNotFoundException {
    MyClass object = new MyClass(1, "abc");
    byte[] serializedObject = SerializationUtils.serialize(object);

    ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);

    // Use a custom ClassLoader that cannot resolve the class
    ClassLoader unableClassLoader = new UnableClassLoader();

    ClassLoaderAwareObjectInputStream ois = new ClassLoaderAwareObjectInputStream(bais, unableClassLoader);
    Assert.assertThrows(ClassNotFoundException.class, () -> {
      ois.readObject();
    });

    ois.close();
  }

  // Custom class for serialization testing
  public static class MyClass implements Serializable {
    private int id;
    private String name;

    public MyClass() {
      this.id = 0;
      this.name = "";
    }

    public MyClass(int id, String name) {
      this.id = id;
      this.name = name;
    }

    // hashCode(), equals(), and toString() methods
  }

  // Custom class loader that cannot resolve any class
  public static class UnableClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      throw new ClassNotFoundException();
    }
  }

}