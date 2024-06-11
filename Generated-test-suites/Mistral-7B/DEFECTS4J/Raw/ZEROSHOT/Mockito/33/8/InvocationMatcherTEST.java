package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.matchers.Arguments.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotVerifiedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mock.MockComponent;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private Invocation invocation;
 private MockComponent mock;
 private Location location;
 private Method method;
 private List<Matcher> matchers;
 private PrintSettings printSettings;

 @Before
 public void setUp() throws Exception {
 mock = mock(MockComponent.class);
 location = new Location("InvocationMatcherTest.java", 1);
 method = new Method("testMethod", new Class[] { String.class, Integer.TYPE });
 invocation = new Invocation(mock, method, new Object[] {"testString", 123}, location);
 matchers = Collections.singletonList(Matchers.is("testString"));
 printSettings = new PrintSettings();
 invocationMatcher = new InvocationMatcher(invocation, matchers);
 }

 @Test
 public void testConstructorWithInvocationAndMatchers() {
 InvocationMatcher invocationMatcherWithInvocationAndMatchers = new InvocationMatcher(invocation, matchers);
 assertEquals(invocation, invocationMatcherWithInvocationAndMatchers.getInvocation());
 assertEquals(matchers, invocationMatcherWithInvocationAndMatchers.getMatchers());
 }

 @Test
 public void testConstructorWithInvocationOnly() {
 InvocationMatcher invocationMatcherWithInvocationOnly = new InvocationMatcher(invocation);
 assertEquals(invocation, invocationMatcherWithInvocationOnly.getInvocation());
 assertNull(invocationMatcherWithInvocationOnly.getMatchers());
 }

 @Test
 public void testGetMethod() throws Exception {
 Method method = invocationMatcher.getMethod();
 assertEquals(this.method, method);
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = invocationMatcher.getInvocation();
 assertEquals(this.invocation, invocation);
 }

 @Test
 public void testGetMatchers() {
 List<Matcher> matchers = invocationMatcher.getMatchers();
 assertEquals(this.matchers, matchers);
 }

 @Test
 public void testToString() {
 String toString = invocationMatcher.toString();
 assertTrue(toString.contains(method.getName()));
 assertTrue(toString.contains("testString"));
 }

 @Test
 public void testMatchesInvocation() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation actual = new Invocation(mock, method, new Object[] {"testString", 123}, location);
 assertTrue(invocationMatcher.matches(actual));
 }

 @Test
 public void testDoesNotMatchInvocationWithDifferentMock() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation actual = new Invocation(mock(String.class), method, new Object[] {"testString", 123}, location);
 assertFalse(invocationMatcher.matches(actual));
 }

 @Test(expected = InvocationNotVerifiedException.class)
 public void testDoesNotMatchInvocationWithDifferentMethod() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation actual = new Invocation(mock, new Method("differentMethod", new Class[] { String.class, Integer.TYPE }), new Object[] {"testString", 123}, location);
 assertFalse(invocationMatcher.matches(actual));
 }

 @Test
 public void testHasSimilarMethod() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation candidate = new Invocation(mock, method, new Object[] {"testString", 123}, location);
 assertTrue(invocationMatcher.hasSimilarMethod(candidate));
 }

 @Test
 public void testHasSimilarMethodButDifferentArguments() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation candidate = new Invocation(mock, method, new Object[] {"differentString", 123}, location);
 assertFalse(invocationMatcher.hasSimilarMethod(candidate));
 }

 @Test
 public void testHasSameMethod() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation candidate = new Invocation(mock, method, new Object[] {"testString", 123}, location);
 assertTrue(invocationMatcher.hasSameMethod(candidate));
 }

 @Test
 public void testHasSameMethodButDifferentMethods() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Invocation candidate = new Invocation(mock, new Method("differentMethod", new Class[] { String.class, Integer.TYPE }), new Object[] {"testString", 123}, location);
 assertFalse(invocationMatcher.hasSameMethod(candidate));
 }

 @Test
 public void testToStringWithPrintSettings() {
 String toString = invocationMatcher.toString(printSettings);
 assertTrue(toString.contains("InvocationMatcher"));
 assertTrue(toString.contains("testMethod"));
 assertTrue(toString.contains("testString"));
 }

 @Test
 public void testCaptureArgumentsFromInvocation() throws Exception {
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 Object[] arguments = new Object[] {"testString", 123};
 invocationMatcher.captureArgumentsFrom(new Invocation(mock, method, arguments, location));
 Matcher matcher = argThat(new CapturesArguments<Object>() {
 @Override
 public void captureFrom(Object argument) {
 assertEquals("testString", argument);
 }
 });
 assertTrue(matchers.contains(matcher));
 }

 @Test
 public void testCreateFrom() throws Exception {
 List<Invocation> invocations = Collections.singletonList(invocation);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 InvocationMatcher invocationMatcher = invocationMatchers.get(0);
 assertEquals(invocation, invocationMatcher.getInvocation());
 assertEquals(matchers, invocationMatcher.getMatchers());
 }
}