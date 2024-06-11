import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> ValuedEnumClass = ValuedEnum.class;

 @Test
 public void testGetEnum() throws Exception {
 ValuedEnum javaVersionEnum = ValuedEnum.getEnum(ValuedEnumClass, "JavaVersionEnum", 100);
 Assert.assertNotNull(javaVersionEnum);
 Assert.assertEquals("JavaVersionEnum", javaVersionEnum.getName());
 Assert.assertEquals(100, javaVersionEnum.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullClass() {
 ValuedEnum.getEnum(null, "JavaVersionEnum", 100);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNullName() {
 ValuedEnum.getEnum(ValuedEnumClass, null, 100);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumWithNegativeValue() {
 ValuedEnum.getEnum(ValuedEnumClass, "JavaVersionEnum", -100);
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumWithNullValue() {
 ValuedEnum.getEnum(ValuedEnumClass, "JavaVersionEnum", null);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetEnumWithInvalidValue() {
 ValuedEnum.getEnum(ValuedEnumClass, "JavaVersionEnum", Integer.MAX_VALUE);
 }

 @Test
 public void testGetEnumMap() {
 List enumList = ValuedEnum.getEnumList(ValuedEnumClass);
 Map enumMap = ValuedEnum.getEnumMap(ValuedEnumClass);
 for (Object obj : enumList) {
 ValuedEnum enumItem = (ValuedEnum) obj;
 Assert.assertEquals(enumItem, enumMap.get(enumItem.getName()));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumMapWithNullClass() {
 ValuedEnum.getEnumMap(null);
 }

 @Test
 public void testGetEnumList() {
 List enumList = ValuedEnum.getEnumList(ValuedEnumClass);
 for (Object obj : enumList) {
 ValuedEnum enumItem = (ValuedEnum) obj;
 System.out.println(enumItem.getName() + "=" + enumItem.getValue());
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testIterator() {
 Iterator iterator = ValuedEnum.iterator(ValuedEnumClass);
 iterator.next(); //Advance the iterator to the next element
 iterator.next(); //Advance the iterator to the next element
 iterator.next(); //Advance the iterator to the next element
 iterator.next(); //Advance the iterator to the next element
 iterator.hasNext(); // checks if there is an element after the current position.
 Assert.assertFalse(iterator.hasNext()); // Should be false since we have reached the end
 }
}