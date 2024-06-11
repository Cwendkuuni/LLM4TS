import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswer_WithMockableType() throws Throwable {
 // Given
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Integer.class, "valueOf", new Class[] { String.class }));
 invocation.setArguments(new Object[]{"10"});
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Integer.class);

 // When
 Object result = returnsDeepStubs.answer(invocation);

 // Then
 Assert.assertEquals(mock, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_WithNonMockableType() throws Throwable {
 // Given
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(String.class, "length", new Class[] {}));

 // When
 // Then should throw an UnsupportedOperationException
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testGetMock_WithMatchingInvocation() throws Throwable {
 // Given
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Integer.class, "valueOf", new Class[] { String.class }));
 invocation.setArguments(new Object[]{"10"});
 InvocationContainerImpl container = new InvocationContainerImpl(mock, null, null);
 container.addAnswer(Answers.RETURNS_DEEP_STUBS.getAnswer(), false);

 // When
 Object result = returnsDeepStubs.getMock(invocation, container);

 // Then
 Assert.assertEquals(mock, result);
 }

 @Test(expected = RuntimeException.class)
 public void testGetMock_WithNonMatchingInvocation() throws Throwable {
 // Given
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Integer.class, "valueOf", new Class[] { String.class }));
 InvocationContainerImpl container = new InvocationContainerImpl(mock, null, null);

 // When
 // Then should throw a RuntimeException
 returnsDeepStubs.getMock(new InvocationOnMock() {
 @SuppressWarnings("unchecked")
 @Override
 public Method getMethod() throws NoSuchMethodException {
 return (Method) Mockito.method(Double.class, "+(Double)", new Class[] { Double.class });
 }
 }, container);
 }

 @Test
 public void testRecordDeepStubMock() throws Throwable {
 // Given
 Object mock = Mockito.mock(Integer.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(Mockito.method(Integer.class, "valueOf", new Class[] { String.class }));
 InvocationContainerImpl container = new InvocationContainerImpl(mock, null, null);

 // When
 Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

 // Then
 Assert.assertEquals(mock, result);
 }

 @Test
 public void testActualParameterizedType() throws Throwable {
 // Given
 Object mock = Mockito.mock(Map.class);
 CreationSettings creationSettings = new CreationSettings(Map.class, null, null);

 // When
 GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

 // Then
 Type expectedType = new TypeToken<Map<String, Number>>() {}.getType();
 Assert.assertEquals(expectedType, result.getType());
 }
}