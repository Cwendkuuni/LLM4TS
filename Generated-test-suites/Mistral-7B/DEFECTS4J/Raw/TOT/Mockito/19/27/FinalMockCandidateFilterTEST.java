package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.junit. MockitoJUnitRunner;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private Object mock1, mock2;
 private Field field;
 private Stubber stubber;

 @Before
 public void setUp() {
 field = mock(Field.class);
 fieldInstance = mock(Object.class);
 filter = new FinalMockCandidateFilter();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 stubber = mock(Stubber.class);
 when(new BeanPropertySetter(fieldInstance, field).canSet(ArgumentMatchers.any())).thenReturn(true);
 when(new BeanPropertySetter(fieldInstance, field).set(ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArguments()[1];
 }
 });
 when(new FieldSetter(fieldInstance, field).canSet(ArgumentMatchers.any())).thenReturn(true);
 when(new FieldSetter(fieldInstance, field).set(ArgumentMatchers.any(), ArgumentMatchers.any())).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArguments()[1];
 }
 });
 when(new Reporter()).thenReturn(stubber);
 }

 @Test
 public void testSingleMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(1)).setField(fieldInstance, field.getType(), fieldInstance);
 }

 @Test(expected = RuntimeException.class)
 public void testRuntimeExceptionDuringPropertySet() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 when(new BeanPropertySetter(fieldInstance, field).canSet(mock2)).thenReturn(false);
 when(new FieldSetter(fieldInstance, field).canSet(mock2)).thenReturn(true);
 when(new FieldSetter(fieldInstance, field).set(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(new RuntimeException());
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(0)).setField(fieldInstance, field.getType(), fieldInstance);
 verify(mock2, times(1)).setField(fieldInstance, field.getType(), fieldInstance);
 stubber.reportError(ArgumentMatchers.anyString(), ArgumentMatchers.any(Throwable.class));
 }

 @Test
 public void testMultipleMocksNoMatchingMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(0)).setField(fieldInstance, field.getType(), fieldInstance);
 verify(mock2, times(0)).setField(fieldInstance, field.getType(), fieldInstance);
 }

 @Test
 public void testMultipleMocksSingleMatchingMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 when(new BeanPropertySetter(fieldInstance, field).canSet(mock1)).thenReturn(true);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(1)).setField(fieldInstance, field.getType(), fieldInstance);
 verify(mock2, times(0)).setField(fieldInstance, field.getType(), fieldInstance);
 }

 @Test
 public void testMultipleMocksMultipleMatchingMocks() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 when(new BeanPropertySetter(fieldInstance, field).canSet(mock1)).thenReturn(true);
 when(new BeanPropertySetter(fieldInstance, field).canSet(mock2)).thenReturn(true);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(1)).setField(fieldInstance, field.getType(), fieldInstance);
 verify(mock2, times(1)).setField(fieldInstance, field.getType(), fieldInstance);
 }

 @Test
 public void testMultipleMocksNoMatchingMockWithNoOngoingInjecter() {
 List<Object> mocks = Collections.singletonList(mock1);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();
 verify(mock1, times(0)).setField(fieldInstance, field.getType(), fieldInstance);
 }
}