import static org.junit.Assert.*;
import org.junit.*;

public class ObjectsTest {

  @Test
  public void testEqual_typicalCase() {
    Object a = new Object();
    Object b = new Object();
    boolean result = Objects.equal(a, b);
    assertFalse(result);
    result = Objects.equal(a, a);
    assertTrue(result);
  }

  @Test
  public void testEqual_nullCase() {
    Object a = null;
    Object b = null;
    boolean result = Objects.equal(a, b);
    assertTrue(result);
    a = new Object();
    result = Objects.equal(a, b);
    assertFalse(result);
  }

  @Test
  public void testEqual_mixCase() {
    Object a = new Object();
    Object b = null;
    boolean result = Objects.equal(a, b);
    assertFalse(result);
    a = null;
    result = Objects.equal(a, b);
    assertTrue(result);
  }

  @Test
  public void testHashCode_typicalCase() {
    Object[] objects = {1, 2, 3};
    int result = Objects.hashCode(objects);
    int expected = Arrays.hashCode(objects);
    assertEquals(expected, result);
  }

  @Test
  public void testHashCode_nullCase() {
    Object[] objects = null;
    int result = Objects.hashCode(objects);
    assertEquals(0, result);
  }

  @Test
  public void testToStringHelper_typicalCase() {
    Object obj = new Object();
    Objects.ToStringHelper helper = Objects.toStringHelper(obj);
    helper.add("name", "John");
    helper.add("age", 30);
    String result = helper.toString();
    String expected = obj.getClass().getSimpleName() + "{name=John, age=30}";
    assertEquals(expected, result);
  }

  @Test
  public void testToStringHelper_nullCase() {
    Objects.ToStringHelper helper = Objects.toStringHelper((Object) null);
    helper.add("name", "John");
    helper.add("age", 30);
    String result = helper.toString();
    String expected = "null{name=John, age=30}";
    assertEquals(expected, result);
  }

  @Test
  public void testFirstNonNull_typicalCase() {
    String first = "Hello";
    String second = "World";
    String result = Objects.firstNonNull(first, second);
    assertEquals(first, result);
  }

  @Test
  public void testFirstNonNull_nullCase() {
    String first = null;
    String second = "World";
    String result = Objects.firstNonNull(first, second);
    assertEquals(second, result);
  }

}