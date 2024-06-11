import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert; import org.junit.Test;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUEDED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testConstructor() {
 ValuedEnum enumInstance = new ValuedEnum("TestEnum", 1) {
 // override toString() method to check its value
 @Override
 public String toString() {
 return "TestEnum[" + getName() + "=" + getValue() + "]";
 }
 };

 Assert.assertEquals("TestEnum[name=TestEnum, value=1]", enumInstance.toString());
 }

 @Test
 public void testGetEnum() throws Exception {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(VALuedEnumClass, 1);
 Assert.assertNotNull(javaVersionEnum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum.getEnum(null, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNegativeValue() {
 ValuedEnum.getEnum(VALuedEnumClass, -1);
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(VALuedEnumClass, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithUnknownEnum() {
 ValuedEnum unknownEnum = ValuedEnum.getEnum(VALuedEnumClass, 1000);
 Assert.assertNull(unknownEnum);
 }

 @Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALuedEnumClass);
 Map<ValuedEnum, Integer> enumMap = ValuedEnum.getEnumMap(VALuedEnumClass);

 for (ValuedEnum enumItem : enumList) {
 Integer expectedValue = enumItem.getValue();
 Integer actualValue = enumMap.get(enumItem);
 Assert.assertEquals("Unexpected value for enum " + enumItem, expectedValue, actualValue);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumMapWithNullEnumClass() {
 ValuedEnum.getEnumMap(null);
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALuedEnumClass);

 for (ValuedEnum enumItem : enumList) {
 Assert.assertNotNull(enumItem);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumListWithNullEnumClass() {
 ValuedEnum.getEnumList(null);
 }

 @Test
 public void testIterator() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALuedEnumClass);
 Iterator<ValuedEnum> iterator = ValuedEnum.iterator(VALuedEnumClass);

 while (iterator.hasNext()) {
 ValuedEnum nextEnum = iterator.next();
 ValuedEnum expectedEnum = enumList.get(enumList.indexOf(nextEnum));
 Assert.assertEquals("Unexpected enum in iterator", expectedEnum, nextEnum);
 }
 }

 @Test(expected = NullPointerException.class)
 public void testIteratorWithNullEnumClass() {
 ValuedEnum.iterator(null);
 }
}