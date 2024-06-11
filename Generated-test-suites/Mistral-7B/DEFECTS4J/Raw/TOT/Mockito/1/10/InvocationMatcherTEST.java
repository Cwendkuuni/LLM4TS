import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

 private Invocation invocation;
 private Stubber stubber;
 private InvocationMatcher invocationMatcher;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 stubber = Mockito.mock(Stubber.class);
 invocation = Mockito.mock(Invocation.class);
 when(invocation.getMock()).thenReturn(stubber);
 when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
 invocationMatcher = new InvocationMatcher(invocation);
 }

 @Test
 public void testGetMethod() {
 Method method = invocationMatcher.getMethod();
 assertThat(method, isA(Method.class));
 }

 @Test
 public void testGetInvocation() {
 Invocation returnedInvocation = invocationMatcher.getInvocation();
 assertThat(returnedInvocation, is(invocation));
 }

 @Test
 public void testGetMatchers() {
 List<Matcher> matchers = invocationMatcher.getMatchers();
 assertThat(matchers.isEmpty(), is(true));
 }

 @Test
 public void testToString() {
 String print = invocationMatcher.toString();
 assertThat(print, isInstanceOf(String.class));
 }

 @Test
 public void testMatches_withSameInvocation() {
 Invocation sameInvocation = Mockito.mock(Invocation.class);
 when(sameInvocation.getMock()).thenReturn(stubber);
 when(sameInvocation.getMethod()).thenReturn(invocation.getMethod());
 InvocationMatcher sameInvocationMatcher = new InvocationMatcher(sameInvocation);
 boolean result = invocationMatcher.matches(sameInvocationMatcher);
 assertThat(result, is(true));
 }

 @Test
 public void testMatches_withDifferentInvocation() {
 Invocation differentInvocation = Mockito.mock(Invocation.class);
 when(differentInvocation.getMock()).thenReturn(stubber);
 when(differentInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 boolean result = invocationMatcher.matches(differentInvocationMatcher);
 assertThat(result, is(false));
 }

 @Test
 public void testMatches_withNullInvocation() {
 InvocationMatcher nullInvocationMatcher = new InvocationMatcher(null);
 boolean result = invocationMatcher.matches(nullInvocationMatcher);
 assertThat(result, is(false));
 }

 @Test
 public void testMatches_withNullMatchers() {
 List<Matcher> emptyList = Collections.emptyList();
 InvocationMatcher invocationMatcherWithEmptyList = new InvocationMatcher(invocation, emptyList);
 InvocationMatcher emptyMatcherTest = new InvocationMatcher(invocation);
 boolean result = invocationMatcherWithEmptyList.matches(emptyMatcherTest);
 assertThat(result, is(true));
 }

 @Test
 public void testHasSimilarMethod_withSameInvocation() {
 Invocation sameInvocation = Mockito.mock(Invocation.class);
 InvocationMatcher sameInvocationMatcher = new InvocationMatcher(sameInvocation);
 boolean result = invocationMatcher.hasSimilarMethod(sameInvocationMatcher);
 assertThat(result, is(true));
 }

 @Test
 public void testHasSimilarMethod_withDifferentMethodName() {
 Invocation differentInvocation = Mockito.mock(Invocation.class);
 when(differentInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 boolean result = invocationMatcher.hasSimilarMethod(differentInvocationMatcher);
 assertThat(result, is(false));
 }

 @Test
 public void testHasSimilarMethod_withSameMethodButUnverifiedDifferentInvocation() {
 Invocation differentInvocation = Mockito.mock(Invocation.class);
 when(differentInvocation.getMethod()).thenReturn(invocation.getMethod());
 when(differentInvocation.isVerified()).thenReturn(false);
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 boolean result = invocationMatcher.hasSimilarMethod(differentInvocationMatcher);
 assertThat(result, is(true));
 }

 @Test
 public void testHasSimilarMethod_withDifferentMethod() {
 Invocation differentInvocation = Mockito.mock(Invocation.class);
 when(differentInvocation.getMethod()).thenReturn(Mockito.mock(Method.class));
 InvocationMatcher differentInvocationMatcher = new InvocationMatcher(differentInvocation);
 boolean result = invocationMatcher.hasSimilarMethod(differentInvocationMatcher);
 assertThat(result, is(false));
 }

 @Test
 public void testCaptureArgumentsFrom_emptyMatcherList() {
 invocationMatcher.captureArgumentsFrom(invocation);
 verifyZeroInteractions(stubber);
 }

 @Test
 public void testCaptureArgumentsFrom_oneMatcher() {
 InvocationMatcher invocationMatcherWithOneMatcher = new InvocationMatcher(invocation, Collections.singletonList(Mockito.mock(Matcher.class)));
 invocationMatcherWithOneMatcher.captureArgumentsFrom(invocation);
 verifyZeroInteractions(stubber);
 }

 @Test
 public void testCaptureArgumentsFrom_multipleMatchers() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(Matcher.class));
 matchers.add(Mockito.mock(Matcher.class));
 InvocationMatcher invocationMatcherWithMultipleMatchers = new InvocationMatcher(invocation, matchers);
 invocationMatcherWithMultipleMatchers.captureArgumentsFrom(invocation);
 for (Matcher matcher : matchers) {
 verify(stubber).thenReturn(Mockito.anyObject());
 }
 }

 @Test
 public void testCaptureArgumentsFrom_varargMatcher() {
 List<Matcher> matchers = new ArrayList<>();
 matchers.add(Mockito.mock(VarargMatcher.class));
 InvocationMatcher invocationMatcherWithVarargMatcher = new InvocationMatcher(invocation, matchers);
 invocationMatcherWithVarargMatcher.captureArgumentsFrom(invocation);
 Object[] arguments = invocation.getArguments();
 for (int i = 0; i < arguments.length; i++) {
 if (i == arguments.length - 1 && arguments[i] != null && arguments[i].getClass().isArray()) {
 for (Object object : (Object[]) arguments[i]) {
 verify(stubber).thenReturn(object);
 }
 } else {
 verify(stubber).thenReturn(arguments[i]);
 }
 }
 }
}