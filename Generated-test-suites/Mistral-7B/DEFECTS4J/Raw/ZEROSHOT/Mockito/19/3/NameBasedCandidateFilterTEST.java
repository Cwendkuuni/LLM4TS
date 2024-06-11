package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtilSpy;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Rule
 public TestRule mockitoRule = MockitoJUnit.rule();

 @Before
 public void setUp() {
 mockUtilSpy = Mockito.spy(new MockUtil());
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 mocks = new ArrayList<>();
 field = Mockito.mock(Field.class);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_oneMock_returnsNextFilter() {
 // given
 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 // then
 assertEquals(nextMock, result.getFilter());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameName_returnsMatchingMock() {
 // given
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Mockito.when(mockUtilSpy.getMockName(mock1)).thenReturn("fieldName");
 Mockito.when(mockUtilSpy.getMockName(mock2)).thenReturn("fieldName");
 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 // then
 assertEquals(mock1, result.getInjected());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithDifferentNames_returnsNextFilter() {
 // given
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Mockito.when(mockUtilSpy.getMockName(mock1)).thenReturn("mock1Name");
 Mockito.when(mockUtilSpy.getMockName(mock2)).thenReturn("mock2Name");
 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 // then
 assertEquals(nextMock, result.getFilter());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithConflictingFieldNames_returnsMatchingMock() {
 // given
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 Mockito.when(mockUtilSpy.getMockName(mock1)).thenReturn("fieldName");
 Mockito.when(mockUtilSpy.getMockName(mock2)).thenReturn("fieldName");
 Collection<Field> fields = new ArrayList<>();
 Field fieldMock1 = Mockito.mock(Field.class);
 Field fieldMock2 = Mockito.mock(Field.class);
 fields.add(fieldMock1);
 fields.add(fieldMock2);
 Mockito.when(mock1.getClass().getDeclaredFields()).thenReturn(fields);
 Mockito.when(mock1.getClass().getDeclaredField(Mockito.eq("fieldName"))).thenReturn(fieldMock1);
 Mockito.when(mock2.getClass().getDeclaredFields()).thenReturn(fields);
 Mockito.when(mock2.getClass().getDeclaredField(Mockito.eq("fieldName"))).thenReturn(fieldMock2);
 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 // then
 assertEquals(mock1, result.getInjected());
 }
}