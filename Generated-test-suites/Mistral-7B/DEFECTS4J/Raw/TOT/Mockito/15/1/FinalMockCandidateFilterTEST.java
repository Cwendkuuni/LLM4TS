package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mock1;
 private Object mock2;
 private Field field;

 @Before
 public void setup() {
 mocks = Arrays.asList(mock(Object.class), mock(Object.class));
 fieldInstance = mock(Object.class);
 mock1 = spy(mock(Object.class));
 mock2 = spy(mock(Object.class));
 field = mock(Field.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testSingleMockInjection() {
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(Object.class);

 mocks.clear();
 mocks.add(mock1);

 when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(new OngoingInjecter() {
 @Override
 public boolean thenInject() {
 return true;
 }
 });

 filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1).setField(fieldInstance);
 }

 @Test(expected = MockitoException.class)
 public void testExceptionWhenInjectingWithSingleMockAndError() {
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(Object.class);

 mocks.clear();
 mocks.add(mock1);

 when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(new OngoingInjecter() {
 @Override
 public boolean thenInject() {
 throw new RuntimeException("Test Exception");
 }
 });

 try {
 filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 } catch (Exception e) {
 // expected exception
 }

 verify(mock1, never()).setField(fieldInstance);
 }

 @Test
 public void testMultipleMocksNoInjection() {
 when(field.getName()).thenReturn("testField");
 when(field.getType()).thenReturn(Object.class);

 filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 verify(mock1, never()).setField(fieldInstance);
 verify(mock2, never()).setField(fieldInstance);
 }

}