import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.Arguments.not;
import static org.mockito.internal.matchers.Arguments.isInstanceOf;
import static org.mockito.internal.matchers.Arguments.is;
import static org.mockito.internal.matchers.Arguments.same;
import static org.mockito.internal.matchers.Arguments.arrayWithSize;
import static org.mockito.internal.matchers.Arguments.arrayContaining;
import static org.mockito.internal.matchers.Arguments.argThat;
import static org.mockito.internal.matchers.Arguments.isA;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private Object mockObject;

 @Before
 public void setUp() {
 mockObject = mock(Object.class);
 invocation = new Invocation(mockObject, new Method(Object.class, "testMethod", new Class[]{Integer.class, String.class}), "testMethod", new Object[]{1, "test"}, new Location(1, 1, "TestClass.java"), null);
 invocationMatcher = new InvocationMatcher(invocation);
 }

 @Test
 public void testGetMethod() {
 Method expectedMethod = new Method(Object.class, "testMethod", new Class[]{Integer.class, String.class});
 Method actualMethod = invocationMatcher.getMethod();
 assertTrue(expectedMethod.equals(actualMethod));
 }

 @Test
 public void testGetInvocation() {
 Invocation expectedInvocation = invocation;
 Invocation actualInvocation = invocationMatcher.getInvocation();
 assertTrue(expectedInvocation.equals(actualInvocation));
 }

 @Test
 public void testGetMatchers() {
 List<Matcher> expectedMatchers = Collections.emptyList();
 List<Matcher> actualMatchers = invocationMatcher.getMatchers();
 assertTrue(expectedMatchers.equals(actualMatchers));
 }

 @Test
 public void testToString() {
 String expectedToString = "InvocationMatcher[testMethod(Integer, String)]";
 String actualToString = invocationMatcher.toString();
 assertTrue(expectedToString.equals(actualToString));
 }

 @Test
 public void testMatches_sameInvocation() {
 Invocation sameInvocation = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), invocation.getArguments(), invocation.getLocation(), null);
 InvocationMatcher sameInvocationMatcher = new InvocationMatcher(sameInvocation);
 assertTrue(invocationMatcher.matches(sameInvocationMatcher));
 }

 @Test
 public void testMatches_differentInvocation_differentMock() {
 Invocation differentInvocation = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), invocation.getArguments(), invocation.getLocation(), null);
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(new Invocation(new Object(), invocation.getMethod(), invocation.getMethod().getName(), invocation.getArguments(), invocation.getLocation(), null));
 assertFalse(invocationMatcher.matches(differentInvocationMatcher));
 }

 @Test
 public void testMatches_differentInvocation_differentMethod() {
 Invocation differentInvocation = new Invocation(mockObject, new Method(Object.class, "testAnotherMethod", new Class[]{Integer.class, String.class}), "testAnotherMethod", invocation.getArguments(), invocation.getLocation(), null);
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 assertFalse(invocationMatcher.matches(differentInvocationMatcher));
 }

 @Test
 public void testMatches_differentInvocation_sameMethod_differentArguments() {
 Invocation differentInvocation = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), new Object[]{2, "otherTest"}, invocation.getLocation(), null);
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 assertFalse(invocationMatcher.matches(differentInvocationMatcher));
 }

 @Test
 public void testMatches_differentInvocation_sameMethod_sameArguments() {
 Invocation differentInvocation = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), invocation.getArguments(), invocation.getLocation(), null);
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 assertTrue(invocationMatcher.matches(differentInvocationMatcher));
 }

 @Test
 public void testMatches_invocationWithVararg_sameInvocationWithVararg() {
 Invocation invocationWithVararg = new Invocation(mockObject, new Method(Object.class, "testMethod", new Class[]{Integer.class, String[].class}), "testMethod", new Object[]{1, new Object[]{1, 2}}, new Location(1, 1, "TestClass.java"), null);
 InvocationMatcher invocationWithVarargMatcher = new InvocationMatcher(invocationWithVararg);
 Invocation invocationWithVararg2 = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), new Object[]{1, new Object[]{1, 2}}, invocation.getLocation(), null);
 InvocationMatcher invocationWithVararg2Matcher = new InvocationMatcher(invocationWithVararg2);
 assertTrue(invocationWithVarargMatcher.matches(invocationWithVararg2Matcher));
 }

 @Test
 public void testMatches_invocationWithVararg_differentInvocationWithVararg() {
 Invocation invocationWithVararg = new Invocation(mockObject, new Method(Object.class, "testMethod", new Class[]{Integer.class, String[].class}), "testMethod", new Object[]{1, new Object[]{1, 2}}, new Location(1, 1, "TestClass.java"), null);
 InvocationMatcher invocationWithVarargMatcher = new InvocationMatcher(invocationWithVararg);
 Invocation differentInvocationWithVararg = new Invocation(mockObject, new Method(Object.class, "testMethod", new Class[]{Integer.class, String[].class}), "testMethod", new Object[]{1, new Object[]{3, 4}}, invocation.getLocation(), null);
 InvocationMatcher differentInvocationWithVarargMatcher = new InvocationMatcher(differentInvocationWithVararg);
 assertFalse(invocationWithVarargMatcher.matches(differentInvocationWithVarargMatcher));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 Object[] actualArguments = new Object[]{1, "test"};
 assertTrue(invocationMatcher.safelyArgumentsMatch(actualArguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatch_throwsException() {
 Object[] actualArguments = new Object[]{null, "test"};
 invocationMatcher.safelyArgumentsMatch(actualArguments);
 }

 @Test
 public void testHasSimilarMethod_sameMethod() {
 Invocation candidate = new Invocation(mockObject, invocation.getMethod(), invocation.getMethod().getName(), invocation.getArguments(), invocation.getLocation(), null);
 assertTrue(invocationMatcher.hasSimilarMethod(candidate));
 }

 @Test
 public void testHasSimilarMethod_differentMethod() {
 Invocation candidate = new Invocation(mockObject, new Method(Object.class, "testAnotherMethod", new Class[]{Integer.class, String.class}), "testAnotherMethod", invocation.getArguments(), invocation.getLocation(), null);
 assertFalse(invocationMatcher.hasSimilarMethod(candidate));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 List<Matcher> matchers = new LinkedList<>();
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 int argumentIndex = 0;

 Matcher expectedArgMatcher = argThat(is(1));
 matchers.add(expectedArgMatcher);
 invocationMatcherWithMatchers.captureArgumentsFrom(invocation);
 Object expectedArg = invocation.getArgumentAt(argumentIndex, Object.class);
 assertTrue(expectedArgMatcher.matches(expectedArg));

 Matcher secondArgMatcher = argThat(is("test"));
 matchers.add(secondArgMatcher);
 argumentIndex++;
 invocationMatcherWithMatchers.captureArgumentsFrom(invocation);
 Object secondExpectedArg = invocation.getArgumentAt(argumentIndex, Object.class);
 assertTrue(secondArgMatcher.matches(secondExpectedArg));
 }

 @Test
 public void testCaptureArgumentsFrom_vararg() {
 List<Matcher> matchers = new LinkedList<>();
 InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
 int argumentIndex = 0;

 Matcher varargMatcher = arrayContaining(is(1), is(2));
 matchers.add(varargMatcher);
 invocationMatcherWithMatchers.captureArgumentsFrom(invocation);
 Object[] expectedVarargs = invocation.getRawArguments()[argumentIndex];
 assertArrayEquals(new Object[]{1, 2}, expectedVarargs);
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = new ArrayList<>();
 invocations.add(invocation);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 InvocationMatcher invocationMatcher = invocationMatchers.get(0);
 assertTrue(invocationMatcher.matches(invocationMatcher));
 }
}