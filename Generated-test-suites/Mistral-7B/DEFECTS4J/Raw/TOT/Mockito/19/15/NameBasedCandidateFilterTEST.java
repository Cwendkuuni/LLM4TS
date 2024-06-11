package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object fieldInstance;
 private Collection<Object> mocksA;
 private Collection<Object> mocksB;
 private Collection<Object> mocksC;

 @Before
 public void setUp() {
 mocksA = mock(Collection.class);
 mocksB = mock(Collection.class);
 mocksC = mock(Collection.class);

 filter = new NameBasedCandidateFilter(new NoOpMockCandidateFilter()) {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return super.filterCandidate(mocks, field, fieldInstance);
 }
 };
 fieldInstance = spy(new Object());
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ShouldReturnCorrectMock() {
 String mockNameA = Mockito.mockName(mocksA);
 String mockNameB = Mockito.mockName(mocksB);
 String mockNameC = Mockito.mockName(mocksC);

 Mockito.when(mocksA.contains(Mockito.any())).thenReturn(true);
 Mockito.when(mocksB.contains(Mockito.any())).thenReturn(true);
 Mockito.when(mocksC.contains(Mockito.any())).thenReturn(false);

 Mockito.when(mocksA.get(Mockito.anyInt())).thenReturn(mockNameA);
 Mockito.when(mocksB.get(Mockito.anyInt())).thenReturn(mockNameB);

 Object mockA = filter.filterCandidate(mocksA, new Field("field1", fieldInstance.getClass()), fieldInstance);
 Object mockB = filter.filterCandidate(mocksB, new Field("field1", fieldInstance.getClass()), fieldInstance);
 Object mockC = filter.filterCandidate(mocksC, new Field("field1", fieldInstance.getClass()), fieldInstance);

 assertEquals(mockNameA, mockA);
 assertEquals(mockNameB, mockB);
 assertEquals(null, mockC);
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithDifferentNames_ShouldReturnNull() {
 String mockNameA = Mockito.mockName(mocksA);
 String mockNameB = Mockito.mockName(mocksB);
 String mockNameC = Mockito.mockName(mocksC);

 Mockito.when(mocksA.contains(Mockito.any())).thenReturn(true);
 Mockito.when(mocksB.contains(Mockito.any())).thenReturn(true);
 Mockito.when(mocksC.contains(Mockito.any())).thenReturn(true);

 Mockito.when(mocksA.get(Mockito.anyInt())).thenReturn(mockNameA);
 Mockito.when(mocksB.get(Mockito.anyInt())).thenReturn(mockNameB);
 Mockito.when(mocksC.get(Mockito.anyInt())).thenReturn(mockNameC);

 Object mockA = filter.filterCandidate(mocksA, new Field("field1", fieldInstance.getClass()), fieldInstance);
 Object mockB = filter.filterCandidate(mocksB, new Field("field1", fieldInstance.getClass()), fieldInstance);
 Object mockC = filter.filterCandidate(mocksC, new Field("field1", fieldInstance.getClass()), fieldInstance);

 assertEquals(mockNameA, mockA);
 assertEquals(mockNameB, mockB);
 assertEquals(null, mockC);
 }

 @Test
 public void testFilterCandidate_EmptyCollection_ShouldPassToNextFilter() {
 Object mock = mock(Object.class);

 Mockito.when(mocks.isEmpty()).thenReturn(true);

 Object result = filter.filterCandidate(mocks, new Field("field1", fieldInstance.getClass()), fieldInstance);

 verifyZeroInteractions(mock);

 verify(next).filterCandidate(mocks, new Field("field1", fieldInstance.getClass()), fieldInstance);
 }

 @Test
 public void testFilterCandidate_SingleMock_ShouldReturnMock() {
 Object mock = mock(Object.class);
 String mockName = Mockito.mockName(mock);

 Mockito.when(mocks.contains(Mockito.any())).thenReturn(true);
 Mockito.when(mocks.get(Mockito.anyInt())).thenReturn(mock);

 Object result = filter.filterCandidate(mocks, new Field("field1", fieldInstance.getClass()), fieldInstance);

 assertEquals(mock, result);
 }

 @Test
 public void testFilterCandidate_NullCollection_ShouldThrowNullPointerException() {
 Field field = new Field("field1", fieldInstance.getClass());

 try {
 filter.filterCandidate(null, field, fieldInstance);
 fail("Should throw NullPointerException as mocks is null");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testFilterCandidate_NullField_ShouldThrowNullPointerException() {
 Collection<Object> mocks = new ArrayList<>();
 Object mock = mock(Object.class);

 try {
 filter.filterCandidate(mocks, null, fieldInstance);
 fail("Should throw NullPointerException as field is null");
 } catch (NullPointerException e) {
 // expected
 }
}

 @Test
 public void testFilterCandidate_NullFieldInstance_ShouldThrowNullPointerException() {
 Collection<Object> mocks = new ArrayList<>();
 Field field = new Field("field1", fieldInstance.getClass());

 try {
 filter.filterCandidate(mocks, field, null);
 fail("Should throw NullPointerException as fieldInstance is null");
 } catch (NullPointerException e) {
 // expected
 }
}
}

class NoOpMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return null;
 }
}