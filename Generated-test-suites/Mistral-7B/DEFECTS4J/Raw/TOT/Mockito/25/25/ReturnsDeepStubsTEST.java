import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockMap;
 private Object mockSet;
 private Object mockNumber;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mockMap = mock(Map.class, with(Mockito.CALLS_REAL_METHODS).with(Mockito.RETURNS_DEEP_STUBS));
 mockSet = mock(Set.class, with(Mockito.CALLS_REAL_METHODS).with(Mockito.RETURNS_DEEP_STUBS));
 mockNumber = mock(Number.class, with(Mockito.CALLS_REAL_METHODS).with(Mockito.RETURNS_DEEP_Stubs));
 }

 @Test
 public void testReturningMap() {
 Map<Number, Set<Number>> map = (Map<Number, Set<Number>>) returnsDeepStubs.answer(ArgumentMatchers.argument(0));

 Set<Number> set = map.entrySet().iterator().next().getValue().iterator().next();
 Number number = set.iterator().next();

 verify(mockMap, times(1)).entrySet();
 verify(mockMap).iterator();
 verify(mockMap).getValue();
 verify(mockSet, times(1)).iterator();
 verify(mockSet).hasNext();
 verify(mockSet).next();
 verify(mockNumber, times(1)).iterator();
 verify(mockNumber).hasNext();
 verify(mockNumber).next();
 }

 @Test
 public void testReturningEmptyMap() {
 Map<Number, Set<Number>> map = (Map<Number, Set<Number>>) returnsDeepStubs.answer(ArgumentMatchers.mock(List.class));

 verify(mockMap, never()).entrySet();
 verify(mockMap, never()).iterator();
 verify(mockMap, never()).getValue();
 }

 @Test
 public void testReturningNullMap() {
 Map<Number, Set<Number>> map = (Map<Number, Set<Number>>) returnsDeepStubs.answer(ArgumentMatchers.mock(Map.class));

 assertNull(map);
 }

 @Test
 public void testReturningEmptySet() {
 Set<Number> set = (Set<Number>) returnsDeepStubs.answer(ArgumentMatchers.mock(Map.class));

 assertEmpty(set);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturningEmptySetWithIterator() {
 Set<Number> set = (Set<Number>) returnsDeepStubs.answer(ArgumentMatchers.mock(Map.class));
 Iterator<Number> iterator = set.iterator();
 iterator.next();
 }

 @Test
 public void testReturningEmptyNumber() {
 Number number = (Number) returnsDeepStubs.answer(ArgumentMatchers.mock(Map.class));

 assertEquals(0, number.intValue());
 }

 @Test
 public void testReturningNullNumber() {
 Number number = (Number) returnsDeepStubs.answer(ArgumentMatchers.mock(Map.class));

 assertNull(number);
 }

 @Test
 public void testReturningNullForUnmockableType() {
 Object object = returnsDeepStubs.answer(ArgumentMatchers.mock(String.class));

 assertNull(object);
 }
}