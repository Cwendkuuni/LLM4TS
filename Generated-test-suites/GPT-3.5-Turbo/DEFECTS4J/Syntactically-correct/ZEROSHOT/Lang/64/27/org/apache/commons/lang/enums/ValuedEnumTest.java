import org.junit.Test;
import java.util.List;
import java.util.Map;

public class ValuedEnumTest {

  @Test
  public void testConstructor() {
    ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
    assertEquals("Java 1.0", enum1.getName());
    assertEquals(100, enum1.getValue());
  }

  @Test
  public void testGetEnumByValue() {
    ValuedEnum enum1 = JavaVersionEnum.getEnum(100);
    assertEquals(JavaVersionEnum.JAVA1_0, enum1);
    
    ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
    assertEquals(JavaVersionEnum.JAVA1_1, enum2);
  }

  @Test
  public void testGetEnumByName() {
    ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
    assertEquals(JavaVersionEnum.JAVA1_0, enum1);
    
    ValuedEnum enum2 = JavaVersionEnum.getEnum("Java 1.1");
    assertEquals(JavaVersionEnum.JAVA1_1, enum2);
  }

  @Test
  public void testGetEnumMap() {
    Map enumMap = JavaVersionEnum.getEnumMap();
    assertEquals(4, enumMap.size());
    assertTrue(enumMap.containsKey("Java 1.0"));
    assertTrue(enumMap.containsKey("Java 1.1"));
    assertTrue(enumMap.containsKey("Java 1.2"));
    assertTrue(enumMap.containsKey("Java 1.3"));
  }

  @Test
  public void testGetEnumList() {
    List enumList = JavaVersionEnum.getEnumList();
    assertEquals(4, enumList.size());
    assertTrue(enumList.contains(JavaVersionEnum.JAVA1_0));
    assertTrue(enumList.contains(JavaVersionEnum.JAVA1_1));
    assertTrue(enumList.contains(JavaVersionEnum.JAVA1_2));
    assertTrue(enumList.contains(JavaVersionEnum.JAVA1_3));
  }

  @Test
  public void testIterator() {
    Iterator iterator = JavaVersionEnum.iterator();
    assertEquals(JavaVersionEnum.JAVA1_0, iterator.next());
    assertEquals(JavaVersionEnum.JAVA1_1, iterator.next());
    assertEquals(JavaVersionEnum.JAVA1_2, iterator.next());
    assertEquals(JavaVersionEnum.JAVA1_3, iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testCompareTo() {
    assertEquals(0, JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_0));
    assertTrue(JavaVersionEnum.JAVA1_0.compareTo(JavaVersionEnum.JAVA1_1) < 0);
    assertTrue(JavaVersionEnum.JAVA1_1.compareTo(JavaVersionEnum.JAVA1_0) > 0);
  }

  @Test
  public void testToString() {
    assertEquals("JavaVersion[Java 1.0=100]", JavaVersionEnum.JAVA1_0.toString());
    assertEquals("JavaVersion[Java 1.1=110]", JavaVersionEnum.JAVA1_1.toString());
  }

}