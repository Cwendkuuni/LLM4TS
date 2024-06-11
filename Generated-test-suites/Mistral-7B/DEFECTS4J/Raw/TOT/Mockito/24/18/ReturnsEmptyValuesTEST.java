package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private Object mock;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class);
 returnsEmptyValues = new ReturnsEmptyValues();
 Mockito.doAnswer(returnsEmptyValues).when(mock).toString();
 }

 @Test
 public void testToStringMethod() {
 String expected = "Mock for Object, hashCode: " + mock.hashCode();
 String actual = (String) Mockito.invokeMethod(mock, "toString");
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToMethod() {
 Object anotherMock = Mockito.mock(Object.class);
 int comparisonResult = Mockito.invokeMethod(mock, "compareTo", ArgumentMatchers.any(Object.class), Mockito.eq(anotherMock));
 assertTrue(comparisonResult > 0);
 }

 @Test
 public void testPrimitiveTypes() {
 int intValue = (int) Mockito.invokeMethod(mock, "intValue", ArgumentMatchers.anyInt());
 int expectedIntValue = 0;
 assertEquals(expectedIntValue, intValue);

 double doubleValue = (double) Mockito.invokeMethod(mock, "doubleValue", ArgumentMatchers.anyDouble());
 double expectedDoubleValue = 0.0;
 assertEquals(expectedDoubleValue, doubleValue, 0.0);

 boolean booleanValue = (boolean) Mockito.invokeMethod(mock, "booleanValue", ArgumentMatchers.anyBoolean());
 boolean expectedBooleanValue = false;
 assertEquals(expectedBooleanValue, booleanValue);
 }

 @Test
 public void testCollectionTypes() {
 Collection<String> collection = (Collection<String>) Mockito.invokeMethod(mock, "values");
 assertTrue(collection.isEmpty());

 Set<Integer> set = (Set<Integer>) Mockito.invokeMethod(mock, "keySet");
 assertTrue(set.isEmpty());

 Map<Integer, String> map = (Map<Integer, String>) Mockito.invokeMethod(mock, "entrySet");
 assertTrue(map.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyCollection() {
 Collection<String> collection = (Collection<String>) Mockito.invokeMethod(mock, "values");
 collection.add("test"); //Should throw UnsupportedOperationException
 }
}