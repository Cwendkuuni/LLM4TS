import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnumByValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_1_VALUE);
 ValuedEnum nullEnum = ValuedEnum.getEnum(ValuedEnum.class, 0);

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals("Java 1.1", java1_1.getName());
 assertNull(nullEnum);
 }

 @Test
 public void testGetEnumByName() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.0");
 ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, "Java 1.1");

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals("Java 1.1", java1_1.getName());
 }

 @Test
 public void testCompareEnums() {
 ValuedEnum java1_0 = new ValuedEnum("Java 1.0", 100);
 ValuedEnum java1_1 = new ValuedEnum("Java 1.1", 110);
 ValuedEnum java1_2 = new ValuedEnum("Java 1.2", 120);

 assertTrue(java1_0.compareTo(java1_0) == 0);
 assertTrue(java1_0.compareTo(java1_1) < 0);
 assertTrue(java1_1.compareTo(java1_0) > 0);
 assertTrue(java1_0.compareTo(java1_2) < 0);
 assertTrue(java1_2.compareTo(java1_0) > 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum enumValue = ValuedEnum.getEnum(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullName() {
 ValuedEnum enumValue = ValuedEnum.getEnum(ValuedEnum.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidValue() {
 ValuedEnum enumValue = ValuedEnum.getEnum(ValuedEnum.class, 1000);
 }
}