import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALuedEnumClass = ValuedEnum.class;

 @Test
 public void testConstructor() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10) {
 };
 assertEquals("TestEnum", enumInstance.getName());
 assertEquals(10, enumInstance.getValue());
 }

 @Test
 public void testGetEnum() {
 ValuedEnum testEnum = new ValuedEnum("TestEnum", 10) {
 };
 ValuedEnum gottenEnum = ValuedEnum.getEnum(VALuedEnumClass, 10);
 assertEquals(testEnum, gottenEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum.getEnum(null, 10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(VALuedEnumClass, null);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALuedEnumClass);
 Map<ValuedEnum, Integer> expectedMap = new HashMap<>();
 for (ValuedEnum enumValue : enumList) {
 expectedMap.put(enumValue, enumValue.getValue());
 }
 Map<ValuedEnum, Integer> enumMap = ValuedEnum.getEnumMap(VALuedEnumClass);
 assertEquals(expectedMap, enumMap);
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALuedEnumClass);
 List<ValuedEnum> expectedList = new ArrayList<>(enumList);
 Collections.reverse(expectedList);
 List<ValuedEnum> actualList = ValuedEnum.getEnumList(VALuedEnumClass);
 Collections.sort(actualList);
 assertEquals(expectedList, actualList);
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALuedEnumClass);
 Iterator<ValuedEnum> expectedIterator = enumList.iterator();
 Iterator<ValuedEnum> actualIterator = ValuedEnum.iterator(VALuedEnumClass);
 while (expectedIterator.hasNext()) {
 ValuedEnum expectedEnum = expectedIterator.next();
 ValuedEnum actualEnum = actualIterator.next();
 assertEquals(expectedEnum, actualEnum);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIteratorWithNullClass() {
 ValuedEnum.iterator(null);
 }

 @Test
 public void testCompareTo() {
 ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
 ValuedEnum enum2 = new ValuedEnum("TestEnum2", 2);
 ValuedEnum enum3 = new ValuedEnum("TestEnum1", 1);
 int result1 = enum1.compareTo(enum2);
 int result2 = enum1.compareTo(enum3);
 assertNotEquals(0, result1);
 assertEquals(0, result2);
 }

 @Test
 public void testToString() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 10) {
 };
 String toString = enumInstance.toString();
 String expectedToString = "TestEnum[TestEnum=10]";
 assertEquals(expectedToString, toString);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullEnum() {
 ValuedEnum nullEnum = null;
 nullEnum.toString();
 }
}