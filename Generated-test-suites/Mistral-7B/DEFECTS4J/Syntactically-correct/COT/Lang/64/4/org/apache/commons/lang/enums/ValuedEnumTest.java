import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUEDED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testConstructor() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {
 };
 assertEquals("TestEnum", enumInstance.getName());
 assertEquals(1, enumInstance.getValue());
 }

 @Test
 public void testGetEnum() throws Exception {
 ValuedEnum testEnum = ValuedEnum.getEnum(VALuedEnumClass, "TestEnum");
 assertEquals("TestEnum", testEnum.getName());
 assertEquals(1, testEnum.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullEnumClass() {
 ValuedEnum.getEnum(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullName() {
 ValuedEnum.getEnum(VALuedEnumClass, null);
 }

 @Test
 public void testGetEnumWithInvalidName() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {
 };
 assertNull(ValuedEnum.getEnum(VALuedEnumClass, "InvalidName"));
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALuedEnumClass);
 ValuedEnum firstEnum = enumList.iterator().next();
 assertNull(ValuedEnum.getEnum(VALuedEnumClass, firstEnum.getValue() + 1));
 }

 @Test
 public void testGetEnumMap() {
 Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALuedEnumClass);
 ValuedEnum firstEnum = enumMap.keySet().iterator().next();
 assertEquals(firstEnum.getName(), enumMap.get(firstEnum.getName()));
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALuedEnumClass);
 assertEquals(enumList.size(), Enum.getEnumConstants(VALuedEnumClass).length);
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALuedEnumClass);
 Iterator<ValuedEnum> iterator = enumList.iterator();
 ValuedEnum firstEnum = iterator.next();
 assertTrue(iterator.hasNext());
 ValuedEnum secondEnum = iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NullPointerException.class)
 public void testToStringWithNullEnum() {
 String toString = ValuedEnum.toString(null);
 fail("toString method should throw NullPointerException when passed null enum");
 }

 @Test
 public void testToString() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {
 };
 String toString = enumInstance.toString();
 String expected = "TestEnum[TestEnum=1]";
 assertEquals(expected, toString);
 }

 @Test
 public void testCompareTo() {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2);
 int result = firstEnum.compareTo(secondEnum);
 assertEquals(-1, result);
 }

 @Test
 public void testCompareToEqual() {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 int result = firstEnum.compareTo(firstEnum);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToSameClass() {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 ValuedEnum secondEnum = new ValuedEnum("FirstEnum", 1);
 int result = firstEnum.compareTo(secondEnum);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToDifferentClass() throws Exception {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 ValuedEnum secondEnum = new OtherValuedEnum("SecondEnum", 2);
 int result = firstEnum.compareTo(secondEnum);
 assertTrue(result < 0);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToClassCastException() {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 Object secondEnum = new Object();
 int result = firstEnum.compareTo(secondEnum);
 fail("compareTo method should throw ClassCastException when passed an object of different class");
 }

 @Test
 public void testGetEnumWithSameValueInDifferentClasses() throws Exception {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 OtherValuedEnum secondEnum = new OtherValuedEnum("OtherFirstEnum", 1);
 ValuedEnum getFirstEnum = ValuedEnum.getEnum(VALuedEnumClass, 1);
 OtherValuedEnum getSecondEnum = ValuedEnum.getEnum(OtherValuedEnumClass, 1);
 assertEquals(firstEnum, getFirstEnum);
 assertEquals(secondEnum, getSecondEnum);
 }

 @Test
 public void testGetEnumWithDifferentValuesInSameClass() throws Exception {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 ValuedEnum secondEnum = new ValuedEnum("SecondEnum", 2);
 assertNotEquals(firstEnum, secondEnum);
 }

 @Test
 public void testGetEnumWithDifferentValuesInDifferentClasses() throws Exception {
 ValuedEnum firstEnum = new ValuedEnum("FirstEnum", 1);
 OtherValuedEnum secondEnum = new OtherValuedEnum("OtherFirstEnum", 3);
 assertNotEquals(firstEnum, secondEnum);
 }

 private static class OtherValuedEnum extends ValuedEnum {
 OtherValuedEnum(String name, int value) {
 super(name, value);
 }
 }
}