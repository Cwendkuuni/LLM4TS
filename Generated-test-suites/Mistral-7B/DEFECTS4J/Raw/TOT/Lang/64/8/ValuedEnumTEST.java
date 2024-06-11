import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());

 assertEquals("Java 1.1", java1_1.getName());
 assertEquals(110, java1_1.getValue());

 assertEquals("Java 1.2", java1_2.getName());
 assertEquals(120, java1_2.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidEnumName() {
 ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", "INVALID_ENUM");
 fail("No IllegalArgumentException was thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithInvalidEnumValue() {
 ValuedEnum invalidEnum = ValuedEnum.getEnum("JavaVersionEnum", 500);
 fail("No IllegalArgumentException was thrown");
 }

 @Test
 public void testGetEnumMap() {
 Map<ValuedEnum, String> enumMap = ValuedEnum.getEnumMap(ValuedEnum.class);

 assertEquals(3, enumMap.size());

 for (ValuedEnum enumItem : enumMap.keySet()) {
 String name = enumItem.getName();
 int value = enumItem.getValue();

 assertNotNull(name);
 assertNotNull(value);

 assertEquals(enumItem, enumMap.get(enumItem));
 }
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);

 assertEquals(3, enumList.size());

 for (ValuedEnum enumItem : enumList) {
 String name = enumItem.getName();
 int value = enumItem.getValue();

 assertNotNull(name);
 assertNotNull(value);
 }
 }
}