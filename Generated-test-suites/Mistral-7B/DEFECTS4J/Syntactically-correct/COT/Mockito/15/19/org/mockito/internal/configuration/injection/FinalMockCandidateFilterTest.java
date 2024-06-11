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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.ReflectionUtils.setField;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mock;
 private Field field;

 @Before
 public void setup() {
 mocks = Arrays.asList(new MockObject(), new MockObject());
 filter = new FinalMockCandidateFilter();
 fieldInstance = new Object();
 field = new Field("testField", Object.class, fieldInstance.getClass());
 mock = new MockObject();
 }

 @Test
 public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 assertFalse(result.thenInject()); // since SingleMockCandidateFilter returns an OngoingInjecter that does nothing if a candidate is found

 // Check that the field was set
 setField(fieldInstance, field, mock);
 assertNotNull(field.get(fieldInstance));
 assertSame(mock, field.get(fieldInstance));
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_SingleMock_FieldSetterException() {
 when(field.isAccessible()).thenReturn(true); // make the field accessible for testing purpose
 when(field.canSetAccessible(any())).thenReturn(true); // make the field modifiable for testing purpose
 doThrow(new RuntimeException()).when(new FieldSetter(fieldInstance, field)).set(any(), any());

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 assertFalse(result.thenInject());

 verify(field).setAccessible(isInstanceOf(Field.class));
 verify(field).canSetAccessible(isInstanceOf(Field.class));
 verifyNoMoreInteractions(field);

 assertThrows(MockitoException.class, () -> field.set(fieldInstance, mock));
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoMatchingMock() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 assertInstanceOf(OngoingInjecter.class, result);
 assertFalse(result.thenInject());
 }

 @Test
 public void testFilterCandidate_MultipleMocks_FieldSetterSuccess() {
 when(mocks.iterator().next().getClass().getField("testField")).thenReturn(field);
 when(mocks.iterator().next().getClass().getField("testField").get(mocks.iterator().next())).thenReturn(mock);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 assertFalse(result.thenInject());

 // Check that the field was set
 setField(fieldInstance, field, mock);
 assertNotNull(field.get(fieldInstance));
 assertSame(mock, field.get(fieldInstance));
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_MultipleMocks_FieldSetterException() {
 when(mocks.iterator().next().getClass().getField("testField")).thenReturn(field);
 when(mocks.iterator().next().getClass().getField("testField").get(mocks.iterator().next())).thenReturn(mock);
 when(field.isAccessible()).thenReturn(true);
 when(field.canSetAccessible(any())).thenReturn(true);
 doThrow(new RuntimeException()).when(new FieldSetter(fieldInstance, field)).set(any(), any());

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 assertFalse(result.thenInject());

 verify(field).setAccessible(isInstanceOf(Field.class));
 verify(field).canSetAccessible(isInstanceOf(Field.class));
 verifyNoMoreInteractions(field);

 assertThrows(MockitoException.class, () -> field.set(fieldInstance, mock));
 }

}