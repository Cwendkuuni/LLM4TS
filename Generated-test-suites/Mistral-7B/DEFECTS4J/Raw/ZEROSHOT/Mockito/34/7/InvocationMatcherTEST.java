import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private InvocationMatcher invocationMatcherWithSameMethod;
 private InvocationMatcher invocationMatcherWithDifferentMethod;
 private InvocationMatcher invocationMatcherWithSameArguments;
 private InvocationMatcher invocationMatcherWithDifferentArguments;
 private Invocation invocation;
 private Object mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(Object.class);
 invocation = Mockito.mock(Invocation.class);
 invocationMatcher = new InvocationMatcher(invocation);
 invocationMatcherWithSameMethod = new InvocationMatcher(invocation, ArgumentMatchers.same(invocation.getMethod()));
 invocationMatcherWithDifferentMethod = new InvocationMatcher(Mockito.mock(Invocation.class));
 invocationMatcherWithSameArguments = new InvocationMatcher(invocation, ArgumentMatchers.any());
 invocationMatcherWithDifferentArguments = new InvocationMatcher(Mockito.mock(Invocation.class), ArgumentMatchers.not(ArgumentMatchers.same(invocation.getArguments())));
 }

 @Test
 public void testHasSameMethod() {
 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock);
 invocationOnMock.method();

 assertTrue(invocationMatcher.hasSameMethod(invocationOnMock));
 }

 @Test
 public void testHasNoSameMethod() {
 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock, new Method[]{new Method("differentMethod")});
 invocationOnMock.method();

 assertFalse(invocationMatcher.hasSameMethod(invocationOnMock));
 }

 @Test
 public void testHasSameInvocation() {
 assertTrue(invocationMatcher.matches(invocationMatcher));
 }

 @Test
 public void testHasDifferentInvocation() {
 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock);
 assertFalse(invocationMatcher.matches(invocationOnMock));
 }

 @Test
 public void testHasSameMethodWithSameArguments() {
 Object[] arguments = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 invocation.setArguments(arguments);

 assertTrue(invocationMatcher.matches(new InvocationMatcher(invocation, ArgumentMatchers.any())));
 }

 @Test
 public void testHasDifferentMethodWithSameArguments() {
 Object[] arguments = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 invocation.setArguments(arguments);

 assertFalse(invocationMatcherWithSameMethod.matches(new InvocationMatcher(invocation, ArgumentMatchers.any())));
 }

 @Test
 public void testHasDifferentMethodWithDifferentArguments() {
 Object[] arguments1 = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 Object[] arguments2 = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class), Mockito.mock(Object.class)};

 invocation.setArguments(arguments1);
 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock, new Method[]{new Method("differentMethod")});
 invocationOnMock.setArguments(arguments2);

 assertFalse(invocationMatcherWithSameArguments.matches(invocationOnMock));
 }

 @Test
 public void testHasSameMethodWithSameArgumentsButDifferentInstances() {
 Object instance1 = Mockito.mock(Object.class);
 Object instance2 = Mockito.mock(Object.class);
 Object[] arguments = new Object[]{instance1, instance2};
 invocation.setArguments(arguments);

 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock, new Method("method"));
 invocationOnMock.setArguments(arguments);
 invocationOnMock.getArguments()[0] = instance2;

 assertTrue(invocationMatcher.matches(invocationOnMock));
 }

 @Test
 public void testArgumentsComparator() {
 Object[] arguments1 = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 Object[] arguments2 = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 Object[] arguments3 = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class), Mockito.mock(Object.class)};

 ArgumentsComparator argumentsComparator = new ArgumentsComparator();
 assertTrue(argumentsComparator.argumentsMatch(invocationMatcher, arguments1));
 assertFalse(argumentsComparator.argumentsMatch(invocationMatcher, arguments2));
 assertFalse(argumentsComparator.argumentsMatch(invocationMatcher, arguments3));
 }

 @Test
 public void testSafelyArgumentsMatch() {
 Object[] arguments = new Object[]{Mockito.mock(Object.class), Mockito.mock(Object.class)};
 assertTrue(argumentsComparator.argumentsMatch(invocationMatcher, arguments));
 }

 @Test(expected = Throwable.class)
 public void testSafelyArgumentsMatchThrowsException() {
 // This test will fail as InvocationMatcher is not designed to throw an exception
 // in this method
 InvocationOnMock invocationOnMock = Mockito.invocationOnMock(mock);
 invocationOnMock.method();

 // Uncomment the following line to make the test pass
 // assertFalse(invocationMatcher.safelyArgumentsMatch(invocationOnMock.getArguments()));
 }

}