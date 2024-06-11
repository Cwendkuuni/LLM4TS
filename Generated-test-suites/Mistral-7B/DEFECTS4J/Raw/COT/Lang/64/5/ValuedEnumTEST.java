import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

 private static final ValuedEnum JAVA_VERSION_1_0 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_0");
 private static final ValuedEnum JAVA_VERSION_1_1 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_1");
 private static final ValuedEnum JAVA_VERSION_1_2 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_2");
 private static final ValuedEnum JAVA_VERSION_1_3 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_3");

 @Test
 public void testGetEnumValue() {
 ValuedEnum enumValue = JAVA_VERSION_1_0;
 int expectedValue = 100;
 int actualValue = enumValue.getValue();
 Assert.assertEquals("Unexpected value for JavaVersionEnum JAVA1_0", expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum enumValue = null;
 try {
 enumValue = ValuedEnum.getEnum(null, 100);
 } catch (IllegalArgumentException iae) {
 // expected, no need to do anything
 }
 Assert.assertNull("Unexpected value for null class", enumValue);
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 ValuedEnum enumValue = ValuedEnum.getEnum(ValuedEnum.class, 500);
 Assert.assertNull("Unexpected value for invalid value", enumValue);
 }

 @Test
 public void testGetEnumWithValidValue() {
 ValuedEnum enumValue = ValuedEnum.getEnum(ValuedEnum.class, 110);
 Assert.assertEquals("Unexpected value for JavaVersionEnum JAVA1_1", JAVA_VERSION_1_1, enumValue);
 }

 @Test
 public void testEnumComparison() {
 int comparisonResult = JAVA_VERSION_1_0.compareTo(JAVA_VERSION_1_1);
 Assert.assertThat("Unexpected comparison result", comparisonResult, is(-1));
 }

 @Test
 public void testEnumComparisonEquals() {
 int comparisonResult = JAVA_VERSION_1_0.compareTo(JAVA_VERSION_1_0);
 Assert.assertEquals("Unexpected comparison result", 0, comparisonResult);
 }

 @Test
 public void testEnumToString() {
 String toStringResult = JAVA_VERSION_1_0.toString();
 String expectedToString = "JavaVersion[Java 1.0=100]";
 Assert.assertEquals("Unexpected toString result", expectedToString, toStringResult);
 }
}