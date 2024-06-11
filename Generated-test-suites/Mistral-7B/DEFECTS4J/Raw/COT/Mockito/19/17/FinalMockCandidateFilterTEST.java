package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.testutils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.reflection.Reflections.getFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setFieldValue;
import static org.mockito.internal.util.reflection.Reflections.setPrivateField;

public class FinalMockCandidateFilterTest {

 private Reporter reporter;
 private FinalMockCandidateFilter filter;
 private OngoingInjecter injecter;
 private Object fieldInstance;
 private Object mock;
 private Field field;

 @Before
 public void setup() {
 reporter = mock(Reporter.class);
 filter = new FinalMockCandidateFilter();
 injecter = mock(OngoingInjecter.class);
 fieldInstance = mock(Object.class);
 mock = mock(Object.class);
 field = ReflectionUtils.getField(Field.class, Object.class, "field");
 }

 // Test method to check basic functionality of filterCandidate method with one mock
 @Test
 public void testFilterCandidateWithOneMock() {
 Object result = filter.filterCandidate(Arrays.asList(mock), field, fieldInstance);

 assertSame(mock, result);
 }

 // Test method to check basic functionality of filterCandidate method with multiple mocks
 @Test
 public void testFilterCandidateWithMultipleMocks() {
 Object result = filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

 assertNull(result);
 }

 // Test method to check exception handling when a candidate couldn't be found
 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithNoCandidate() {
 filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
 }

 // Test method to check exception handling when a candidate couldn't be injected through a BeanPropertySetter
 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithNoCandidateViaBeanPropertySetter() {
 setPrivateField(filter, "reporter", reporter);
 setFieldValue(filter, "mocks", Collections.emptyList());
 setFieldValue(filter, "field", field);
 setFieldValue(filter, "fieldInstance", fieldInstance);

 try {
 filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
 } catch (RuntimeException e) {
 throw e;
 } finally {
 reset(reporter);
 }
 }

 // Test method to check exception handling when a candidate couldn't be injected through a FieldSetter
 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithNoCandidateViaFieldSetter() {
 setPrivateField(filter, "reporter", reporter);
 setFieldValue(filter, "mocks", Arrays.asList(mock));
 setFieldValue(filter, "field", field);
 setFieldValue(filter, "fieldInstance", fieldInstance);

 try {
 filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);

 setFieldValue(mock, "field", null);
 filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
 } catch (RuntimeException e) {
 throw e;
 } finally {
 reset(mock);
 reset(reporter);
 }
 }

 // Test method to check exception handling when a candidate couldn't be injected through a BeanPropertySetter and a FieldSetter
 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithNoCandidateViaBeanPropertySetterAndFieldSetter() {
 setPrivateField(filter, "reporter", reporter);
 setFieldValue(filter, "mocks", Arrays.asList(mock));
 setFieldValue(filter, "field", field);
 setFieldValue(filter, "fieldInstance", fieldInstance);

 try {
 setFieldValue(mock, "field", null);
 filter.filterCandidate(Collections.singletonList(mock), field, fieldInstance);
 } catch (RuntimeException e) {
 throw e;
 } finally {
 reset(mock);
 reset(reporter);
 }
 }
}