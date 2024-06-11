package org.mockito.internal.invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Arguments;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 static class TestClass {

 void testMethod(String arg1, int arg2, Object[] arg3) {
 }

 void otherMethod(String arg1, int arg2) {
 }
}

 @Test
 public void testCreationWithEmptyMatchersList() {
 TestClass mockedObject = mock(TestClass.class);
 Invocation invocation = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test", 1, new Object[]{1, 2}}, new Location(7, 8));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 assertTrue(invocationMatcher.matches(invocation));
 }

 @Test
 public void testCreationWithCustomMatchersList() {
 TestClass mockedObject = mock(TestClass.class);
 Invocation invocation = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test", 1, new Object[]{1, 2}}, new Location(7, 8));
 List<Matcher> matchersList = Arrays.asList(eq("test"), Matchers.is(1), new VarargMatcher(Matchers.hasItem(Matchers.is(1))));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchersList);

 assertTrue(invocationMatcher.matches(invocation));
 }

 @Test
 public void testMatchesTwoSameInvocations() {
 TestClass mockedObject = mock(TestClass.class);
 Invocation invocation1 = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test1", 1, new Object[]{1, 2}}, new Location(7, 8));
 Invocation invocation2 = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test1", 1, new Object[]{1, 2}}, new Location(7, 9));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

 assertTrue(invocationMatcher.matches(invocation2));
 }

 @Test
 public void testMatchesTwoDifferentInvocations() {
 TestClass mockedObject = mock(TestClass.class);
 Invocation invocation1 = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test1", 1, new Object[]{1, 2}}, new Location(7, 8));
 Invocation invocation2 = new InvocationOnMock(mockedObject, TestClass.class.getMethod("otherMethod", String.class, int.class), new Object[]{"test2", 2}, new Location(7, 9));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

 assertFalse(invocationMatcher.matches(invocation2));
 }

 @Test
 public void testMatchesInvocationWithVarargs() {
 TestClass mockedObject = spy(TestClass.class);
 Invocation invocation = new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), new Object[]{"test", 1}, new Location(7, 8));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.is("test"), Matchers.is(1), new VarargMatcher(Arrays.asList(Matchers.is(1), Matchers.is(2)))));

 try {
 mockedObject.testMethod("test", 1, new Object[]{1, 2});
 } catch (InvocationTargetException e) {
 Arguments arguments = e.getArgs();
 invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mockedObject, TestClass.class.getMethod("testMethod", String.class, int.class, Object[].class), arguments, new Location(7, 8)));
 verify(mockedObject).testMethod("test", 1, new Object[]{1, 2});
 }
 }

 @Test
 public void testMatchesInvocationWithNoVarargs() {
 TestClass mockedObject = mock(TestClass.class);
 Invocation invocation = new InvocationOnMock(mockedObject, TestClass.class.getMethod("otherMethod", String.class, int.class), new Object[]{"test", 1}, new Location(7, 8));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(Matchers.is("test"), Matchers.is(1)));

 verifyZeroInteractions(mockedObject);
 invocationMatcher.captureArgumentsFrom(invocation);
 verify(mockedObject).otherMethod("test", 1);
 }
}