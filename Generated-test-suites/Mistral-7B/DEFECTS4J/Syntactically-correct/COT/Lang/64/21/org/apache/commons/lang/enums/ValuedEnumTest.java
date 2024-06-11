import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnumByName() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 assertEquals("Java 1.0", javaVersionEnum.getName());
 assertEquals(100, javaVersionEnum.getValue());
 }

 @Test
 public void testGetEnumByValue() {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 100);
 assertEquals("Java 1.0", javaVersionEnum.getName());
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersionEnumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 Map<Integer, ValuedEnum> javaVersionEnumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);

 assertEquals(javaVersionEnumList.size(), javaVersionEnumMap.size());

 for (ValuedEnum javaVersionEnum : javaVersionEnumList) {
 assertEquals(javaVersionEnum, javaVersionEnumMap.get(javaVersionEnum.getValue()));
 }
 }

 @Test
 public void testGetEnumIterator() {
 List<ValuedEnum> javaVersionEnumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 Iterator<ValuedEnum> javaVersionEnumIterator = ValuedEnum.iterator(VALUED_ENUM_CLASS);

 assertEquals(javaVersionEnumList.size(), javaVersionEnumIterator.size());

 while (javaVersionEnumIterator.hasNext()) {
 ValuedEnum javaVersionEnum = javaVersionEnumIterator.next();
 assertTrue(javaVersionEnumList.contains(javaVersionEnum));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumByClassAndNullValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumByClassAndNegativeValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, -1);
 }

 @Test
 public void testCompareToSameEnum() {
 ValuedEnum javaVersion1 = new ValuedEnum("JavaVersion1", 1) {
 @Override
 public int compareTo(Object other) {
 return super.compareTo(other);
 }
 };
 ValuedEnum javaVersion2 = javaVersion1;

 int compareResult = javaVersion1.compareTo(javaVersion2);
 assertEquals(0, compareResult);
 }

 @Test
 public void testCompareToDifferentEnum() {
 ValuedEnum javaVersion1 = new ValuedEnum("JavaVersion1", 1) {
 @Override
 public int compareTo(Object other) {
 return super.compareTo(other);
 }
 };
 ValuedEnum javaVersion2 = new ValuedEnum("JavaVersion2", 2) {
 @Override
 public int compareTo(Object other) {
 return super.compareTo(other);
 }
 };

 int compareResult = javaVersion1.compareTo(javaVersion2);
 assertEquals(-1, compareResult);
 }

 @Test
 public void testCompareToDifferentClass() {
 ValuedEnum javaVersion1 = new ValuedEnum("JavaVersion1", 1) {
 @Override
 public int compareTo(Object other) {
 return -1;
 }
 };
 Object anotherObject = new Object();

 int compareResult = javaVersion1.compareTo(anotherObject);
 assertEquals(-1, compareResult);
 }
}