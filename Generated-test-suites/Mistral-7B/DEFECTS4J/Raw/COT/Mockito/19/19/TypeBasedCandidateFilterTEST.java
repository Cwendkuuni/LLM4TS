package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class TypeBasedCandidateFilterTest {

 private MockCandidateFilter nextMock;

 @Before
 public void setup() {
 nextMock = mock(MockCandidateFilter.class);
 }

 // Test constructor
 @Test
 public void testTypeBasedCandidateFilterConstructor() {
 new TypeBasedCandidateFilter(nextMock);
 }

 // Test filterCandidate method with empty collection
 @Test
 public void testFilterCandidateEmptyCollection() {
 TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 // Test filterCandidate method with collection containing mocks of type that matches the field type
 @Test
 public void testFilterCandidateCollectionWithMatchingMocks() {
 TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(String.class));
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 // Test filterCandidate method with collection containing mocks of type that does not match the field type
 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidateCollectionWithNonMatchingMocks() {
 TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Integer.class));
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();
 filter.filterCandidate(mocks, field, fieldInstance);
 }

 // Test filterCandidate method with next filter returning an injector
 @Test
 public void testFilterCandidateNextReturnsInjector() {
 TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
 List<Object> mockTypeMatches = new ArrayList<>();
 mockTypeMatches.add(mock(String.class));
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();
 OngoingInjecter nextResult = mock(OngoingInjecter.class);
 when(nextMock.filterCandidate(mockTypeMatches, field, fieldInstance)).thenReturn(nextResult);
 OngoingInjecter result = filter.filterCandidate(mockTypeMatches, field, fieldInstance);
 assertNotNull(result);
 }
}