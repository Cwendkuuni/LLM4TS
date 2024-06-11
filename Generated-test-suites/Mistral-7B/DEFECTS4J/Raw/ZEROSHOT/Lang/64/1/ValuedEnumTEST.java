package org.apache.commons.lang.enums;

import static org.junit.Assert.*;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

 private static final Class<ValuedEnum> VALUEDENUM_CLASS = ValuedEnum.class;

 @SuppressWarnings("unchecked")
 private static <T extends ValuedEnum> T getEnum(Class<T> enumClass, int value) {
 return (T) ValuedEnum.getEnum(enumClass, value);
 }

 @SuppressWarnings("unchecked")
 private static <T extends ValuedEnum> T getEnum(Class<T> enumClass, String name) {
 return (T) ValuedEnum.getEnum(enumClass, name);
 }

 @org.junit.Test
 public void testGetEnumByValue() {
 ValuedEnum java1_0 = getEnum(VALUEDENUM_CLASS, 100);
 ValuedEnum java1_1 = getEnum(VALUEDENUM_CLASS, 110);
 ValuedEnum java1_2 = getEnum(VALUEDENUM_CLASS, 120);

 assertNotNull(java1_0);
 assertNotNull(java1_1);
 assertNotNull(java1_2);

 assertEquals(java1_0.getValue(), 100);
 assertEquals(java1_1.getValue(), 110);
 assertEquals(java1_2.getValue(), 120);
 }

 @org.junit.Test
 public void testGetEnumByName() {
 ValuedEnum java1_0 = getEnum(VALUEDENUM_CLASS, "Java 1.0");
 ValuedEnum java1_1 = getEnum(VALUEDENUM_CLASS, "Java 1.1");
 ValuedEnum java1_2 = getEnum(VALUEDENUM_CLASS, "Java 1.2");

 assertNotNull(java1_0);
 assertNotNull(java1_1);
 assertNotNull(java1_2);

 assertEquals(java1_0.getName(), "Java 1.0");
 assertEquals(java1_1.getName(), "Java 1.1");
 assertEquals(java1_2.getName(), "Java 1.2");
 }

 @org.junit.Test
 public void testGetEnumMap() {
 List<ValuedEnum> enumList = ValuedEnum.getEnumList(VALUEDENUM_CLASS);
 Map<Integer, ValuedEnum> enumMap = ValuedEnum.getEnumMap(VALUEDENUM_CLASS);

 assertEquals(enumList.size(), enumMap.size());
 for (ValuedEnum enumItem : enumList) {
 ValuedEnum mappedEnum = enumMap.get(enumItem.getValue());
 assertEquals(enumItem, mappedEnum);
 }
 }

 @org.junit.Test
 public void testToString() {
 ValuedEnum java1_0 = getEnum(VALUEDENUM_CLASS, "Java 1.0");
 ValuedEnum java1_1 = getEnum(VALUEDENUM_CLASS, "Java 1.1");
 ValuedEnum java1_2 = getEnum(VALUEDENUM_CLASS, "Java 1.2");

 assertEquals(java1_0.toString(), "ValuedEnum[Java 1.0=100]");
 assertEquals(java1_1.toString(), "ValuedEnum[Java 1.1=110]");
 assertEquals(java1_2.toString(), "ValuedEnum[Java 1.2=120]");
 }

 @org.junit.Test
 public void testCompareTo() {
 ValuedEnum java1_0 = getEnum(VALUEDENUM_CLASS, 100);
 ValuedEnum java1_1 = getEnum(VALUEDENUM_CLASS, 110);
 ValuedEnum java1_2 = getEnum(VALUEDENUM_CLASS, 120);

 assertEquals(java1_0.compareTo(java1_1), -10);
 assertEquals(java1_1.compareTo(java1_0), 10);
 assertEquals(java1_0.compareTo(java1_0), 0);
 assertEquals(java1_1.compareTo(java1_1), 0);
 assertEquals(java1_1.compareTo(java1_2), -1);
 assertEquals(java1_2.compareTo(java1_1), 1);
 }

 @org.junit.Test
 public void testGetEnumByClassAndValue() {
 Class<?> enumClass = Class.forName("org.apache.commons.lang.enums.TestValuedEnum");
 ValuedEnum testValuedEnum1 = getEnum((Class<ValuedEnum>) enumClass, 1);
 ValuedEnum testValuedEnum2 = getEnum((Class<ValuedEnum>) enumClass, 2);

 assertNotNull(testValuedEnum1);
 assertNotNull(testValuedEnum2);

 assertEquals(testValuedEnum1.getName(), "TestValuedEnum_1");
 assertEquals(testValuedEnum1.getValue(), 1);
 assertEquals(testValuedEnum2.getName(), "TestValuedEnum_2");
 assertEquals(testValuedEnum2.getValue(), 2);
 }

 @org.junit.Test
 public void testGetEnumByClassAndName() {
 Class<?> enumClass = Class.forName("org.apache.commons.lang.enums.TestValuedEnum");
 ValuedEnum testValuedEnum1 = getEnum((Class<ValuedEnum>) enumClass, "TestValuedEnum_1");
 ValuedEnum testValuedEnum2 = getEnum((Class<ValuedEnum>) enumClass, "TestValuedEnum_2");

 assertNotNull(testValuedEnum1);
 assertNotNull(testValuedEnum2);

 assertEquals(testValuedEnum1.getName(), "TestValuedEnum_1");
 assertEquals(testValuedEnum1.getValue(), 1);
 assertEquals(testValuedEnum2.getName(), "TestValuedEnum_2");
 assertEquals(testValuedEnum2.getValue(), 2);
 }
}