import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private Object mockObject;

 @Before
 public void setup() {
 mockObject = mock(Object.class);
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{}));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 assertEquals("InvocationMatcher[testMethod(Object)]", invocationMatcher.toString());
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int", "String"}));
 List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.notNull(), ArgumentsMatcher.isInstanceOf(String.class));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

 assertEquals("InvocationMatcher[testMethod(Object, String)]", invocationMatcher.toString());
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int", "String"}));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Method method = invocationMatcher.getMethod();

 assertEquals(new Method(Object.class, "testMethod", new Class[]{"int", "String"}), method);
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Invocation actualInvocation = invocationMatcher.getInvocation();

 assertEquals(invocation, actualInvocation);
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 List<Matcher> matchers = Arrays.asList(ArgumentsMatcher.notNull(), ArgumentsMatcher.isInstanceOf(String.class));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

 List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();

 assertEquals(matchers, matchersFromInvocationMatcher);
 }

 @Test
 public void testMatchesWithSameInvocation() {
 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
 when(invocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation2.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation1.getMock()).thenReturn(mockObject);
 when(invocation2.getMock()).thenReturn(mockObject);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

 assertTrue(invocationMatcher1.matches(invocationMatcher2));
 }

 @Test
 public void testMatchesWithDifferentInvocations() {
 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
 when(invocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation2.getMock()).thenReturn(mockObject);
 when(invocation2.getMethod()).thenReturn(new Method(Object.class, "testOtherMethod", new Class[]{"int"}));
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

 assertFalse(invocationMatcher1.matches(invocationMatcher2));
 }

 @Test
 public void testMatchesWithOverloadedMethods() {
 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
 when(invocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation2.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"String"}));
 when(invocation1.getMock()).thenReturn(mockObject);
 when(invocation2.getMock()).thenReturn(mockObject);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

 assertFalse(invocationMatcher1.matches(invocationMatcher2));
 }

 @Test
 public void testMatchesWithVerifiedInvocation() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation.isVerified()).thenReturn(true);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 assertFalse(invocationMatcher.matches(Mockito.mock(InvocationMatcher.class)));
 }

 @Test
 public void testHasSimilarMethodWithSameMethod() {
 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
 when(invocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 when(invocation2.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"int"}));
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

 assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testHasSimilarMethodWithDifferentMethods() {
 Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
 Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
 when(invocation1.getMethod()).thenReturn(new Method(Object.class, "testMethod1", new Class[]{"int"}));
 when(invocation2.getMethod()).thenReturn(new Method(Object.class, "testMethod2", new Class[]{"String"}));
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

 assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testSafelyArgumentsMatchWithMatchingArguments() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Object[] arguments = new Object[]{1, "test"};
 when(invocation.getArguments()).thenReturn(arguments);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchWithNonMatchingArguments() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Object[] arguments = new Object[]{1, "test"};
 when(invocation.getArguments()).thenReturn(new Object[]{2, "test2"});
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 // Test should throw an exception
 invocationMatcher.safelyArgumentsMatch(arguments);
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Object argument1 = new Object(){};
 Object argument2 = new Object(){};
 when(invocation.getArguments()).thenReturn(new Object[]{argument1, argument2});
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 // Test for non-vararg method
 Matcher matcher1 = ArgumentsMatcher.isInstanceOf(String.class);
 when(invocationMatcher.getMatchers().get(0)).thenReturn(matcher1);
 invocationMatcher.captureArgumentsFrom(invocation);
 verify(matcher1).matches(argument1);

 // Test for vararg method - this test will be skipped as it's not supported
 // invocationMatcher.captureArgumentsFrom(invocation);
 // Matcher matcherVararg = ArgumentsMatcher.isInstanceOf(String[].class);
 // verify(matcherVararg).matches(invocation.getRawArguments()[0]);
 }

 @Test
 public void testCaptureArgumentsFromInvocation_VarargsMatcher() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Object[] arguments = new Object[]{"test1", "test2"};
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"String..."}));
 when(invocation.getArguments()).thenReturn(arguments);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Matcher matcher = ArgumentsMatcher.contains("test2");
 when(invocationMatcher.getMatchers().get(0)).thenReturn(matcher);
 invocationMatcher.captureArgumentsFrom(invocation);
 verify(matcher).matches(Arrays.asList(arguments).subList(1, Arrays.asList(arguments).size()));
 }

 @Test
 public void testCaptureArgumentsFromInvocation_CaptureAllVarargs() {
 Invocation invocation = Mockito.mock(InvocationOnMock.class);
 Object[] arguments = new Object[]{"test1", "test2", "test3"};
 when(invocation.getMethod()).thenReturn(new Method(Object.class, "testMethod", new Class[]{"String..."}));
 when(invocation.getArguments()).thenReturn(arguments);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Matcher matcher = ArgumentsMatcher.allOf(Arrays.asList(ArgumentsMatcher.isInstanceOf(String.class), ArgumentsMatcher.isInstanceOf(String.class), ArgumentsMatcher.isInstanceOf(String.class)));
 when(invocationMatcher.getMatchers().get(0)).thenReturn(matcher);
 invocationMatcher.captureArgumentsFrom(invocation);

 List<Object> capturedArguments = matcher.getAllMatches();
 assertEquals(Arrays.asList(arguments), capturedArguments);
 }
}