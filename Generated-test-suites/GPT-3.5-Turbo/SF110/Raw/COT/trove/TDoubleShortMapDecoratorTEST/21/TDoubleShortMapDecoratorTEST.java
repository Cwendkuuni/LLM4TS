import gnu.trove.map.*;
import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

  @Test
  public void testPut() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertTrue(decorator.containsKey(1.0));
    assertTrue(decorator.containsValue((short)1));
  }

  @Test
  public void testGet() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertEquals(1, decorator.get(1.0).intValue());
  }

  @Test
  public void testClear() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    decorator.clear();

    assertEquals(0, decorator.size());
    assertFalse(decorator.containsKey(1.0));
    assertFalse(decorator.containsValue((short)1));
  }

  @Test
  public void testRemove() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    decorator.remove(1.0);

    assertFalse(decorator.containsKey(1.0));
    assertFalse(decorator.containsValue((short)1));
  }

  // Additional test cases for edge cases and exception handling scenarios

  @Test
  public void testPutNullKey() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(null, (short)1);

    assertTrue(decorator.containsKey(null));
    assertTrue(decorator.containsValue((short)1));
  }

  @Test
  public void testPutNullValue() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, null);

    assertTrue(decorator.containsKey(1.0));
    assertNull(decorator.get(1.0));
  }

  @Test
  public void testGetNullKey() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertNull(decorator.get(null));
  }

  @Test
  public void testRemoveNullKey() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertNull(decorator.remove(null));
  }

  @Test
  public void testContainsValue() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertTrue(decorator.containsValue((short)1));
    assertFalse(decorator.containsValue((short)2));
  }

  @Test
  public void testContainsKey() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);

    assertTrue(decorator.containsKey(1.0));
    assertFalse(decorator.containsKey(2.0));
  }

  @Test
  public void testSize() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    decorator.put(1.0, (short)1);
    decorator.put(2.0, (short)2);

    assertEquals(2, decorator.size());
  }

  @Test
  public void testIsEmpty() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

    assertTrue(decorator.isEmpty());

    decorator.put(1.0, (short)1);

    assertFalse(decorator.isEmpty());

    decorator.clear();

    assertTrue(decorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
    Map<Double, Short> map = new HashMap<>();
    map.put(1.0, (short)1);
    map.put(2.0, (short)2);

    decorator.putAll(map);

    assertTrue(decorator.containsKey(1.0));
    assertTrue(decorator.containsKey(2.0));
  }

  @Test
  public void testWrapAndUnwrap() {
    TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

    assertEquals(1.0, decorator.unwrapKey(decorator.wrapKey(1.0)), 0.0001);
    assertEquals(1, decorator.unwrapValue(decorator.wrapValue((short)1)));
  }

  @Test
  public void testExternalizable() throws IOException, ClassNotFoundException {
    TDoubleShortMapDecorator decorator1 = new TDoubleShortMapDecorator();
    decorator1.put(1.0, (short)1);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    decorator1.writeExternal(objectOutputStream);
    objectOutputStream.flush();
    objectOutputStream.close();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
    decorator2.readExternal(objectInputStream);
    objectInputStream.close();

    assertTrue(decorator2.containsKey(1.0));
    assertTrue(decorator2.containsValue((short)1));
  }
}