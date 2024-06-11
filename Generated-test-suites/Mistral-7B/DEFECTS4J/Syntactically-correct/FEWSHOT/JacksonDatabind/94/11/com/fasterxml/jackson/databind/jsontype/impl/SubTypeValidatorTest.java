package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.impl.SubTypeValidator;

@RunWith(Parameterized.class)
@MethodSorters(MethodSorters.NAME_ASCENDING)
public class SubTypeValidatorTest {

 private final String className;
 private final boolean expectedResult;

 public SubTypeValidatorTest(String className, boolean expectedResult) {
 this.className = className;
 this.expectedResult = expectedResult;
 }

 @Parameterized.Parameters(name = "{index}: validateSubType({0}) should return {1}")
 public static Iterable<Object[]> data() {
 return new IteratorTestDataGenerator().generateData(SubTypeValidator.class, "validateSubType",
 new Class[] { DeserializationContext.class, JavaType.class }, new Object[] { null, null });
 }

 @Test
 public void testValidateSubType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = new TestDeserializationContext();
 JavaType type = context.constructType(className);
 validator.validateSubType(context, type);
 assertEquals(expectedResult, true);
 }

 @Test(expected = JsonMappingException.class)
 public void testValidateSubTypeWithIllegalType() throws Exception {
 SubTypeValidator validator = SubTypeValidator.instance();
 DeserializationContext context = new TestDeserializationContext();
 JavaType type = context.constructType(className);
 validator.validateSubType(context, type);
 fail("Expected JsonMappingException but it did not occur");
 }

 @Test
 public void testGetInstance() throws Exception {
 SubTypeValidator validator1 = SubTypeValidator.instance();
 SubTypeValidator validator2 = SubTypeValidator.instance();
 assertTrue(validator1 == validator2);
 }

 private static class IteratorTestDataGenerator {
 public Iterator<Object[]> generateData(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object[] values) {
 try {
 Method method = clazz.getDeclaredMethod(methodName, paramTypes);
 method.setAccessible(true);
 return new ReflectiveArrayGenerator().generateData(method, values);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
 }

 private static class ReflectiveArrayGenerator {
 public Iterator<Object[]> generateData(Method method, Object[] values) throws NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException, IllegalAccessException {
 int len = Array.getLength(values);
 Object[] result = new Object[len * 2];
 for (int i = 0; i < len; i++) {
 Object arg = values[i];
 Object obj = method.invoke(null, arg);
 int index = i * 2;
 result[index] = arg;
 result[index + 1] = obj;
 }
 return Arrays.asList(result).iterator();
 }
}

 static class TestDeserializationContext extends DeserializationContext {
 @Override
 public JavaType constructType(Class<?> rawType) {
 return super.constructType(rawType);
 }
}
}