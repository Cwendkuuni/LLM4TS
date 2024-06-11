import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {
  
  @Test
  public void testGetName() {
    JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
    assertEquals("Java 1.0", enum1.getName());
  }
  
  @Test
  public void testGetValue() {
    JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
    assertEquals(100, enum1.getValue());
  }
  
  @Test
  public void testGetEnum() {
    JavaVersionEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
    assertEquals(JavaVersionEnum.JAVA1_0, enum1);
    
    JavaVersionEnum enum2 = JavaVersionEnum.getEnum(100);
    assertEquals(JavaVersionEnum.JAVA1_0, enum2);
    
    JavaVersionEnum enum3 = JavaVersionEnum.getEnum("Invalid Enum");
    assertNull(enum3);
    
    JavaVersionEnum enum4 = JavaVersionEnum.getEnum(999);
    assertNull(enum4);
  }
  
  @Test
  public void testGetEnumMap() {
    assertNotNull(JavaVersionEnum.getEnumMap());
  }
  
  @Test
  public void testGetEnumList() {
    assertNotNull(JavaVersionEnum.getEnumList());
  }
  
  @Test
  public void testIterator() {
    assertNotNull(JavaVersionEnum.iterator());
  }
  
  @Test
  public void testCompareTo() {
    JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
    JavaVersionEnum enum2 = JavaVersionEnum.JAVA1_1;
    
    assertTrue(enum1.compareTo(enum1) == 0);
    assertTrue(enum1.compareTo(enum2) < 0);
    assertTrue(enum2.compareTo(enum1) > 0);
  }
  
  @Test
  public void testToString() {
    JavaVersionEnum enum1 = JavaVersionEnum.JAVA1_0;
    assertEquals("JavaVersion[Java 1.0=100]", enum1.toString());
  }
}