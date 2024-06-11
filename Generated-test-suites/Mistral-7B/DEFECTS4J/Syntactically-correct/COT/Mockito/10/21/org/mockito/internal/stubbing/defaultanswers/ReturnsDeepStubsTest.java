import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 @Mock
 private Object mock;

 @Test
 public void testAnswerMethod() throws Throwable {
 // Given
 when(mock).methodToTest(ArgumentMatchers.any()).thenAnswer(returnsDeepStubs);

 // When & Then
 InvocationOnMock invocation = Mockito.mock(ClassWithMethodToTest.class).invocationOnMethod("methodToTest", ArgumentMatchers.any());
 Object result = returnsDeepStubs.answer(invocation);

 // Check if the deep stub was returned
 verify(mock).methodToTest(ArgumentMatchers.eq(result));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNonMockableType() throws Throwable {
 // Given
 when(mock).methodToTest(ArgumentMatchers.any()).thenAnswer(returnsDeepStubs);

 // When & Then
 InvocationOnMock invocation = Mockito.mock(NonMockableClass.class).invocationOnMethod("methodToTest", ArgumentMatchers.any());

 // Then an exception is thrown
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testDeepStubbing() throws Throwable {
 // Given
 when(mock.methodToTest(ArgumentMatchers.any())).thenAnswer(returnsDeepStubs);

 // When
 Object deepStub = Mockito.mock(ClassWithNestedGenericType.class);
 Object nestedObject = deepStub.methodWithNestedGenericType().iterator().next().getValue().iterator().next();
 Object result = mock.methodToTest(nestedObject);

 // Then the deep stub was returned
 verify(deepStub).methodWithNestedGenericType();
 verify(result).equals(nestedObject);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordedAnswerAlreadyExists() throws Throwable {
 // Given
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 when(mock.methodToTest(ArgumentMatchers.any())).thenAnswer(new DeeplyStubbedAnswer(mock));
 when(mock.methodToTest(ArgumentMatchers.any())).thenAnswer(returnsDeepStubs);

 // When
 Object result1 = mock.methodToTest(ArgumentMatchers.any());
 Object result2 = mock.methodToTest(ArgumentMatchers.any());

 // Then an exception is thrown
 returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class).invocationOnMethod("methodToTest", ArgumentMatchers.any()));
 }
}

 // Classes and interfaces used in the tests
 interface ClassWithMethodToTest {}
 interface ClassWithNestedGenericType<T> {
 T methodWithNestedGenericType();
 }
 class NonMockableClass {}