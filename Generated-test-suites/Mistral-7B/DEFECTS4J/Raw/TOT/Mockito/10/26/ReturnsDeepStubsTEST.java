import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockMap;

 @Before
 public void setUp() {
 mockMap = Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 returnsDeepStubs = Mockito.spy(ReturnsDeepStubs.class);
 }

 @Test
 public void testAnswerWithSameType() throws Throwable {
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(mockMap));
 assertSame(mockMap, result);
 }

 @Test
 public void testAnswerWithSubClass() throws Throwable {
 Object mockSubMap = Mockito.mock(HashMap.class, Answers.RETURNS_DEEP_STUBS);
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(mockSubMap));
 assertSame(mockSubMap, result);
 }

 @Test
 public void testAnswerWithDifferentType() throws Throwable {
 Object mockList = Mockito.mock(List.class, Answers.RETURNS_DEEP_STUBS);
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(mockList));
 assertNotSame(mockList, result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerWithNonMockableType() throws Throwable {
 Object result = returnsDeepStubs.answer(mock(IOException.class));
 fail("Should not be able to deep stub non-mockable type");
 }

 @Test
 public void testDeepStubbing() throws Throwable {
 Object mapEntrySet = mockMap.entrySet();
 Object mapEntrySetIterator = mock(Iterator.class, Answers.RETURNS_DEEP_STUBS);
 when(mapEntrySet.iterator()).thenReturn(mapEntrySetIterator);
 Object mapEntryValue = mock(Number.class);
 when(mapEntrySetIterator.next()).thenReturn(Mockito.argThat(i -> i.getValue() == mapEntryValue));
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(mapEntrySet, Mockito.eq(0)));
 assertSame(mapEntryValue, result);
 }

 @Test(expected = Throwable.class)
 public void testDeepStubbingWithIncorrectInvocation() throws Throwable {
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(mockMap, ArgumentMatchers.anyInt()));
 fail("Should not be able to deep stub incorrect invocation");
 }

 @Test
 public void testNewDeepStubMock() throws Throwable {
 Object deepStubMock = returnsDeepStubs.newDeepStubMock(getMockMetadata(Map.class));
 assertNotNull(deepStubMock);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Throwable {
 Object deepStubMock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(deepStubMock).getInvocationContainer();
 Object result = returnsDeepStubs.recordDeepStubAnswer(deepStubMock, container);
 assertSame(deepStubMock, result);
 }

 @Test
 public void testDeepStubbingWithStubbedInvocations() throws Throwable {
 Object deepStubMock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(deepStubMock).getInvocationContainer();
 when(deepStubMock.entrySet()).thenAnswer(i -> new EntrySetAnswer("testValue"));
 when(deepStubMock.iterator()).thenAnswer(i -> new IteratorAnswer("testValue"));
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(deepStubMock));
 assertEquals("testValue", result);
 }

 @Test
 public void testDeepStubbingWithDifferentMethods() throws Throwable {
 Object deepStubMock1 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 Object deepStubMock2 = mock(Set.class, Answers.RETURNS_DEEP_STUBS);
 when(deepStubMock1.entrySet()).thenAnswer(i -> new EntrySetAnswer("testValue1"));
 when(deepStubMock2.iterator()).thenAnswer(i -> new IteratorAnswer("testValue2"));
 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArguments(deepStubMock1, deepStubMock2));
 assertNotEquals("testValue1", result);
 assertNotEquals("testValue2", result);
 }

 private GenericMetadataSupport getMockMetadata(Class<?> clazz) {
 CreationSettings creationSettings = new CreationSettings();
 creationSettings.setTypeToMock(clazz);
 return GenericMetadataSupport.inferFrom(creationSettings);
 }

 private static class EntrySetAnswer implements Answer<Object> {
 private final String value;

 EntrySetAnswer(String value) {
 this.value = value;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return value;
 }
 }

 private static class IteratorAnswer implements Answer<Object> {
 private final String value;

 IteratorAnswer(String value) {
 this.value = value;
 }

 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return value;
 }
 }

}