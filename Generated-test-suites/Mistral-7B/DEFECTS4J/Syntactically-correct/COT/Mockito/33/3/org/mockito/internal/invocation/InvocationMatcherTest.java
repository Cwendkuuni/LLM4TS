package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.base.MockitoBaseException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocation.PrintableInvocation;
import org.mockito.internal.reporting.PrintingFriendlyInvocation.PrintSettings;
import org.mockito.internal.util.reflection.Whitebox;

public class InvocationMatcherTest {

 private static final Class<InvocationMatcher> INVOCATION_MATCHER_CLASS = InvocationMatcher.class;

 @Test
 public void testConstructorWithInvocationAndEmptyMatcherList() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 assertEquals(invocation, invocationMatcher.getInvocation());
 assertTrue(invocationMatcher.getMatchers().isEmpty());
 }

 @Test
 public void testConstructorWithInvocationAndMatcherList() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Matchers.eq("expectedArg1"), Matchers.eq("expectedArg2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 assertEquals(invocation, invocationMatcher.getInvocation());
 assertFalse(invocationMatcher.getMatchers().isEmpty());
 }

 @Test
 public void testGetMethod() throws NoSuchMethodException {
 Invocation invocation = mock(Invocation.class);
 Method method = mock(Method.class);
 when(invocation.getMethod()).thenReturn(method);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 assertEquals(method, invocationMatcher.getMethod());
 }

 @Test(expected = NoSuchMethodException.class)
 public void testGetMethodThrowsExceptionWhenInvocationHasNoMethod() {
 Invocation invocation = mock(Invocation.class, new MockitoBaseException("Invocation has no method"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 invocationMatcher.getMethod();
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = mock(Invocation.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 assertEquals(invocation, invocationMatcher.getInvocation());
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Matchers.eq("expectedArg1"), Matchers.eq("expectedArg2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 assertFalse(invocationMatcher.getMatchers().isEmpty());
 }

 @Test
 public void testToString() throws Exception {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Matchers.eq("expectedArg1"), Matchers.eq("expectedArg2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 PrintSettings printSettings = new PrintSettings();
 assertEquals(invocation.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
 }

 @Test
 public void testMatches() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
 InvocationMatcher otherInvocationMatcher = new InvocationMatcher(invocation2);
 assertTrue(invocationMatcher.matches(invocation1));
 assertFalse(invocationMatcher.matches(invocation2));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), new Object[]{1, 2});
 assertTrue(invocationMatcher.safelyArgumentsMatch(new Object[]{1, 2}));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchThrowsException() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), new Object[]{1, 2});
 assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{3, 3}));
 }

 @Test
 public void testHasSimilarMethod() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testHasSimilarMethodReturnsFalse() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testHasSameMethod() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 when(invocation1.getMethod()).thenReturn(method1);
 when(invocation2.getMethod()).thenReturn(method2);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
 }

 @Test
 public void testHasSameMethodReturnsFalse() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 Method method1 = mock(Method.class);
 Method method2 = mock(Method.class);
 when(invocation1.getMethod()).thenReturn(method1);
 when(invocation2.getMethod()).thenReturn(method2);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
 assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
 }

 @Test
 public void testGetLocation() {
 Invocation invocation = mock(Invocation.class);
 Location location = mock(Location.class);
 when(invocation.getLocation()).thenReturn(location);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 assertEquals(location, invocationMatcher.getLocation());
 }

 @Test
 public void testToStringWithPrintSettings() throws Exception {
 Invocation invocation = mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Matchers.eq("expectedArg1"), Matchers.eq("expectedArg2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 PrintSettings printSettings = new PrintSettings();
 assertEquals(invocation.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 Invocation invocation = mock(Invocation.class, new Object[]{1, 2, 3});
 List<Matcher> matchers = Arrays.asList(CaptureArgument("capturedArg1"), CaptureArgument("capturedArg2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 ArgumentCaptor<CapturesArguments> captor = argumentCaptor(CaptureArguments.class);
 verify(invocation).getArguments();
 invocationMatcher.captureArgumentsFrom(invocation);
 verify(invocation).getArguments();
 CapturesArguments capturedArguments = captor.getValue();
 assertEquals("expectedArg1", capturedArguments.getValue()[0]);
 assertEquals("expectedArg2", capturedArguments.getValue()[1]);
 }

 @Test
 public void testCreateFrom() {
 Invocation invocation1 = mock(Invocation.class);
 Invocation invocation2 = mock(Invocation.class);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));
 assertEquals(1, invocationMatchers.size());
 assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation1)));
 assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation2)));
 }

 @Test
 public void testCreateFromEmptyList() {
 List<Invocation> invocations = Collections.emptyList();
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 assertTrue(invocationMatchers.isEmpty());
 }
}