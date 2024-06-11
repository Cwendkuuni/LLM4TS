import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnumByValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_1_VALUE);

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());

 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());
 }

 @Test
 public void testGetEnumByValueNull() {
 ValuedEnum enumByValueNull = ValuedEnum.getEnum(ValuedEnum.class, 150);
 assertNull(enumByValueNull);
 }

 @Test
 public void testGetEnumByName() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
 ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.1");

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());

 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());
 }

 @Test
 public void testGetEnumByNameNull() {
 ValuedEnum enumByNameNull = ValuedEnum.getEnum(ValuedEnum.class, null);
 assertNull(enumByNameNull);
 }

 @Test
 public void testCompareTwoDifferentEnums() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
 ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.1");

 int comparisonResult = java1_0.compareTo(java1_1);
 assertEquals(-1, comparisonResult);
 }

 @Test
 public void testCompareSameEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
 int comparisonResult = java1_0.compareTo(java1_0);
 assertEquals(0, comparisonResult);
 }
}