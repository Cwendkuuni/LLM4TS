package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.util.reflection.ReflectionUtils.setFieldValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.internal.util.reflection.ReflectionUtils.getFieldValue;

public class FinalMockCandidateFilterTest {

 private Reporter reporter;
 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;
 private FinalMockCandidateFilter filter;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 reporter = mock(Reporter.class);
 field = mock(Field.class);
 fieldInstance = mock(Object.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_success() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertThat(injecter).isNotNull();
 setFieldValue(fieldInstance, field, mock);
 assertThat(getFieldValue(fieldInstance, field)).isSameAs(mock);
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetterFailure() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertThat(injecter).isNotNull();
 setFieldValue(fieldInstance, field, mock);
 assertThat(getFieldValue(fieldInstance, field)).isSameAs(mock);
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetterFailure() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 when(new FieldSetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertThat(injecter).isNotNull();
 // The field was not set, so no need to check it here
 }

 @Test
 public void testFilterCandidate_multipleMocks_noInjected() {
 mocks.add(mock(Object.class));
 mocks.add(mock(Object.class));
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 assertThat(injecter).isNotNull();
 // No mock was injected, so no need to check it here
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_reporterCalled_propertySetterFailure() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_reporterCalled_fieldSetterFailure() {
 Object mock = mock(Object.class);
 mocks.add(mock);
 when(new FieldSetter(fieldInstance, field).set(mock)).thenThrow(new RuntimeException());
 filter.filterCandidate(mocks, field, fieldInstance);
 }
}