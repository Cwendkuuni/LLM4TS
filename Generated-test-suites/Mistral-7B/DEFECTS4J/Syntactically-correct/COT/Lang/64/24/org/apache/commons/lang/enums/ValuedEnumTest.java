import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", 100);
 assertEquals("JavaVersion[Java 1.0=100]", javaVersion1_0.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullClass() {
 ValuedEnum.getEnum(null, 100);
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullName() {
 ValuedEnum.getEnum(VALUED_Enum_CLASS, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumInvalidName() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, "InvalidName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNegativeValue() {
 ValuedEnum.getEnum(VALUED_ENUM_CLASS, -1);
 }

 @Test
 public void testGetEnumUnknownValue() {
 ValuedEnum unknownEnum = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 500);
 assertNull(unknownEnum);
 }

 @Test
 public void testGetEnumValue() {
 ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", 100);
 assertEquals(100, javaVersion1_0.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullEnum() {
 ValuedEnum nullEnum = null;
 nullEnum.getValue();
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 Map<String, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);
 for (ValuedEnum enumItem : enumList) {
 assertEquals(enumItem, enumMap.get(enumItem.getName()));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumMapNullClass() {
 ValuedEnum.getEnumMap(null);
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 for (ValuedEnum enumItem : enumList) {
 System.out.println(enumItem);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumListNullClass() {
 ValuedEnum.getEnumList(null);
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(VALUED_ENUM_CLASS);
 while (iterator.hasNext()) {
 assertSame(iterator.next(), enumList.remove(0));
 }
 assertFalse(iterator.hasNext());
 }

 @Test(expected = NullPointerException.class)
 public void testIteratorNullClass() {
 ValuedEnum.iterator(null);
 }

}