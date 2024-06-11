package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockDeepStub;

 @Before
 public void setUp() {
 mockDeepStub = Mockito.mock(Map.class, with(ReturnsDeepStubs.class));
 returnsDeepStubs = Mockito.mock(Answer.class);
 }

 @Test
 public void testDeepStubForCollection() {
 // given
 List<Number> numbers = new ArrayList<>();
 when(mockDeepStub.entrySet().iterator().next().getValue().iterator().next())
 .thenReturn(numbers);

 // when
 Number number = (Number) mockDeepStub.entrySet().iterator().next().getValue().iterator().next();

 // then
 assertEquals(numbers, number);
 }

 @Test
 public void testDeepStubForMap() {
 // given
 Map<String, Map<String, Number>> map = new HashMap<>();
 when(mockDeepStub.entrySet().iterator().next().getValue())
 .thenReturn(map);

 // when
 Map<String, Number> mapValue = (Map<String, Number>) mockDeepStub.entrySet().iterator().next().getValue();

 // then
 assertEquals(map, mapValue);
 }

 @Test
 public void testDeepStubForNestedGenericTypes() {
 // given
 Map<String, List<Map<String, List<Number>>>> map = new HashMap<>();
 when(mockDeepStub.entrySet().iterator().next().getValue())
 .thenReturn(map);

 // when
 List<Map<String, List<Number>>> list = (List<Map<String, List<Number>>>) mockDeepStub.entrySet().iterator().next().getValue();
 Map<String, List<Number>> innerMap = list.get(0);
 List<Number> numbers = innerMap.get(0);

 // then
 assertEquals(map, list);
 assertEquals(innerMap, numbers);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testExceptionWhenInvokingUnsupportedMethodOnDeepStub() {
 // given
 Map<String, Map<String, Number>> map = new HashMap<>();
 when(mockDeepStub.entrySet().iterator().next().getValue())
 .thenReturn(map);

 // when & then
 verify(mockDeepStub).remove(anyString());
 }
}