import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

public class ValuedEnumTest {

  @Test
  public void testGetEnum() {
    try {
      Method method = ValuedEnum.class.getDeclaredMethod("getEnum", Class.class, int.class);
      method.setAccessible(true);
      
      // Test values
      Class enumClass = JavaVersionEnum.class;
      int value1 = 100;
      int value2 = 200;
      
      // Test getEnum method
      Enum enum1 = (Enum) method.invoke(null, enumClass, value1);
      Enum enum2 = (Enum) method.invoke(null, enumClass, value2);
      
      assertNotNull(enum1);
      assertNull(enum2);
      assertEquals("Java 1.0", enum1.getName());
    } catch (NoSuchMethodException e) {
      fail("Method getEnum not found");
    } catch (IllegalAccessException e) {
      fail("Illegal access to method getEnum");
    } catch (InvocationTargetException e) {
      fail("Error invoking method getEnum");
    }
  }

  @Test
  public void testGetValue() {
    JavaVersionEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
    JavaVersionEnum enum2 = new JavaVersionEnum("Java 2.0", 200);
    
    assertEquals(100, enum1.getValue());
    assertEquals(200, enum2.getValue());
  }

  @Test
  public void testCompareTo() {
    JavaVersionEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
    JavaVersionEnum enum2 = new JavaVersionEnum("Java 2.0", 200);
    
    assertTrue(enum1.compareTo(enum2) < 0);
  }

  @Test
  public void testToString() {
    JavaVersionEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
    
    assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
  }
}