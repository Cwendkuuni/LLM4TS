package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitoSession;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private List<Object> mocks;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Field field;
 private Reporter reporter;

 @Before
 public void setup() {
 filter = new FinalMockCandidateFilter();
 fieldInstance = mock(Object.class);
 mocks = Arrays.asList(mock1 = mock(Object.class), mock2 = mock(Object.class), mock3 = mock(Object.class));
 field = spy(new Field("testField", int.class, fieldInstance.getClass()));
 reporter = mock(Reporter.class);
 when(field.getType()).thenReturn(int.class.getType());
 }

 @Test
 public void testFilterCandidateWithOneMock() {
 filter.filterCandidate(mocks, field, fieldInstance);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1).getClass(); // verify that the first mock is returned
 verify(fieldInstance).getClass(); // verify that the field instance is not modified
 verify(field).set(mock1, anyInt()); // verify that the property is set on the mock
 verify(reporter, never()).cannotInjectDependency(eq(field), eq(mock1), any(RuntimeException.class));
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 filter.filterCandidate(mocks, field, fieldInstance);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 // verify that no injection happens
 verify(mock1, never()).getClass();
 verify(mock2, never()).getClass();
 verify(mock3, never()).getClass();
 verify(fieldInstance, never()).getClass();
 verify(field, never()).set(mock1, anyInt());
 verify(field, never()).set(mock2, anyInt());
 verify(field, never()).set(mock3, anyInt());
 verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithFieldAccessFailure() {
 when(field.isAccessible()).thenReturn(false);

 filter.filterCandidate(mocks, field, fieldInstance);

 final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1, never()).getClass();
 verify(mock2, never()).getClass();
 verify(mock3, never()).getClass();
 verify(fieldInstance, never()).getClass();
 verify(field, never()).set(mock1, anyInt());
 verify(field, never()).set(mock2, anyInt());
 verify(field, never()).set(mock3, anyInt());
 verify(reporter).cannotInjectDependency(eq(field), eq(fieldInstance), any(RuntimeException.class));
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithPropertySetterFailure() {
 when(field.isAccessible()).thenReturn(true);
 when(new BeanPropertySetter(fieldInstance, field).canSet(mock1)).thenReturn(false);

 filter.filterCandidate(mocks, field, fieldInstance);

 final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1, never()).getClass();
 verify(mock2, never()).getClass();
 verify(mock3, never()).getClass();
 verify(fieldInstance, never()).getClass();
 verify(field).set(mock1, anyInt());
 verify(reporter).cannotInjectDependency(eq(field), eq(mock1), any(RuntimeException.class));
 }

 @Test
 public void testFilterCandidateWithNullCollection() {
 filter.filterCandidate(null, field, fieldInstance);

 final OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1, never()).getClass();
 verify(mock2, never()).getClass();
 verify(mock3, never()).getClass();
 verify(fieldInstance, never()).getClass();
 verify(field, never()).set(mock1, anyInt());
 verify(field, never()).set(mock2, anyInt());
 verify(field, never()).set(mock3, anyInt());
 verify(reporter, never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
 }
}