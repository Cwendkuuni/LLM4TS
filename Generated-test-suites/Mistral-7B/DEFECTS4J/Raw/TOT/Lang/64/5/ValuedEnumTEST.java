import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnumByValue() {
 ValuedEnum java1_0 = ValuedEnum.JAVA1_0; // valid value
 ValuedEnum unknown = new ValuedEnum("UnknownEnum", 1000); // unknown value

 // Test getting an enum by its value
 ValuedEnum result = ValuedEnum.getEnum(ValuedEnum.class, java1_0.getValue());
 assertEquals(java1_0, result);

 // Test getting null when an unknown enum is requested
 result = ValuedEnum.getEnum(ValuedEnum.class, unknown.getValue());
 assertNull(result);
 }

 @Test
 public void testGetEnumByName() {
 String javaVersion1_0Name = "JavaVersionEnum[Java 1.0=100]";
 String javaVersion1_1Name = "JavaVersionEnum[Java 1.1=110]";

 // Test getting an enum by its name
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, javaVersion1_0Name);
 assertEquals(ValuedEnum.JAVA1_0, java1_0);

 // Test getting an unknown enum by its name
 ValuedEnum unknownEnum = new ValuedEnum("UnknownEnum", 1000);
 String unknownEnumName = unknownEnum.toString();
 ValuedEnum result = ValuedEnum.getEnum(ValuedEnum.class, unknownEnumName);
 assertNull(result);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> javaVersions = List.of(ValuedEnum.JAVA1_0, ValuedEnum.JAVA1_1, ValuedEnum.JAVA1_2, ValuedEnum.JAVA1_3);
 Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(ValuedEnum.class);

 // Test that the map contains all the enum values
 for (ValuedEnum javaVersion : javaVersions) {
 String name = javaVersion.toString();
 ValuedEnum value = enumMap.get(name);
 assertEquals(javaVersion, value);
 }

 // Test that the map does not contain null values
 assertFalse(enumMap.containsValue(null));
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> javaVersions = List.of(ValuedEnum.JAVA1_0, ValuedEnum.JAVA1_1, ValuedEnum.JAVA1_2, ValuedEnum.JAVA1_3);
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);

 // Test that the list contains all the enum values
 assertEquals(javaVersions, enumList);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 // Test that an IllegalArgumentException is thrown when an null class is passed to getEnum methods
 ValuedEnum result = ValuedEnum.getEnum(null, 100);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNegativeValue() {
 // Test that an IllegalArgumentException is thrown when a negative value is passed to getEnum methods
 ValuedEnum result = ValuedEnum.getEnum(ValuedEnum.class, -1);
 fail("Expected IllegalArgumentException");
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumWithNullEnumName() {
 // Test that a NullPointerException is thrown when a null name is passed to getEnum method
 ValuedEnum result = ValuedEnum.getEnum(ValuedEnum.class, null);
 fail("Expected NullPointerException");
 }
}