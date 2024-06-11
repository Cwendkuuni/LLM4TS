package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;
 private Map<String, Number> map;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mock = mock(Object.class, returnsDeepStubs);
 map = mock(Map.class, returnsDeepStubs);
 }

 @Test
 public void testReturnsDeepStubsForObjects() throws Exception {
 Object deepStub = returnsDeepStubs.answer(ArgumentMatchers.any(Object.class));
 verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals(mock, deepStub);
 }

 @Test
 public void testReturnsDeepStubsForMaps() throws Exception {
 Object deepStubMapEntry = mock(Map.Entry.class).answer(ArgumentMatchers.any(Object.class), ArgumentMatchers.any(Object.class));
 Object deepStubMapIterator = mock(Iterator.class).answer(ArgumentMatchers.any(Map.Entry.class).answers( deepStubMapEntry));
 Object deepStubMapEntryValueIterator = mock(Iterator.class).answer(ArgumentMatchers.any(Number.class));
 Object deepStubMapValue = mock(Number.class);
 Object deepStubMap = mock(Map.class, returnsDeepStubs);
 Object deepStub = returnsDeepStubs.answer(ArgumentMatchers.calling(Map.class).nothings().withArguments(map.getClass(), (Class[]) ArgumentMatchers.anyArray()));

 Map<String, Number> expectedDeepStubMap = new HashMap<>();
 expectedDeepStubMap.put("testKey", deepStubMapValue);

 verify(map).entrySet().iterator().next().getValue().iterator().next();
 verify(deepStubMapEntry).getValue();
 verify(deepStubMapEntryValueIterator).next();
 verify(deepStubMapValue).equals(deepStubMapValue);
 verify(deepStubMap).equals(expectedDeepStubMap);

 assertEquals(expectedDeepStubMap, deepStub);
 }

 @Test(expected = IllegalStateException.class)
 public void testDoesNotReturnDeepStubsForNonMockableTypes() throws Exception {
 Object nonMockableType = new Object(){};
 Object deepStub = returnsDeepStubs.answer(ArgumentMatchers.any(Class.class).eq(nonMockableType.getClass()));
 fail("Expected IllegalStateException but got: " + deepStub);
 }
}