package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injector.OngoingStubbing;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Stubber stubber;
 private Object fieldInstance;

 @Before
 public void setUp() {
 stubber = mock(Stubber.class);
 nextMock = spy(new NameBasedCandidateFilter(stubber));
 filter = new NameBasedCandidateFilter(nextMock);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_oneMock_oneField() {
 List<Object> mocks = Collections.singletonList(mock(Object.class));
 when(stubber.createMock(Field.class)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock(Field.class);
 }
 });
 when(nextMock.filterCandidate(mocks, any(Field.class), eq(fieldInstance)))
 .thenReturn(mock(OngoingStubbing.class));

 Object result = filter.filterCandidate(mocks, new Field("testField", String.class), fieldInstance);
 assertEquals(mock(OngoingStubbing.class), result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_noNameMatch() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class));
 when(stubber.createMock(Field.class)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock(Field.class);
 }
 });
 when(nextMock.filterCandidate(mocks, any(Field.class), eq(fieldInstance)))
 .thenReturn(mock(OngoingStubbing.class));

 Object result = filter.filterCandidate(mocks, new Field("testField1", String.class), fieldInstance);
 assertEquals(mock(OngoingStubbing.class), result);
 }

 @Test
 public void testFilterCandidate_multipleMocks_nameMatch() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "Mock1"));
 mocks.add(mock(Object.class, "Mock2"));
 when(stubber.createMock(Field.class)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock(Field.class);
 }
 });
 when(nextMock.filterCandidate(mocks, any(Field.class), eq(fieldInstance)))
 .thenReturn(mock(OngoingStubbing.class));

 Object result = filter.filterCandidate(mocks, new Field("Mock1", String.class), fieldInstance);
 assertEquals(mock(OngoingStubbing.class), result);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_multipleMocks_conflictingNames() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "Mock1"));
 mocks.add(mock(Object.class, "Mock1"));
 when(stubber.createMock(Field.class)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return mock(Field.class);
 }
 });
 when(nextMock.filterCandidate(mocks, any(Field.class), eq(fieldInstance)))
 .thenReturn(mock(OngoingStubbing.class));

 filter.filterCandidate(mocks, new Field("Mock1", String.class), fieldInstance);
 }
}