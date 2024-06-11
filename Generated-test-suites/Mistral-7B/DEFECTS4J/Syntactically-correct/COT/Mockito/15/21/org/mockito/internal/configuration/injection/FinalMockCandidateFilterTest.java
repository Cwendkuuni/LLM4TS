package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 fieldInstance = mock(Object.class);
 field = MockitoStubs.spy(Field.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 Object mock = mock(Object.class);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 assertFalse(injecter.thenInject());

 try {
 new FieldSetter(fieldInstance, field).set(mock);
 } catch (Exception e) {
 throw new MockitoException("Unexpected exception", e);
 }

 // The test below verifies that the field was set in the mock
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 verify(mock).__set__(field, invocation.getArgument(0));
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(injecter);
 assertFalse(injecter.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidateWithExceptions() throws Exception {
 mocks.add(new Object() {
 @Override
 public void set(Object o, Object field, Object value) throws Exception {
 throw new Exception("Test exception");
 }
 });
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertFalse(injecter.thenInject());
 // The test below verifies that the exception is thrown
 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidateWithNullMocks() {
 Collection<Object> nullMocks = null;
 assertNull(filter.filterCandidate(nullMocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidateWithNullFieldInstance() {
 Object nullFieldInstance = null;
 assertNull(filter.filterCandidate(mocks, field, nullFieldInstance));
 }

 @Test
 public void testFilterCandidateWithNullField() {
 Field nullField = null;
 assertNull(filter.filterCandidate(mocks, nullField, fieldInstance));
 }
}