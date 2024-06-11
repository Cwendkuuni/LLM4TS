import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static final long serialVersionUID = -7105341425736035847L;

 @Test
 public void testAnswerWithNonMockableType() throws Throwable {
 Object nonMockableTypeMock = mock(NonMockableType.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getMethod()).thenReturn(new Method("testMethod"));
 when(invocation.getMock()).thenReturn(nonMockableTypeMock);

 Object result = new ReturnsDeepStubs().answer(invocation);

 assertSame(new ReturnsEmptyValues().returnValueFor(NonMockableType.class), result);
 }

 @Test
 public void testAnswerWithMockableType() throws Throwable {
 MockitoCore mockitoCore = MockitoCore.createInstance();
 Object mockableTypeMock = mock(MockableType.class, new ReturnsDeepStubs(), mockitoCore);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getMethod()).thenReturn(new Method("testMethod"));
 when(invocation.getMock()).thenReturn(mockableTypeMock);

 Object result = new ReturnsDeepStubs().answer(invocation);

 assertSame(mockableTypeMock, result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNullInvocation() throws Throwable {
 new ReturnsDeepStubs().answer(null);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNullMethod() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setMethod(null);

 new ReturnsDeepStubs().answer(invocation);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNullInvoker() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setInvoker(null);

 new ReturnsDeepStubs().answer(invocation);
 }

 @Test
 public void testGetMockWithNoMatchingStubbedInvocation() throws Throwable {
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(Object.class));
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getInvocation()).thenReturn(new InvocationOnMock(null, null, null, new Object[0], new String[0]));
 when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));

 Object result = new ReturnsDeepStubs().getMock(invocation, new GenericMetadataSupport(Object.class));

 assertNull(result);
 }

 @Test
 public void testGetMockWithMatchingStubbedInvocation() throws Throwable {
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(Object.class));
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object stubbedMock = Mockito.mock(StubbedType.class);

 when(invocation.getInvocation()).thenReturn(new InvocationOnMock(null, new Method("testMethod"), null, new Object[0], new String[0]));
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(stubbedMock, new Method("testMethod"), null, new Object[0], new String[0]));
 when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));

 Object result = new ReturnsDeepStubs().getMock(invocation, new GenericMetadataSupport(StubbedType.class));

 assertSame(stubbedMock, result);
 }

 @Test
 public void testCreateNewDeepStubMockWithEmptyExtraInterfacesArray() throws Throwable {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(MockableTypeWithEmptyExtraInterfaces.class);
 Object mock = mock(MockableTypeWithEmptyExtraInterfaces.class, new ReturnsDeepStubs(), new MockitoCore());

 Object result = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);

 assertSame(mock, result);
 }

 @Test
 public void testCreateNewDeepStubMockWithExtraInterfacesArray() throws Throwable {
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(MockableTypeWithExtraInterfaces.class);
 Object mock = mock(MockableTypeWithExtraInterfaces.class, new ReturnsDeepStubs(), new MockitoCore());

 Object result = new ReturnsDeepStubs().createNewDeepStubMock(returnTypeGenericMetadata);

 assertSame(mock, result);
 }

 @Test
 public void testCreateNewDeepStubMockWithNullReturnTypeGenericMetadata() throws Throwable {
 new ReturnsDeepStubs().createNewDeepStubMock(null);
 }

 @Test
 public void testRecordDeepStubMockWithNoMatchingAnswer() throws Throwable {
 Object mock = mock(MockableType.class, new ReturnsDeepStubs(), new MockitoCore());
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getInvocation()).thenReturn(new InvocationOnMock(null, null, null, new Object[0], new String[0]));
 when(invocation.getMock()).thenReturn(mock);

 new ReturnsDeepStubs().recordDeepStubMock(mock, container);

 verifyZeroInteractions(mock);
 }

 @Test
 public void testRecordDeepStubMockWithMatchingAnswer() throws Throwable {
 Object mock = mock(MockableType.class, new ReturnsDeepStubs(), new MockitoCore());
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 Object dummyAnswer = new Object(){};
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(invocation.getInvocation()).thenReturn(new InvocationOnMock(null, null, null, new Object[0], new String[0]));
 when(invocation.getMock()).thenReturn(mock);

 when(container.getAnswer(eq(invocation))).thenReturn(dummyAnswer);

 new ReturnsDeepStubs().recordDeepStubMock(mock, container);

 verify(mock).answer(invocation);
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockWithNullInvocation() throws Throwable {
 Object mock = mock(MockableType.class, new ReturnsDeepStubs(), new MockitoCore());
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();

 new ReturnsDeepStubs().recordDeepStubMock(mock, null);
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockWithNullContainer() throws Throwable {
 Object mock = mock(MockableType.class, new ReturnsDeepStubs(), new MockitoCore());

 new ReturnsDeepStubs().recordDeepStubMock(mock, new InvocationContainerImpl());
 }

 @Test
 public void testActualParameterizedType() throws Throwable {
 Object mock = mock(Object.class, new ReturnsDeepStubs());

 assertEquals(new GenericMetadataSupport(Object.class), new ReturnsDeepStubs().actualParameterizedType(mock));
 }

 @Test
 public void testActualParameterizedTypeWithExtraInterfaces() throws Throwable {
 Object mock = mock(MockableTypeWithExtraInterfaces.class, new ReturnsDeepStubs());

 GenericMetadataSupport expectedMetadata = new GenericMetadataSupport(MockableTypeWithExtraInterfaces.class);
 expectedMetadata.addExtraInterfaces(new Class[] {ExtraInterface1.class, ExtraInterface2.class});

 assertEquals(expectedMetadata, new ReturnsDeepStubs().actualParameterizedType(mock));
 }

 @Test
 public void testActualParameterizedTypeWithGenericReturnType() throws Throwable {
 Object mock = mock(GenericReturnType.class, new ReturnsDeepStubs());

 GenericMetadataSupport expectedMetadata = new GenericMetadataSupport(GenericReturnType.class, new Type[] {String[].class, List.class});

 assertEquals(expectedMetadata, new ReturnsDeepStubs().actualParameterizedType(mock));
 }

 @Test
 public void testActualParameterizedTypeWithArrayReturnType() throws Throwable {
 Object mock = mock(ArrayReturnType.class, new ReturnsDeepStubs());

 GenericMetadataSupport expectedMetadata = new GenericMetadataSupport(ArrayReturnType.class, new Type[] {int[].class, Object[].class});

 assertEquals(expectedMetadata, new ReturnsDeepStubs().actualParameterizedType(mock));
 }
}