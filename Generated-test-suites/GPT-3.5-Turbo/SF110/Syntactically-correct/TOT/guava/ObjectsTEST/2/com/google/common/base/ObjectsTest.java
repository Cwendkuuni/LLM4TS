import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

  @Test
  public void testEqual() {
    // Typical use case - objects are equal
    assertTrue(Objects.equal("abc", "abc"));

    // Typical use case - objects are not equal
    assertFalse(Objects.equal("abc", "def"));

    // Edge case - both objects are null
    assertTrue(Objects.equal(null, null));

    // Edge case - one object is null and the other is not
    assertFalse(Objects.equal("abc", null));
    assertFalse(Objects.equal(null, "abc"));
  }

  @Test
  public void testHashCode() {
    // Typical use case - multiple objects
    assertEquals(Objects.hashCode("abc", 123, true), Objects.hashCode("abc", 123, true));

    // Typical use case - single object
    assertEquals(Objects.hashCode("abc"), Objects.hashCode("abc"));

    // Edge case - no objects
    assertEquals(Objects.hashCode(), Objects.hashCode());

    // Edge case - null object
    assertEquals(Objects.hashCode("abc", null), Objects.hashCode("abc", null));
  }

  @Test
  public void testToStringHelper() {
    // Typical use case - adding field and value
    assertEquals("Objects{field=value}", Objects.toStringHelper("Objects").add("field", "value").toString());

    // Edge case - adding null field
    assertEquals("Objects{null=value}", Objects.toStringHelper("Objects").add(null, "value").toString());

    // Edge case - adding null value
    assertEquals("Objects{field=null}", Objects.toStringHelper("Objects").add("field", null).toString());

    // Edge case - omitting null values
    assertEquals("Objects{field=value}", Objects.toStringHelper("Objects").add("field", "value").omitNullValues().toString());

    // Edge case - calling addValue method
    assertEquals("Objects{field}", Objects.toStringHelper("Objects").addValue("field").toString());
  }

  @Test
  public void testFirstNonNull() {
    // Typical use case - first object is not null
    assertEquals("abc", Objects.firstNonNull("abc", "def"));

    // Typical use case - first object is null
    assertEquals("def", Objects.firstNonNull(null, "def"));

    // Edge case - both objects are null
    assertNull(Objects.firstNonNull(null, null));
  }
}