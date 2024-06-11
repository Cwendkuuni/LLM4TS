import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_Enum_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersionEnum1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0_VALUE");
 Assert.assertEquals("JavaVersionEnum_JAVA1_0", javaVersionEnum1.toString());

 ValuedEnum javaVersionEnum2 = ValuedEnum.getEnum("JavaVersionEnum", 100);
 Assert.assertEquals("JavaVersionEnum_JAVA1_0", javaVersionEnum2.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullEnumClass() {
 ValuedEnum.getEnum(null, "JAVA1_0_VALUE");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(VALUED_Enum_CLASS, null);
 }

 @Test
 public void testGetEnumWhenEnumDoesNotExist() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(VALUED_Enum_CLASS, "InvalidValue");
 Assert.assertNull(javaVersionEnum);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_Enum_CLASS);
 Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_Enum_CLASS);
 for (ValuedEnum value : enumList) {
 Assert.assertEquals(value, enumMap.get(value.getValue()));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumMapWithNullEnumList() {
 ValuedEnum.getEnumMap(null);
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_Enum_CLASS);
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(VALUED_Enum_CLASS);
 while (iterator.hasNext()) {
 ValuedEnum next = iterator.next();
 Assert.assertEquals(next, enumList.get(enumList.indexOf(next)));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testIteratorWithNullEnumClass() {
 ValuedEnum.iterator(null);
 }

 @Test
 public void testCompareTo() {
 ValuedEnum enum1 = new EnumTestValue1(1, "Value1");
 ValuedEnum enum2 = new EnumTestValue2(2, "Value2");
 ValuedEnum enum3 = new EnumTestValue3(3, "Value3");

 int comparisonResult1 = enum1.compareTo(enum1);
 Assert.assertEquals(0, comparisonResult1);

 int comparisonResult2 = enum1.compareTo(enum2);
 Assert.assertEquals(-1, comparisonResult2);

 int comparisonResult3 = enum2.compareTo(enum1);
 Assert.assertEquals(1, comparisonResult3);

 int comparisonResult4 = enum1.compareTo(enum3);
 Assert.assertEquals(-1, comparisonResult4);

 int comparisonResult5 = enum3.compareTo(enum1);
 Assert.assertEquals(1, comparisonResult5);
 }

 @Test
 public void testToString() {
 ValuedEnum enum1 = new EnumTestValue1(1, "Value1");
 ValuedEnum enum2 = new EnumTestValue2(2, "Value2");
 ValuedEnum enum3 = new EnumTestValue3(3, "Value3");

 String toStringResult1 = enum1.toString();
 Assert.assertEquals("EnumTestValue1[Value1=1]", toStringResult1);

 String toStringResult2 = enum2.toString();
 Assert.assertEquals("EnumTestValue2[Value2=2]", toStringResult2);

 String toStringResult3 = enum3.toString();
 Assert.assertEquals("EnumTestValue3[Value3=3]", toStringResult3);
 }
}

class EnumTestValue1 extends ValuedEnum {
 EnumTestValue1(int value, String name) {
 super(name, value);
 }
}

class EnumTestValue2 extends ValuedEnum {
 EnumTestValue2(int value, String name) {
 super(name, value);
 }
}

class EnumTestValue3 extends ValuedEnum {
 EnumTestValue3(int value, String name) {
 super(name, value);
 }
}