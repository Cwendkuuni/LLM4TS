import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object shallowMock;
 private Object deepMock;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();

 shallowMock = Mockito.mock(Map.class);
 deepMock = Mockito.mock(Map.class);
 }

 @Test
 public void testAnswer_ShallowMock_ReturnsSameInstance() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().equals(shallowMock);

 Object result = returnsDeepStubs.answer(invocation);

 Assert.assertSame(shallowMock, result);
 }

 @Test
 public void testAnswer_DeepMock_ReturnsNewInstance() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock().equals(deepMock);

 Object shallowResult = returnsDeepStubs.answer(invocation);
 Object deepResult = returnsDeepStubs.answer(invocation);

 Assert.assertNotSame(shallowResult, deepResult);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_UnmockableType() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(Number[].class);

 returnsDeepStubs.answer(invocation);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMock_UnmockableType() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(Number[].class);

 returnsDeepStubs.getMock(invocation);
 }

 @Test
 public void testGetMock_MatchesStubbedInvocation() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

 Mockito.when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
 Mockito.when(stubbedInvocationMatcher.answer(ArgumentMatchers.any())).thenAnswer(Mockito.mock(Answer.class));
 Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

 Object result = returnsDeepStubs.getMock(invocation);

 Assert.assertSame(Mockito.mock(Answer.class), result);
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);

 Assert.assertNotNull(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRecordDeepStubMock_NullContainer() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 returnsDeepStubs.recordDeepStubMock(invocation, null);
 }
}