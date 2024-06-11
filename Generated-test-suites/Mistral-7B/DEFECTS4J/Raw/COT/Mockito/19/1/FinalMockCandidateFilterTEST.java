package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitosis.MockitoSession;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Reporter reporter;
 private Object fieldInstance;
 private Field field;
 private List mocks;

 @Before
 public void setup() {
 mocks = Collections.singletonList(mock(Object.class));
 filter = new FinalMockCandidateFilter();
 field = new Field("testField", Object.class) {
 private boolean accessed = false;
 };
 fieldInstance = spy(new Object());
 reporter = mock(Reporter.class);
 }

 @Test
 public void testFilterCandidate_oneMock() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 Object matchingMock = mocks.get(0);

 injecter.thenInject();

 // check if the property was set
 BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
 verify(matchingMock).<Void>set(beanPropertySetter);

 // check if the field was set as a backup
 FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
 verify(matchingMock).<Void>set(fieldSetter);
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_exceptionThrownDuringPropertySetter() {
 when(mocks.get(0).<Void>set(beanPropertySetter)).thenThrow(new RuntimeException());

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();

 // the exception should be reported
 verify(reporter).cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidate_exceptionThrownDuringFieldSetter() {
 when(mocks.get(0).<Void>set(fieldSetter)).thenThrow(new RuntimeException());

 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
 injecter.thenInject();

 // the exception should be reported
 verify(reporter).cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
 }

 @Test
 public void testFilterCandidate_multipleMocks() {
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 injecter.thenInject();

 // no mock should have been set
 verifyNoMoreInteractions(mocks);
 }
}