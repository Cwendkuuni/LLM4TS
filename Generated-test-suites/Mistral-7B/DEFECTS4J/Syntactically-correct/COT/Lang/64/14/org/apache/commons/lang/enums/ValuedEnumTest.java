import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
 ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersion", "Java 1.1");
 ValuedEnum javaVersion1_2 = ValuedEnum.getEnum("JavaVersion", "Java 1.2");

 assertEquals(javaVersion1_0, ValuedEnum.JAVA1_0);
 assertEquals(javaVersion1_1, ValuedEnum.JAVA1_1);
 assertEquals(javaVersion1_2, ValuedEnum.JAVA1_2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullEnumClass() {
 ValuedEnum.getEnum(null, "JavaVersion, Java 1.0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, null);
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 ValuedEnum enumObject = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 1000);
 assertNull(enumObject);
 }

 @Test
 public void testGetEnumWithValidValue() {
 ValuedEnum javaVersion1_3 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 130);
 assertEquals(javaVersion1_3, ValuedEnum.JAVA1_3);
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 assertEquals(3, enumList.size());
 }

 @Test
 public void testGetEnumMap() {
 Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);
 assertEquals(3, enumMap.size());
 }

 @Test
 public void testCompareTo() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
 ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersion", "Java 1.1");
 ValuedEnum javaVersion1_2 = ValuedEnum.getEnum("JavaVersion", "Java 1.2");

 assertEquals(javaVersion1_0.compareTo(javaVersion1_0), 0);
 assertEquals(javaVersion1_0.compareTo(javaVersion1_1), -1);
 assertEquals(javaVersion1_1.compareTo(javaVersion1_0), 1);
 assertEquals(javaVersion1_1.compareTo(javaVersion1_1), 0);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToWithDifferentEnumType() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
 Object differentEnumType = new Object();
 assertEquals(javaVersion1_0.compareTo(differentEnumType), Integer.MIN_VALUE);
 }

 @Test
 public void testToString() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
 assertEquals(javaVersion1_0.toString(), "JavaVersion[Java 1.0=100]");
 }
}