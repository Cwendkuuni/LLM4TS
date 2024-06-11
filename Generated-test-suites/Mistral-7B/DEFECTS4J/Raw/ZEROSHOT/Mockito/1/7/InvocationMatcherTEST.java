package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.junit.MockitoJUnitRunner;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private Invocation invocation;
 private InvocationMatcher matcher;
 private Object mock;

 @Before
 public void setUp() {
 mock = mock(String.class);
 invocation = Mockito.mock(DescribedInvocation.class);
 when(invocation.getInvocation()).thenReturn(new InvocationOnMock(mock, new Method("toString"), new Object[0], null));
 when(invocation.getMethod()).thenReturn(new Method("toString", new Class[0]));
 when(invocation.getLocation()).thenReturn(new Location("test.java", 1, 1));
 }

 @Test
 public void testInvocationMatcherConstructorWithMatchers() {
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(ArgumentMatchers.isA(String.class));
 matchers.add(ArgumentMatchers.isA(Integer.class));
 matchers.add(ArgumentMatchers.isNull());

 matcher = new InvocationMatcher(invocation, matchers);

 assertEquals("toString()", matcher.getMethod().getName());
 assertEquals(invocation, matcher.getInvocation());
 assertEquals(matchers, matcher.getMatchers());
 }

 @Test
 public void testInvocationMatcherConstructorWithoutMatchers() {
 matcher = new InvocationMatcher(invocation);

 assertEquals("toString()", matcher.getMethod().getName());
 assertEquals(invocation, matcher.getInvocation());
 assertEquals(Collections.emptyList(), matcher.getMatchers());
 }

 @Test
 public void testMatches() {
 Invocation anotherInvocation = Mockito.mock(DescribedInvocation.class);
 when(anotherInvocation.getInvocation()).thenReturn(new InvocationOnMock(mock, new Method("toString"), new Object[0], null));
 when(anotherInvocation.getMethod()).thenReturn(new Method("toString", new Class[0]));

 matcher = new InvocationMatcher(invocation);

 assertTrue(matcher.matches(invocation));
 assertFalse(matcher.matches(anotherInvocation));
 }

 @Test
 public void testHasSameMethod() {
 Invocation anotherInvocation = Mockito.mock(DescribedInvocation.class);
 when(anotherInvocation.getInvocation()).thenReturn(new InvocationOnMock(mock, new Method("toString", new Class[]{String.class}), new Object[]{mock}, null));

 matcher = new InvocationMatcher(invocation);

 assertTrue(matcher.hasSameMethod(invocation));
 assertFalse(matcher.hasSameMethod(anotherInvocation));
 }

 @Test
 public void testToString() {
 ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
 mock.toString();
 verify(mock).toString();
 matcher = new InvocationMatcher(invocation);
 String toString = matcher.toString();
 assertEquals("toString()", toString);
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
 mock.toString(stringCaptor.capture(), integerCaptor.capture());
 matcher = new InvocationMatcher(invocation);
 matcher.captureArgumentsFrom(invocation);

 assertEquals("toString", invocation.getMethod().getName());
 assertEquals(2, invocation.getRawArguments().length);
 assertEquals(stringCaptor.getValue(), invocation.getArguments()[0]);
 assertEquals(integerCaptor.getValue(), invocation.getArguments()[1]);

 Matcher stringMatcher = ArgumentMatchers.argThat(is(equalTo(stringCaptor.getValue())));
 Matcher integerMatcher = ArgumentMatchers.argThat(is(equalTo(integerCaptor.getValue())));
 assertEquals(Collections.singletonList(stringMatcher), matcher.getMatchers());
 assertEquals(Collections.singletonList(integerMatcher), matcher.getMatchers());
 }

 @Test
 public void testCaptureArgumentsFromInvocationWithVarargs() {
 String[] strings = new String[]{"test1", "test2"};
 mock.toString(strings);
 matcher = new InvocationMatcher(invocation);
 matcher.captureArgumentsFrom(invocation);

 assertEquals("toString", invocation.getMethod().getName());
 assertEquals(3, invocation.getRawArguments().length);
 assertEquals(strings, invocation.getArguments()[0]);

 ArgumentCaptor<String[]> stringArrayCaptor = ArgumentCaptor.forClass(String[].class);
 Matcher stringArrayMatcher = ArgumentMatchers.argThat(is(equalTo(stringArrayCaptor.capture())));
 assertEquals(Collections.singletonList(stringArrayMatcher), matcher.getMatchers());
 assertEquals(strings, stringArrayCaptor.getValue());
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = new LinkedList<>();
 invocations.add(invocation);
 invocations.add(invocation);

 List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
 assertEquals(2, matchers.size());
 }
}