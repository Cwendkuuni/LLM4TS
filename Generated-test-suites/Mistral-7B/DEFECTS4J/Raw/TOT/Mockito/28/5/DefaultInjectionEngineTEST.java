package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.DefaultMockitoAnnotations;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Field field1;
 private Field field2;
 private Object testInstance;
 private Set<Object> mocks;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();

 testInstance = new Object() {};
 field1 = TestUtils.getField(testInstance, "field1");
 field2 = TestUtils.getField(testInstance, "field2");
 field1.setAccessible(true);
 field2.setAccessible(true);

 mocks = new HashSet<>();
 mocks.add(mock(TypeA.class));
 mocks.add(mock(TypeB.class));
 }

 @Test
 public void testInjectMocksOnFields_singleLevel_singleMockCandidateByType() {
 // Given
 when(field1.getType()).thenReturn(TypeA.class);

 // When
 injectionEngine.injectMocksOnFields(Collections.singleton(field1), mocks, testInstance);

 // Then
 verify(field1).set(testInstance, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verifyZeroInteractions(mocks.iterator().next());
 }

 @Test
 public void testInjectMocksOnFields_singleLevel_singleMockCandidateByName() {
 // Given
 when(field1.getName()).thenReturn("nameA");
 when(field1.getType()).thenReturn(TypeA.class);
 when(mockCandidateFilter.filterCandidate(mocks, field1, testInstance)).thenReturn(mocks.iterator().next());

 // When
 injectionEngine.injectMocksOnFields(Collections.singleton(field1), mocks, testInstance);

 // Then
 verify(field1).set(testInstance, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verifyZeroInteractions(mocks.iterator().next());
 }

 @Test
 public void testInjectMocksOnFields_singleLevel_noMockCandidate() {
 // Given
 when(field1.getName()).thenReturn("nameA");
 when(field1.getType()).thenReturn(TypeC.class);

 // When
 injectionEngine.injectMocksOnFields(Collections.singleton(field1), mocks, testInstance);

 // Then
 // No interaction is expected since no mock candidate is found
 }

 @Test
 public void testInjectMocksOnFields_multipleLevels_singleMockCandidateByType() {
 // Given
 Class<?> classWithField1 = new ClassWithField1();
 Class<?> classWithField2 = new ClassWithField2();
 when(field1.getType()).thenReturn(TypeA.class);
 when(field2.getType()).thenReturn(ClassWithField2.class);
 when(mockCandidateFilter.filterCandidate(mocks, field2, classWithField2)).thenReturn(mocks.iterator().next());

 // When
 injectionEngine.injectMocksOnFields(Arrays.asList(field1, field2), mocks, testInstance);

 // Then
 verify(field1).set(testInstance, mocks.iterator().next());
 verify(field2).set(classWithField2, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.iterator().next());
 verifyZeroInteractions(mocks.iterator().next());
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_multipleLevels_multipleMockCandidates() {
 // Given
 Class<?> classWithField1 = new ClassWithField1();
 Class<?> classWithField2 = new ClassWithField2();
 when(field1.getType()).thenReturn(TypeA.class);
 when(field2.getType()).thenReturn(ClassWithField2.class);
 when(mockCandidateFilter.filterCandidate(mocks, field2, classWithField2))
 .thenReturn(mocks.iterator().next()); // first candidate
 when(mockCandidateFilter.filterCandidate(mocks, field2, classWithField2))
 .thenReturn(mocks.iterator().next()); // second candidate

 // When
 // Throws MockitoException since multiple mock candidates are found for the same field
 injectionEngine.injectMocksOnFields(Arrays.asList(field1, field2), mocks, testInstance);
 }

 @Test
 public void testInjectMocksOnFields_multipleLevels_noMockCandidate() {
 // Given
 Class<?> classWithField1 = new ClassWithField1();
 Class<?> classWithField2 = new ClassWithField2();
 when(field1.getType()).thenReturn(TypeA.class);
 when(field2.getType()).thenReturn(ClassWithField3.class);

 // When
 injectionEngine.injectMocksOnFields(Arrays.asList(field1, field2), mocks, testInstance);

 // Then
 // No interaction is expected since no mock candidate is found for field2
 }
}

class ClassWithField1 {
 private Field field1;

 public Field getField1() {
 return field1;
 }

 static class InnerClass {
 Field field2;
 }

 Field getField2() {
 return new InnerClass().field2;
 }
}

class ClassWithField2 {
 private Field field2;

 public Field getField2() {
 return field2;
 }
}

class ClassWithField3 {
 private Field field3;

 public Field getField3() {
 return field3;
 }
}

class TestUtils {
 public static <T> Field getField(Object instance, String name) {
 try {
 return (Field) Whitebox.getInternalState(instance, name);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
}