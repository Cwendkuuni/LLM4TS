package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injectionStrategy;
 private Object testObject;
 private Field injectMocksField;
 private List<Field> orderedFields;
 private Set<Object> mockCandidates;

 @Before
 public void setUp() {
 injectionStrategy = new PropertyAndSetterInjection();
 testObject = new TestClass();
 injectMocksField = TestClass.class.getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 orderedFields = orderedInstanceFieldsFrom(TestClass.class);
 mockCandidates = newMockSafeHashSet(mock(MockCandidate.class));
 }

 @Test
 public void testProcessInjection_InjectMocksAnnotatedFieldIsInjected() throws IllegalAccessException, InstantiationException, InvocationTargetException {
 Object mock = mock(MockCandidate.class);
 injectMocksField.set(testObject, mock);
 FieldInitializationReport report = new FieldInitializer(testObject, injectMocksField).initialize();
 float result = testObject.someMethod();
 verify(mock).someMethod();

 assertTrue(injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates));
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_InjectMocksAnnotatedFieldThrowsExceptionDuringInitialization() throws IllegalAccessException, InstantiationException, InvocationTargetException {
 FieldInitializationReport report = new FieldInitializer(testObject, injectMocksField).initialize();
 throw new RuntimeException();
 }

 @Test
 public void testProcessInjection_NoMockCandidatesFoundForInjectMocksAnnotatedField() {
 assertFalse(injectionStrategy.processInjection(injectMocksField, testObject, mockCandidates));
 }

 @Test
 public void testInjectMockCandidates_NoMockCandidatesFoundForAnyField() {
 Class<?> awaitingInjectionClazz = TestClass.class;
 assertFalse(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 }

 @Test
 public void testInjectMockCandidates_MockCandidatesFoundByType() {
 Object mockByType = mock(TypeBasedMockCandidate.class);
 mockCandidates.add(mockByType);
 Class<?> awaitingInjectionClazz = TestClassWithTypeBasedMockCandidate.class;
 assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 verify(mockByType).someMethod();
 }

 @Test
 public void testInjectMockCandidates_MockCandidatesFoundByName() {
 Object mockByName = mock(NameBasedMockCandidate.class);
 mockCandidates.add(mockByName);
 Class<?> awaitingInjectionClazz = TestClassWithNameBasedMockCandidate.class;
 assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 verify(mockByName).someMethod();
 }

 @Test
 public void testInjectMockCandidates_MockCandidatesFoundByNameButNotByType() {
 Object mockByName = mock(NameBasedMockCandidate.class);
 mockCandidates.add(mockByName);
 Class<?> awaitingInjectionClazz = TestClassWithNameBasedMockCandidate.class;
 assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 verify(mockByName).someMethod();
 mockCandidates.clear();
 mockCandidates.add(mock(TypeBasedMockCandidate.class));
 assertFalse(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 }

 @Test
 public void testInjectMockCandidates_NoMockCandidatesFoundButFieldsAreNotFailed() {
 Class<?> awaitingInjectionClazz = TestClassWithNoMockCandidates.class;
 assertTrue(injectionStrategy.injectMockCandidates(awaitingInjectionClazz, mockCandidates, testObject));
 verifyZeroInteractions(mockCandidates.toArray(new Object[0]));
 }

 @Test
 public void testInjectMockCandidatesOnFields_MockCandidatesFoundForField() {
 Object mock = mock(MockCandidate.class);
 mockCandidates.add(mock);
 Class<?> awaitingInjectionClazz = TestClassWithMockableField.class;
 assertTrue(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedFields));
 verify(mock).someMethod();
 }

 @Test
 public void testInjectMockCandidatesOnFields_NoMockCandidatesFoundForField() {
 Class<?> awaitingInjectionClazz = TestClassWithNoMockCandidates.class;
 assertTrue(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedFields));
 verifyNoInteractions(mockCandidates.toArray(new Object[0]));
 }

 @Test
 public void testInjectMockCandidatesOnFields_MockCandidatesFoundForAnotherFieldButNotForCurrentField() {
 Object mock = mock(MockCandidate.class);
 mockCandidates.add(mock);
 Class<?> awaitingInjectionClazz = TestClassWithMockableField.class;
 assertTrue(injectionStrategy.injectMockCandidatesOnFields(mockCandidates, testObject, false, orderedFields));
 verifyNoInteractions(mock);
 }

 @Test
 public void testOrderedInstanceFieldsFrom_FieldsAreOrderedCorrectly() {
 List<Field> orderedFields = orderedInstanceFieldsFrom(TestClassWithOrderedFields.class);
 assertEquals(3, orderedFields.size());
 assertEquals("field3", orderedFields.get(0).getName());
 assertEquals("field2", orderedFields.get(1).getName());
 assertEquals("field1", orderedFields.get(2).getName());
 }
}

class TestClass {
 @InjectMocks
 private Object injectMocksField;

 public float someMethod() {
 return 0.0f;
 }
}

class TestClassWithTypeBasedMockCandidate {
 @InjectMocks
 private TypeBasedMockCandidate typeBasedMockCandidate;

 public void someMethod() {
 // empty
 }

 static class TypeBasedMockCandidate {
 @Mock
 public void someMethod() {
 // empty
 }
 }
}

class TestClassWithNameBasedMockCandidate {
 @InjectMocks
 private NameBasedMockCandidate nameBasedMockCandidate;

 public void someMethod() {
 // empty
 }

 static class NameBasedMockCandidate {
 @Mock
 public void someMethod() {
 // empty
 }
 }
}

class TestClassWithNoMockCandidates {
 @InjectMocks
 private Object injectMocksField;

 public void someMethod() {
 // empty
 }
}

class TestClassWithMockableField {
 @InjectMocks
 private Object injectMocksField;

 @Mock
 private Object mockableField;

 public void someMethod() {
 injectMocksField.setMockableField(mockableField);
 }

 public void setMockableField(Object mockableField) {
 this.mockableField = mockableField;
 }
}

class TestClassWithOrderedFields {
 @InjectMocks
 private Object injectMocksField;

 @Mock
 private Object field3;
 @Mock
 private Object field2;
 @Mock
 private Object field1;

 public void someMethod() {
 // empty
 }
}