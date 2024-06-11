import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class InvocationMatcherTest {

 private Invocation getTestInvocation() {
 return Mockito.mock(Invocation.class);
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
 InvocationMatcher im = new InvocationMatcher(getTestInvocation(), Collections.emptyList());
 assertSame(getTestInvocation(), im.getInvocation());
 assertNotNull(im.getMatchers());
 assertFalse(im.getMatchers().isEmpty());
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndMatcherList() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 assertSame(invocation, im.getInvocation());
 assertEquals(matchers, im.getMatchers());
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);
 InvocationMatcher im = new InvocationMatcher(invocation, Collections.emptyList());
 assertEquals(method, im.getMethod());
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher im = new InvocationMatcher(invocation, Collections.emptyList());
 assertSame(invocation, im.getInvocation());
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 assertEquals(matchers, im.getMatchers());
 }

 @Test
 public void testToString() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 String printSettings = "PrintSettings{}";
 String expected = invocation.toString(matchers, new PrintSettings());
 String result = im.toString(printSettings);
 assertEquals(expected, result);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMocks() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.emptyList());
 im1.matches(im2);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMethods() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Method method1 = Mockito.mock(Method.class);
 Method method2 = Mockito.mock(Method.class);
 Mockito.when(invocation1.getMethod()).thenReturn(method1);
 Mockito.when(invocation2.getMethod()).thenReturn(method2);
 InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.emptyList());
 im1.matches(im2);
 }

 @Test
 public void testMatchesWithSameInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher im1 = new InvocationMatcher(invocation, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation, Collections.emptyList());
 assertTrue(im1.matches(im2));
 }

 @Test
 public void testMatchesWithSameInvocationAndArguments() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Object[] arguments = new Object[1];
 InvocationMatcher im1 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments[0])));
 InvocationMatcher im2 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments[0])));
 assertTrue(im1.matches(im2));
 }

 @Test
 public void testMatchesWithSameInvocationButDifferentArguments() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Object[] arguments1 = new Object[1];
 Object[] arguments2 = new Object[1];
 InvocationMatcher im1 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments1[0])));
 InvocationMatcher im2 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments2[0])));
 assertFalse(im1.matches(im2));
 }

 @Test(expected = AssertionError.class)
 public void testSafelyArgumentsMatchWithNullArguments() {
 InvocationMatcher im = new InvocationMatcher(getTestInvocation());
 im.safelyArgumentsMatch(null);
 }

 @Test
 public void testHasSimilarMethodWithSameMethodButDifferentMocks() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation1.getMethod()).thenReturn(method);
 Mockito.when(invocation2.getMethod()).thenReturn(method);
 InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.emptyList());
 assertFalse(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethodButSameMocks() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);
 InvocationMatcher im1 = new InvocationMatcher(invocation, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation, Collections.emptyList());
 assertTrue(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethodButDifferentArguments() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Object[] arguments1 = new Object[1];
 Object[] arguments2 = new Object[1];
 Mockito.when(invocation.getMethod()).thenReturn(method);
 InvocationMatcher im1 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments1[0])));
 InvocationMatcher im2 = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(arguments2[0])));
 assertFalse(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testHasSameMethodWithNullInvocation() {
 InvocationMatcher im = new InvocationMatcher(null, Collections.emptyList());
 assertFalse(im.hasSameMethod(getTestInvocation()));
 }

 @Test
 public void testHasSameMethodWithNullInvocation2() {
 InvocationMatcher im = new InvocationMatcher(getTestInvocation(), Collections.emptyList());
 assertFalse(im.hasSameMethod(null));
 }

 @Test
 public void testHasSameMethodWithSameInvocation() {
 Invocation invocation = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation.getMethod()).thenReturn(method);
 InvocationMatcher im1 = new InvocationMatcher(invocation, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation, Collections.emptyList());
 assertTrue(im1.hasSameMethod(im2));
 }

 @Test
 public void testHasSameMethodWithDifferentInvocationsButSameMethod() {
 Invocation invocation1 = Mockito.mock(Invocation.class);
 Invocation invocation2 = Mockito.mock(Invocation.class);
 Method method = Mockito.mock(Method.class);
 Mockito.when(invocation1.getMethod()).thenReturn(method);
 Mockito.when(invocation2.getMethod()).thenReturn(method);
 InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.emptyList());
 InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.emptyList());
 assertTrue(im1.hasSameMethod(im2));
 }

 @Test
 public void testToStringWithNullPrintSettings() {
 Invocation invocation = Mockito.mock(Invocation.class);
 List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 String expected = invocation.toString(matchers, null);
 String result = im.toString(null);
 assertEquals(expected, result);
 }

 @Test
 public void testCaptureArgumentsFromNullInvocation() {
 InvocationMatcher im = new InvocationMatcher(getTestInvocation());
 im.captureArgumentsFrom(null);
 }

 @Test
 public void testCaptureArgumentsFromWithNullInvocationArgument() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher im = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(null)));
 InvocationMatcher mockInvocationMatcher = Mockito.mock(InvocationMatcher.class);
 im.captureArgumentsFrom(mockInvocationMatcher);
 }

 @Test
 public void testCaptureArgumentsFromWithNonNullInvocationArgument() {
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher im = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq("test")));
 InvocationMatcher mockInvocationMatcher = Mockito.mock(InvocationMatcher.class);
 Mockito.when(mockInvocationMatcher.getArguments()).thenReturn(new Object[0]);
 im.captureArgumentsFrom(mockInvocationMatcher);
 }
}