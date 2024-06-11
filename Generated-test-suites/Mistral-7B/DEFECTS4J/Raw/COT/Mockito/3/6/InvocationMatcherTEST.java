import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.DescribedInvocation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

 @Test
 public void testConstructorWithInvocationAndEmptyMatcherList() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 // Add your assertions here
 }

 @Test
 public void testConstructorWithInvocationAndMatcherList() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 List<Matcher> matchers = Arrays.asList(MockitoTestUtils.createMatcher("1"), MockitoTestUtils.createMatcher("2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 // Add your assertions here
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 Method method = invocationMatcher.getMethod();
 // Add your assertions here
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 Invocation result = invocationMatcher.getInvocation();
 Assert.assertEquals(invocation, result);
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
 List<Matcher> matchers = invocationMatcher.getMatchers();
 // Add your assertions here
 }

 @Test
 public void testToString() {
 Invocation invocation = MockitoTestUtils.createMockInvocation();
 List<Matcher> matchers = Arrays.asList(MockitoTestUtils.createMatcher("1"), MockitoTestUtils.createMatcher("2"));
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
 String toString = invocationMatcher.toString();
 // Add your assertions here
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMocks() {
 Invocation invocation1 = MockitoTestUtils.createMockInvocation();
 Invocation invocation2 = MockitoTestUtils.createMockInvocation();
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
 invocationMatcher1.matches(invocationMatcher2);
 }

 @Test(expected = AssertionError.class)
 public void testHasSameMethodWithDifferentMethods() {
 Invocation invocation1 = MockitoTestUtils.createMockInvocation("method1", new Object[0]);
 Invocation invocation2 = MockitoTestUtils.createMockInvocation("method2", new Object[0]);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
 invocationMatcher1.hasSameMethod(invocationMatcher2);
 }

 @Test
 public void testHasSameMethodWithSameMethods() {
 Invocation invocation1 = MockitoTestUtils.createMockInvocation("method", new Object[0]);
 Invocation invocation2 = MockitoTestUtils.createMockInvocation("method", new Object[0]);
 InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.emptyList());
 boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);
 // Add your assertions here
 }

 @Test
 public void testSafelyArgumentsMatchWithSameArguments() {
 Invocation invocation = MockitoTestUtils.createMockInvocation("method", new Object[0]);
 Object[] arguments = new Object[0];
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
 boolean result = invocationMatcher.safelyArgumentsMatch(arguments);
 // Add your assertions here
 }

 @Test(expected = AssertionError.class)
 public void testSafelyArgumentsMatchWithDifferentArguments() {
 Invocation invocation = MockitoTestUtils.createMockInvocation("method", new Object[0]);
 Object[] differentArguments = new Object[]{"differentArgument"};
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.emptyList());
 invocationMatcher.safelyArgumentsMatch(differentArguments);
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 Invocation invocation = MockitoTestUtils.createMockInvocation("method", new Object[]{1, 2, "three"});
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(MockitoTestUtils.captureMatcher("1"), MockitoTestUtils.captureMatcher("2"), MockitoTestUtils.captureMatcher("3")));
 List<Object> capturedArguments = new LinkedList<>();
 invocationMatcher.captureArgumentsFrom(invocation);
 List<Object> expectedArguments = Arrays.asList(1, 2, "three");
 capturedArguments.addAll(Arrays.asList(invocation.getArguments()));
 Assert.assertEquals(expectedArguments, capturedArguments);
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = Arrays.asList(MockitoTestUtils.createMockInvocation("method1"), MockitoTestUtils.createMockInvocation("method2"));
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 // Add your assertions here
 }

}