package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.BeanPropertySetter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.FieldSetter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldInfo;
import org.mockito.mockitis.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 fieldInstance = Mockito.mock(Object.class);
 field = Mockito.mock(Field.class);

 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetterSuccess() {
 Object mock = Mockito.mock(Object.class);
 mocks.add(mock);

 BeanPropertySetter propertySetter = Mockito.spy(new BeanPropertySetter(fieldInstance, field));
 FieldSetter fieldSetter = Mockito.mock(FieldSetter.class);
 Reporter reporter = Mockito.mock(Reporter.class);

 when(propertySetter.set(any(Object.class), any(Object.class))).thenReturn(true);
 when(fieldSetter.set(any(Object.class), any(Object.class))).thenCallRealMethod();

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock, injecter.thenInject());

 verify(propertySetter).set(mock, fieldInstance);
 verify(fieldSetter, never()).set(any(Object.class), any(Object.class));
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetterFailure() {
 Object mock = Mockito.mock(Object.class);
 mocks.add(mock);

 BeanPropertySetter propertySetter = Mockito.spy(new BeanPropertySetter(fieldInstance, field));
 FieldSetter fieldSetter = Mockito.mock(FieldSetter.class);
 Reporter reporter = Mockito.mock(Reporter.class);

 when(propertySetter.set(any(Object.class), any(Object.class))).thenThrow(new RuntimeException("Error setting property"));
 when(fieldSetter.set(any(Object.class), any(Object.class))).thenReturn(true);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(injecter.thenInject());

 verify(reporter).cannotInjectDependency(any(Field.class), eq(mock), any(RuntimeException.class));
 }

 @Test
 public void testFilterCandidate_singleMock_fieldAccessSuccess() {
 Object mock = Mockito.mock(Object.class);
 mocks.add(mock);

 BeanPropertySetter propertySetter = Mockito.spy(new BeanPropertySetter(fieldInstance, field));
 FieldSetter fieldSetter = Mockito.spy(new FieldSetter(fieldInstance, field));
 Reporter reporter = Mockito.mock(Reporter.class);

 when(propertySetter.set(any(Object.class), any(Object.class))).thenReturn(false);
 when(fieldSetter.set(any(Object.class), any(Object.class))).thenReturn(true);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock, injecter.thenInject());

 verify(propertySetter, never()).set(any(Object.class), any(Object.class));
 verify(fieldSetter).set(mock, fieldInstance);
 }

 @Test
 public void testFilterCandidate_multipleMocks_returnNull() {
 Object mock1 = Mockito.mock(Object.class);
 Object mock2 = Mockito.mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(injecter.thenInject());
 }
}