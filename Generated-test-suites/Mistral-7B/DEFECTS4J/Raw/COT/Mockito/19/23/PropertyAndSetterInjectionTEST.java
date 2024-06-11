import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.junit.MockitoJUnitRuntime;
import org.mockito.stub.MockSettings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Object testObject;
 private Set<Object> mockCandidates;
 private Field injectMocksField;

 @Before
 public void setup() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 testObject = new TestClass();
 injectMocksField = TestClass.class.getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 mockCandidates = Sets.newMockSafeHashSet();
 }

 @Test
 public void testProcessInjection_NoInjectionOccurs() {
 // Given
 Object mockCandidate1 = mock(Type1.class);
 mockCandidates.add(mockCandidate1);
 Object mockCandidate2 = mock(Type2.class);
 mockCandidates.add(mockCandidate2);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 assertFalse(injectionOccurred);
 assertEquals(2, mockCandidates.size());
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_InitializationThrowsException() {
 // Given
 FieldInitializationReport report = mock(FieldInitializationReport.class);
 when(report.fieldInstance()).thenThrow(new IllegalArgumentException());
 when(new FieldInitializer(testObject, injectMocksField).initialize()).thenReturn(report);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 fail("Method should have thrown exception");
 }

 @Test
 public void testProcessInjection_InjectionOccurs() {
 // Given
 Object mockCandidate = mock(Type.class);
 mockCandidates.add(mockCandidate);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 assertEquals(0, mockCandidates.size());
 verify(testObject, times(1)).setInjectMocksField(mockCandidate);
 }

 @Test
 public void testProcessInjection_MultipleCandidates() {
 // Given
 Object mockCandidate1 = mock(Type1.class);
 Object mockCandidate2 = mock(Type2.class);
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 assertEquals(1, mockCandidates.size());
 verify(testObject, times(1)).setInjectMocksField(mockCandidate1);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_MultipleCandidatesWithSameName() {
 // Given
 Object mockCandidate1 = mock(Type1.class);
 Object mockCandidate2 = mock(Type2.class);
 mockCandidate1.getClass().setName("Type1Alias");
 mockCandidates.add(mockCandidate1);
 mockCandidates.add(mockCandidate2);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, testObject, mockCandidates);

 // Then
 fail("Method should have thrown exception");
 }

 @Test
 public void testProcessInjection_NoCandidates() {
 // Given
 Set<Object> emptyMockCandidates = new HashSet<>();

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, testObject, emptyMockCandidates);

 // Then
 assertFalse(testObject.getClass().isInstance(injectMocksField.get(testObject)));
 }

 @Test
 public void testInitializeInjectMocksField() throws IllegalAccessException, InstantiationException {
 // Given
 Object mockCandidate = mock(Type.class);
 when(new FieldInitializer(testObject, injectMocksField).initialize()).thenReturn(new FieldInitializationReport("Type.class", mockCandidate));

 // When
 FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);

 // Then
 assertEquals(report.fieldInstance(), mockCandidate);
 }

 @Test
 public void testInitializeInjectMocksField_ThrowsException() {
 // Given
 when(new FieldInitializer(testObject, injectMocksField).initialize()).thenThrow(new InstantiationException());

 // When
 try {
 propertyAndSetterInjection.initializeInjectMocksField(injectMocksField, testObject);
 fail("Method should have thrown exception");
 } catch (Exception e) {
 // Then
 assertEquals(InstantiationException.class, e.getClass());
 }
 }

 @Test
 public void testInjectMockCandidates() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Object instance = testObject;
 Set<Object> mocks = mockCandidates;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertFalse(injectionOccurred);
 assertEquals(2, mocks.size());
 }

 @Test
 public void testInjectMockCandidates_InjectionOccurs() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Object instance = testObject;
 Set<Object> mocks = mockCandidates;
 Object mockCandidate = mock(Type.class);
 mocks.add(mockCandidate);
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertTrue(injectionOccurred);
 assertEquals(1, mocks.size());
 verify(instance, times(1)).setFieldName(mockCandidate);
 }

 @Test
 public void testInjectMockCandidates_MultipleCandidatesForSameField() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Object instance = testObject;
 Set<Object> mocks = mockCandidates;
 Object mockCandidate1 = mock(Type1.class);
 Object mockCandidate2 = mock(Type1.class);
 mocks.add(mockCandidate1);
 mocks.add(mockCandidate2);
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertTrue(injectionOccurred);
 assertEquals(1, mocks.size());
 verify(instance, times(1)).setFieldName(mockCandidate1);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidates_MultipleCandidatesWithSameType() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Object instance = testObject;
 Set<Object> mocks = mockCandidates;
 Object mockCandidate1 = mock(Type.class);
 Object mockCandidate2 = mock(Type.class);
 mocks.add(mockCandidate1);
 mocks.add(mockCandidate2);
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 fail("Method should have thrown exception");
 }

 @Test
 public void testInjectMockCandidates_NoCandidates() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;
 Object instance = testObject;
 Set<Object> mocks = mockCandidates;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, instance);

 // Then
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields() {
 // Given
 Set<Object> mocks = mockCandidates;
 Object instance = testObject;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(TestClass.class);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertFalse(injectionOccurred);
 assertEquals(2, mocks.size());
 }

 @Test
 public void testInjectMockCandidatesOnFields_InjectionOccurs() {
 // Given
 Set<Object> mocks = mockCandidates;
 Object instance = testObject;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(TestClass.class);
 Object mockCandidate = mock(Type.class);
 mocks.add(mockCandidate);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertTrue(injectionOccurred);
 assertEquals(1, mocks.size());
 verify(instance, times(1)).setFieldName(mockCandidate);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidatesOnFields_MultipleCandidatesForSameField() {
 // Given
 Set<Object> mocks = mockCandidates;
 Object instance = testObject;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(TestClass.class);
 Object mockCandidate1 = mock(Type.class);
 Object mockCandidate2 = mock(Type.class);
 mocks.add(mockCandidate1);
 mocks.add(mockCandidate2);

 // When
 propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 fail("Method should have thrown exception");
 }

 @Test
 public void testInjectMockCandidatesOnFields_MultipleCandidatesWithSameType() {
 // Given
 Set<Object> mocks = mockCandidates;
 Object instance = testObject;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(TestClass.class);
 Object mockCandidate1 = mock(Type.class);
 Object mockCandidate2 = mock(Type.class);
 mocks.add(mockCandidate1);
 mocks.add(mockCandidate2);

 // When
 propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertEquals(2, mocks.size());
 }

 @Test
 public void testInjectMockCandidatesOnFields_NoCandidates() {
 // Given
 Set<Object> mocks = mockCandidates;
 Object instance = testObject;
 List<Field> orderedInstanceFields = orderedInstanceFieldsFrom(TestClass.class);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, instance, false, orderedInstanceFields);

 // Then
 assertFalse(injectionOccurred);
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 // Given
 Class<?> awaitingInjectionClazz = TestClass.class;

 // When
 List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // Then
 assertEquals(3, orderedInstanceFields.size());
 Field field1 = orderedInstanceFields.get(0);
 assertTrue(field1.getName().equals("field1"));
 Field field2 = orderedInstanceFields.get(1);
 assertTrue(field2.getName().equals("field2"));
 Field field3 = orderedInstanceFields.get(2);
 assertTrue(field3.getName().equals("field3"));
 }
}