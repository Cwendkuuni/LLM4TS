package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.SimpleType;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Reporter reporter;
 private Object fieldInstance;
 private Field field;
 private List<Object> mocks;

 @Before
 public void setUp() {
 filter = new FinalMockCandidateFilter();
 reporter = mock(Reporter.class);
 fieldInstance = mock(SimpleType.class);
 field = new FieldType("testField", String.class).getField(fieldInstance);
 mocks = Arrays.asList(mock(String.class), mock(String.class));
 }

 @Test
 public void shouldReturnOngoingInjecterWithPropertySetterWhenSingleMock() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(new OngoingInjecter() {
 @Override
 public Object thenInject() {
 try {
 new BeanPropertySetter(fieldInstance, field).set(mocks.get(0), "testValue");
 return mocks.get(0);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }
 }, injecter);
 }

 @Test
 public void shouldReturnOngoingInjecterWithFieldSetterWhenPropertySetterFails() {
 try {
 new BeanPropertySetter(fieldInstance, field).set(mocks.get(0), "testValue");
 } catch (IllegalAccessException e) {
 //do nothing
 }
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(new OngoingInjecter() {
 @Override
 public Object thenInject() {
 try {
 new FieldSetter(fieldInstance, field).set(mocks.get(0), "testValue");
 return mocks.get(0);
 } catch (IllegalAccessException | InvocationTargetException e) {
 new Reporter().cannotInjectDependency(field, mocks.get(0), e);
 return null;
 }
 }
 }, injecter);
 }

 @Test
 public void shouldReturnNullInjecterWhenMultipleMocks() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertNull(injecter);
 }
}