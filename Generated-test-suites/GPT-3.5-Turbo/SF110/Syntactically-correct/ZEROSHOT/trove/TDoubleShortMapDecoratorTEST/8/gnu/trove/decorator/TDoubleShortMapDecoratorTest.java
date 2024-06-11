import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {

  private TDoubleShortMapDecorator decorator;

  @Before
  public void setUp() {
    decorator = new TDoubleShortMapDecorator();
  }

  @Test
  public void testPutAndGet() {
    assertEquals(decorator.put(1.0, (short) 1), null);
    assertEquals(decorator.get(1.0), new Short((short) 1));
  }

  @Test
  public void testClear() {
    decorator.put(1.0, (short) 1);
    decorator.put(2.0, (short) 2);
    decorator.clear();
    assertEquals(decorator.size(), 0);
  }

  @Test
  public void testRemove() {
    decorator.put(1.0, (short) 1);
    assertEquals(decorator.remove(1.0), new Short((short) 1));
    assertEquals(decorator.size(), 0);
  }

  @Test
  public void testContainsValue() {
    decorator.put(1.0, (short) 1);
    assertTrue(decorator.containsValue((short) 1));
    assertFalse(decorator.containsValue((short) 2));
  }

  @Test
  public void testContainsKey() {
    decorator.put(1.0, (short) 1);
    assertTrue(decorator.containsKey(1.0));
    assertFalse(decorator.containsKey(2.0));
  }

  @Test
  public void testSize() {
    assertEquals(decorator.size(), 0);
    decorator.put(1.0, (short) 1);
    decorator.put(2.0, (short) 2);
    decorator.put(3.0, (short) 3);
    assertEquals(decorator.size(), 3);
  }

  @Test
  public void testIsEmpty() {
    assertTrue(decorator.isEmpty());
    decorator.put(1.0, (short) 1);
    assertFalse(decorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    Map<Double, Short> map = new HashMap<>();
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    decorator.putAll(map);
    assertTrue(decorator.containsKey(1.0));
    assertTrue(decorator.containsKey(2.0));
  }
}