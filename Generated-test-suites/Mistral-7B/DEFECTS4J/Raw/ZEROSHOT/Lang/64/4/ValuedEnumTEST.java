import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.Enum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static class TestValuedEnum extends ValuedEnum {

 /**
 * Constructor for enum item.
 *
 * @param name the name of enum item
 * @param value the value of enum item
 */
 TestValuedEnum(String name, int value) {
 super(name, value);
 }

 }

 @Test
 public void testGetEnum() {
 Class<TestValuedEnum> enumClass = TestValuedEnum.class;

 TestValuedEnum enum1 = TestValuedEnum.getEnum(enumClass, 1);
 TestValuedEnum enum2 = TestValuedEnum.getEnum(enumClass, 2);

 assertNotNull(enum1);
 assertNotNull(enum2);
 assertEquals(1, enum1.getValue());
 assertEquals(2, enum2.getValue());

 TestValuedEnum nullEnum = TestValuedEnum.getEnum(enumClass, 0);
 assertNull(nullEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullEnumClass() {
 TestValuedEnum.getEnum(null, 1);
 }

 @Test
 public void testCompareTo() {
 TestValuedEnum enum1 = new TestValuedEnum("Enum1", 1);
 TestValuedEnum enum2 = new TestValuedEnum("Enum2", 2);
 TestValuedEnum enum3 = new TestValuedEnum("Enum1", 1);

 assertEquals(0, enum1.compareTo(enum3));
 assertEquals(-1, enum3.compareTo(enum1));
 assertEquals(1, enum1.compareTo(enum2));
 }

 @Test
 public void testToString() {
 TestValuedEnum enumInstance = new TestValuedEnum("TestEnum", 3);
 String expected = "TestEnum[TestEnum=3]";
 assertEquals(expected, enumInstance.toString());
 }
}