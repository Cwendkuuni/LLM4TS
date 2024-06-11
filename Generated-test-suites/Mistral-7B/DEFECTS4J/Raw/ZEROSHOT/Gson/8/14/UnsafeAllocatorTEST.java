import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.Class;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

  @Test
  public void testNewInstance_Object() {
    Object obj = unsafeAllocator.newInstance(Object.class);
    assertNotNull(obj);
  }

  @Test
  public void testNewInstance_Serializable() {
    Class<MySerializable> clazz = MySerializable.class;
    MySerializable obj = (MySerializable) unsafeAllocator.newInstance(clazz);
    assertNotNull(obj);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_Interface() {
    Class<MyInterface> clazz = MyInterface.class;
    unsafeAllocator.newInstance(clazz);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstance_AbstractClass() {
    Class<MyAbstractClass> clazz = MyAbstractClass.class;
    unsafeAllocator.newInstance(clazz);
  }

  @Test
  public void testNewInstance_ThrowRuntimeException() {
    Class<MyThrowRuntimeException> clazz = MyThrowRuntimeException.class;
    assertThrows(RuntimeException.class, () -> unsafeAllocator.newInstance(clazz));
  }

  @Test
  public void testNewInstance_ThrowError() {
    Class<MyThrowError> clazz = MyThrowError.class;
    assertThrows(Error.class, () -> unsafeAllocator.newInstance(clazz));
  }

  @Test
  public void testNewInstance_ThrowCheckedException() {
    Class<MyCheckedIOException> clazz = MyCheckedIOException.class;
    assertThrows(IOException.class, () -> unsafeAllocator.newInstance(clazz));
  }

  @Test
  public void testNewInstance_ThrowUncheckedException() {
    Class<MyUncheckedException> clazz = MyUncheckedException.class;
    assertThrows(UncheckedException.class, () -> unsafeAllocator.newInstance(clazz));
  }

  @Test
  public void testNewInstance_PrivateConstructor() {
    Class<MyPrivateConstructor> clazz = MyPrivateConstructor.class;
    final Constructor<MyPrivateConstructor> constructor = clazz.getDeclaredConstructor();
    constructor.setAccessible(true);
    MyPrivateConstructor instance = constructor.newInstance();
    UnsafeAllocatorImpl unsafeAllocatorImpl = (UnsafeAllocatorImpl) this.unsafeAllocator;
    assertThrows(IllegalAccessError.class, () -> unsafeAllocatorImpl.newInstance(clazz));
  }

  @Test
  public void testNewInstance_NoDefaultConstructor() {
    Class<MyNoDefaultConstructor> clazz = MyNoDefaultConstructor.class;
    assertThrows(NoSuchMethodException.class, () -> clazz.getDeclaredConstructor());
    assertThrows(NoSuchMethodException.class, () -> clazz.getConstructors());
    assertThrows(NoSuchMethodException.class, () -> unsafeAllocator.newInstance(clazz));
  }

  @Test
  public void testNewInstance_SerializableWithCustomSerializer() {
    Class<MySerializableWithCustomSerializer> clazz = MySerializableWithCustomSerializer.class;
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(new ByteArrayOutputStream());
    MySerializableWithCustomSerializer obj = new MySerializableWithCustomSerializer();
    objectOutputStream.writeObject(obj);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    MySerializableWithCustomSerializer deserializedObj = (MySerializableWithCustomSerializer) objectInputStream.readObject();
    assertNotNull(deserializedObj);
    assertEquals(obj, deserializedObj);

    MySerializableWithCustomSerializer deserializedObjByUnsafeAllocator = (MySerializableWithCustomSerializer) unsafeAllocator.newInstance(clazz);
    assertEquals(obj, deserializedObjByUnsafeAllocator);
  }

  static class MyAbstractClass {}

  static interface MyInterface {}

  static class MySerializable implements Serializable {
    private static final long serialVersionUID = 1L;
  }

  static class MySerializableWithCustomSerializer implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int value;

    MySerializableWithCustomSerializer() {
      this.value = 1;
    }

    private Object readResolve() throws ObjectStreamException {
      return new MySerializableWithCustomSerializer();
    }
  }

  static class MyCheckedIOException extends IOException {}

  static class MyUncheckedException extends Exception {}

  static class MyThrowRuntimeException extends RuntimeException {}

  static class MyThrowError extends Error {}

  static class MyNoDefaultConstructor {
    private MyNoDefaultConstructor() {}
  }

  static class MyPrivateConstructor {
    private MyPrivateConstructor() {}

    private static final long serialVersionUID = 1L;
  }
}