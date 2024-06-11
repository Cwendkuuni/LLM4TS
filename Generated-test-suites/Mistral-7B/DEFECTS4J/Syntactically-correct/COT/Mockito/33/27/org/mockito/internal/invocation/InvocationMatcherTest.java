import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.collections.LinkedHashMaps;
import org.mockito.internal.util.collections.LinkedLists;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InvocationMatcherTest {

 private Invocation createInvocationWithMethodAndArguments(String methodName, Class<?> clazz, Object[] arguments) throws Exception {
 Class<?> testClass = clazz;
 Method method = testClass.getMethod(methodName);
 return new Invocation(method, new Object[] { arguments }, new Location(testClass, -1, -1));
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndEmptyMatcherList() {
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 InvocationMatcher im = new InvocationMatcher(invocation);
 List<Matcher> matchers = im.getMatchers();
 assertTrue(matchers.isEmpty());
 }

 @Test
 public void testInvocationMatcherConstructorWithInvocationAndNonEmptyMatcherList() {
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 List<Matcher> matchers = Collections.singletonList(ArgThat.is("arg1"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 Assert.assertSame(matchers, im.getMatchers());
 }

 @Test
 public void testGetMethod() {
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 InvocationMatcher im = new InvocationMatcher(invocation);
 Method method = im.getMethod();
 assertEquals("testMethod", method.getName());
 }

 @Test
 public void testGetInvocation() {
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 InvocationMatcher im = new InvocationMatcher(invocation);
 Invocation res = im.getInvocation();
 assertEquals(invocation, res);
 }

 @Test
 public void testGetMatchers() {
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 List<Matcher> matchers = Collections.singletonList(ArgThat.is("arg1"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 List<Matcher> res = im.getMatchers();
 assertEquals(matchers, res);
 }

 @Test
 public void testToString() throws Exception {
 String methodName = "testMethod";
 Class<?> clazz = InvocationMatcher.class;
 Object[] arguments = new Object[] { "arg1" };
 Invocation invocation = createInvocationWithMethodAndArguments(methodName, clazz, arguments);
 InvocationMatcher im = new InvocationMatcher(invocation);
 String res = im.toString();
 String expected = String.format("Invocation on %s%n%s%nArguments:%n[%s]", clazz.getName(), methodName, Arrays.toString(arguments));
 assertEquals(expected, res);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMock() {
 Invocation invocation1 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 Invocation invocation2 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 im1.matches(im2);
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithDifferentMethod() throws Exception {
 Invocation invocation1 = createInvocationWithMethodAndArguments("testMethod1", InvocationMatcher.class, new Object[] {});
 Invocation invocation2 = createInvocationWithMethodAndArguments("testMethod2", InvocationMatcher.class, new Object[] {});
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 im1.matches(im2);
 }

 @Test
 public void testMatchesWithSameMethodAndSameArguments() throws Exception {
 Invocation invocation1 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 Invocation invocation2 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 im1.matches(im2);
 }

 @Test
 public void testMatchesWithSameMethodButDifferentArguments() {
 Invocation invocation1 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 Invocation invocation2 = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg2" });
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 assertFalse(im1.matches(im2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethodButDifferentInvocations() {
 InvocationMatcher im1 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod1", InvocationMatcher.class, new Object[] {}));
 InvocationMatcher im2 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod2", InvocationMatcher.class, new Object[] {}));
 assertFalse(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testHasSimilarMethodWithSameMethodAndSameInvocations() {
 InvocationMatcher im1 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {}));
 InvocationMatcher im2 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {}));
 assertTrue(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testHasSimilarMethodWithDifferentMethods() {
 InvocationMatcher im1 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod1", InvocationMatcher.class, new Object[] {}));
 InvocationMatcher im2 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod2", InvocationMatcher.class, new Object[] {}));
 assertFalse(im1.hasSimilarMethod(im2));
 }

 @Test
 public void testSafelyArgumentsMatchWithSameArguments() {
 InvocationMatcher im = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" }));
 Object[] arguments = new Object[] { "arg1" };
 assertTrue(im.safelyArgumentsMatch(arguments));
 }

 @Test(expected = AssertionError.class)
 public void testSafelyArgumentsMatchWithDifferentArguments() {
 InvocationMatcher im = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" }));
 Object[] arguments = new Object[] { "arg2" };
 assertFalse(im.safelyArgumentsMatch(arguments));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithNullInvocation() {
 InvocationMatcher im = new InvocationMatcher(null);
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {});
 assertFalse(im.matches(invocation));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesWithNullMatchers() {
 InvocationMatcher im = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {}), null);
 InvocationMatcher im2 = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] {}));
 assertFalse(im.matches(im2));
 }

 @Test
 public void testCaptureArgumentsFrom() {
 InvocationMatcher im = new InvocationMatcher(createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" }));
 Invocation invocation = createInvocationWithMethodAndArguments("testMethod", InvocationMatcher.class, new Object[] { "arg1" });
 im.captureArgumentsFrom(invocation);
 List<Capture> captures = im.getMatchers().get(0).getCaptures();
 assertEquals(1, captures.size());
 assertEquals("arg1", captures.get(0).getValue());
 }

 @Test
 public void testCreateFrom() {
 List<Invocation> invocations = LinkedLists.newLinkedList();
 invocations.add(createInvocationWithMethodAndArguments("testMethod1", InvocationMatcher.class, new Object[] {}));
 invocations.add(createInvocationWithMethodAndArguments("testMethod2", InvocationMatcher.class, new Object[] {"arg1"}));
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 assertEquals(2, invocationMatchers.size());
 InvocationMatcher im1 = invocationMatchers.get(0);
 InvocationMatcher im2 = invocationMatchers.get(1);
 assertEquals(createInvocationWithMethodAndArguments("testMethod1", InvocationMatcher.class, new Object[] {}), im1.getInvocation());
 assertEquals(Collections.emptyList(), im1.getMatchers());
 assertEquals(createInvocationWithMethodAndArguments("testMethod2", InvocationMatcher.class, new Object[] {"arg1"}), im2.getInvocation());
 List<Matcher> matchers = Collections.singletonList(ArgThat.is("arg1"));
 assertEquals(matchers, im2.getMatchers());
 }

 @Test(expected = AssertionError.class)
 public void testCreateFromWithNullInvocations() {
 List<Invocation> invocations = null;
 InvocationMatcher.createFrom(invocations);
 }
}