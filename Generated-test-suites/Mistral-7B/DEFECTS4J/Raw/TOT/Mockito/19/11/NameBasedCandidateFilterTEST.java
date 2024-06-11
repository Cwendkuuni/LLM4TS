package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private Object fieldInstance;
 private Collection<Object> mocks;
 private Field field;

 @Before
 public void setup() {
 mockUtil = Mockito.spy(new MockUtil());
 filter = new NameBasedCandidateFilter(new DummyFilter()) {
 @Override
 public String getMockName(Object mock) {
 return "mockName";
 }
 };
 fieldInstance = new Object();
 field = MockUtil.getField(NameBasedCandidateFilter.class, "next");
 mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class));
 }

 @Test
 public void testFilterCandidate_sameName_returnsCorrectMock() {
 when(mockUtil.getMockName(mocks.get(0))).thenReturn("fieldName");

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(mocks.get(0)).equals(result.getMock());
 }

 @Test
 public void testFilterCandidate_differentName_returnsNextFilter() {
 when(mockUtil.getMockName(mocks.get(0))).thenReturn("differentName");

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verifyZeroInteractions(mocks.get(0));
 verify(mocks.get(1)).isInstanceOf(NameBasedCandidateFilter.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_noMatches_throwsException() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_conflictingNames_returnsCorrectMock() {
 when(mockUtil.getMockName(mocks.get(0))).thenReturn("conflictingName");
 when(mockUtil.getMockName(mocks.get(1))).thenReturn("conflictingName");

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(mocks.get(1)).equals(result.getMock());
 }
}

class DummyFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException();
 }
}