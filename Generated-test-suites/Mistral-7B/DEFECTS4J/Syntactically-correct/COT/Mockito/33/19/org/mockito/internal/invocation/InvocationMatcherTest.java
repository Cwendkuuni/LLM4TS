import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Invocation;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcher$;
import org.mockito.internal.matchers.ArgumentsMatcher;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private Invocation invocation;
 private Object[] arguments;

 @Before
 public void setup() {
 invocation = mock(Invocation.class);
 invocationMatcher = new InvocationMatcher(invocation);
 arguments = new Object[]{"Hello", 1};
 }

 @Test
 public void testConstructorWithEmptyListOfMatchers() {
 InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
 assertEquals(Collections.emptyList(), emptyMatcher.getMatchers());
 }

 @Test
 public void testConstructorWithListOfMatchers() {
 List<Matcher> matchers = Arrays.asList(is("Hello"), is(1));
 InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);
 assertEquals(matchers, matcherWithMatchers.getMatchers());
 }

 @Test
 public void testGetInvocation() {
 assertSame(invocation, invocationMatcher.getInvocation());
 }

 @Test
 public void testGetMethod() {
 Method method = Mockito.method(String.class, "testMethod", new Class[]{String.class, int.class});
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is(method), is("Hello"), is(1)));
 assertEquals(method, matcher.getMethod());
 }

 @Test
 public void testToString() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 String toString = matcher.toString();
 assertTrue(toString.contains("InvocationMatcher"));
 assertTrue(toString.contains("String testMethod"));
 assertTrue(toString.contains("args[0]=Hello"));
 assertTrue(toString.contains("args[1]=1"));
 }

 @Test
 public void testMatches() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 Invocation actual = Mockito.invocationWith(argThat(is("Hello")), argThat(is(1)));
 assertTrue(matcher.matches(actual));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMocks() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 Invocation actual = mock(Invocation.class);
 assertTrue(matcher.matches(actual)); //should fail with AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMethods() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 Invocation actual = mock(Invocation.class).withCallRealMethods().withSettings(setting().name("testMethod2"));
 assertTrue(matcher.matches(actual)); //should fail with AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentArguments() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 Invocation actual = mock(Invocation.class).withCallRealMethods().args(new Object[]{new Object(),"2"});
 assertTrue(matcher.matches(actual)); //should fail with AssertionError
 }

 @Test
 public void testSafelyArgumentsMatch() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(isA(ArgumentsMatcher.class)));
 Object[] actualArgs = new Object[]{"Hello", 1};
 assertTrue(matcher.safelyArgumentsMatch(actualArgs));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchWithException() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(isA(ArgumentsMatcher.class)));
 Object[] actualArgs = new Object[]{"Hello", new RuntimeException()};
 assertFalse(matcher.safelyArgumentsMatch(actualArgs));
 }

 @Test
 public void testHasSimilarMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 InvocationMatcher matcher2 = new InvocationMatcher(invocation, Arrays.asList(is("World"), is(2)));
 assertFalse(matcher1.hasSimilarMethod(matcher2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethod() {
 InvocationMatcher matcher1 = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 InvocationMatcher matcher2 = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 assertTrue(matcher1.hasSimilarMethod(matcher2));
 }

 @Test(expected = AssertionError.class)
 public void testHasSimilarMethodWithDifferentMethods() {
 InvocationMatcher matcher1 = new InvocationMatcher(invocation, Arrays.asList(is("Hello"), is(1)));
 InvocationMatcher matcher2 = new InvocationMatcher(invocation, Arrays.asList(is("World"), is(1)));
 assertFalse(matcher1.hasSimilarMethod(matcher2));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 InvocationMatcher matcher = new InvocationMatcher(invocation, Arrays.asList(new ArgumentsTester("Hello"), new ArgumentsTester("World")));
 matcher.captureArgumentsFrom(invocation);
 verify(matcher.getInvocation(), times(1)).getArguments();
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = Arrays.asList(invocation, invocation);
 List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);
 assertEquals(2, matchers.size());
 }

}

class ArgumentsTester implements ArgumentMatchers {
 private final String argument;

 public ArgumentsTester(String argument) {
 this.argument = argument;
 }

 @Override
 public void match(Object argument) {
 assertEquals(this.argument, argument);
 }
}