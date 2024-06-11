package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private NameBasedCandidateFilter nextMock;
 private Object fieldInstance;
 private Field field;
 private Collection<Object> mocks;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 filter = new NameBasedCandidateFilter(nextMock = mock(MockCandidateFilter.class));
 field = MockUtil.getField(NameBasedCandidateFilter.class, "next");
 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 }).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
 fieldInstance = mock(Object.class);
 mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class));
 }

 @Test
 public void testFilterCandidateWithMultipleMocksWithSameName() {
 Object mockNameMatches = mocks.get(1);
 verify(nextMock).filterCandidate(Mockito.argThat(m -> m.equals(mockNameMatches)), field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 Object mock = mocks.get(2);
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = AssertionError.class)
 public void testFilterCandidateWithNoMatchingMock() {
 mockitoClearInvocations(nextMock);
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithNullMocks() {
 mockitoClearInvocations(nextMock);
 field.set(filter, null);
 verify(nextMock).filterCandidate(Mockito.anyCollection(), field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithNullField() {
 mockitoClearInvocations(nextMock);
 field = null;
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithNullFieldInstance() {
 mockitoClearInvocations(nextMock);
 fieldInstance = null;
 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 }
}