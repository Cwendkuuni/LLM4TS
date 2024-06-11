import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNullClass() {
 ValuedEnum.getEnum(null, "JAVA1_0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNullName() {
 ValuedEnum.getEnum("JavaVersionEnum", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetEnumInvalidName() {
 ValuedEnum.getEnum("JavaVersionEnum", "INVALID");
 }

 @Test
 public void testGetEnumValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

 assertEquals(100, java1_0.getValue());
 assertEquals(110, java1_1.getValue());
 assertEquals(120, java1_2.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNegativeValue() {
 ValuedEnum enumValue = ValuedEnum.getEnum("JavaVersionEnum", -1);
 }

 @Test
 public void testCompareTo() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

 assertEquals(java1_0.compareTo(java1_1), -10);
 assertEquals(java1_1.compareTo(java1_2), 10);
 assertEquals(java1_0.compareTo(java1_2), -20);
 }

 @Test(expected = ClassCastException.class)
 public void testCompareToDifferentTypes() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 String testString = new String("Test");
 int testInt = 10;

 java1_0.compareTo(testString);
 java1_0.compareTo(testInt);
 }

 @Test(expected = NullPointerException.class)
 public void testCompareToNull() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");

 java1_0.compareTo(null);
 }

 @Test
 public void testToString() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 }
}