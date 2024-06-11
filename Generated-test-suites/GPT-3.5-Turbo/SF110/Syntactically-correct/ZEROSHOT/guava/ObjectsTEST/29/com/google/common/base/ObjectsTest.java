import org.junit.*;
import static org.junit.Assert.*;

public class ObjectsTest {
  
  // Testing equal method
  
  @Test
  public void testEqual_returnsTrue_whenObjectsAreSame() {
    Object obj = new Object();
    assertTrue(Objects.equal(obj, obj));
  }
  
  @Test
  public void testEqual_returnsTrue_whenObjectsAreEqual() {
    String str1 = "Hello";
    String str2 = new String("Hello");
    assertTrue(Objects.equal(str1, str2));
  }
  
  @Test
  public void testEqual_returnsFalse_whenObjectIsNull() {
    assertFalse(Objects.equal(null, new Object()));
  }
  
  @Test
  public void testEqual_returnsFalse_whenBothObjectsAreNull() {
    assertTrue(Objects.equal(null, null));
  }
  
  @Test
  public void testEqual_returnsFalse_whenObjectsAreNotEqual() {
    assertFalse(Objects.equal("Hello", "World"));
  }
  
  // Testing hashCode method
  
  @Test
  public void testHashCode_returnsSameValue_forSameObjects() {
    String str1 = "Hello";
    String str2 = "Hello";
    assertEquals(Objects.hashCode(str1, str2), Objects.hashCode(str2, str1));
  }
  
  @Test
  public void testHashCode_returnsDifferentValue_forDifferentObjects() {
    String str1 = "Hello";
    String str2 = "World";
    assertNotEquals(Objects.hashCode(str1), Objects.hashCode(str2));
  }
  
  @Test
  public void testHashCode_returnsSameValue_forEqualObjects() {
    String str1 = "Hello";
    String str2 = new String("Hello");
    assertEquals(Objects.hashCode(str1), Objects.hashCode(str2));
  }
  
  // Testing toStringHelper methods
  
  @Test
  public void testToStringHelper_returnsToString_withAddedValues() {
    Objects.ToStringHelper helper = Objects.toStringHelper("MyClass");
    helper.addValue("Hello")
          .add("name", "John")
          .add("age", 25);
    assertEquals("MyClass{Hello, name=John, age=25}", helper.toString());
  }
  
  @Test
  public void testToStringHelper_returnsToString_withoutNullValues() {
    Objects.ToStringHelper helper = Objects.toStringHelper("MyClass").omitNullValues();
    helper.add("name", "John")
          .add("age", null);
    assertEquals("MyClass{name=John}", helper.toString());
  }
  
  // Testing firstNonNull method
  
  @Test
  public void testFirstNonNull_returnsFirstObject_whenNotNull() {
    Object obj1 = new Object();
    Object obj2 = new Object();
    assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
  }
  
  @Test
  public void testFirstNonNull_returnsSecondObject_whenFirstIsNull() {
    Object obj2 = new Object();
    assertEquals(obj2, Objects.firstNonNull(null, obj2));
  }
  
  @Test
  public void testFirstNonNull_returnsNull_whenBothObjectsAreNull() {
    assertNull(Objects.firstNonNull(null, null));
  }
}