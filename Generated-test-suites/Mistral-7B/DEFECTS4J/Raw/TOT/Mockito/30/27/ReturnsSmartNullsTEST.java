package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private FakeClass fakeClass;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 fakeClass = mock(FakeClass.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedNullMethod() {
 // Given: unstubbed method returns null
 when(fakeClass.getNullMethod()).thenReturn(null);

 // When: calling unstubbed method
 Object result = fakeClass.getNullMethod();

 // Then: ReturnsSmartNull is returned instead of null
 assertEquals("SmartNull returned by unstubbed getNullMethod() method on mock", result);
 }

 @Test
 public void testReturnsDefaultValueForStubbedMethod() {
 // Given: method is stubbed
 when(fakeClass.getStubbedMethod()).thenReturn("default value");

 // When: calling stubbed method
 Object result = fakeClass.getStubbedMethod();

 // Then: default value is returned
 assertEquals("default value", result);
 }

 @Test(expected = NullPointerException.class)
 public void testReturnsSmartNullForUnstubbedNonMockableMethod() {
 // Given: method is not mockable
 final String NON_MOCKABLE_CLASS = "NonMockableClass";
 when(fakeClass.newNonMockableClass()).thenReturn(new NON_MOCKABLE_CLASS());

 // When: calling unstubbed non-mockable method
 fakeClass.newNonMockableClass().doSomething();
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethodWithArguments() {
 // Given: unstubbed method takes arguments
 when(fakeClass.getNullMethodWithArguments(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()))
 .thenReturn(null);

 // When: calling unstubbed method with arguments
 Object[] arguments = new Object[]{1, "test"};
 Object result = fakeClass.getNullMethodWithArguments(arguments[0], arguments[1]);

 // Then: ReturnsSmartNull is returned instead of null
 assertEquals("SmartNull returned by unstubbed getNullMethodWithArguments(int, String) method on mock", result);
 }

 @Test
 public void testToStringMethodReturnsSmartNull() {
 // Given: unstubbed toString method is called
 when(fakeClass.toString()).thenReturn(null);

 // When: calling toString method
 String result = fakeClass.toString();

 // Then: ReturnsSmartNull is returned instead of null
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
 }
}

class FakeClass {
 private int intField;
 private String stringField;

 public IntMethod getNullMethod() { return null; }
 public IntMethod getStubbedMethod() { return new IntMethod(); }
 public NonMockableClass newNonMockableClass() { return new NonMockableClass(); }
 public IntMethodWithArguments getNullMethodWithArguments(int intArg, String stringArg) { return null; }

 public String toString() { return null; }

 public static class IntMethod {}
 public static class NonMockableClass {
 public void doSomething() {}
 }
}