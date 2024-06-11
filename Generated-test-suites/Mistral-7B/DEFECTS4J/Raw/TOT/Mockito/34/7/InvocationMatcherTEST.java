import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.reporting.PrintSettings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

 private Object mock;
 private InvocationMatcher invocationMatcher;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class);
 invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[0]));
 }

 @Test
 public void testMatchInvocationWithSameMethodAndArguments() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"});
 InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.singletonList(ArgumentMatchers.eq("test")));

 assertTrue(invocationMatcher.matches(matcher));
 }

 @Test
 public void testMatchInvocationWithDifferentMethod() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest2(), new Object[]{"test"});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[0]));

 assertFalse(invocationMatcher.matches(matcher));
 }

 @Test
 public void testMatchInvocationWithSameMethodButDifferentArguments() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test1"});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[]{"test2"}));

 assertFalse(invocationMatcher.matches(matcher));
 }

 @Test
 public void testMatchInvocationWithSameMethodAndDifferentArgumentsButSameValues() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{new Integer(1)});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[]{new Integer(1)}));

 assertTrue(invocationMatcher.matches(matcher));
 }

 @Test
 public void testMatchInvocationWithSameMethodAndDifferentArgumentsButDifferentTypes() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{new Integer(1)});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"}));

 assertFalse(invocationMatcher.matches(matcher));
 }

 @Test
 public void testMatchInvocationWithSameMethodButArgumentsCaptured() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"});
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

 invocationMatcher.captureArgumentsFrom(invocation);
 verify(mock).method(argumentCaptor.capture());

 assertTrue(invocationMatcher.matches(new InvocationMatcher(invocation, Collections.singletonList(argumentCaptor.getValue()))));
 }

 @Test
 public void testMatchInvocationWithSameMethodButArgumentsCapturedButDifferentTypes() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"});
 ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);

 invocationMatcher.captureArgumentsFrom(invocation);
 verify(mock).method(ArgumentMatchers.isA(Integer.class), argumentCaptor.capture());

 assertFalse(invocationMatcher.matches(new InvocationMatcher(invocation, Collections.singletonList(argumentCaptor.getValue()))));
 }

 @Test
 public void testMatchInvocationWithUnverifiedInvocation() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest(), new Object[0]));

 assertTrue(invocationMatcher.matches(matcher));
 }

 @Test(expected = AssertionError.class)
 public void testMatchInvocationWithUnmatchingInvocationThrowsAssertionError() {
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTest(), new Object[]{"test"});
 InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMock(mock, new MethodTest2(), new Object[0]));

 invocationMatcher.matches(matcher);
 }

 private class MethodTest {
 static void method(String arg) {
 }
 }

 private class MethodTest2 {
 static void method(Integer arg) {
 }
 }
}