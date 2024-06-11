import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.invocation.Location;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

 private Invocation createMockInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
 return new Invocation(new Location(0, 0), methodName, parameterTypes, arguments);
 }

 @Test
 public void testConstructorWithInvocationAndEmptyMatchersList() {
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Collections.emptyList());
 Assert.assertNotNull(im.getMatchers());
 }

 @Test
 public void testConstructorWithInvocationAndMatchersList() {
 Matcher m1 = Arguments.is(1);
 Matcher m2 = Arguments.is("test");
 List<Matcher> matchers = new LinkedList<>();
 matchers.add(m1);
 matchers.add(m2);
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), matchers);
 Assert.assertEquals(matchers, im.getMatchers());
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 InvocationMatcher im = new InvocationMatcher(invocation);
 Method method = im.getMethod();
 Assert.assertEquals("testMethod", method.getName());
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 InvocationMatcher im = new InvocationMatcher(invocation);
 Assert.assertEquals(invocation, im.getInvocation());
 }

 @Test
 public void testGetMatchers() {
 Matcher m1 = Arguments.is(1);
 Matcher m2 = Arguments.is("test");
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Arrays.asList(m1, m2));
 List<Matcher> matchers = im.getMatchers();
 Assert.assertEquals(2, matchers.size());
 Assert.assertEquals(m1, matchers.get(0));
 Assert.assertEquals(m2, matchers.get(1));
 }

 @Test(expected = AssertionError.class)
 public void testToStringWithNullPrintSettings() {
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Arrays.asList(new ArgumentMatcher<Object>() {}));
 im.toString(null);
 }

 @Test
 public void testToStringWithPrintSettings() {
 PrintSettings printSettings = new PrintSettings();
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Arrays.asList(new ArgumentMatcher<Object>() {}));
 String result = im.toString(printSettings);
 Assert.assertNotNull(result);
 }

 @Test
 public void testMatchesWithSameInvocation() {
 Invocation invocation = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 InvocationMatcher im1 = new InvocationMatcher(invocation);
 InvocationMatcher im2 = new InvocationMatcher(invocation);
 Assert.assertTrue(im1.matches(im2));
 }

 @Test
 public void testMatchesWithDifferentInvocationButSameMethod() {
 Invocation invocation1 = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 Invocation invocation2 = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 Assert.assertTrue(im1.matches(im2));
 }

 @Test
 public void testMatchesWithDifferentInvocationButDifferentMethod() {
 Invocation invocation1 = createMockInvocation("testMethod1", new Class[]{}, new Object[]{});
 Invocation invocation2 = createMockInvocation("testMethod2", new Class[]{}, new Object[]{});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 Assert.assertFalse(im1.matches(im2));
 }

 @Test
 public void testSafelyArgumentsMatchWithSameArguments() {
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Collections.emptyList());
 Object[] arguments = new Object[] {};
 boolean result = im.safelyArgumentsMatch(arguments);
 Assert.assertTrue(result);
 }

 @Test
 public void testSafelyArgumentsMatchWithDifferentArguments() {
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Collections.emptyList());
 Object[] arguments = new Object[] {1, "test"};
 boolean result = im.safelyArgumentsMatch(arguments);
 Assert.assertFalse(result);
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchWithThrowable() {
 InvocationMatcher im = new InvocationMatcher(createMockInvocation("testMethod", new Class[]{}, new Object[]{}), Collections.emptyList());
 throw new RuntimeException();
 }

 @Test
 public void testHasSimilarMethodWithSameInvocations() {
 Invocation invocation1 = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 Invocation invocation2 = createMockInvocation("testMethod", new Class[]{}, new Object[]{});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSimilarMethod(im2);
 Assert.assertTrue(result);
 }

 @Test
 public void testHasSimilarMethodWithDifferentInvocationsButSameMethod() {
 Invocation invocation1 = createMockInvocation("testMethod", new Class[]{"java.lang.String"}, new Object[]{"Hello"});
 Invocation invocation2 = createMockInvocation("testMethod", new Class[]{"java.lang.String"}, new Object[]{"World"});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSimilarMethod(im2);
 Assert.assertTrue(result);
 }

 @Test
 public void testHasSimilarMethodWithDifferentInvocationsButDifferentMethods() {
 Invocation invocation1 = createMockInvocation("testMethod1", new Class[]{"java.lang.String"}, new Object[]{"Hello"});
 Invocation invocation2 = createMockInvocation("testMethod2", new Class[]{"java.lang.Integer"}, new Object[]{1});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSimilarMethod(im2);
 Assert.assertFalse(result);
 }

 @Test
 public void testCaptureArgumentsFrom() {
 Invocation invocation = createMockInvocation("testMethod", new Class[]{"java.lang.String"}, new Object[]{"Hello"});
 InvocationMatcher im = new InvocationMatcher(invocation);
 Matcher matcher = Arguments.is("World");
 im.captureArgumentsFrom(invocation);
 Object capturedArgument = matcher.getValue();
 Assert.assertEquals("World", capturedArgument);
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = new LinkedList<>();
 Invocation invocation1 = createMockInvocation("testMethod1", new Class[]{"java.lang.String"}, new Object[]{"Hello"});
 Invocation invocation2 = createMockInvocation("testMethod2", new Class[]{"java.lang.Integer"}, new Object[]{1});
 invocations.add(invocation1);
 invocations.add(invocation2);
 List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);
 Assert.assertEquals(2, result.size());
 }
}