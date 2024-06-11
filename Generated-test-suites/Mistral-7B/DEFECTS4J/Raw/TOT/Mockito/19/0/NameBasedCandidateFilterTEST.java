package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object fieldInstance;
 private Field field;
 private List<Object> mocks;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 mockUtilSetup();
 filter = new NameBasedCandidateFilter(new NoopMockCandidateFilter());
 fieldInstance = mock(TestClass.class);
 field = MockUtil.getField(TestClass.class, "fieldName");
 }

 @Test
 public void testFilterCandidate_SingleMock_CorrectName() {
 Object mock = mock(TestClass.class, "testMockName");
 mocks.add(mock);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 @Test
 public void testFilterCandidate_SingleMock_IncorrectName() {
 Object mock = mock(TestClass.class, "incorrectMockName");
 mocks.add(mock);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_FirstCorrectName() {
 Object mock1 = mock(TestClass.class, "mock1Name");
 Object mock2 = mock(TestClass.class, "mock2Name");
 mocks.add(mock1);
 mocks.add(mock2);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_SecondCorrectName() {
 Object mock1 = mock(TestClass.class, "mock1Name");
 Object mock2 = mock(TestClass.class, "mock2Name");
 mocks.add(mock1);
 mocks.add(mock2);
 mock2.answers(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return fieldInstance;
 }
 });
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_MultipleMocks_ConflictingNames() {
 Object mock1 = mock(TestClass.class, "mock1Name");
 Object mock2 = mock(TestClass.class, "mock1Name");
 mocks.add(mock1);
 mocks.add(mock2);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 }

 private void mockUtilSetup() {
 Mockito.initMocks(this);
 }

 static class NoopMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return null;
 }
 }

 static class TestClass {
 private String fieldName;
 // getter and setter methods for fieldName
 }
}