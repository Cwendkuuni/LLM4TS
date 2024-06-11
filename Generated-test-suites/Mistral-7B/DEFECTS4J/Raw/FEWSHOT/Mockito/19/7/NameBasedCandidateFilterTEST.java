package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

 private MockCandidateFilter nextMock;
 private NameBasedCandidateFilter filter;

 @Test
 public void testEmptyCollection() {
 filter = new NameBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 Field field = MockUtil.fieldThat().type(String.class).name("fieldName").get();
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 Assert.assertNull(result);
 }

 @Test
 public void testSingleMockWithSameName() {
 nextMock = MockUtil.mock(String.class);
 filter = new NameBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(nextMock);
 Field field = MockUtil.fieldThat().type(String.class).name("fieldName").get();
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 Assert.assertEquals(nextMock, result.getMock());
 }

 @Test
 public void testMultipleMocksWithSameName() {
 nextMock = MockUtil.mock(String.class);
 Object mock1 = nextMock;
 Object mock2 = MockUtil.mock(String.class);
 filter = new NameBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = MockUtil.fieldThat().type(String.class).name("fieldName").get();
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 Assert.assertEquals(mock1, result.getMock());
 }

 @Test
 public void testMultipleMocksWithDifferentNames() {
 nextMock = MockUtil.mock(String.class);
 Object mock1 = nextMock;
 Object mock2 = MockUtil.mock(Integer.class);
 filter = new NameBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = MockUtil.fieldThat().type(String.class).name("fieldName").get();
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 Assert.assertEquals(mock1, nextMock.filterCandidate(mocks, field, fieldInstance).getMock());
 }

 @Test
 public void testNextFilterReturnsNull() {
 nextMock = MockUtil.mock(String.class);
 filter = new NameBasedCandidateFilter(nextMock);
 Collection<Object> mocks = new ArrayList<>();
 Field field = MockUtil.fieldThat().type(String.class).name("fieldName").get();
 Object fieldInstance = new Object();

 nextMock.stub(MockUtil.methodNamed("filterCandidate")).toReturn(null);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 Assert.assertNull(result);
 }
}