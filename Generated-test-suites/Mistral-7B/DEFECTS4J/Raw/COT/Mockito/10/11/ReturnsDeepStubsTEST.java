import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.RunWithMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.withSettings;

@RunWith(RunWithMockito.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswer() throws Exception {
 // Test method: public Object answer(InvocationOnMock)
 // given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

 when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(any()))
 .thenReturn(Mockito.mock(Map.class));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object answer = returnsDeepStubs.answer(invocation);

 // then
 verify(returnTypeGenericMetadata).resolveGenericReturnType(invocation.getMethod());
 verify(returnTypeGenericMetadata).rawType();
 verifyZeroInteractions(invocation);

 assertNotNull(answer);

 // Test edge case: method with exception
 InvocationOnMock exceptionInvocation = Mockito.mock(InvocationOnMock.class);
 when(exceptionInvocation.getMethod()).thenThrow(new RuntimeException());

 try {
 returnsDeepStubs.answer(exceptionInvocation);
 fail();
 } catch (Throwable e) {
 // expected
 }
 }

 @Test
 public void testDeepStub() throws Exception {
 // Test method: private Object deepStub(InvocationOnMock, GenericMetadataSupport)
 // given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
 when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
 Object mock = mock(Map.class);

 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
 when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);

 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 when(handler.getMock()).thenReturn(mock);
 when(handler.getInvocationContainer()).thenReturn(container);
 when(MockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

 // then
 verify(container).addAnswer(any(), anyBoolean());
 verify(returnTypeGenericMetadata).resolveGenericReturnType(invocation.getMethod());
 verify(returnTypeGenericMetadata).rawType();
 verifyZeroInteractions(invocation);
 verify(handler).getMock();

 assertEquals(mock, deepStub);
 }

 @Test
 public void testNewDeepStubMock() throws Exception {
 // Test method: private Object newDeepStubMock(GenericMetadataSupport)
 // given
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
 when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);

 // when
 Object newDeepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

 // then
 verify(returnTypeGenericMetadata).hasRawExtraInterfaces();
 verifyZeroInteractions(newDeepStubMock);

 assertEquals(Mockito.mock(Map.class), newDeepStubMock);
 }

 @Test
 public void testWithSettingsUsing() throws Exception {
 // Test method: private MockSettings withSettingsUsing(GenericMetadataSupport)
 // given
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
 when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
 when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);

 // when
 MockSettings withSettingsUsing = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);

 // then
 verify(returnTypeGenericMetadata).rawExtraInterfaces();
 verify(returnTypeGenericMetadata).hasRawExtraInterfaces();
 verifyZeroInteractions(withSettingsUsing);

 assertEquals(withSettings().serializable().defaultAnswer(any()), withSettingsUsing);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 // Test method: private Object recordDeepStubAnswer(Object, InvocationContainerImpl)
 // given
 Object mock = Mockito.mock(Map.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 when(container.addAnswer(any(), anyBoolean())).thenReturn(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object recordedAnswer = returnsDeepStubs.recordDeepStubAnswer(mock, container);

 // then
 verify(container).addAnswer(any(), anyBoolean());
 verifyZeroInteractions(mock);

 assertEquals(mock, recordedAnswer);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test method: private GenericMetadataSupport actualParameterizedType(Object)
 // given
 Object mock = Mockito.mock(Object.class);

 // when
 GenericMetadataSupport actualParameterizedType = new ReturnsDeepStubs().actualParameterizedType(mock);

 // then
 assertEquals(Mockito.mock(GenericMetadataSupport.class), actualParameterizedType);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Exception {
 // Test method: private ReturnsDeepStubs returnsDeepStubsAnswerUsing(GenericMetadataSupport)
 // given
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
 when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);

 // when
 ReturnsDeepStubs returnsDeepStubsAnswerUsing = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

 // then
 verify(returnTypeGenericMetadata).rawType();
 verify(returnTypeGenericMetadata).hasRawExtraInterfaces();
 verifyZeroInteractions(returnsDeepStubsAnswerUsing);

 assertEquals(new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata), returnsDeepStubsAnswerUsing);
 }

 @Test
 public void testDeeplyStubbedAnswer() throws Exception {
 // Test method: private static class DeeplyStubbedAnswer implements Answer<Object>, Serializable
 // given
 Object mock = Mockito.mock(Map.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getMethod()).thenReturn(new Method(Map.class.getName(), "testMethod", new Class[0]));

 DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

 // when
 Object answer = deeplyStubbedAnswer.answer(invocation);

 // then
 verify(mockitoCore()).mock(Map.class, withSettings());
 verify(invocation.getMock()).testMethod();

 assertEquals(mock, answer);
 }

 @Test
 public void testMockitoCore() throws Exception {
 // Test method: private static MockitoCore mockitoCore()
 // given
 // nothing to test

 // when
 // nothing to test

 // then
 // nothing to test
 }

 @Test
 public void testDelegate() throws Exception {
 // Test method: private static ReturnsEmptyValues delegate()
 // given
 // nothing to test

 // when
 // nothing to test

 // then
 // nothing to test
 }
}