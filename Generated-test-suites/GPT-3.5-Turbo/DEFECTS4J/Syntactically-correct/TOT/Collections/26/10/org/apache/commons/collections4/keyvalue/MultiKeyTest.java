import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

  private MultiKey<Integer> multiKey;

  @Before
  public void setUp() {
    multiKey = new MultiKey<>(1, 2, 3);
  }

  @Test
  public void testConstructorWithThreeKeys() {
    assertNotNull(multiKey);
  }

  @Test
  public void testGetKeys() {
    Integer[] expectedKeys = {1, 2, 3};
    assertArrayEquals(expectedKeys, multiKey.getKeys());
  }

  @Test
  public void testGetKeyAtIndex() {
    assertEquals(Integer.valueOf(1), multiKey.getKey(0));
    assertEquals(Integer.valueOf(2), multiKey.getKey(1));
    assertEquals(Integer.valueOf(3), multiKey.getKey(2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetKeyWithInvalidIndex() {
    multiKey.getKey(3);
  }

  @Test
  public void testSize() {
    assertEquals(3, multiKey.size());
  }

  @Test
  public void testEquals() {
    MultiKey<Integer> other = new MultiKey<>(1, 2, 3);
    assertTrue(multiKey.equals(other));
  }

  @Test
  public void testEqualsWithDifferentKeys() {
    MultiKey<Integer> other = new MultiKey<>(3, 2, 1);
    assertFalse(multiKey.equals(other));
  }

  @Test
  public void testEqualsWithDifferentSize() {
    MultiKey<Integer> other = new MultiKey<>(1, 2, 3, 4);
    assertFalse(multiKey.equals(other));
  }

  @Test
  public void testHashCode() {
    MultiKey<Integer> other = new MultiKey<>(1, 2, 3);
    assertEquals(multiKey.hashCode(), other.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
  }

  @Test
  public void testConstructorWithNullKeys() {
    try {
      MultiKey<Integer> nullKeys = new MultiKey<>(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }

  @Test
  public void testConstructorWithDifferentKeyTypes() {
    Object[] keys = {1, "two", 3.0};
    MultiKey<Object> multiKey = new MultiKey<>(keys);
    assertNotNull(multiKey);
  }

  @Test
  public void testConstructorWithModifiedKeys() {
    Integer key1 = 1;
    Integer key2 = 2;
    Integer[] keys = {key1, key2};
    MultiKey<Integer> multiKey = new MultiKey<>(keys);
    keys[0] = 3;
    assertEquals(Integer.valueOf(1), multiKey.getKey(0));
  }

  @Test
  public void testConstructorWithLargeNumberOfKeys() {
    Integer[] keys = new Integer[1000000];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = i + 1;
    }
    MultiKey<Integer> multiKey = new MultiKey<>(keys);
    assertNotNull(multiKey);
  }

  @Test
  public void testReadResolve() {
    MultiKey<Integer> other = (MultiKey<Integer>) multiKey.readResolve();
    assertEquals(multiKey.hashCode(), other.hashCode());
  }
}