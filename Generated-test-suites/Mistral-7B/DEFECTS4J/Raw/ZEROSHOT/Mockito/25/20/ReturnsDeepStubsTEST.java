package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericsNest;
 private Object mockNumber;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();

 mockGenericsNest = mock(Map.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return Collections.emptyMap();
 }
 });

 mockNumber = mock(Number.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return 0;
 }
 });
 }

 @Test
 public void testGetMock() {
 // test with empty map
 InvocationOnMock emptyMapInvocation = Mockito.mock(InvocationOnMock.class);
 emptyMapInvocation.setMethod(Map.class.getMethod("entrySet"));
 emptyMapInvocation.setArgument(0, mockGenericsNest);

 Object result = returnsDeepStubs.answer(emptyMapInvocation);

 verify(mockGenericsNest).entrySet();
 assertEquals(mockGenericsNest, result);

 // test with ArrayList of Number
 InvocationOnMock arrayListInvocation = Mockito.mock(InvocationOnMock.class);
 arrayListInvocation.setMethod(Collections.class.getMethod("emptyList"));
 arrayListInvocation.setArgument(0, Arrays.asList(mockNumber));

 InvocationOnMock iteratorInvocation = Mockito.mock(InvocationOnMock.class);
 iteratorInvocation.setMethod(Iterator.class.getMethod("next"));
 iteratorInvocation.setArgument(0, mockNumber);

 InvocationOnMock entrySetInvocation = Mockito.mock(InvocationOnMock.class);
 entrySetInvocation.setMethod(Map.class.getMethod("entrySet"));
 entrySetInvocation.setArgument(0, mockGenericsNest);
 entrySetInvocation.setReturnValue(Arrays.asList(iteratorInvocation));

 InvocationOnMock iteratorInvocation2 = Mockito.mock(InvocationOnMock.class);
 iteratorInvocation2.setMethod(Iterator.class.getMethod("next"));
 iteratorInvocation2.setArgument(0, mockNumber);
 iteratorInvocation2.setReturnValue(mockNumber);

 InvocationOnMock valueInvocation = Mockito.mock(InvocationOnMock.class);
 valueInvocation.setMethod(Number.class.getMethod("iterator"));
 valueInvocation.setArgument(0, mockNumber);
 valueInvocation.setReturnValue(iteratorInvocation2);

 InvocationOnMock listInvocation = Mockito.mock(InvocationOnMock.class);
 listInvocation.setMethod(Collections.class.getMethod("singletonList"));
 listInvocation.setArgument(0, mockNumber);

 InvocationOnMock mapInvocation = Mockito.mock(InvocationOnMock.class);
 mapInvocation.setMethod(Map.class.getMethod("get"));
 mapInvocation.setArgument(0, ArgumentMatchers.eq(mockNumber))
 .setReturnValue(mockNumber);

 InvocationOnMock getInvocation = Mockito.mock(InvocationOnMock.class);
 getInvocation.setMethod(Map.class.getMethod("get", ArgumentMatchers.eq( ArgumentMatchers.any(Map.class)), ArgumentMatchers.eq(ArgumentMatchers.any(Set.class)) ));
 getInvocation.setArgument(1, mockGenericsNest);
 getInvocation.setArgument(2, mockNumber);
 getInvocation.setReturnValue(mockNumber);

 InvocationOnMock mapEntrySetInvocation = Mockito.mock(InvocationOnMock.class);
 mapEntrySetInvocation.setMethod(Map.class.getMethod("entrySet"));
 mapEntrySetInvocation.setArgument(0, mockGenericsNest);
 mapEntrySetInvocation.setReturnValue(Arrays.asList(mapInvocation));

 InvocationOnMock setInvocation = Mockito.mock(InvocationOnMock.class);
 setInvocation.setMethod(Set.class.getMethod("iterator"));
 setInvocation.setArgument(0, mockGenericsNest);
 setInvocation.setReturnValue(iteratorInvocation);

 InvocationOnMock iteratorInvocation3 = Mockito.mock(InvocationOnMock.class);
 iteratorInvocation3.setMethod(Iterator.class.getMethod("next"));
 iteratorInvocation3.setArgument(0, mockNumber);
 iteratorInvocation3.setReturnValue(mockNumber);

 InvocationOnMock valueInvocation2 = Mockito.mock(InvocationOnMock.class);
 valueInvocation2.setMethod(Number.class.getMethod("iterator"));
 valueInvocation2.setArgument(0, mockNumber);
 valueInvocation2.setReturnValue(iteratorInvocation3);

 InvocationOnMock valueInvocation3 = Mockito.mock(InvocationOnMock.class);
 valueInvocation3.setMethod(Number.class.getMethod("clone"));
 valueInvocation3.setArgument(0, mockNumber);
 valueInvocation3.setReturnValue(mockNumber);

 InvocationOnMock getInvocation2 = Mockito.mock(InvocationOnMock.class);
 getInvocation2.setMethod(Map.class.getMethod("get", ArgumentMatchers.eq( ArgumentMatchers.any(Map.class)), ArgumentMatchers.eq(ArgumentMatchers.any(Set.class)), ArgumentMatchers.eq(ArgumentMatchers.eq(Number.class)) ));
 getInvocation2.setArgument(1, mockGenericsNest);
 getInvocation2.setArgument(2, mapEntrySetInvocation);
 getInvocation2.setArgument(3, mockNumber);
 getInvocation2.setReturnValue(mockNumber);

 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 mockGenericsNest.answer(mapEntrySetInvocation);
 mapEntrySetInvocation.answer(getInvocation);
 getInvocation.answer(valueInvocation);
 valueInvocation.answer(valueInvocation2);
 valueInvocation2.answer(valueInvocation3);
 valueInvocation3.answer(getInvocation2);
 getInvocation2.answer(mapInvocation);
 mapInvocation.answer(setInvocation);
 setInvocation.answer(iteratorInvocation);
 iteratorInvocation.answer(iteratorInvocation3);
 iteratorInvocation3.answer(valueInvocation3);
 valueInvocation3.answer(mockNumber);

 container.addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockGenericsNest;
 }
 }, false);

 when(mockGenericsNest.getClass()).thenReturn(GenericNest.class);

 Object result = returnsDeepStubs.answer(getInvocation2);

 assertEquals(mockNumber, result);
 }

}