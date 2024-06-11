import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerWithEmptyReturnType() throws Exception {
 Object mock = mock(Object.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(mock.answer(invocation)).thenAnswer(Answers.returns(returnsDeepStubs));

 Object result = mock.answer(invocation);

 assertSame(returnsDeepStubs, result);
 }

 @Test
 public void testAnswerWithNonMockableType() throws Exception {
 Object nonMockableTypeMock = mock(NonMockableType.class, new ReturnsDeepStubs());

 assertSame(new ReturnsEmptyValues().returnValueFor(NonMockableType.class), nonMockableTypeMock.answer(Mockito.mock(InvocationOnMock.class)));
 }

 @Test
 public void testGetMockWithEmptyInvocationContainer() throws Exception {
 InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
 InvocationContainerImpl emptyInvocationContainer = new InvocationContainerImpl(handler);

 Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class), new GenericMetadataSupport(Object.class));

 assertSame(new Object(), result);
 }

 @Test
 public void testGetMockWithMatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class), Arrays.asList(stubbedInvocationMatcher));

 when(mockitoCore.mock(TestClass.class, withSettings())).thenReturn(mock);
 when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

 Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport(TestClass.class));

 assertSame(mock, result);
 }

 @Test(expected = Throwable.class)
 public void testGetMockWithUnmatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport(TestClass.class));

 fail("Should have thrown an exception");
 }

 @Test
 public void testCreateNewDeepStubMockWithEmptyReturnType() throws Exception {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Object.class);

 Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

 assertNotNull(mock);
 }

 @Test
 public void testCreateNewDeepStubMockWithGenericReturnType() throws Exception {
 Class<?> genericType = TestClassWithGenericReturnType.class;
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(genericType);

 Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

 assertInstanceOf(TestClassWithGenericReturnType.class, mock);
 }

 @Test
 public void testRecordDeepStubMockWithEmptyContainer() throws Exception {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl emptyInvocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 Object result = returnsDeepStubs.recordDeepStubMock(mock, emptyInvocationContainer);

 assertSame(mock, result);
 }

 @Test
 public void testRecordDeepStubMockWithMatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 when(mockitoCore.mock(TestClass.class, withSettings())).thenReturn(mock);
 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 Object result = returnsDeepStubs.recordDeepStubMock(mock, invocationContainer);

 assertSame(mock, result);
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockWithUnmatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 Object result = returnsDeepStubs.recordDeepStubMock(mock, invocationContainer);

 fail("Should have thrown an exception");
 }

 @Test
 public void testVerifyInvocationContainerWithMatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class), Arrays.asList(stubbedInvocationMatcher));

 when(mockitoCore.mock(TestClass.class, withSettings())).thenReturn(mock);
 when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
 @Override protected GenericMetadataSupport actualParameterizedType(Object mock) {
 return new GenericMetadataSupport(TestClass.class);
 }
 };
 returnsDeepStubs.answer(invocation);

 verify(mockitoCore).mock(TestClass.class, withSettings());
 verify(stubbedInvocationMatcher).answer(invocation);
 }

 @Test
 public void testVerifyInvocationContainerWithEmptyContainer() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
 @Override protected GenericMetadataSupport actualParameterizedType(Object mock) {
 return new GenericMetadataSupport(TestClass.class);
 }
 };
 returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));

 verifyZeroInteractions(mockitoCore);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyInvocationContainerWithUnmatchedInvocation() throws Exception {
 Object mock = mock(TestClass.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl invocationContainer = new InvocationContainerImpl(Mockito.mock(InternalMockHandler.class));

 when(mockitoCore.mock(TestClass.class, withSettings())).thenReturn(mock);
 when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
 @Override protected GenericMetadataSupport actualParameterizedType(Object mock) {
 return new GenericMetadataSupport(TestClassWithDifferentReturnType.class);
 }
 };
 returnsDeepStubs.answer(invocation);

 verify(mockitoCore).mock(TestClassWithDifferentReturnType.class, withSettings());
 }
}

class TestClass {
}

class TestClassWithGenericReturnType<T> {
}

class NonMockableType implements Serializable {
}