package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*; // for mockito and BDD style mocking

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockAdapter;
import org.mockito.stubbing.Answer;

import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @SuppressWarnings("unchecked")
 @Test
 public void testAnswerWithMatchingInvocation() throws Exception {
 // Given
 InterfaceA mockInterfaceA = mock(InterfaceA.class, with(Answers.RETURNS_DEEP_STUBS));
 InterfaceB mockInterfaceB = mockInterfaceA.entrySet().iterator().next().getValue();
 Number number = mockInterfaceB.iterator().next();

 // When
 Object result = number.iterator().next();

 // Then
 assertSame(number, result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testAnswerWithNonMatchingInvocation() throws Exception {
 // Given
 InterfaceA mockInterfaceA = mock(InterfaceA.class, with(Answers.RETURNS_DEEP_STUBS));

 // When
 Object result = mockInterfaceA.toString();

 // Then
 assertNull(result);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testReturnEmptyValuesForNonMockableType() throws Exception {
 // Given
 InterfaceNonMockable mockInterfaceNonMockable = mock(InterfaceNonMockable.class, with(Answers.RETURNS_DEEP_STUBS));

 // When
 Object result = mockInterfaceNonMockable.someMethod();

 // Then
 assertEquals("", result.toString());
 }

 @Test
 public void testGetMockWithEmptyInvocationContainer() throws Exception {
 // Given
 InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
 InvocationContainerImpl container = new InvocationContainerImpl(handler);

 // When
 Object result = returnsDeepStubs.getMock(mock(Object.class), new GenericMetadataSupport(Object.class));

 // Then
 assertNull(result);
 }

 @Test
 public void testGetMockWithNoMatchingStubbedInvocations() throws Exception {
 // Given
 Class<?> clazz = MockitoCore.class;
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(clazz));
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

 // When
 Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(clazz));

 // Then
 assertNull(result);
 }

 @Test(expected = Throwable.class)
 public void testGetMockWithThrowingException() throws Exception {
 // Given
 Class<?> clazz = MockitoCore.class;
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(clazz));
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 doThrow(new RuntimeException("Exception thrown for testing purposes")) // BDD style
 .when(container)
 .method(anyMethod());

 // When
 Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(clazz));

 // Then
 fail("Expected a Throwable, but got " + result);
 }

 @Test
 public void testCreateNewDeepStubMock() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));

 // When
 Object deepStubMock = returnsDeepStubs.createNewDeepStubMock(new GenericMetadataSupport(InterfaceA.class));

 // Then
 assertEquals(InterfaceA.class, deepStubMock.getClass());
 verify(deepStubMock).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);
 Answer answer = mock(Answer.class);
 container.addAnswer(answer, false);

 // Then
 assertEquals(testInterface, deepStubMock);
 verify(answer).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 }

 @Test
 public void testRecordDeepStubMockWithMultipleInvocations() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));
 Answer mockAnswer = mock(Answer.class);
 container.addAnswer(mockAnswer, false);

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);
 Object deepStubMock2 = returnsDeepStubs.recordDeepStubMock(testInterface, container);

 // Then
 assertEquals(testInterface, deepStubMock);
 assertEquals(testInterface, deepStubMock2);
 verify(mockAnswer).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockAnswer, times(2)).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 }

 @Test
 public void testReturnDeepStubWithMatchingStubbedInvocations() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));
 Answer mockAnswer = mock(Answer.class);
 container.addAnswer(mockAnswer, false);

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);

 // Then
 assertSame(deepStubMock, returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(InterfaceA.class)));
 }

 @Test
 public void testReturnDeepStubWithMultipleMatchingStubbedInvocations() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));
 Answer mockAnswer = mock(Answer.class);
 container.addAnswer(mockAnswer, false);
 Answer mockAnswer2 = mock(Answer.class);
 container.addAnswer(mockAnswer2, false);

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);
 Object deepStubMock2 = returnsDeepStubs.recordDeepStubMock(testInterface, container);

 // Then
 assertSame(deepStubMock, returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(InterfaceA.class)));
 assertSame(deepStubMock2, returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(InterfaceA.class)));

 // Should return the same object for both invocations
 verify(mockAnswer).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockAnswer2).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockAnswer).eq(mockAnswer2);
 }

 @Test
 public void testReturnEmptyValuesForMultipleMatchingStubbedInvocations() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));
 Answer mockAnswer = mock(Answer.class);
 container.addAnswer(mockAnswer, false);
 Answer mockAnswer2 = mock(Answer.class);
 container.addAnswer(mockAnswer2, false);

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);
 Object deepStubMock2 = returnsDeepStubs.recordDeepStubMock(testInterface, container);

 // Then
 assertEquals(deepStubMock, deepStubMock2);
 // Both should return the same empty object
 assertEquals(Collections.emptyList(), deepStubMock.methodWithListReturnType());
 assertEquals(Collections.emptySet(), deepStubMock.methodWithSetReturnType());
 assertEquals(Collections.emptyMap(), deepStubMock.methodWithMapReturnType());

 // Should return the same object for both invocations
 verify(mockAnswer).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockAnswer2).answer(ArgumentMatchers.isA(InvocationOnMock.class));
 verify(mockAnswer).eq(mockAnswer2);
 }

 @Test
 public void testReturnDeepStubWithEmptyInvocationContainer() throws Exception {
 // Given
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));

 // When
 Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(Object.class));

 // Then
 assertNull(result);
 }

 @Test
 public void testRecordDeepStubMockWithException() throws Exception {
 // Given
 InterfaceA testInterface = mock(InterfaceA.class, withSettings().defaultAnswer(returnsDeepStubs));
 InvocationContainerImpl container = new InvocationContainerImpl(mock(InternalMockHandler.class));
 doThrow(new RuntimeException("Exception thrown for testing purposes")) // BDD style
 .when(container)
 .method(anyMethod());

 // When
 Object deepStubMock = returnsDeepStubs.recordDeepStubMock(testInterface, container);

 // Then
 assertNull(deepStubMock);
 }

 @Test
 public void testGetMockWithException() throws Exception {
 // Given
 Class<?> clazz = MockitoCore.class;
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock(clazz));
 InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
 doThrow(new RuntimeException("Exception thrown for testing purposes")) // BDD style
 .when(container)
 .method(anyMethod());

 // When
 Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport(clazz));

 // Then
 assertNull(result);
 }
}