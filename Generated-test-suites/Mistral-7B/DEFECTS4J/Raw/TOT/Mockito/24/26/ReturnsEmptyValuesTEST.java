package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer;

 @Before
 public void setup() {
 answer = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringMethod() {
 Object mock = mock(String.class);
 InvocationOnMock invocation = Mockito.method(String.class, "toString");
 String expectedResult = "Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode();
 Object result = answer.answer(invocation);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = mock(Comparable.class);
 Object mock2 = mock(Comparable.class);
 InvocationOnMock compareToInvocation1 = Mockito.method(Comparable.class, "compareTo", ArgumentMatchers.any());
 InvocationOnMock compareToInvocation2 = Mockito.method(Comparable.class, "compareTo", ArgumentMatchers.any());
 Object result1 = answer.answer(compareToInvocation1.withArguments(mock1));
 Object result2 = answer.answer(compareToInvocation2.withArguments(mock2));
 verify(mock1).equals(mock2); // mocks are equal, so compareTo() should return 0
 assertEquals(0, (Integer) result1);
 assertEquals(1, (Integer) result2);
 }

 @Test
 public void testCollectionReturningMethod() {
 Collection<Integer> collection = Mockito.spy(new ArrayList<Integer>());
 InvocationOnMock invocation = Mockito.method(Collection.class, "isEmpty");
 Object result = answer.answer(invocation.withArguments(collection));
 assertTrue((Boolean) result);
 }

 @Test
 public void testSetReturningMethod() {
 Set<Integer> set = Mockito.spy(new HashSet<Integer>());
 InvocationOnMock invocation = Mockito.method(Set.class, "isEmpty");
 Object result = answer.answer(invocation.withArguments(set));
 assertTrue((Boolean) result);
 }

 @Test
 public void testMapReturningMethod() {
 Map<String, Integer> map = Mockito.spy(new HashMap<String, Integer>());
 InvocationOnMock invocation = Mockito.method(Map.class, "isEmpty");
 Object result = answer.answer(invocation.withArguments(map));
 assertTrue((Boolean) result);
 }

 @Test(expected = AssertionError.class)
 public void testNullReturningMethod() {
 Object mock = mock(Object.class);
 InvocationOnMock invocation = Mockito.method(Object.class, "toString");
 Object result = answer.answer(invocation);
 assertNotNull(result);
 }

 @Test
 public void testCustomClassReturningMethod() {
 MyClass customClassMock = mock(MyClass.class);
 InvocationOnMock invocation = Mockito.method(MyClass.class, "myMethod");
 Object result = answer.answer(invocation);
 assertNull(result);
 }
}

class MyClass {
 // empty class for testing purposes
}