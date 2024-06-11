import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.CapturesArguments;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

 private Invocation getMockInvocation() {
 return Mockito.mock(Invocation.class);
 }

 @Test
 public void testConstructorWithInvocationAndEmptyMatcherList() {
 InvocationMatcher testObject = new InvocationMatcher(getMockInvocation());

 assertTrue(testObject.matches(getMockInvocation()));
 }

 @Test
 public void testConstructorWithInvocationAndMatcherList() {
 List<Matcher> matchers = Arrays.asList(Mockito.eq("hello"));
 InvocationMatcher testObject = new InvocationMatcher(getMockInvocation(), matchers);

 Invocation invocation = Mockito.mock(Invocation.class);
 Mockito.when(invocation.getArguments()).thenReturn(new Object[]{ "world" });

 assertTrue(testObject.matches(invocation));
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Mockito.when(invocation.getMethod()).thenReturn(new Method(InvocationMatcher.class.getDeclaredMethod("toString", PrintSettings.class)));

 InvocationMatcher testObject = new InvocationMatcher(invocation);
 Method actualMethod = testObject.getMethod();

 assertTrue(actualMethod.equals(invocation.getMethod()));
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 Invocation actualInvocation = testObject.getInvocation();

 assertTrue(actualInvocation.equals(invocation));
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("hello"), Mockito.eq(1));
 InvocationMatcher testObject = new InvocationMatcher(invocation, matchers);

 List<Matcher> actualMatchers = testObject.getMatchers();

 assertTrue(actualMatchers.equals(matchers));
 }

 @Test
 public void testToString() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("hello"), Mockito.eq(1));
 InvocationMatcher testObject = new InvocationMatcher(invocation, matchers);

 PrintSettings printSettings = Mockito.mock(PrintSettings.class);

 String actualToString = testObject.toString(printSettings);

 String expectedToString = "Invocation [method=InvocationMatcher.toString(PrintSettings), arguments=[], target=mock: org.mockito.internal.invocation.Invocation@" + System.identityHashCode(invocation) + "]";

 assertTrue(actualToString.equals(expectedToString));
 }

 @Test
 public void testMatchesWithSameInvocationAndMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 assertTrue(testObject.matches(invocation));
 }

 @Test
 public void testMatchesWithDifferentInvocationAndMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);

 assertFalse(testObject.matches(invocation2));
 }

 @Test
 public void testMatchesWithSameInvocationButDifferentArguments() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 Object[] invocationArguments = new Object[]{"arg1"};
 Mockito.when(invocation.getArguments()).thenReturn(invocationArguments);

 assertFalse(testObject.matches(invocation));
 }

 @Test
 public void testMatchesWithDifferentInvocationsButSameArguments() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);
 Object[] invocation1Arguments = new Object[]{"arg1"};
 Object[] invocation2Arguments = new Object[]{"arg1"};

 Mockito.when(invocation1.getArguments()).thenReturn(invocation1Arguments);
 Mockito.when(invocation2.getArguments()).thenReturn(invocation2Arguments);

 assertTrue(testObject.matches(invocation2));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentInvocationsAndDifferentArguments() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);
 Object[] invocation1Arguments = new Object[]{"arg1"};
 Object[] invocation2Arguments = new Object[]{"arg2"};

 Mockito.when(invocation1.getArguments()).thenReturn(invocation1Arguments);
 Mockito.when(invocation2.getArguments()).thenReturn(invocation2Arguments);

 testObject.matches(invocation2);
 }

 @Test
 public void testHasSimilarMethodWithSameInvocationAndMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 assertTrue(testObject.hasSimilarMethod(invocation));
 }

 @Test
 public void testHasSimilarMethodWithDifferentInvocationsButSameMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);

 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation1.getMethod()).thenReturn(method);
 Mockito.when(invocation2.getMethod()).thenReturn(method);

 assertTrue(testObject.hasSimilarMethod(invocation2));
 }

 @Test
 public void testHasSimilarMethodWithDifferentInvocationAndDifferentMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);

 Mockito.when(invocation1.getMethod()).thenReturn(method1);
 Mockito.when(invocation2.getMethod()).thenReturn(method2);

 assertFalse(testObject.hasSimilarMethod(invocation2));
 }

 @Test
 public void testSafelyArgumentsMatchWithSameArguments() {
 InvocationMatcher testObject = new InvocationMatcher(getMockInvocation(), Arrays.asList(Mockito.eq(1)));
 Object[] arguments = new Object[]{1};

 assertTrue(testObject.safelyArgumentsMatch(arguments));
 }

 @Test
 public void testSafelyArgumentsMatchWithDifferentArguments() {
 InvocationMatcher testObject = new InvocationMatcher(getMockInvocation(), Arrays.asList(Mockito.eq(1)));
 Object[] arguments = new Object[]{2};

 assertFalse(testObject.safelyArgumentsMatch(arguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchWithNullArguments() {
 InvocationMatcher testObject = new InvocationMatcher(getMockInvocation(), Arrays.asList(Mockito.eq(1)));
 Object[] arguments = null;

 testObject.safelyArgumentsMatch(arguments);
 }

 @Test
 public void testHasSameMethodWithSameInvocationAndMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 assertTrue(testObject.hasSameMethod(invocation));
 }

 @Test
 public void testHasSameMethodWithDifferentInvocationsButSameMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);

 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation1.getMethod()).thenReturn(method);
 Mockito.when(invocation2.getMethod()).thenReturn(method);

 assertTrue(testObject.hasSameMethod(invocation2));
 }

 @Test
 public void testHasSameMethodWithDifferentInvocationsAndDifferentMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation1);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);

 Mockito.when(invocation1.getMethod()).thenReturn(method1);
 Mockito.when(invocation2.getMethod()).thenReturn(method2);

 assertFalse(testObject.hasSameMethod(invocation2));
 }

 @Test
 public void testCaptureArgumentsFromInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher testObject = new InvocationMatcher(invocation);

 CapturesArguments captureArguments = Mockito.mock(CapturesArguments.class);
 testObject.captureArgumentsFrom(invocation);
 Mockito.verify(captureArguments).captureFrom(Mockito.anyObject());
 }

 @Test
 public void testCreateFromListOfInvocations() {
 List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));
 List<InvocationMatcher> expectedInvocationMatchers = InvocationMatcher.createFrom(invocations);

 List<InvocationMatcher> actualInvocationMatchers = new LinkedList<InvocationMatcher>();
 for (Invocation invocation : invocations) {
 actualInvocationMatchers.add(new InvocationMatcher(invocation));
 }

 assertTrue(expectedInvocationMatchers.equals(actualInvocationMatchers));
 }
}