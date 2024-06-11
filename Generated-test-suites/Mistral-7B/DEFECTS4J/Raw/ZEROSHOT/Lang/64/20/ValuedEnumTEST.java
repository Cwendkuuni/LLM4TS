import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUED_ENUM_CLASS = ValuedEnum.class;

 @Test
 public void testGetEnum() {
 ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
 ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
 ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");
 ValuedEnum java1_3 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_3");

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
 }

 @Test
 public void testGetEnumByValue() {
 ValuedEnum java1_0 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 100);
 ValuedEnum java1_1 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 110);
 ValuedEnum java1_2 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 120);
 ValuedEnum java1_3 = ValuedEnum.getEnum(VALUED_ENUM_CLASS, 130);

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
 }

 @Test
 public void testGetEnumMap() {
 Map<ValuedEnum, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUED_ENUM_CLASS);
 ValuedEnum java1_0 = enumMap.get(ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0"));
 ValuedEnum java1_1 = enumMap.get(ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1"));
 ValuedEnum java1_2 = enumMap.get(ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2"));
 ValuedEnum java1_3 = enumMap.get(ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_3"));

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
 }

 @Test
 public void testGetEnumList() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUED_ENUM_CLASS);
 ValuedEnum java1_0 = enumList.get(0);
 ValuedEnum java1_1 = enumList.get(1);
 ValuedEnum java1_2 = enumList.get(2);
 ValuedEnum java1_3 = enumList.get(3);

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
 }

 @Test
 public void testGetEnumByValueUsingReflection() throws NoSuchMethodException,
 IllegalAccessException, InvocationTargetException {
 ValuedEnum java1_0 = (ValuedEnum) ValuedEnum.class.getMethod("getEnum,", int.class).invoke(null, 100);
 ValuedEnum java1_1 = (ValuedEnum) ValuedEnum.class.getMethod("getEnum,", int.class).invoke(null, 110);
 ValuedEnum java1_2 = (ValuedEnum) ValuedEnum.class.getMethod("getEnum,", int.class).invoke(null, 120);
 ValuedEnum java1_3 = (ValuedEnum) ValuedEnum.class.getMethod("getEnum,", int.class).invoke(null, 130);

 assertEquals("JavaVersionEnum[Java 1.0=100]", java1_0.toString());
 assertEquals("JavaVersionEnum[Java 1.1=110]", java1_1.toString());
 assertEquals("JavaVersionEnum[Java 1.2=120]", java1_2.toString());
 assertEquals("JavaVersionEnum[Java 1.3=130]", java1_3.toString());
 }

 @Test
 public void testCompareTo() {
 ValuedEnum java1_0 = new ValuedEnum("TestEnum", 1) {
 @Override
 public int compareTo(Object o) {
 return 0;
 }
 };
 ValuedEnum java1_1 = new ValuedEnum("TestEnum", 2) {
 @Override
 public int compareTo(Object o) {
 return 1;
 }
 };

 assertEquals(0, java1_0.compareTo(java1_0));
 assertEquals(-1, java1_0.compareTo(java1_1));
 assertEquals(1, java1_1.compareTo(java1_0));
 }
}