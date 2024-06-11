import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.answers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testAnswerWithMatchingInvocation() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

 // act
 Object result = returnsDeepStubs.answer(invocation);

 // assert
 Assert.assertSame(mock, result);
 // verify
 verify(mockitoCore).isTypeMockable(String.class);
 verify(returnTypeGenericMetadata).rawType();
 verify(returnTypeGenericMetadata).resolveGenericReturnType(Mockito.any(Method.class));
 }

 @Test
 public void testAnswerWithNonMockableType() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String[].class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String[].class);
 when(mockitoCore.isTypeMockable(String[].class)).thenReturn(false);

 // act
 Object result = returnsDeepStubs.answer(invocation);

 // assert
 Assert.assertEquals(returnsEmptyValues, result);
 // verify
 verify(mockitoCore).isTypeMockable(String[].class);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithThrowableInvocation() throws Throwable {
 // arrange
 Object mock = Mockito.mock(Exception.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Exception.class);
 when(mockitoCore.isTypeMockable(Exception.class)).thenReturn(true);

 // act and assert
 returnsDeepStubs.answer(invocation);
 fail("Expected a Throwable but nothing was thrown.");
 }

 @Test
 public void testGetMockWithMatchingInvocationAndReturnType() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 // act
 Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 // assert
 Assert.assertSame(mock, result);
 // verify
 verify(mockitoCore).isTypeMockable(String.class);
 verify(returnTypeGenericMetadata).rawType();
 verify(returnTypeGenericMetadata).resolveGenericReturnType(Mockito.any(Method.class));
 }

 @Test
 public void testGetMockWithNonMatchingInvocation() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

 // act
 when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {stubbedInvocationMatcher});
 when(stubbedInvocationMatcher.answer(Mockito.any(InvocationOnMock.class))).thenReturn("Stubbed answer");

 Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 // assert and verify
 Assert.assertNotSame(mock, result);
 // verify
 verify(mockitoCore).isTypeMockable(String.class);
 verify(returnTypeGenericMetadata).rawType();
 verify(returnTypeGenericMetadata).resolveGenericReturnType(Mockito.any(Method.class));
 verify(container).getInvocationForStubbing();
 verify(container).getStubbedInvocations();
 verify(stubbedInvocationMatcher).answer(invocation);
 }

 @Test
 public void testGetMockWithMatchingInvocationButNonMockableReturnType() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
 when(mockitoCore.isTypeMockable(Integer.class)).thenReturn(false);

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 // act
 Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 // assert
 Assert.assertSame(returnsEmptyValues, result);
 // verify
 verify(mockitoCore).isTypeMockable(Integer.class);
 verify(returnTypeGenericMetadata).rawType();
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockWithTwoMatchingInvocations() throws Throwable {
 // arrange
 Object mock = Mockito.mock(String.class);
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(returnTypeGenericMetadata);
 when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 StubbedInvocationMatcher stubbedInvocationMatcher1 = Mockito.mock(StubbedInvocationMatcher.class);
 StubbedInvocationMatcher stubbedInvocationMatcher2 = Mockito.mock(StubbedInvocationMatcher.class);

 // act
 when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {stubbedInvocationMatcher1, stubbedInvocationMatcher2});

 Object result1 = returnsDeepStubs.getMock(invocation1, returnTypeGenericMetadata);
 Object result2 = returnsDeepStubs.getMock(invocation2, returnTypeGenericMetadata);

 // assert and verify
 Assert.assertSame(mock, result1);
 Assert.assertSame(mock, result2);
 // verify
 verify(mockitoCore).isTypeMockable(String.class);
 verify(returnTypeGenericMetadata).rawType();
 verify(returnTypeGenericMetadata).resolveGenericReturnType(Mockito.any(Method.class));
 verify(container).getInvocationForStubbing();
 verify(container).getStubbedInvocations();
 verify(stubbedInvocationMatcher1).answer(invocation1);
 verify(stubbedInvocationMatcher2).answer(invocation2);
 }
}