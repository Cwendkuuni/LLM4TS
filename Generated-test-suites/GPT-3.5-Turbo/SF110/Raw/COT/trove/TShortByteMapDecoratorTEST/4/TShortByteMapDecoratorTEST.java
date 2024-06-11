import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;

public class TShortByteMapDecoratorTest {

  private TShortByteMapDecorator decorator = new TShortByteMapDecorator();

  @Test
  public void putAndGetTest() {
    Short key = 1;
    Byte value = 100;
    decorator.put(key, value);
    assertEquals(value, decorator.get(key));
  }

  @Test
  public void putAndGetNullKeyTest() {
    Byte value = 100;
    decorator.put(null, value);
    assertEquals(value, decorator.get(null));
  }

  @Test
  public void putAndGetNullValueTest() {
    Short key = 1;
    decorator.put(key, null);
    assertNull(decorator.get(key));
  }

  @Test
  public void removeTest() {
    Short key = 1;
    Byte value = 100;
    decorator.put(key, value);
    Byte removedValue = decorator.remove(key);
    assertEquals(value, removedValue);
  }

  @Test
  public void removeNullKeyTest() {
    Short key = null;
    decorator.put(key, (byte) 100);
    Byte removedValue = decorator.remove(key);
    assertNull(removedValue);
  }

  @Test
  public void containsValueTest() {
    Byte value = 100;
    decorator.put((short) 1, value);
    assertTrue(decorator.containsValue(value));
  }

  @Test
  public void containsKeyTest() {
    Short key = 1;
    decorator.put(key, (byte) 100);
    assertTrue(decorator.containsKey(key));
  }

  @Test
  public void containsNullKeyTest() {
    decorator.put(null, (byte) 100);
    assertTrue(decorator.containsKey(null));
  }

  @Test
  public void sizeTest() {
    decorator.put((short) 1, (byte) 100);
    decorator.put((short) 2, (byte) 200);
    assertEquals(2, decorator.size());
  }

  @Test
  public void isEmptyTest() {
    assertTrue(decorator.isEmpty());
    decorator.put((short) 1, (byte) 100);
    assertFalse(decorator.isEmpty());
  }
}
