import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMockAdapter;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

 @Test
 public void testInvocationMatcherWithSingleMatcher() {
 Invocation invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(isA(String.class))));

 Method method = Mockito.method(Object.class, "methodWithStringArgument");

 InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(Mockito.mock(Object.class), Arrays.asList(argThat(isA(String.class))));
 InvocationOnMock invocationOnMock = new InvocationOnMockAdapter(invocation);

 invocationOnMock.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock.expects(once()).probesReplay().andReturns(new Object());

 invocationMatcher.matches(invocationOnMock);
 }

 @Test(expected = org.mockito.exceptions.misusing.InvalidUseOfMatchers.class)
 public void testInvocationMatcherWithTwoDifferentMatchers() {
 Invocation invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class))));

 Method method = Mockito.method(Object.class, "methodWithDifferentArguments");

 InvocationOnMock invocationOnMock = new InvocationOnMockAdapter(invocation);

 invocationOnMock.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock.expects(once()).probesReplay().andReturns(new Object());

 invocationMatcher.matches(invocationOnMock);
 }

 @Test(expected = org.mockito.exceptions.misusing.InvalidUseOfMatchers.class)
 public void testInvocationMatcherWithEmptyMatcherList() {
 Invocation invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

 Method method = Mockito.method(Object.class, "methodWithEmptyMatcherList");

 InvocationOnMock invocationOnMock = new InvocationOnMockAdapter(invocation);

 invocationOnMock.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock.expects(once()).probesReplay().andReturns(new Object());

 invocationMatcher.matches(invocationOnMock);
 }

 @Test
 public void testInvocationMatcherWithArgumentMismatch() {
 Invocation invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(isA(String.class))));

 Method method = Mockito.method(Object.class, "methodWithIntegerArgument");

 InvocationOnMock invocationOnMock = new InvocationOnMockAdapter(invocation);

 invocationOnMock.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock.expects(once()).probesReplay().andThrows(new RuntimeException());

 assertFalse(invocationMatcher.matches(invocationOnMock));
 }

 @Test
 public void testInvocationMatcherWithMismatchedMethod() {
 Invocation invocation = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(argThat(isA(Object.class))));

 Method method1 = Mockito.method(Object.class, "method1");
 Method method2 = Mockito.method(Object.class, "method2");

 InvocationOnMock invocationOnMock = new InvocationOnMockAdapter(invocation);

 invocationOnMock.expects(once()).probesReplay().method(method1).withAnyArguments();
 invocationOnMock.expects(once()).probesReplay().andReturns(new Object());

 assertFalse(invocationMatcher.matches(invocationOnMock));
 }

 @Test
 public void testInvocationMatcherWithMismatchedMock() {
 Invocation invocation1 = Mockito.mock(Object.class);
 Invocation invocation2 = Mockito.mock(Object.class);
 InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Arrays.asList(argThat(isA(Object.class))));

 Method method = Mockito.method(Object.class, "method");

 InvocationOnMock invocationOnMock1 = new InvocationOnMockAdapter(invocation1);
 InvocationOnMock invocationOnMock2 = new InvocationOnMockAdapter(invocation2);
 invocationOnMock1.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock1.expects(once()).probesReplay().andReturns(new Object());
 invocationOnMock2.expects(once()).probesReplay().method(method).withAnyArguments();
 invocationOnMock2.expects(once()).probesReplay().andReturns(new Object());

 assertFalse(invocationMatcher.matches(invocationOnMock2));
 }

 @Test
 public void testCreateFromInvocations() {
 List<Invocation> invocations = Arrays.asList(
 Mockito.mock(Invocation.class),
 Mockito.mock(Invocation.class),
 Mockito.mock(Invocation.class)
 );
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
 }
}