import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 private InternalMockHandler<Object> mockHandler;
 private InvocationContainerImpl invocationContainer;
 private Object mock;

 @Test
 public void testAnswer_ReturnsExistingMock() throws Exception {
 // Given
 mockHandler = spy(new MockUtil().getMockHandler(mock(Object.class)));
 invocationContainer = spy(new InvocationContainerImpl());
 mockHandler.setInvocationContainer(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 invocationContainer.addStubbedInvocation(stubbedInvocationMatcher);
 when(stubbedInvocationMatcher.matches(Mockito.any(InvocationOnMock.class)))
 .thenReturn(true);
 when(stubbedInvocationMatcher.answer(Mockito.any(InvocationOnMock.class)))
 .thenReturn("existingMock");

 // When
 Object result = returnsDeepStubs.answer(Mockito.argThat(i -> i.getMock().equals(mock)));

 // Then
 verify(stubbedInvocationMatcher).answer(Mockito.eq(Mockito.argThat(i -> i.getMock().equals(mock))));
 verify(mockHandler).getInvocationContainer();
 verifyNoMoreInteractions(stubbedInvocationMatcher);
 verifyZeroInteractions(mockHandler);
 assertEquals("existingMock", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_ReturnsEmptyValues_ForNonMockableType() throws Exception {
 // Given
 mockHandler = spy(new MockUtil().getMockHandler(mock(Object.class)));
 invocationContainer = spy(new InvocationContainerImpl());
 mockHandler.setInvocationContainer(invocationContainer);

 // When & Then
 returnsDeepStubs.answer(Mockito.any(InvocationOnMock.class));
 }

 @Test
 public void testGetMock_ReturnsNewMock() throws Exception {
 // Given
 mockHandler = spy(new MockUtil().getMockHandler(mock(Object.class)));
 invocationContainer = mock(InvocationContainerImpl.class);
 mockHandler.setInvocationContainer(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());

 // When
 Object result = returnsDeepStubs.getMock(Mockito.any(InvocationOnMock.class));

 // Then
 verify(mockHandler).getInvocationContainer();
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 assertEquals(mock, result);
 }

 @Test
 public void testRecordDeepStubMock_ReturnsNewMock() throws Exception {
 // Given
 mockHandler = mock(InternalMockHandler.class);
 invocationContainer = mock(InvocationContainerImpl.class);
 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = mock(InvocationOnMock.class);

 // When
 Object result = returnsDeepStubs.recordDeepStubMock(invocation, invocationContainer);

 // Then
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 assertEquals(mock, result);
 }

 @Test
 public void testAnswer_ReturnsNewMock_WhenNoMatchingStubbedInvocation() throws Exception {
 // Given
 mockHandler = spy(new MockUtil().getMockHandler(mock(Object.class)));
 invocationContainer = mock(InvocationContainerImpl.class);
 mockHandler.setInvocationContainer(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());

 // When
 Object result = returnsDeepStubs.answer(Mockito.any(InvocationOnMock.class));

 // Then
 verify(mockHandler).getInvocationContainer();
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 verifyNoMoreInteractions(invocationContainer);
 verifyZeroInteractions(mockHandler);
 assertNotNull(result);
 }

 @Test
 public void testGetMock_ReturnsExistingMock_WhenInvocationMatchesStubbedInvocation() throws Exception {
 // Given
 mockHandler = spy(new MockUtil().getMockHandler(mock(Object.class)));
 invocationContainer = mock(InvocationContainerImpl.class);
 mockHandler.setInvocationContainer(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 invocationContainer.addStubbedInvocation(stubbedInvocationMatcher);
 when(stubbedInvocationMatcher.matches(Mockito.any(InvocationOnMock.class)))
 .thenReturn(true);

 // When
 Object result = returnsDeepStubs.getMock(Mockito.any(InvocationOnMock.class));

 // Then
 verify(stubbedInvocationMatcher).answer(Mockito.eq(Mockito.argThat(i -> i.getMock().equals(mock))));
 verify(mockHandler).getInvocationContainer();
 verifyNoMoreInteractions(stubbedInvocationMatcher);
 assertEquals(mock, result);
 }

 @Test
 public void testRecordDeepStubMock_ReturnsExistingMock_WhenInvocationMatchesStubbedInvocation() throws Exception {
 // Given
 mockHandler = mock(InternalMockHandler.class);
 invocationContainer = mock(InvocationContainerImpl.class);
 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 invocationContainer.addStubbedInvocation(stubbedInvocationMatcher);
 when(stubbedInvocationMatcher.matches(Mockito.any(InvocationOnMock.class)))
 .thenReturn(true);
 when(stubbedInvocationMatcher.answer(Mockito.any(InvocationOnMock.class)))
 .thenReturn(mock);

 // When
 Object result = returnsDeepStubs.recordDeepStubMock(invocation, invocationContainer);

 // Then
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 verify(stubbedInvocationMatcher).answer(Mockito.eq(Mockito.argThat(i -> i.getMock().equals(mock))));
 assertEquals(mock, result);
 }

 @Test
 public void testAnswer_ReturnsNewMock_WhenInvocationDoesNotMatchAnyStubbedInvocation() throws Exception {
 // Given
 mockHandler = mock(InternalMockHandler.class);
 invocationContainer = mock(InvocationContainerImpl.class);
 when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
 mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationOnMock differentInvocation = mock(InvocationOnMock.class);

 // When
 Object result = returnsDeepStubs.answer(invocation);

 // Then
 verify(mockHandler).getInvocationContainer();
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 verifyNoMoreInteractions(invocationContainer);
 assertNotNull(result);
 verify(mockHandler).getInvocationContainer();
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 assertEquals(mock, result);

 // When
 Object differentResult = returnsDeepStubs.answer(differentInvocation);

 // Then
 verify(mockHandler).getInvocationContainer();
 verify(invocationContainer).addAnswer(Mockito.any(Answer.class), Mockito.eq(false));
 verifyNoMoreInteractions(invocationContainer);
 assertNotNull(differentResult);
 assertNotSame(result, differentResult);
 }
}