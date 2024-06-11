import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import java.lang.reflect.Method;
import java.util.*;

public class InvocationMatcherTest {

 private Invocation createInvocationMock(Class<?> clazz, Method method, Object[] arguments) {
 return new DescribedInvocation(new Location(0, 0), clazz, method, arguments);
 }

 @Test
 public void testConstructorWithInvocationAndEmptyMatchers() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testConstructorWithInvocationAndEmptyMatchers"), new Object[0]));
 Assert.assertNotNull(matcher);
 }

 @Test
 public void testConstructorWithInvocationAndMatchers() {
 List<Matcher> matchers = Arrays.asList(new ArgumentMatcher<Integer>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals(1);
 }
 }, new ArgumentMatcher<String>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals("hello");
 }
 });
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testConstructorWithInvocationAndMatchers"), new Object[0]), matchers);
 Assert.assertNotNull(matcher);
 }

 @Test
 public void testGetMethod() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testGetMethod"), new Object[0]));
 Method method = matcher.getMethod();
 Assert.assertEquals(getClass().getMethod("testGetMethod"), method);
 }

 @Test
 public void testGetInvocation() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testGetInvocation"), new Object[0]));
 Invocation invocation = matcher.getInvocation();
 Assert.assertEquals(matcher.getInvocation(), invocation);
 }

 @Test
 public void testGetMatchers() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testGetMatchers"), new Object[0]));
 List<Matcher> matchers = matcher.getMatchers();
 Assert.assertTrue(matchers.isEmpty());
 }

 @Test
 public void testToString() {
 List<Matcher> matchers = Arrays.asList(new ArgumentMatcher<Integer>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals(1);
 }
 }, new ArgumentMatcher<String>() {
 @Override
 public boolean matches(Object argument) {
 return argument.equals("hello");
 }
 });
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testToString"), new Object[0]), matchers);
 String expected = "InvocationMatcher:\n" +
         "arguments: [1, hello]\n" +
         "method: testToString(<args>)\n";
 String actual = matcher.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMock() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMock"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMock"), new Object[0]));
 Invocation invocation = createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMock"), new Object[0]);
 matcher1.matches(invocation);
 matcher2.matches(invocation);
 }

 @Test
 public void testMatchesWithDifferentMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMethod"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMethod2"), new Object[0]));
 Invocation invocation = createInvocationMock(InvocationMatcher.class, getClass().getMethod("testMatchesWithDifferentMethod"), new Object[0]);
 assertFalse(matcher1.matches(invocation));
 assertFalse(matcher2.matches(invocation));
 }

 @Test
 public void testHasSameMethodWithSameMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSameMethodWithSameMethod"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSameMethodWithSameMethod"), new Object[0]));
 assertTrue(matcher1.hasSameMethod(matcher2));
 }

 @Test
 public void testHasSameMethodWithDifferentMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSameMethodWithSameMethod"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSameMethodWithDifferentMethod"), new Object[0]));
 assertFalse(matcher1.hasSameMethod(matcher2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithSameMethod"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithSameMethod"), new Object[0]));
 assertTrue(matcher1.hasSimilarMethod(matcher2));
 }

 @Test
 public void testHasSimilarMethodWithDifferentButCompatibleMethods() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithDifferentButCompatibleMethods"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithDifferentButCompatibleMethods2"), new Object[0]));
 assertTrue(matcher1.hasSimilarMethod(matcher2));
 }

 @Test
 public void testHasSimilarMethodWithDifferentMethods() {
 InvocationMatcher matcher1 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithDifferentMethods"), new Object[0]));
 InvocationMatcher matcher2 = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testHasSimilarMethodWithDifferentMethods2"), new Object[0]));
 assertFalse(matcher1.hasSimilarMethod(matcher2));
 }

 @Test(expected = AssertionError.class)
 public void testSafelyArgumentsMatchWithDifferentArguments() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testSafelyArgumentsMatchWithDifferentArguments"), new Object[0]));
 Object[] arguments = new Object[]{1, 2.0, "hello"};
 assertFalse(matcher.safelyArgumentsMatch(arguments));
 }

 @Test
 public void testSafelyArgumentsMatchWithSameArguments() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testSafelyArgumentsMatchWithSameArguments"), new Object[0]));
 Object[] arguments = new Object[]{1, 2, "hello"};
 assertTrue(matcher.safelyArgumentsMatch(arguments));
 }

 @Test
 public void testCaptureArgumentsFromInvocationWithNoVarargs() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCaptureArgumentsFromInvocationWithNoVarargs"), new Object[0]));
 List<Matcher> matchers = matcher.getMatchers();
 matcher.captureArgumentsFrom(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCaptureArgumentsFromInvocationWithNoVarargs"), new Object[]{1, 2, "hello"}));
 assertEquals(3, matchers.size());
 }

 @Test
 public void testCaptureArgumentsFromInvocationWithVarargs() {
 InvocationMatcher matcher = new InvocationMatcher(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCaptureArgumentsFromInvocationWithVarargs"), new Object[0]));
 List<Matcher> matchers = matcher.getMatchers();
 matcher.captureArgumentsFrom(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCaptureArgumentsFromInvocationWithVarargs"), new Object[]{1, 2, "hello", "world"}));
 assertEquals(4, matchers.size());
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = Arrays.asList(createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCreateFrom1"), new Object[]{1}),
         createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCreateFrom1"), new Object[]{2}),
         createInvocationMock(InvocationMatcher.class, getClass().getMethod("testCreateFrom1"), new Object[]{3}));
 List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
 assertEquals(3, matchers.size());
 }
}