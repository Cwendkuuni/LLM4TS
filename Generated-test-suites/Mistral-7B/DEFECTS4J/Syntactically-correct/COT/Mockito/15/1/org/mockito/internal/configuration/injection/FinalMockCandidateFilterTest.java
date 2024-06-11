package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setUp() {
 mocks = Arrays.asList(mock(ClassA.class), mock(ClassB.class));
 field = new Field("testField", String.class);
 fieldInstance = new Object();
 }

 @Test
 public void testFilterCandidate_SingleMock_InjectsSuccessfully() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertTrue(injecter.thenInject());
 }

 @Test
 public void testFilterCandidate_SingleMock_ExceptionThrown() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 doThrow(new Exception("Test Exception")).when(mocks.iterator().next()).set(field);
 FinalMockCandidateFilter filterInstance = spy(filter);
 OngoingInjecter injecter = filterInstance.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 verify(filterInstance).throwException(argThat(e -> e instanceof MockitoException), eq("Problems injecting dependency in testField"));
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_SingleMock_ExceptionThrownDirectly() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 new FieldSetter(fieldInstance, field).set(mocks.iterator().next());
 filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 }

 @Test
 public void testFilterCandidate_MultipleMocks_ReturnsDefaultInjecter() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 }

 @Test
 public void testFilterCandidate_NullMocks() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);
 assertFalse(injecter.thenInject());
 }

 @Test
 public void testFilterCandidate_NullField() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, null, fieldInstance);
 assertFalse(injecter.thenInject());
 }

 @Test
 public void testFilterCandidate_NullFieldInstance() {
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);
 assertFalse(injecter.thenInject());
 }
}