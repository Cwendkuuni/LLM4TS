package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 mockUtil = Mockito.spy(new MockUtil());
 filter = new NameBasedCandidateFilter(null);
 }

 @Test
 public void testFilterCandidate_SingleMock_ReturnCorrectMock() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = mock(Object.class);
 String mockName = Name.getMockName(mocks.get(0));

 List<Object> mockNameMatches = new ArrayList<>();
 mockNameMatches.add(mocks.get(0));

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mockNameMatches.get(0), result.getCandidate());
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ReturnCorrectMock() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object1.class));
 mocks.add(mock(Object1.class));
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = mock(Object.class);

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mocks.get(0), result.getCandidate());
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_NullMocks_ThrowException() {
 Collection<Object> mocks = null;
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = mock(Object.class);

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullField_ThrowException() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 Field field = null;
 Object fieldInstance = mock(Object.class);

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullFieldInstance_ThrowException() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = null;

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_EmptyMocks_ReturnNull() {
 Collection<Object> mocks = new ArrayList<>();
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = mock(Object.class);

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(null, result.getCandidate());
 }

 @Test
 public void testFilterCandidate_DifferentTypeMocks_ReturnNull() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object1.class));
 mocks.add(mock(Object2.class));
 Field field = Mockito.mock(Field.class);
 Object fieldInstance = mock(Object.class);

 NameBasedCandidateFilter spyFilter = spy(filter);
 OngoingInjecter result = spyFilter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(null, result.getCandidate());
 }
}