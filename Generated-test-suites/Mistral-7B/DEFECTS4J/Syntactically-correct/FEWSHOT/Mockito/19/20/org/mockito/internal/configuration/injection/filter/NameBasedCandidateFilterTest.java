package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockUtil;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.naming.Name;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Collection<Object> mocks;

 @Test
 public void testEmptyCollection() {
 filter = new NameBasedCandidateFilter(null);
 Assert.assertNull(filter.filterCandidate(mocks, null, null));
 }

 @Test
 public void testSingleMock() {
 mocks = new ArrayList<>();
 mocks.add(Mockito.spy(new Object()));
 filter = new NameBasedCandidateFilter(null);
 Field field = MockUtil.findField(Object.class, "fieldName");
 Object fieldInstance = Mockito.spy(new Object());
 Name mockName = new Name("mockName");
 Mockito.doReturn(mockName).when(mockUtil).getMockName(Mockito.any());

 Assert.assertEquals(Mockito.spy(new Object()), filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testMultipleMocksWithSameName() {
 mocks = new ArrayList<>();
 mocks.add(Mockito.spy(new Object1()));
 mocks.add(Mockito.spy(new Object1()));
 filter = new NameBasedCandidateFilter(null);
 Field field = MockUtil.findField(Object1.class, "fieldName");
 Object fieldInstance = Mockito.spy(new Object1());
 Name mockName1 = new Name("mockName");
 Name mockName2 = new Name("mockName");
 Mockito.doReturn(mockName1).when(mockUtil).getMockName(mocks.get(0));
 Mockito.doReturn(mockName2).when(mockUtil).getMockName(mocks.get(1));

 Assert.assertEquals(mocks.get(1), filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testMultipleMocksWithDifferentNames() {
 mocks = new ArrayList<>();
 mocks.add(Mockito.spy(new Object1()));
 mocks.add(Mockito.spy(new Object2()));
 filter = new NameBasedCandidateFilter(null);
 Field field = MockUtil.findField(Object1.class, "fieldName1");
 Object fieldInstance1 = Mockito.spy(new Object1());
 Name mockName1 = new Name("mockName1");
 Mockito.doReturn(mockName1).when(mockUtil).getMockName(mocks.get(0));
 Field field2 = MockUtil.findField(Object2.class, "fieldName2");
 Object fieldInstance2 = Mockito.spy(new Object2());
 Name mockName2 = new Name("mockName2");
 Mockito.doReturn(mockName2).when(mockUtil).getMockName(mocks.get(1));

 Assert.assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance1));
 }

 @Test
 public void testNextFilter() {
 mocks = new ArrayList<>();
 mocks.add(Mockito.spy(new Object1()));
 mocks.add(Mockito.spy(new Object2()));
 filter = new NameBasedCandidateFilter(new IgnoredFilter());
 Field field = MockUtil.findField(Object1.class, "fieldName1");
 Object fieldInstance1 = Mockito.spy(new Object1());
 Name mockName1 = new Name("mockName1");
 Mockito.doReturn(mockName1).when(mockUtil).getMockName(mocks.get(0));
 Field field2 = MockUtil.findField(Object2.class, "fieldName2");
 Object fieldInstance2 = Mockito.spy(new Object2());
 Name mockName2 = new Name("mockName2");
 Mockito.doReturn(mockName2).when(mockUtil).getMockName(mocks.get(1));

 List<Object> mockNameMatches = new ArrayList<>();
 if (mocks.size() > 1) {
 for (Object mock : mocks) {
 if (field.getName().equals(mockName1.toString())) {
 mockNameMatches.add(mock);
 }
 }
 }
 IgnoredFilter ignoredFilter = new IgnoredFilter();
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance1);
 Assert.assertEquals(mocks.get(0), result);

 mockNameMatches.clear();
 if (mocks.size() > 1) {
 for (Object mock : mocks) {
 if (field.getName().equals(mockName2.toString())) {
 mockNameMatches.add(mock);
 }
 }
 }
 ResultFilter resultFilter = new ResultFilter();
 result = filter.filterCandidate(mockNameMatches, field, fieldInstance2);
 Assert.assertEquals(mocks.get(1), result);
 }
}