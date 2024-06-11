package org.apache.commons.lang.enums;
import org.junit.Test;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ValuedEnumTest {

  @Test
  public void testConstructor() {
    ValuedEnum valuedEnum = new TestValuedEnum("TestEnum", 123);
    assertNotNull(valuedEnum);
    assertEquals("TestEnum", valuedEnum.getName());
    assertEquals(123, valuedEnum.getValue());
  }

  @Test
  public void testGetEnumByValue() {
    ValuedEnum valuedEnum = TestValuedEnum.getEnum(123);
    assertNotNull(valuedEnum);
    assertEquals("TestEnum", valuedEnum.getName());
    assertEquals(123, valuedEnum.getValue());
  }

  @Test
  public void testGetEnumByInvalidValue() {
    ValuedEnum valuedEnum = TestValuedEnum.getEnum(456);
    assertNull(valuedEnum);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEnumWithNullClass() {
    TestValuedEnum.getEnum(null, 123);
  }

  @Test
  public void testGetEnumMap() {
    Map<String, TestValuedEnum> enumMap = TestValuedEnum.getEnumMap();
    assertNotNull(enumMap);
    assertEquals(3, enumMap.size());
    assertTrue(enumMap.containsKey("ENUM1"));
    assertTrue(enumMap.containsKey("ENUM2"));
    assertTrue(enumMap.containsKey("ENUM3"));
    assertEquals(100, enumMap.get("ENUM1").getValue());
    assertEquals(200, enumMap.get("ENUM2").getValue());
    assertEquals(300, enumMap.get("ENUM3").getValue());
  }

  @Test
  public void testGetEnumList() {
    List<TestValuedEnum> enumList = TestValuedEnum.getEnumList();
    assertNotNull(enumList);
    assertEquals(3, enumList.size());
    assertEquals("ENUM1", enumList.get(0).getName());
    assertEquals("ENUM2", enumList.get(1).getName());
    assertEquals("ENUM3", enumList.get(2).getName());
  }

  @Test
  public void testIterator() {
    Iterator<TestValuedEnum> iterator = TestValuedEnum.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    TestValuedEnum enum1 = iterator.next();
    assertEquals("ENUM1", enum1.getName());
    assertEquals(100, enum1.getValue());
    assertTrue(iterator.hasNext());
    TestValuedEnum enum2 = iterator.next();
    assertEquals("ENUM2", enum2.getName());
    assertEquals(200, enum2.getValue());
    assertTrue(iterator.hasNext());
    TestValuedEnum enum3 = iterator.next();
    assertEquals("ENUM3", enum3.getName());
    assertEquals(300, enum3.getValue());
    assertFalse(iterator.hasNext());
  }

  private static class TestValuedEnum extends ValuedEnum {
    public static final TestValuedEnum ENUM1 = new TestValuedEnum("ENUM1", 100);
    public static final TestValuedEnum ENUM2 = new TestValuedEnum("ENUM2", 200);
    public static final TestValuedEnum ENUM3 = new TestValuedEnum("ENUM3", 300);

    private static final Map<String, TestValuedEnum> enumMap = new HashMap<String, TestValuedEnum>();
    private static final List<TestValuedEnum> enumList = new ArrayList<TestValuedEnum>();

    static {
      enumMap.put("ENUM1", ENUM1);
      enumMap.put("ENUM2", ENUM2);
      enumMap.put("ENUM3", ENUM3);

      enumList.add(ENUM1);
      enumList.add(ENUM2);
      enumList.add(ENUM3);
    }

    private TestValuedEnum(String name, int value) {
      super(name, value);
    }

    public static TestValuedEnum getEnum(int value) {
      return (TestValuedEnum) getEnum(TestValuedEnum.class, value);
    }

    public static Map<String, TestValuedEnum> getEnumMap() {
      return enumMap;
    }

    public static List<TestValuedEnum> getEnumList() {
      return enumList;
    }

    public static Iterator<TestValuedEnum> iterator() {
      return enumList.iterator();
    }
  }
}