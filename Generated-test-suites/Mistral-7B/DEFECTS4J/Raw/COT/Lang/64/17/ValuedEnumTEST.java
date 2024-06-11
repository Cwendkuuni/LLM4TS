import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersionEnum", "Java1_0");
 Assert.assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullEnumClass() {
 ValuedEnum.getEnum(null, "Java1_0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, null);
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 ValuedEnum javaVersion1_4 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, Integer.MAX_VALUE);
 Assert.assertNull(javaVersion1_4);
 }

 @Test
 public void testGetEnumWithValidValue() {
 ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 110);
 Assert.assertEquals("JavaVersion[Java 1.1=110]", javaVersion1_1.toString());
 }

 @Test
 public void testgetValue() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100) {
 @Override
 public String toString() {
 return "JavaVersion1_0";
 }
 };

 Assert.assertEquals(100, javaVersion1_0.getValue());
 }

 @Test
 public void testCompareToSameEnum() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100);
 ValuedEnum javaVersion1_0Copy = new ValuedEnum("JavaVersion1_0", 100);

 Assert.assertEquals(0, javaVersion1_0.compareTo(javaVersion1_0Copy));
 }

 @Test
 public void testCompareToDifferentEnumLessThan() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100);
 ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110);

 Assert.assertEquals(-1, javaVersion1_0.compareTo(javaVersion1_1));
 }

 @Test
 public void testCompareToDifferentEnumGreaterThan() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100);
 ValuedEnum javaVersion1_1 = new ValuedEnum("JavaVersion1_1", 110);

 Assert.assertEquals(1, javaVersion1_1.compareTo(javaVersion1_0));
 }

 @Test
 public void testToString() {
 ValuedEnum javaVersion1_0 = new ValuedEnum("JavaVersion1_0", 100);

 Assert.assertEquals("JavaVersion[JavaVersion1_0=100]", javaVersion1_0.toString());
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALUED_ENUM_CLASS);
 Map<ValuedEnum, Integer> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);

 for (ValuedEnum enumElement : enumList) {
 Assert.assertEquals(enumElement, enumMap.get(enumElement));
 }
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);

 Assert.assertEquals(3, enumList.size());
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = Enum.getEnumList(VALUED_ENUM_CLASS);
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(VALUED_ENUM_CLASS);

 Assert.assertEquals(3, enumList.size());
 int i = 0;
 while (iterator.hasNext()) {
 ValuedEnum next = iterator.next();
 Assert.assertEquals(next, enumList.get(i++));
 }
 }
}