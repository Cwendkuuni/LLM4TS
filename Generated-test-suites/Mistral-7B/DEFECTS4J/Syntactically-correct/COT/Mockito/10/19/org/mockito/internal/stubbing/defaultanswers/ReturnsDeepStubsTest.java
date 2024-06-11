import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static final ReturnsDeepStubs DEEPS_STUBS = new ReturnsDeepStubs();

 @Test
 public void testAnswerMethodWithMatchingInvocation() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // when
 Object answer = DEEPS_STUBS.answer(ArgumentMatchers.any(InvocationOnMock.class));
 // then
 Object deepStub = mock; // this is the same mock instance
 verify(deepStub).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals(answer, deepStub);
 }

 @Test(expected = Throwable.class)
 public void testAnswerMethodWithNonMockableType() throws Throwable {
 // given
 when(mock(IOException.class, new ReturnsDeepStubs()).answer(Mockito.any(InvocationOnMock.class)))
 .thenThrow(new IOException());
 // when, then
 // an IOException should be thrown
 }

 @Test
 public void testDeepStubMethodWithMatchingInvocation() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericDeclaration returnType = null;
 Type returnTypeType = null;
 do {
 returnType = invocation.getMethod().getReturnType();
 returnTypeType = returnType.getType();
 } while (returnType instanceof GenericDeclaration);
 // when
 Object answer = DEEPS_STUBS.deepStub(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.argThat(type -> returnTypeType.equals(type)));
 // then
 assertEquals(deepStub, answer);
 }

 @Test
 public void testDeepStubMethodWithNoMatchingInvocation() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 // when
 Object answer = DEEPS_STUBS.deepStub(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.not(ArgumentMatchers.argThat(type -> mock.getClass().equals(type))));
 // then
 assertNull(answer);
 }

 @Test(expected = Throwable.class)
 public void testDeepStubMethodWithThrowableException() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 // when, then
 // an exception should be thrown
 DEEPS_STUBS.deepStub(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.any(Throwable.class));
 }

 @Test
 public void testNewDeepStubMockMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 // when
 Object deepStub = DEEPS_STUBS.newDeepStubMock(returnTypeGenericMetadata);
 // then
 assertEquals(mock.getClass(), deepStub.getClass());
 }

 @Test
 public void testWithSettingsUsingMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 // when
 MockSettings mockSettings = DEEPS_STUBS.withSettingsUsing(returnTypeGenericMetadata);
 // then
 assertTrue(mockSettings.isSerializable());
 assertTrue(mockSettings.getAnswer() instanceof ReturnsDeepStubs);
 }

 @Test
 public void testRecordDeepStubAnswerMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 // when
 Object answer = DEEPS_STUBS.recordDeepStubAnswer(mock, container);
 // then
 assertEquals(mock, answer);
 }

 @Test
 public void testActualParameterizedTypeMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // when
 GenericMetadataSupport actualParameterizedType = DEEPS_STUBS.actualParameterizedType(mock);
 // then
 assertEquals(mock.getClass(), actualParameterizedType.getRawType());
 }

 @Test
 public void testReturnsDeepStubsAnswerUsingMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 // when
 ReturnsDeepStubs returnsDeepStubsAnswer = DEEPS_STUBS.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
 // then
 assertEquals(DEEPS_STUBS, returnsDeepStubsAnswer);
 }

 @Test
 public void testDeeplyStubbedAnswerMethod() throws Throwable {
 // given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // when
 Object deeplyStubbedAnswer = DEEPS_STUBS.deeplyStubbedAnswer(mock);
 // then
 assertEquals(mock, deeplyStubbedAnswer);
 }

 @Test
 public void testMockitoCoreMethod() throws Throwable {
 // given
 // when
 MockitoCore mockitoCore = DEEPS_STUBS.mockitoCore();
 // then
 assertNotNull(mockitoCore);
 }

 @Test
 public void testDelegateMethod() throws Throwable {
 // given
 // when
 ReturnsEmptyValues delegate = DEEPS_STUBS.delegate();
 // then
 assertNotNull(delegate);
 }
}