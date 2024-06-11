import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtilMock;

 @Before
 public void setUp() {
 mockUtilMock = mock(MockUtil.class);
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_SingleMock_NoConflictingField() {
 // Given
 String mockName = "mockName";
 Object mock = mock(Object.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockName;
 }
 });
 Collection<Object> mocks = new ArrayList<Object>();
 mocks.add(mock);
 Field field = mock.getClass().getDeclaredField("fieldName");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock, result.getMock());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoConflictingField() {
 // Given
 String mockName1 = "mockName1";
 Object mock1 = mock(Object.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockName1;
 }
 });
 String mockName2 = "mockName2";
 Object mock2 = mock(Object.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockName2;
 }
 });
 Collection<Object> mocks = new ArrayList<Object>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = mock1.getClass().getDeclaredField("fieldName");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_ConflictingField() {
 // Given
 String mockName1 = "mockName1";
 Object mock1 = mock(Object.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockName1;
 }
 });
 String mockName2 = "mockName2";
 Object mock2 = mock(Object.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mockName2;
 }
 });
 Collection<Object> mocks = new ArrayList<Object>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field1 = mock1.getClass().getDeclaredField("fieldName1");
 Object fieldInstance1 = new Object();
 Field field2 = mock2.getClass().getDeclaredField("fieldName1");
 Object fieldInstance2 = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance1);

 // Then
 assertEquals(mock2, result.getMock());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullMocks() {
 Collection<Object> mocks = null;
 Field field = mockUtilMock.getMockField("fieldName", new Object());
 Object fieldInstance = new Object();
 // When & Then
 throw new AssertionError("Method should have thrown an  IllegalArgumentException");
 }

 @Test(expected = IllegalAccessException.class)
 public void testFilterCandidate_NullField() {
 Collection<Object> mocks = new ArrayList<Object>();
 Object fieldInstance = new Object();
 // When & Then
 throw new AssertionError("Method should have thrown an  IllegalAccessException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullFieldInstance() {
 Collection<Object> mocks = new ArrayList<Object>();
 Field field = mockUtilMock.getMockField("fieldName", new Object());
 // When & Then
 throw new AssertionError("Method should have thrown an  IllegalArgumentException");
 }
}