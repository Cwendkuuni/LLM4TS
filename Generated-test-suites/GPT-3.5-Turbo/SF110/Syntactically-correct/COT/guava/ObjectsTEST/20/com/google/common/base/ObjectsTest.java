
import static org.junit.Assert.*;
import org.junit.Test;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ObjectsTest {

  @Test
  public void testEqual() {
    // Testing when both objects are null
    assertTrue(Objects.equal(null, null));
  
    // Testing when one object is null and the other is not
    assertFalse(Objects.equal(null, new Object()));
    assertFalse(Objects.equal(new Object(), null));
    
    // Testing when both objects are equal
    Object obj1 = new Object();
    Object obj2 = obj1;
    assertTrue(Objects.equal(obj1, obj2));
    
    // Testing when both objects are not equal
    Object obj3 = new Object();
    assertFalse(Objects.equal(obj1, obj3));
  }
  
  @Test
  public void testHashCode() {
    // Testing when no objects are provided
    assertEquals(0, Objects.hashCode());
    
    // Testing when one object is provided
    Object obj = new Object();
    assertEquals(Objects.hashCode(obj), obj.hashCode());
    
    // Testing when multiple objects are provided
    Object obj1 = new Object();
    Object obj2 = new Object();
    assertEquals(Objects.hashCode(obj1, obj2), Arrays.hashCode(new Object[]{obj1, obj2}));
  }
  
  @Test
  public void testToStringHelper() {
    // Testing toStringHelper for Object self
    Object obj = new Object();
    Objects.ToStringHelper helper = Objects.toStringHelper(obj);
    assertNotNull(helper);
    assertTrue(helper.toString().startsWith(obj.getClass().getSimpleName()));
    
    // Testing toStringHelper for Class<?> clazz
    Class<?> clazz = Objects.class;
    helper = Objects.toStringHelper(clazz);
    assertNotNull(helper);
    assertTrue(helper.toString().startsWith(clazz.getSimpleName()));
    
    // Testing toStringHelper for String className
    String className = "SomeClass";
    helper = Objects.toStringHelper(className);
    assertNotNull(helper);
    assertTrue(helper.toString().startsWith(className));
  }
  
  @Test
  public void testFirstNonNull() {
    // Testing when both objects are null
    assertNull(Objects.firstNonNull(null, null));
    
    // Testing when the first object is null
    Object obj = new Object();
    assertEquals(obj, Objects.firstNonNull(null, obj));
    
    // Testing when the second object is null
    assertEquals(obj, Objects.firstNonNull(obj, null));
    
    // Testing when neither object is null
    Object obj1 = new Object();
    Object obj2 = new Object();
    assertEquals(obj1, Objects.firstNonNull(obj1, obj2));
  }
  
  @Test
  public void testToStringHelper_omitNullValues() {
    Objects.ToStringHelper helper = Objects.toStringHelper("SomeClass").omitNullValues();
    assertNotNull(helper);
    assertEquals(helper.toString(), "SomeClass{}");
  }
  
  @Test
  public void testToStringHelper_add() {
    Objects.ToStringHelper helper = Objects.toStringHelper("SomeClass");
    
    helper.add("name", "John")
          .add("age", 30);
    
    assertEquals(helper.toString(), "SomeClass{name=John, age=30}");
  }
  
  @Test
  public void testToStringHelper_addValue() {
    Objects.ToStringHelper helper = Objects.toStringHelper("SomeClass");
    
    helper.addValue("John")
          .addValue(30);
    
    assertEquals(helper.toString(), "SomeClass{John, 30}");
  }
  
}
