package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Before
 public void setUp() {
 mockUtil = spy(MockUtil.class);
 when(mockUtil.getMockName(mock(Object.class))).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "mockName";
 }
 });

 filter = new NameBasedCandidateFilter(null);
 fieldInstance = new Object();
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 // Test basic functionality
 List<Object> result = filter.filterCandidate(mocks, new Field("fieldName", fieldInstance.getClass()), fieldInstance);
 assertEquals(1, result.size());
 assertEquals("mockName", result.get(0).getClass().getDeclaredField("fieldName").getName());

 // Test edge cases and exceptions
 List<Object> conflictingMocks = new ArrayList<>();
 conflictingMocks.add(mock(Object.class));
 conflictingMocks.add(mock(Object.class));
 conflictingMocks.add(mock(Object.class));
 conflictingMocks.set(1, new Object() {}); // Add a non-mock object

 try {
 filter.filterCandidate(conflictingMocks, new Field("fieldName", fieldInstance.getClass()), fieldInstance);
 } catch (IllegalArgumentException e) {
 assertEquals("There should be only one mock matching the field name", e.getMessage());
 }

 List<Object> emptyMocks = new ArrayList<>();
 assertEquals(0, filter.filterCandidate(emptyMocks, new Field("fieldName", fieldInstance.getClass()), fieldInstance).size());
 }
}