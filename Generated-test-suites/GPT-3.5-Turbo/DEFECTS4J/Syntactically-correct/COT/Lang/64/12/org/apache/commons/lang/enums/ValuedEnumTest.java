import static org.junit.Assert.*;
import org.junit.Test;

public class ValuedEnumTest {
  
  // Test getEnum method
  @Test
  public void testGetEnum() {
    ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
    assertEquals("Java 1.0", enum1.getName());
    assertEquals(100, enum1.getValue());
    
    ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
    assertEquals("Java 1.1", enum2.getName());
    assertEquals(110, enum2.getValue());
    
    ValuedEnum enum3 = JavaVersionEnum.getEnum("Java 1.4");
    assertNull(enum3);
  }
  
  // Test getValue method
  @Test
  public void testGetValue() {
    ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
    assertEquals(100, enum1.getValue());
    
    ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
    assertEquals(110, enum2.getValue());
  }
  
  // Test compareTo method
  @Test
  public void testCompareTo() {
    ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
    ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
    
    assertTrue(enum1.compareTo(enum2) < 0);
    assertTrue(enum2.compareTo(enum1) > 0);
    assertTrue(enum1.compareTo(enum1) == 0);
  }
  
  // Test toString method
  @Test
  public void testToString() {
    ValuedEnum enum1 = JavaVersionEnum.JAVA1_0;
    assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
    
    ValuedEnum enum2 = JavaVersionEnum.JAVA1_1;
    assertEquals("JavaVersion[Java 1.1=110]", enum2.toString());
  }
}