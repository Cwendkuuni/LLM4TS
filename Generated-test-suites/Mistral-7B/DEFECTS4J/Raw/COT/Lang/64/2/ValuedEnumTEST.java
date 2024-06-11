import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private ValuedEnum getEnumInstance(Class<? extends ValuedEnum> enumClass, int value) {
 try {
 return enumClass.getEnumConstants()[getIndexOfEnumConstantWithValue(enumClass, value)];
 } catch (IllegalArgumentException e) {
 return null;
 }
 }

 @Test
 public void testGetEnum() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 ValuedEnum javaVersion1_0 = getEnumInstance(javaVersionEnum, ValuedEnum.JAVA1_0_VALUE);
 assertNotNull(javaVersion1_0);
 assertEquals("Java 1.0", javaVersion1_0.getName());
 assertEquals(ValuedEnum.JAVA1_0_VALUE, javaVersion1_0.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumNullEnumClass() {
 getEnumInstance(null, 0);
 }

 @Test
 public void testGetEnumWithInvalidValue() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 ValuedEnum enumWithInvalidValue = getEnumInstance(javaVersionEnum, 1000);
 assertNull(enumWithInvalidValue);
 }

 @Test
 public void testGetEnumWithNegativeValue() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 ValuedEnum enumWithNegativeValue = getEnumInstance(javaVersionEnum, -1);
 assertNull(enumWithNegativeValue);
 }

 @Test
 public void testGetEnumMap() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 List enumList = getEnumList(javaVersionEnum.getClass());
 Map enumMap = ValuedEnum.getEnumMap(javaVersionEnum.getClass());
 for (Object enumConstant : enumList) {
 assertEquals(enumConstant, enumMap.get(enumConstant));
 }
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumMapNullEnumClass() {
 getEnumMap(null);
 }

 @Test
 public void testGetEnumList() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 List enumList = getEnumList(javaVersionEnum.getClass());
 assertEquals(4, enumList.size());
 }

 @Test(expected = NullPointerException.class)
 public void testGetEnumListNullEnumClass() {
 getEnumList(null);
 }

 @Test
 public void testIterator() {
 ValuedEnum javaVersionEnum = ValuedEnum.class;
 List enumList = getEnumList(javaVersionEnum.getClass());
 Iterator iterator = ValuedEnum.iterator(javaVersionEnum.getClass());
 while (iterator.hasNext()) {
 assertEquals(iterator.next(), enumList.get(0));
 iterator.remove();
 enumList.remove(0);
 }
 assertEquals(0, enumList.size());
 }

 @Test(expected = NullPointerException.class)
 public void testIteratorNullEnumClass() {
 ValuedEnum.iterator(null);
 }

 @Test
 public void testCompareToSameInstance() {
 ValuedEnum javaVersion1_0 = getEnumInstance(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 int result = javaVersion1_0.compareTo(javaVersion1_0);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToDifferentInstancesWithSameValue() {
 ValuedEnum javaVersion1_0 = getEnumInstance(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 ValuedEnum javaVersion1_0Copy = new ValuedEnum("Java 1.0", ValuedEnum.JAVA1_0_VALUE) {
 };
 int result = javaVersion1_0.compareTo(javaVersion1_0Copy);
 assertEquals(0, result);
 }

 @Test
 public void testCompareToDifferentInstancesWithDifferentValues() {
 ValuedEnum javaVersion1_0 = getEnumInstance(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 ValuedEnum javaVersion1_1 = getEnumInstance(ValuedEnum.class, ValuedEnum.JAVA1_1_VALUE);
 int result = javaVersion1_0.compareTo(javaVersion1_1);
 assertEquals(-1, result);
 }

 @Test
 public void testCompareToDifferentClasses() {
 ValuedEnum javaVersion1_0 = getEnumInstance(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
 AnotherValuedEnum anotherJavaVersion1_0 = getEnumInstance(AnotherValuedEnum.class, AnotherValuedEnum.ANOTHER_JAVA1_0_VALUE);
 int result = javaVersion1_0.compareTo(anotherJavaVersion1_0);
 assertNotNull(result);
 }

 private int getIndexOfEnumConstantWithValue(Class<? extends ValuedEnum> enumClass, int value) {
 int index = 0;
 ValuedEnum enumConstant;
 do {
 enumConstant = (ValuedEnum) enumConstant.getClass().getEnumConstants()[index];
 if (enumConstant.getValue() == value) {
 return index;
 }
 index++;
 } while (enumConstant != null);
 throw new IllegalArgumentException("Value not found in enum");
 }

 private List getEnumList(Class<? extends ValuedEnum> enumClass) {
 try {
 return (List) enumClass.getMethod("getEnumList").invoke(null);
 } catch (IllegalAccessException | InvocationTargetException e) {
 throw new RuntimeException(e);
 }
 }

 private Map getEnumMap(Class<? extends ValuedEnum> enumClass) {
 try {
 return (Map) enumClass.getMethod("getEnumMap").invoke(null);
 } catch (IllegalAccessException | InvocationTargetException e) {
 throw new RuntimeException(e);
 }
 }
}