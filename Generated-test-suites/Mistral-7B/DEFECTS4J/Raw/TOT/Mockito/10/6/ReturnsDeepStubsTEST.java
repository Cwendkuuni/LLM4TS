package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private GenericsNest<Number> mockGenericsNest;
 private GenericsNest<String> mockGenericsNestString;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void returnsDeepStubInstance() {
 mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 GenericsNest<Number> nestedNest = mockGenericsNest.entrySet().iterator().next().getValue();
 Number number = nestedNest.iterator().next();

 verify(nestedNest).iterator();
 verify(number).equals(number);
 }

 @Test
 public void deepStubsCanNestWithMultipleGenerics() {
 mockGenericsNestString = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 GenericsNest<Map<Number, List<Number>>> nestedMap = mockGenericsNestString.entrySet().iterator().next().getValue();
 Map<Number, List<Number>> map = nestedMap.iterator().next();
 List<Number> numbers = map.iterator().next().getValue();
 Number number = numbers.iterator().next();

 verify(nestedMap).iterator();
 verify(map).iterator();
 verify(numbers).iterator();
 verify(number).equals(number);
 }

 @Test(expected = IllegalStateException.class)
 public void cannotCreateDeepStubForNonMockableType() {
 when(mockGenericsNest).thenAnswer(returnsDeepStubs);
 when(mockGenericsNest.getClass()).thenReturn(String.class);

 mockGenericsNest.iterator().next(); // this will throw IllegalStateException
 }

 @Test
 public void testDeepStubsAnswerSerialization() {
 GenericsNest<String> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 GenericsNest<String> deepStub = (GenericsNest<String>) mockitoCore().mock(GenericsNest.class, new ReturnsDeepStubsSerializationFallback(mock.getClass()));
 deepStub.iterator().next();
 }

 @Test
 public void testNestedDeepStubsAnswerSerialization() {
 GenericsNest<Map<Number, Number>> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
 GenericsNest<Map<Number, Number>> deepStub = (GenericsNest<Map<Number, Number>>) mockitoCore().mock(GenericsNest.class, new ReturnsDeepStubsSerializationFallback(mock.getClass()));
 GenericsNest<Map.Entry<Number, Number>> nestedDeepStub = (GenericsNest<Map.Entry<Number, Number>>) deepStub.entrySet().iterator().next().getValue();
 Map.Entry<Number, Number> entry = nestedDeepStub.iterator().next();
 Number number = entry.getValue();

 verify(nestedDeepStub).iterator();
 verify(entry).getValue().equals(number);
 verify(number).equals(number);
 }
}