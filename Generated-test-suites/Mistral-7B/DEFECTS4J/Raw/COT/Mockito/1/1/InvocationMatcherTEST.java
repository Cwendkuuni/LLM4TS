import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentFinder;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CaptureType;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.util.ArgumentsProcessor;
import org.mockito.internal.util.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.Invocation;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 @Test
 public void testConstructorWithInvocationAndEmptyList() {
 Invocation invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 assertThat(invocationMatcher.getInvocation(), is(invocation));
 assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
 }

 @Test
 public void testConstructorWithInvocationAndMatcherList() {
 Invocation invocation = mock(InvocationOnMockImpl.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.<Matcher>asList(eq("expectedValue")));

 assertThat(invocationMatcher.getInvocation(), is(invocation));
 assertThat(invocationMatcher.getMatchers(), hasItem(eq("expectedValue")));
 }

 @Test
 public void testGetMethod() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new MethodTestClass(), "testMethod", new Object[0]), Collections.<Matcher>emptyList());

 Method method = invocationMatcher.getMethod();

 assertThat(method.getName(), is("testMethod"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCaptureArgumentsFromVarargsInvocation() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mock(InvocationOnMock.class));
 InvocationOnMock invocation = mockInvocationWithVarargs();
 invocationMatcher.captureArgumentsFrom(invocation);
 }

 @Test
 public void testCaptureArgumentsFromInvocationWithMatchers() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(mock(InvocationOnMock.class), Arrays.<Matcher>asList(eq("expectedValue1"), eq("expectedValue2")));
 Invocation invocation = mockInvocationWithArguments("arg1", "arg2");
 invocationMatcher.captureArgumentsFrom(invocation);

 Matcher matcher1 = ArgumentFinder.getMatcherFromInvocation(invocationMatcher, 0);
 Matcher matcher2 = ArgumentFinder.getMatcherFromInvocation(invocationMatcher, 1);

 assertThat(matcher1, is(eq("expectedValue1")));
 assertThat(matcher2, is(eq("expectedValue2")));
 }

 @Test
 public void testArgumentsMatchWhenArgumentsAreSame() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object[0]), Arrays.<Matcher>asList(any(String.class)));
 Invocation invocation = mockInvocationWithArguments("expectedValue");

 boolean result = invocationMatcher.matches(invocation);

 assertThat(result, is(true));
 }

 @Test
 public void testArgumentsMatchWhenArgumentsAreDifferent() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object[0]), Arrays.<Matcher>asList(eq("expectedValue1")));
 Invocation invocation = mockInvocationWithArguments("expectedValue2");

 boolean result = invocationMatcher.matches(invocation);

 assertThat(result, is(false));
 }

 @Test
 public void testSafelyArgumentsMatchWhenArgumentsAreTheSame() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object[0]), Arrays.<Matcher>asList(any(String.class)));
 Object[] actualArguments = new Object[]{"expectedValue"};

 boolean result = invocationMatcher.safelyArgumentsMatch(actualArguments);

 assertThat(result, is(true));
 }

 @Test(expected = InvocationTargetException.class)
 public void testSafelyArgumentsMatchWhenArgumentsAreDifferent() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object[0]), Arrays.<Matcher>asList(any(String.class)));
 Object[] actualArguments = new Object[]{"differentValue"};

 invocationMatcher.safelyArgumentsMatch(actualArguments);
 }

 @Test
 public void testHasSimilarMethodWhenMethodsAreTheSame() {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod1", new Class[0])), Collections.<Matcher>emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod1", new Class[0])), Collections.<Matcher>emptyList());

 boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

 assertThat(result, is(true));
 }

 @Test
 public void testHasSimilarMethodWhenMethodsAreDifferent() {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod1", new Class[0])), Collections.<Matcher>emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod2", new Class[0])), Collections.<Matcher>emptyList());

 boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

 assertThat(result, is(false));
 }

 @Test
 public void testHasSameMethodWhenMethodsAreTheSame() {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod", new Class[0])), Collections.<Matcher>emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod", new Class[0])), Collections.<Matcher>emptyList());

 boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

 assertThat(result, is(true));
 }

 @Test
 public void testHasSameMethodWhenMethodsAreDifferent() {
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod1", new Class[0])), Collections.<Matcher>emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method("testMethod2", new Class[0])), Collections.<Matcher>emptyList());

 boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

 assertThat(result, is(false));
 }

 @Test
 public void testToString() {
 InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object[0], new Method("testMethod", new Class[0])), Arrays.<Matcher>asList(eq("expectedValue1"), eq("expectedValue2")));

 String result = invocationMatcher.toString();

 assertThat(result, notNullValue());
 }

 private Invocation mockInvocationWithArguments(Object... arguments) {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getArguments()).thenReturn(arguments);
 return invocation;
 }

 private Invocation mockInvocationWithVarargs() {
 Invocation invocation = mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(ArgumentMatchers.anyString(), ArgumentMatchers.arrayOf(Object.class)));
 when(invocation.getArguments()).thenReturn(new Object[]{"arg1", new Object[]{}});
 return invocation;
 }
}