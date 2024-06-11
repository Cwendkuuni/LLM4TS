import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 assertEquals("Java 1.0", java1_0.getName());
 assertEquals(100, java1_0.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNullClass() {
 ValuedEnum.getEnum(null, "Java1_0");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumNullName() {
 ValuedEnum.getEnum(ValuedEnum.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetEnumInvalidName() {
 ValuedEnum.getEnum(ValuedEnum.class, "InvalidName");
 }

 @Test
 public void testGetEnumValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 int value = java1_0.getValue();
 assertEquals(100, value);
 }

 @Test
 public void testGetEnumIterator() {
 ValuedEnum[] values = ValuedEnum.values();
 List<ValuedEnum> list = java.util.Arrays.asList(values);
 Iterator<ValuedEnum> iterator = list.iterator();
 ValuedEnum java1_0 = iterator.next();
 assertTrue(iterator.hasNext());
 ValuedEnum java1_1 = iterator.next();
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testCompareToSameValue() {
 ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum1", 100);
 ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum2", 100);
 int comparison = java1_0.compareTo(java1_1);
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareToDifferentValue() {
 ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum1", 100);
 ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum2", 110);
 int comparison = java1_0.compareTo(java1_1);
 assertEquals(-1, comparison);
 }

 @Test
 public void testCompareToSameClass() {
 ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum1", 100);
 ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum2", 100);
 int comparison = java1_0.compareTo(java1_1);
 assertEquals(0, comparison);
 }

 @Test
 public void testCompareToDifferentClass() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
 Class<?> enumClass1 = Class.forName("org.apache.commons.lang.enums.ValuedEnum$JavaVersionEnum1");
 Object java1_0 = enumClass1.getDeclaredConstructor(String.class, int.class).newInstance("JavaVersionEnum1", 100);
 Class<?> enumClass2 = Class.forName("org.apache.commons.lang.enums.ValuedEnum$JavaVersionEnum2");
 Object java1_1 = enumClass2.getDeclaredConstructor(String.class, int.class).newInstance("JavaVersionEnum2", 110);
 int comparison = ((ValuedEnum) java1_0).compareTo(java1_1);
 assertEquals(-1, comparison);
 }
}