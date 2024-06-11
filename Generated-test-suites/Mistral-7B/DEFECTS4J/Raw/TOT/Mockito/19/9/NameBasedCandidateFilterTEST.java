package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private MockCandidateFilter filter;
 private Object fieldInstance;
 private List<Object> mocks;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class, "mock1"));
 mocks.add(mock(Object.class, "mock2"));
 filter = new NameBasedCandidateFilter(new DefaultMockCandidateFilter()) {};
 fieldInstance = new Object();
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 // given
 Field field = MockUtil.getField(fieldInstance, "testField");

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(mocks.get(1), result.getMock());
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 // given
 List<Object> mocksToFilter = Collections.singletonList(mocks.get(0));
 OngoingInjecter result = filter.filterCandidate(mocksToFilter, field, fieldInstance);

 // then
 assertEquals(mocks.get(0), result.getMock());
 }

 @Test
 public void testFilterCandidateWithConflictingNames() {
 // given
 Field conflictingField = MockUtil.getField(fieldInstance, "testField");
 Object conflictingMock = mock(Object.class, "mockWithSameName");
 mocks.add(conflictingMock);

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, conflictingField, fieldInstance);

 // then
 assertEquals(conflictingMock, result.getMock());
 }

 @Test
 public void testFilterCandidateWithNoMatchingMocks() {
 // given
 List<Object> mocksToFilter = Collections.singletonList(mocks.get(3)); // new Object()

 // when
 OngoingInjecter result = filter.filterCandidate(mocksToFilter, field, fieldInstance);

 // then
 assertEquals(null, result.getMock());
 }

 @Test
 public void testFilterCandidateWithNullField() {
 // given
 Field nullField = null;

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, nullField, fieldInstance);

 // then
 assertEquals(null, result.getMock());
 }

 @Test
 public void testFilterCandidateWithNullFieldInstance() {
 // given
 Object nullFieldInstance = null;

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, nullFieldInstance);

 // then
 assertEquals(null, result.getMock());
 }

 @Test
 public void testFilterCandidateWithNullMocks() {
 // given
 List<Object> nullMocks = null;

 // when
 OngoingInjecter result = filter.filterCandidate(nullMocks, field, fieldInstance);

 // then
 assertEquals(null, result.getMock());
 }

 @Test
 public void testFilterCandidateWithEmptyMocks() {
 // given
 List<Object> emptyMocks = Collections.emptyList();

 // when
 OngoingInjecter result = filter.filterCandidate(emptyMocks, field, fieldInstance);

 // then
 assertEquals(null, result.getMock());
 }
}

class DefaultMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks,
 Field field, Object fieldInstance) {
 return new OngoingInjecter(null);
 }
}

class OngoingInjecter {
 private Object mock;

 public OngoingInjecter(Object mock) {
 this.mock = mock;
 }

 public Object getMock() {
 return mock;
 }
}