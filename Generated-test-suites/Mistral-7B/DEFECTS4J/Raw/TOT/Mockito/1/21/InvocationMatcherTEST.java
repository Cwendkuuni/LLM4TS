package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

private MockitoMock mock;
private InvocationMatcher matcher;

@Before
public void setup() {
mock = new MockitoMock();
matcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.emptyList());
}

@Test
public void test_matches_same_invocation() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, String.class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{1, "test"});

matcher.matches(testInvocation);

assertTrue(matcher.matches(testInvocation));
}

@Test
public void test_matches_different_method() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod2", new Class[]{String.class}));

assertFalse(matcher.matches(testInvocation));
}

@Test
public void test_matches_different_mock() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMock()).thenReturn(new Object());

assertFalse(matcher.matches(testInvocation));
}

@Test
public void test_matches_different_arguments() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, String.class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{2, "test2"});

assertFalse(matcher.matches(testInvocation));
}

@Test
public void test_matches_unverified_invocation() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, String.class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{1, "test"});
Mockito.verifyZeroInteractions(mock);

assertTrue(matcher.matches(testInvocation));
}

@Test
public void test_matches_verified_invocation() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, String.class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{1, "test"});
Mockito.verify(mock).testMethod(1, "test");

assertTrue(matcher.matches(testInvocation));
}

@Test
public void test_matches_overloaded_method() {
Invocation testInvocation1 = Mockito.mock(Invocation.class);
when(testInvocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class}));
when(testInvocation1.getArguments()).thenReturn(new Object[]{1});

Invocation testInvocation2 = Mockito.mock(Invocation.class);
when(testInvocation2.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, String.class}));
when(testInvocation2.getArguments()).thenReturn(new Object[]{1, "test"});

assertFalse(matcher.matches(testInvocation1));

ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);
Mockito.verify(mock).testMethod(invocationCaptor.capture());
Invocation capturedInvocation = invocationCaptor.getValue();

assertTrue(matcher.matches(capturedInvocation));
}

@Test
public void test_matches_varargs() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int.class, int[].class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{1, new int[]{1, 2, 3}});

matcher = new InvocationMatcher(testInvocation);

assertTrue(matcher.matches(testInvocation));
}

@Test(expected = UnsupportedOperationException.class)
public void test_capture_arguments_from_varargs() {
Invocation testInvocation = Mockito.mock(Invocation.class);
when(testInvocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{int[].class}));
when(testInvocation.getArguments()).thenReturn(new Object[]{new int[]{1, 2, 3}});

matcher.captureArgumentsFrom(testInvocation);
}
}

class MockitoMock {
}