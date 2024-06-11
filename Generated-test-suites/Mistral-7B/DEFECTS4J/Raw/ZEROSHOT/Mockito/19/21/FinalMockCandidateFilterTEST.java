package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;
 private Object matchingMock;
 private Object otherMock;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 fieldInstance = mock(Object.class);
 field = MockUtil.getDeclaredField(FinalMockCandidateFilter.class, "fieldInstance");
 Whitebox.setInternalState(filter = new FinalMockCandidateFilter(), "mocks", mocks);
 Whitebox.setInternalState(filter, "field", field);
 matchingMock = mock(Object.class);
 otherMock = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetterSuccess() {
 PowerMockito.when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(true);

 Object injectedObject = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 assertEquals(matchingMock, injectedObject);
 verify(matchingMock).setField(fieldInstance);
 }

 @Test
 public void testFilterCandidate_singleMock_propertySetterFailure() {
 PowerMockito.when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenThrow(new RuntimeException("Test Exception"));

 assertNull(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
 verify(new Reporter()).cannotInjectDependency(field, matchingMock, "Test Exception");
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetterSuccess() {
 PowerMockito.when(new FieldSetter(fieldInstance, field).set(matchingMock)).thenReturn(matchingMock);
 PowerMockito.when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(false);

 Object injectedObject = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 assertEquals(matchingMock, injectedObject);
 verify(matchingMock).setField(fieldInstance);
 }

 @Test
 public void testFilterCandidate_singleMock_fieldSetterFailure() {
 PowerMockito.when(new FieldSetter(fieldInstance, field).set(matchingMock)).thenThrow(new RuntimeException("Test Exception"));
 PowerMockito.when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenReturn(false);

 assertNull(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
 verify(new Reporter()).cannotInjectDependency(field, matchingMock, "Test Exception");
 }

 @Test
 public void testFilterCandidate_multipleMocks_returnNull() {
 Object injectedObject = filter.filterCandidate(mocks, field, fieldInstance).thenInject();

 assertNull(injectedObject);
 }
}