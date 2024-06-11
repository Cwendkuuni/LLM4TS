import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.reflection.util.ClassUtil;
import org.mockito.internal.util.reflection.util.FieldUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Object target;

 @Before
 public void setUp() {
 injection = new PropertyAndSetterInjection();
 target = mock(TargetClass.class);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithNullInjectMocksField() {
 injection.processInjection(null, target, Collections.emptySet());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithNullInjectMocksFieldOwner() {
 injection.processInjection(new Field("fieldName", String.class, Modifier.PUBLIC, TargetClass.class), null, Collections.emptySet());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithEmptyMockCandidates() {
 injection.processInjection(new Field("fieldName", String.class, Modifier.PUBLIC, TargetClass.class), target, Collections.emptySet());
 }

 @Test
 public void testProcessInjectionWithValidMockCandidates() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(MockClass1.class));
 mocks.add(mock(MockClass2.class));

 FieldInitializationReport report = new FieldInitializer(target, new Field("injectMocksField", TargetClass.class, Modifier.PRIVATE, TargetClass.class)).initialize();
 assertNotNull(report);
 assertFalse(injection.processInjection(new Field("injectMocksField", TargetClass.class, Modifier.PRIVATE, TargetClass.class), target, mocks));

 // Check that the mock was injected
 MockClass1 mock1 = Mockito.spy(mocks.iterator().next());
 Field field1 = FieldUtil.getDeclaredField(TargetClass.class, "field1");
 assertNotNull(field1);
 assertEquals(mock1, FieldUtil.getFieldValue(target, field1));

 mocks.remove(mock1);

 // Check that the second mock was not injected
 Field field2 = FieldUtil.getDeclaredField(TargetClass.class, "field2");
 assertNull(FieldUtil.getFieldValue(target, field2));
 }

 @Test(expected = InvocationTargetException.class)
 public void testInitializeInjectMocksFieldWithExceptionDuringInitialization() {
 Field field = new Field("fieldName", String.class, Modifier.PUBLIC, TargetClass.class);
 Object instance = new Object() {};
 try {
 injection.initializeInjectMocksField(field, instance);
 } catch (MockitoException e) {
 throw e.getCause();
 }
 }

 @Test(expected = MockitoException.class)
 public void testInitializeInjectMocksFieldWithExceptionDuringMockCandidateFiltering() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(spy(new ExceptionThrowingClass()));
 Field field = new Field("fieldName", String.class, Modifier.PUBLIC, TargetClass.class);
 try {
 injection.initializeInjectMocksField(field, target);
 } catch (MockitoException e) {
 assertEquals("Could not inject", e.getMessage());
 }
 }

 @Test(expected = IllegalAccessException.class)
 public void testInitializeInjectMocksFieldWithPrivateField() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(PrivateClass.class));
 Field field = new Field("privateField", String.class, Modifier.PRIVATE, TargetClass.class);
 try {
 injection.initializeInjectMocksField(field, target);
 } catch (IllegalAccessException e) {
 // expected
 }
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithNoMockCandidates() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(MockClass1.class));
 mocks.add(mock(MockClass2.class));
 Class<?> awaitingInjectionClazz = TargetClass.class;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithOneMockCandidate() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(MockClass1.class));
 Class<?> awaitingInjectionClazz = TargetClass.class;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));

 mocks.remove(mock(MockClass1.class));

 Field fieldToBeInjected = orderedInstanceFields.get(0);
 Object mockCandidate = mock(fieldToBeInjected.getType());
 mocks.add(mockCandidate);

 assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));

 // Check that the mock was injected
 assertEquals(mockCandidate, FieldUtil.getFieldValue(target, fieldToBeInjected));
 }

 @Test
 public void testInjectMockCandidatesOnFieldsWithMultipleMockCandidates() {
 Set<Object> mocks = new HashSet<>();
 mocks.add(mock(MockClass1.class));
 mocks.add(mock(MockClass2.class));
 Class<?> awaitingInjectionClazz = TargetClass.class;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));

 mocks.remove(mock(MockClass1.class));

 Field fieldToBeInjected = orderedInstanceFields.get(0);
 Object mockCandidate1 = mock(fieldToBeInjected.getType());
 mocks.add(mockCandidate1);

 assertFalse(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));

 mockCandidate1 = mock(MockClass3.class); // different type
 mocks.add(mockCandidate1);

 assertTrue(injection.injectMockCandidates(awaitingInjectionClazz, mocks, target));

 // Check that the mock was injected
 assertEquals(mockCandidate1, FieldUtil.getFieldValue(target, fieldToBeInjected));
 }

 private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
 return injection.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 }
}

class TargetClass {
 @InjectMocks
 private TargetClass target;
 private String field1;
 private String field2;
}

class MockClass1 {}

class MockClass2 {}

class MockClass3 {}

class PrivateClass {
 private String privateField;
}

class ExceptionThrowingClass {
 @Override
 public String toString() {
 throw new RuntimeException("Could not inject");
 }
}