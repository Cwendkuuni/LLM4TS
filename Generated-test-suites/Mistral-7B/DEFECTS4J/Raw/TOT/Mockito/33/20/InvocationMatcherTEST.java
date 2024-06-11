import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.reporting.PrintSettings;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

public class InvocationMatcherTest {

 private InvocationMatcher invocationMatcher;
 private InvocationOnMock invocationOnMock;
 private Object mock;

 @Before
 public void setup() {
 mock = Mockito.mock(Object.class);
 invocationOnMock = Mockito.mock(InvocationOnMock.class);
 invocationMatcher = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, null, null));
 }

 @Test
 public void shouldMatchInvocationWithSameMethodAndSameArguments() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, Collections.singletonList(isA(String.class)));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldNotMatchInvocationWithDifferentMethod() {
 Invocation invocation = new Invocation(getMethod1(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod2(), mock, invocationOnMock, null, null));

 assertFalse(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldNotMatchInvocationWithDifferentMock() {
 Invocation invocation = new Invocation(getMethod(), mock1, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock2, invocationOnMock, null, null));

 assertFalse(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldNotMatchInvocationWithDifferentArguments() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[]{"arg3", "arg4"});
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]));

 assertFalse(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithSameMethodAndDifferentArgumentsButSameType() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", 1}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", 2}, new Object[0]));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithSameMethodAndDifferentArgumentsButDifferentTypesButCanBeConverted() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", 1L}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", 1}, new Object[0]));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldNotMatchInvocationWithDifferentMethodButSameArguments() {
 Invocation invocation = new Invocation(getMethod1(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]));

 assertFalse(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithSameMethodButDifferentArgumentsButSameTypeButDifferentOrder() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg2", "arg1"}, new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldNotMatchInvocationWithSameMethodButDifferentArgumentsButDifferentTypes() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", 1}, new Object[]{"arg1", "arg2"});
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[0]));

 assertFalse(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithEmptyArguments() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[0], new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, Collections.singletonList(isA(Object[].class)));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithEmptyArgumentsAndEmptyMatcherList() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[0], new Object[0]);
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation);

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithEmptyArgumentsAndMatcherForAnotherArgument() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[0], new Object[]{"arg1"});
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, Collections.singletonList(isA(String.class)));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithArgumentsAndMatcherForAnotherArgument() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[]{"arg3"});
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, Arrays.asList(isA(String.class), isA(Integer.class)));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Test
 public void shouldMatchInvocationWithArgumentsAndMatcherForArguments() {
 Invocation invocation = new Invocation(getMethod(), mock, invocationOnMock, new Object[]{"arg1", "arg2"}, new Object[]{"arg1", "arg3"});
 InvocationMatcher invocationMatcherToTest = new InvocationMatcher(invocation, Arrays.asList(Arguments.equalTo("arg1"), isA(String.class)));

 assumeTrue(invocationMatcher.matches(invocationMatcherToTest));
 }

 @Captor
 private ArgumentCaptor<String> stringArgumentCaptor;

 @Test
 public void shouldCaptureStringArguments() {
 String arg = "string argument";
 invocationMatcher.captureArgumentsFrom(invocationOnMock.verify(mock).methodWithStringArgument(arg));

 assertEquals("string argument", invocationMatcher.getMatchers().get(0).getValue());
 }

 @Test
 public void shouldCaptureMultipleStringArguments() {
 String arg1 = "string argument 1";
 String arg2 = "string argument 2";
 invocationMatcher.captureArgumentsFrom(invocationOnMock.verify(mock).methodWithTwoStringArguments(arg1, arg2));

 assertEquals("string argument 1", invocationMatcher.getMatchers().get(0).getValue());
 assertEquals("string argument 2", invocationMatcher.getMatchers().get(1).getValue());
 }

 @Test
 public void shouldCaptureCustomObjectArguments() {
 MyCustomObject arg = new MyCustomObject();
 invocationMatcher.captureArgumentsFrom(invocationOnMock.verify(mock).methodWithCustomObjectArgument(arg));

 assumeTrue(invocationMatcher.getMatchers().get(0) instanceof ArgumentMatcher<MyCustomObject>);
 assertEquals(arg, ((ArgumentMatcher<MyCustomObject>)invocationMatcher.getMatchers().get(0)).getRealValue());
 }

 @Test
 public void shouldCaptureMultipleCustomObjectArguments() {
 MyCustomObject arg1 = new MyCustomObject();
 MyCustomObject arg2 = new MyCustomObject();
 invocationMatcher.captureArgumentsFrom(invocationOnMock.verify(mock).methodWithTwoCustomObjectArguments(arg1, arg2));

 assumeTrue(invocationMatcher.getMatchers().size() == 2);
 assertEquals(arg1, ((ArgumentMatcher<MyCustomObject>)invocationMatcher.getMatchers().get(0)).getRealValue());
 assertEquals(arg2, ((ArgumentMatcher<MyCustomObject>)invocationMatcher.getMatchers().get(1)).getRealValue());
 }

 @Test
 public void shouldCreateMatcherFromListOfInvocations() {
 List<Invocation> invocations = Arrays.asList(invocation1, invocation2, invocation3);
 List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

 InvocationMatcher invocationMatcher = invocationMatchers.get(0);
 assertEquals(invocation1.getMethod(), invocationMatcher.getMethod());
 assertEquals(invocation1.getMock(), invocationMatcher.getInvocation().getMock());
 }
}