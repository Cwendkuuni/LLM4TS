package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 filter = new NameBasedCandidateFilter(null);
 fieldInstance = new Object();
 mocks = new ArrayList<>();
 }

 @Test
 public void testFilterCandidate_noConflictingMockNames_nextFilterCalled() {
 Object mock1 = MockUtil.createMock(String.class);
 mocks.add(mock1);

 Field field1 = new Field("testField1", String.class, fieldInstance.getClass());
 InvocationOnMock mockName1 = mockUtil.getMockName(mock1);
 when(mock1.toString()).thenReturn(mockName1.toString());

 Object expectedResult = new Object();
 Object result = filter.filterCandidate(mocks, field1, fieldInstance);
 Assert.assertSame(expectedResult, result);
 }

 @Test
 public void testFilterCandidate_conflictingMockNames_correctMockUsed() {
 Object mock1 = MockUtil.createMock(String.class);
 Object mock2 = MockUtil.createMock(String.class);
 mocks.add(mock1);
 mocks.add(mock2);

 Field field1 = new Field("testField1", String.class, fieldInstance.getClass());
 InvocationOnMock mockName1 = mockUtil.getMockName(mock1);
 when(mock1.toString()).thenReturn(mockName1.toString());

 Field field2 = new Field("testField2", String.class, fieldInstance.getClass());
 InvocationOnMock mockName2 = mockUtil.getMockName(mock2);
 when(mock2.toString()).thenReturn(mockName1.toString());

 Object expectedResult = mock1;
 Object result = filter.filterCandidate(mocks, field1, fieldInstance);
 Assert.assertSame(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_nullMocks_throwsIllegalArgumentException() {
 filter.filterCandidate(null, null, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_nullField_throwsIllegalArgumentException() {
 filter.filterCandidate(mocks, null, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_nullFieldInstance_throwsIllegalArgumentException() {
 filter.filterCandidate(mocks, field1, null);
 }

 @Test
 public void testFilterCandidate_singleMock_nextFilterCalled() {
 Object mock = MockUtil.createMock(String.class);
 mocks.add(mock);

 Field field = new Field("testField", String.class, fieldInstance.getClass());
 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertNull(result);
 }
}