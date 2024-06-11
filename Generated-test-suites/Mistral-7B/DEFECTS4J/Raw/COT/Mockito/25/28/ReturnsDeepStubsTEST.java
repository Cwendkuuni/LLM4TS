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
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS = ReturnsDeepStubs.class;
 private static final Class<InternalMockHandler<Object>> INTERNAL_MOCK_HANDLER = InternalMockHandler.class;
 private static final Class<InvocationContainerImpl> INVOCATION_CONTAINER_IMPL = InvocationContainerImpl.class;

 @Test
 public void testAnswerMethod_ReturnsExpectedMock() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object mock = mock(Map.class);

 // when
 Object result = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> invocation.getMock().equals(mock)));

 // then
 assertEquals(mock, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethod_ThrowsExceptionForNonMockableTypes() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object nonMockableType = new Object() {};

 // when
 // then
 throwAssertionError(()-> returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test
 public void testGetMockMethod_ReturnsExpectedMock() {
 // given
 Object mock = mock(Map.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(invocation.getMock()).thenReturn(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStub = returnsDeepStubs.getMock(invocation);

 // then
 assertEquals(mock, deepStub);
 }

 @Test
 public void testGetMockMethod_ReturnsSameMockForIdenticalInvocations() {
 // given
 Object mock = mock(Map.class);
 InvocationOnMock invocation1 = mock(InvocationOnMock.class);
 when(invocation1.getMock()).thenReturn(mock);
 InvocationOnMock invocation2 = mock(InvocationOnMock.class);
 when(invocation2.getMock()).thenReturn(mock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStub1 = returnsDeepStubs.getMock(invocation1);
 Object deepStub2 = returnsDeepStubs.getMock(invocation2);

 // then
 assertEquals(deepStub1, deepStub2);
 }

 @Test
 public void testGetMockMethod_CreatesNewMockForDifferentInvocations() {
 // given
 Object mock1 = mock(Map.class);
 Object mock2 = mock(Map.class);
 InvocationOnMock invocation1 = mock(InvocationOnMock.class);
 when(invocation1.getMock()).thenReturn(mock1);
 InvocationOnMock invocation2 = mock(InvocationOnMock.class);
 when(invocation2.getMock()).thenReturn(mock2);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStub1 = returnsDeepStubs.getMock(invocation1);
 Object deepStub2 = returnsDeepStubs.getMock(invocation2);

 // then
 assertNotEquals(deepStub1, deepStub2);
 }

 @Test
 public void testRecordDeepStubMockMethod_CreatesNewMock() {
 // given
 Object clazz = Map.class;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);

 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);

 // then
 verifyNoMoreInteractions(container);
 assertNotNull(deepStub);
 }

 @Test
 public void testRecordDeepStubMockMethod_AddsAnswerToInvocationContainer() {
 // given
 Object clazz = Map.class;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
Answer<Object> answer = mock(Answer.class);
 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(ArgumentMatchers.any(StubbedInvocationMatcher.class)));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.recordDeepStubMock(invocation, container);

 // then
 verify(container).addAnswer(answer, false);
 }

 @Test
 public void testRecordDeepStubMockMethod_ReturnsNewlyCreatedMock() {
 // given
 Object clazz = Map.class;
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);

 when(container.getInvocationForStubbing()).thenReturn(invocation);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(mock(StubbedInvocationMatcher.class)));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStub = returnsDeepStubs.recordDeepStubMock(invocation, container);

 // then
 assertNotNull(deepStub);
 }

 @Test
 public void testActualParameterizedTypeMethod_ReturnsExpectedMetadata() {
 // given
 Object mock = mock(Map.class);

 // when
 GenericDeclaration typeToMock = mock(GenericDeclaration.class);
 when(typeToMock.getTypeName()).thenReturn("java.util.Map<java.lang.Comparable<? extends java.lang.Comparable>,java.util.Set<java.lang.Number>>");
 when(typeToMock.getGenericComponentType()).thenReturn(Number.class);
 when(typeToMock.getGenericInterfaces()).thenReturn(new Class<?>[]{Map.class, Set.class});
 when(typeToMock.getGenericSuperclass()).thenReturn(new Type[]{Map.class});

 CreationSettings mockSettings = mock(CreationSettings.class);
 when(mockSettings.getTypeToMock()).thenReturn(typeToMock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // then
 GenericMetadataSupport actualParameterizedType = returnsDeepStubs.actualParameterizedType(mock);

 // then
 verify(typeToMock).getGenericInterfaces();
 verify(typeToMock).getGenericComponentType();
 verify(typeToMock).getGenericSuperclass();
 assertEquals(Map.class, actualParameterizedType.getTypeToMock());
 }
}