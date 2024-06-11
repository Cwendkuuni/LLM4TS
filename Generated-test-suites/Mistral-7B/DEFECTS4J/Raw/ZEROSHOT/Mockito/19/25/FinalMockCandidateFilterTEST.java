package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubs.MockInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Reporter reporter;
 private Field field;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Collection<Object> mocks;

 @Before
 public void setUp() {
 filter = new FinalMockCandidateFilter();
 reporter = mock(Reporter.class);
 field = mock(Field.class);
 fieldInstance = mock(Object.class);
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterSuccess() {
 givenMockSetsFieldSuccessfully();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Object result = injecter.thenInject();

 thenInjectedMockIsReturned(mock1, result);
 verify(mock1).setField(fieldInstance);
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterFailure_fieldSetterSuccess() {
 givenMockFailsToSetProperty();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Object result = injecter.thenInject();

 thenInjectedMockIsReturned(mock1, result);
 verify(mock1).setField(fieldInstance);
 }

 @Test
 public void testFilterCandidate_oneMock_propertySetterFailure_fieldSetterFailure() {
 givenMockFailsToSetProperty();
 givenFieldFailsToSetValue();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Object result = injecter.thenInject();

 thenNoInteractionWithMocks();
 verifyNoInteractions(mock1, mock2);
 }

 @Test
 public void testFilterCandidate_multipleMocks_noMatch() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Object result = injecter.thenInject();

 thenNoInteractionWithMocks();
 verifyNoInteractions(mock1, mock2);
 }

 private void givenMockSetsFieldSuccessfully() {
 when(mock1.setField(fieldInstance)).thenReturn(true);
 }

 private void givenMockFailsToSetProperty() {
 when(mock1.setField(fieldInstance)).thenReturn(false);
 }

 private void givenFieldFailsToSetValue() {
 when(new FieldSetter(fieldInstance, field).set(mock1)).thenThrow(RuntimeException.class);
 }

 private void thenInjectedMockIsReturned(Object mock, Object result) {
 verify(mock).setField(fieldInstance);
 verify(mock).equals(result);
 }

 private void thenNoInteractionWithMocks() {
 verifyNoInteractions(mock1, mock2);
 }
}