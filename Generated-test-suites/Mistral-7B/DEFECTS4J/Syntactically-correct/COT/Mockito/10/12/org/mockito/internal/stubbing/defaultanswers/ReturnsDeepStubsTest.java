import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepStubMock;
 private Object parentMock;

 @Before
 public void setup() {
 parentMock = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 returnsDeepStubs = new ReturnsDeepStubs();
 deepStubMock = returnsDeepStubs.newDeepStubMock(new GenericMetadataSupport(Type.class));
 }

 @Test
 public void testAnswerMethodWithMockableType() throws Throwable {
 // Testing the basic functionality of the answer method with a mockable type
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(Number.class);
 Object result = returnsDeepStubs.answer(invocation);
 // The result should be the deep stub of the invocation
 verify(parentMock).answer(invocation);
 assertEquals(deepStubMock, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerMethodWithNonMockableType() throws Throwable {
 // Testing the exception handling scenario when the type is not mockable
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(IOException.class);
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testDeepStubMethod() throws Throwable {
 // Testing the deep stub method
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 invocation.getMethod().setReturnType(Number.class);
 Object result = returnsDeepStubs.deepStub(invocation, new GenericMetadataSupport(Number.class));
 // The result should be the deep stub of the invocation
 verify(deepStubMock).answer(invocation);
 assertEquals(deepStubMock, result);
 }

 @Test
 public void testNewDeepStubMockMethod() throws Throwable {
 // Testing the newDeepStubMock method
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);
 Object newMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
 // The newMock should be a mock with the ReturnsDeepStubs answer
 Mockito.verify(newMock).answer(Mockito.any(InvocationOnMock.class));
 assertEquals(returnsDeepStubs, Mockito.getAnswer(newMock));
 }

 @Test
 public void testRecordDeepStubAnswerMethod() throws Throwable {
 // Testing the recordDeepStubAnswer method
 InvocationContainerImpl container = new InvocationContainerImpl();
 Object answer = returnsDeepStubs.recordDeepStubAnswer(deepStubMock, container);
 // The answer should be the deep stub mock
 assertEquals(deepStubMock, answer);
 // The deep stub answer should be registered in the container
 verify(container).addAnswer(Mockito.isInstance(new DeeplyStubbedAnswer(deepStubMock)), Mockito.eq(false));
 }

 @Test
 public void testActualParameterizedTypeMethod() throws Throwable {
 // Testing the actualParameterizedType method
 Object mock = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 GenericMetadataSupport actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);
 assertEquals(Type.class, actualParameterizedType.rawType());
 }

 @Test
 public void testWithSettingsUsingMethod() throws Throwable {
 // Testing the withSettingsUsing method
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
 MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
 assertEquals(withSettings(), mockSettings);
 mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata.hasRawExtraInterfaces() ? withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings());
 assertEquals(withSettings().serializable(), mockSettings);
 }

 @Test
 public void testReturnsDeepStubsSerializationFallbackAnswerMethod() throws Throwable {
 // Testing the ReturnsDeepStubsSerializationFallback answer method
 ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(new GenericMetadataSupport(Object.class));
 Object result = returnsDeepStubsSerializationFallback.answer(mock(InvocationOnMock.class));
 // The result should be the parent mock
 assertEquals(parentMock, result);
 }

 @Test
 public void testDeeplyStubbedAnswerMethod() throws Throwable {
 // Testing the DeeplyStubbedAnswer method
 DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(deepStubMock);
 Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));
 // The result should be the deep stub mock
 assertEquals(deepStubMock, result);
 }
}