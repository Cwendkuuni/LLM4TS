package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.reflection.FieldType.FIELD;

public class FinalMockCandidateFilterTest {

 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 fieldInstance = new Object();
 field = new Field("testField", String.class, FieldType.FIELD, null, null) {
 @Override
 public Object get(Object instance) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void set(Object instance, Object newValue) {
 // do nothing
 }
 };
 }

 @Test
 public void testSingleMockInjection() {
 Object mock = new Object() {};
 mocks.add(mock);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertNotNull(injecter);
 assertFalse(injecter.thenInject());

 // Check that the field was set to the mock
 FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
 String expected = "testValue";
 fieldSetter.set(mock, expected);
 assertNotNull(field.get(fieldInstance));
 assertEqualTo(expected, field.get(fieldInstance));
 }

 @Test(expected = MockitoException.class)
 public void testMultipleMocksInjectionFailure() {
 Object mock1 = new Object() {};
 Object mock2 = new Object() {};
 mocks.add(mock1);
 mocks.add(mock2);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 injecter.thenInject();
 }

 @Test
 public void testMultipleMocksInjectionSuccess() {
 Object mock1 = new Object() {};
 Object mock2 = new Object() {};
 mocks.add(mock1);
 mocks.add(mock2);
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

 assertNotNull(injecter);
 assertFalse(injecter.thenInject());

 // Check that the first mock was picked
 FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
 String expected = "testValue";
 fieldSetter.set(mock1, expected);
 assertNotNull(field.get(fieldInstance));
 assertEqualTo(expected, field.get(fieldInstance));
 }

 @Test
 public void testExceptionDuringInjection() {
 Field fieldWithException = new Field("testFieldWithException", String[].class, FieldType.FIELD, null, null) {
 @Override
 public Object get(Object instance) {
 throw new RuntimeException("Test exception");
 }
 };
 mocks.add(new Object() {});
 FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
 OngoingInjecter injecter = filter.filterCandidate(mocks, fieldWithException, fieldInstance);

 assertThrows(MockitoException.class, () -> injecter.thenInject());
 }
}