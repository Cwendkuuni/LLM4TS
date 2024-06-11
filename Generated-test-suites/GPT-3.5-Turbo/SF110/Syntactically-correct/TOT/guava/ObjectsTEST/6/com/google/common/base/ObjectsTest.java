import com.google.common.base.Objects;
import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {

  @Test
  public void testEqual() {
    // Typical use case
    assertTrue(Objects.equal("Hello", "Hello"));

    // Edge cases
    assertFalse(Objects.equal(null, "Hello"));
    assertFalse(Objects.equal("Hello", null));
    assertTrue(Objects.equal(null, null));
  }

  @Test
  public void testHashCode() {
    // Typical use case
    Object obj1 = new Object();
    Object obj2 = new Object();
    int hashCode1 = obj1.hashCode();
    int hashCode2 = obj2.hashCode();
    assertEquals(hashCode1 ^ hashCode2, Objects.hashCode(obj1, obj2));

    // Edge cases
    int hashCode3 = obj1.hashCode();
    int hashCode4 = 0;
    assertEquals(hashCode3 ^ hashCode4, Objects.hashCode(obj1, null));
    assertEquals(hashCode4 ^ hashCode3, Objects.hashCode(null, obj1));

    // Error scenarios
    assertEquals(0, Objects.hashCode());
  }

  @Test
  public void testToStringHelper() {
    // Typical use case
    Object obj = new Object();
    assertEquals("ObjectsTest$ToStringHelper{className}", Objects.toStringHelper(obj).toString());

    // Edge cases
    assertEquals("ObjectsTest$ToStringHelper{className}", Objects.toStringHelper(ObjectsTest.ToStringHelper.class).toString());
    assertEquals("ObjectsTest$ToStringHelper{className}", Objects.toStringHelper("ObjectsTest.ToStringHelper").toString());
  }

  @Test
  public void testFirstNonNull() {
    // Typical use case
    Integer num1 = 5;
    Integer num2 = 10;
    assertEquals(num1, Objects.firstNonNull(num1, num2));

    // Edge cases
    Integer num3 = null;
    assertEquals(num2, Objects.firstNonNull(num3, num2));
    assertEquals(num2, Objects.firstNonNull(num2, num3));

    // Error scenarios
    assertNull(Objects.firstNonNull());
  }
}