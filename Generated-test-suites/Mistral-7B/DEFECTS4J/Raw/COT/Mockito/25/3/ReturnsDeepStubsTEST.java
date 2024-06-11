package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static final long serialVersionUID = -7105341425736035847L;

 @Test
 public void testAnswer() throws Exception {
 // Test method: public java.lang.Object answer(org.mockito.internal.invocation.InvocationOnMock)
 // Test case 1: Basic functionality
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 List list = new ArrayList<>();
 list.add(mock);
 Map<Object, Set<Number>> map = (Map<Object, Set<Number>>) list.get(0);
 assertEquals(map, map.entrySet().iterator().next().getValue().iterator().next());

 // Test case 2: Edge case - null invocation
 InvocationOnMock nullInvocation = null;
 try {
 answer(nullInvocation);
 } catch (Throwable e) {
 // Expected exception. Do nothing.
 }

 // Test case 3: Edge case - non-mockable return type
 answer(mock(Number.class));

 // Test case 4: Edge case - already stubbed invocation
 InvocationOnMock alreadyStubbedInvocation = Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock stubbedInvocation = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 alreadyStubbedInvocation.answer(stubbedInvocation);
 verify(stubbedInvocation).answer(any(InvocationOnMock.class));

 // Test case 5: Edge case - deep stubbing
 InvocationOnMock deepInvocation = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationOnMock deepInvocation2 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 deepInvocation.answer(deepInvocation2);
 assertEquals(deepInvocation2, deepInvocation.entrySet().iterator().next().getValue().iterator().next());
 }

 private Object answer(InvocationOnMock invocation) throws Throwable {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 return returnsDeepStubs.answer(invocation);
 }
}