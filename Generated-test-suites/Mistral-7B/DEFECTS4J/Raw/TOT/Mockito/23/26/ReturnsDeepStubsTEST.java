package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private GenericsNest<Number> mockGenericsNest;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnsDeepStubsForMappedType() {
 // given
 GenericsNest<Number> mockGenericsNest = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

 // when
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();

 // then
 assertInstanceOf(Number.class, number);
 // it should return a deeply stubbed Number instance
 Number deepNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 Number deepNumberAgain = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 assertEquals(deepNumber, deepNumberAgain);
 }

 @Test
 public void testDoesNotReturnDeepStubsForNonMockableTypes() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 String stringMock = mock(String.class);

 // when
 String stringResult = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArgument(0, stringMock));

 // then
 assertNotNull(stringResult);
 // it should not return a deeply stubbed instance since String is not mockable by default
 assertNotSame(stringMock, stringResult);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testThrowsExceptionForUnsupportedGenericMetadata() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
 Object unsupportedInstance = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArgument(0, genericMetadataSupport));

 // then
 fail("Expected an UnsupportedOperationException, but got: " + unsupportedInstance);
 }

 @Test
 public void testRecordsDeepStubMock() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 GenericsNest<Number> mockGenericsNest = mock(GenericsNest.class);
 Number deepNumber = mock(Number.class, Answers.RETURNS_DEEP_STUBS);

 // when
 when(mockGenericsNest.entrySet().iterator().next().getValue().iterator().next()).thenReturn(deepNumber);

 // then
 Object stubbedInstance = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArgument(0, mockGenericsNest));

 // it should return the same instance that we stubbed
 assertEquals(deepNumber, stubbedInstance);
 }

 @Test
 public void testSupportsNestedGenericInformation() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 List<List<Set<Number>>> nestedList = mock(List.class, Answers.RETURNS_DEEP_STUBS);
 Set<Number> nestedSet = mock(Set.class, Answers.RETURNS_DEEP_STUBS);
 List<Set<Number>> listOfSets = mock(List.class, Answers.RETURNS_DEEP_STUBS);

 // when
 when(nestedList.get(0)).thenReturn(listOfSets);
 when(listOfSets.get(0)).thenReturn(nestedSet);

 // then
 Object stubbedInstance = returnsDeepStubs.answer(mock(InvocationOnMock.class).withArgument(0, nestedList));
 Object stubbedInstanceInList = ((List) stubbedInstance).get(0);
 Object stubbedInstanceInSet = ((Set) stubbedInstanceInList).iterator().next();

 // it should return the same instances that we stubbed
 assertEquals(nestedSet, stubbedInstanceInSet);
 }
}