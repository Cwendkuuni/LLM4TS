package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.invocation.ProxyMethodInvocation;
import org.mockito.internal.util.naming.MockName;
import org.mockito.internal.util.naming.SimpleName;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.reflect.Whitebox.setInternalState;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private List<Object> mocks;
 private Field field;
 private Object fieldInstance;
 private MethodInterceptor interceptor;

 @Before
 public void setUp() {
 mockUtil = mock(MockUtil.class);
 filter = new NameBasedCandidateFilter(null);
 mocks = new ArrayList<>();
 field = Whitebox.getInternalState(this, "field");
 fieldInstance = mock(Object.class);
 interceptor = mock(MethodInterceptor.class);
 }

 @Test
 public void testFilterCandidate_singleMock() {
 // Given
 Whitebox.setInternalState(filter, "mockUtil", mockUtil);
 mocks.clear();
 mocks.add(mock(Object.class, interceptor));

 // When
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameName() {
 // Given
 Whitebox.setInternalState(filter, "mockUtil", mockUtil);
 mocks.clear();
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));

 // When
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mocks.get(1), result);
 }

 @Test
 public void testFilterCandidate_multipleMocksWithDifferentNames() {
 // Given
 Whitebox.setInternalState(filter, "mockUtil", mockUtil);
 mocks.clear();
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock2")));

 // When
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameNameAndConflictingFields() {
 // Given
 Whitebox.setInternalState(filter, "mockUtil", mockUtil);
 mocks.clear();
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));

 // When we set a private field on the first mock
 setFieldValue(mocks.get(0), "field", new Object());

 // When
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mocks.get(1), result);

 // verify no interactions with the first mock
 verifyNoMoreInteractions(mocks.get(0));
 }

 @Test
 public void testFilterCandidate_multipleMocksWithDifferentNamesAndConflictingFields() {
 // Given
 Whitebox.setInternalState(filter, "mockUtil", mockUtil);
 mocks.clear();
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock1")));
 mocks.add(spy(Object.class, interceptor, new SimpleName("mock2")));

 // When we set a private field on the first mock
 setFieldValue(mocks.get(0), "field", new Object());

 // When
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mocks.get(1), result);

 // verify no interactions with the first mock
 verifyNoMoreInteractions(mocks.get(0));
 }

 private void setFieldValue(Object object, String fieldName, Object value) {
 try {
 Field field = object.getClass().getDeclaredField(fieldName);
 field.setAccessible(true);
 FieldSetter.setFieldValue(field, object, value);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
}
}