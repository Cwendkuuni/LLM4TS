import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher.*;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import java.util.*;

public class InvocationMatcherTest {

 private List<Invocation> invocations;

 @Test
 public void testConstructorWithInvocationAndEmptyMatchers() {
 Invocation invocation = createMockInvocation();
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Assert.assertEquals(invocation, invocationMatcher.getInvocation());
 Assert.assertNotNull(invocationMatcher.getMatchers());
 Assert.assertEquals(0, invocationMatcher.getMatchers().size());
 }

 @Test
 public void testConstructorWithInvocationAndMatchers() {
 Invocation invocation = createMockInvocation();
 List<Matcher> matchers = Arrays.asList(new EqualToString("expected"), new IsInstanceOf(String[].class));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

 Assert.assertEquals(invocation, invocationMatcher.getInvocation());
 Assert.assertEquals(matchers, invocationMatcher.getMatchers());
 }

 @Test
 public void testGetMethod() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMockMethod();
 Method method = invocationMatcher.getMethod();

 Assert.assertEquals("mockMethod", method.getName());
 }

 @Test
 public void testGetInvocation() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMockMethod();
 Invocation invocation = invocationMatcher.getInvocation();

 Assert.assertSame(invocationMatcher, invocation);
 }

 @Test
 public void testGetMatchers() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMatchers();
 List<Matcher> matchers = invocationMatcher.getMatchers();

 Assert.assertNotNull(matchers);
 Assert.assertEquals(2, matchers.size());
 }

 @Test
 public void testToString() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMatchers();
 String toString = invocationMatcher.toString();

 Assert.assertNotNull(toString);
 }

 @Test
 public void testMatches_sameInvocation() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMockMethod();
 InvocationMatcher anotherInvocationMatcher = invocationMatcher;

 Assert.assertTrue(invocationMatcher.matches(anotherInvocationMatcher));
 }

 @Test
 public void testMatches_differentInvocationsButSameMethod() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithMockMethod();

 Assert.assertTrue(invocationMatcher1.matches(invocationMatcher2));
 }

 @Test
 public void testMatches_differentInvocationsDifferentMethods() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithDifferentMethod();

 Assert.assertFalse(invocationMatcher1.matches(invocationMatcher2));
 }

 @Test
 public void testSafelyArgumentsMatch_sameArguments() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMockMethod();
 Object[] arguments = new Object[]{1, 2, 3};

 Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatch_differentArguments() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMockMethod();
 Object[] differentArguments = new Object[]{1, "two", 3};

 invocationMatcher.safelyArgumentsMatch(differentArguments);
 }

 @Test
 public void testHasSimilarMethod_sameMethods() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithMockMethod();

 Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testHasSimilarMethod_differentMethods() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithDifferentMethod();

 Assert.assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
 }

 @Test
 public void testHasSameMethod_sameMethods() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithMockMethod();

 Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
 }

 @Test
 public void testHasSameMethod_differentMethods() {
 InvocationMatcher invocationMatcher1 = createInvocationMatcherWithMockMethod();
 InvocationMatcher invocationMatcher2 = createInvocationMatcherWithDifferentMethod();

 Assert.assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 InvocationMatcher invocationMatcher = createInvocationMatcherWithMatchers();
 Invocation invocation = createMockInvocation();
 invocationMatcher.captureArgumentsFrom(invocation);

 Matcher firstMatcher = invocationMatcher.getMatchers().get(0);
 Matcher secondMatcher = invocationMatcher.getMatchers().get(1);

 if (firstMatcher instanceof ArgumentsMatcher) {
 Assert.assertSame(invocation.getArguments()[0], ((ArgumentsMatcher) firstMatcher).getArguments()[0]);
 }
 if (secondMatcher instanceof ArgumentCaptor) {
 Assert.assertSame(invocation.getArguments()[1], ((ArgumentCaptor) secondMatcher).getValue());
 }
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = Arrays.asList(createMockInvocation(), createMockInvocation());
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

 Assert.assertEquals(2, invocationMatchers.size());
 }

 private Invocation createMockInvocation() {
 Mockito.mock(Object.class);
 return Mockito.mockInvocation(Object.class, new Object[]{});
 }

 private InvocationMatcher createInvocationMatcherWithMockMethod() {
 return new InvocationMatcher(createMockInvocation(), new ArrayList<>(), new MethodStub("mockMethod"));
 }

 private InvocationMatcher createInvocationMatcherWithMatchers() {
 List<Matcher> matchers = Arrays.asList(new EqualToString("expected"), new IsInstanceOf(String[].class));
 return new InvocationMatcher(createMockInvocation(), matchers);
 }

 private InvocationMatcher createInvocationMatcherWithDifferentMethod() {
 return new InvocationMatcher(createMockInvocation(), new ArrayList<>(), new MethodStub("differentMethod"));
 }

 private static class MethodStub implements Method {
 String name;

 MethodStub(String name) {
 this.name = name;
 }

 @Override
 public String getName() {
 return name;
 }

 public Class<?>[] getParameterTypes() {
 return new Class[0];
 }
 }
}