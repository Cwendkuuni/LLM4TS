
import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {

  @Test
  public void testEqual() {
    // typical use case
    assertTrue(Objects.equal("abc", "abc"));
    // edge case
    assertTrue(Objects.equal(null, null));
    assertFalse(Objects.equal(null, "abc"));
    assertFalse(Objects.equal("abc", null));
    assertFalse(Objects.equal("abc", "def"));
  }

  @Test
  public void testHashCode() {
    // typical use case
    int hash1 = Objects.hashCode("abc", 123);
    int hash2 = Objects.hashCode("abc");
    int hash3 = Objects.hashCode();
    assertEquals(hash1, Objects.hashCode(123, "abc"));
    // edge case
    assertEquals(hash2, Objects.hashCode("abc"));
    assertEquals(1, hash3);
  }

  @Test
  public void testToStringHelper() {
    // typical use case
    String expected = "TestObject{name='abc', age=20}";
    TestObject obj = new TestObject("abc", 20);
    assertEquals(expected, obj.toString());
    // edge case
    expected = "TestObject{name=null, age=0}";
    obj = new TestObject(null, 0);
    assertEquals(expected, obj.toString());
  }

  @Test
  public void testFirstNonNull() {
    // typical use case
    Integer result = Objects.firstNonNull(null, 20);
    assertEquals(Integer.valueOf(20), result);
    // edge case
    result = Objects.firstNonNull(null, null);
    assertNull(result);
  }

  class TestObject {
    private String name;
    private int age;

    public TestObject(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
          .add("name", name)
          .add("age", age)
          .omitNullValues()
          .toString();
    }
  }
}

