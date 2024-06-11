package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.reflection.ReflectionUtils.getDeclaredFields;
import static org.mockito.internal.util.reflection.ReflectionUtils.isAnnotated;

import static org.junit.Assert.*;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = new Object() {};
 }

 @Test
 public void testInjectMocksOnFields_NoMocksAnnotated() {
 injectionEngine.injectMocksOnFields(Collections.emptySet(), mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test
 public void testInjectMocksOnFields_NoMocksInClass() {
 Class<?> testClass = testClassInstance.getClass();
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{new FieldType(testClass, "field1")}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test
 public void testInjectMocksOnFields_NoMockCandidateFound() {
 Class<?> mockType = Mockito.mock(String.class).getClass();
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{new FieldType(testClassInstance.getClass(), "field1", mockType)}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_MultipleMockCandidates() {
 Class<?> mockType1 = Mockito.mock(String.class).getClass();
 Class<?> mockType2 = Mockito.mock(Integer.class).getClass();
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{new FieldType(testClassInstance.getClass(), "field1", mockType1), new FieldType(testClassInstance.getClass(), "field1", mockType2)}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 fail("Should have thrown MockitoException");
 }

 @Test
 public void testInjectMocksOnFields_InjectBySetter() {
 Class<?> mockType = Mockito.mock(String.class).getClass();
 Field mockField = new FieldType(testClassInstance.getClass(), "field1", mockType);
 mockField.setAccessible(true);
 mockField.set(testClassInstance, null);
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{mockField}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertEquals(mockType, mockField.get(testClassInstance));
 }

 @Test
 public void testInjectMocksOnFields_InjectByField() {
 Class<?> mockType = Mockito.mock(String.class).getClass();
 Field mockField = new FieldType(testClassInstance.getClass(), "field1", mockType);
 mockField.setAccessible(true);
 mockField.set(testClassInstance, null);
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{mockField}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertFalse(mocks.contains(mockType)); // mock should not be present in mocks set
 assertEquals(mockType, mockField.get(testClassInstance));
 }

 @Test
 public void testInjectMockCandidate_MultipleFields() {
 Class<?> awaitingInjectionClazz = new Object() {}.getClass();
 Set<Field> injectMocksFields = Arrays.asList(new Field[]{new FieldType(awaitingInjectionClazz, "field1"), new FieldType(awaitingInjectionClazz, "field2")}).stream().filter(this::isInjectMocksAnnotated).collect(Collectors.toSet());
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test
 public void testInjectMockCandidate_NoMockCandidates() {
 Class<?> awaitingInjectionClazz = new Object() {}.getClass();
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);
 assertFalse(mocks.isEmpty());
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 Class<?> testClass = testClassInstance.getClass();
 Field[] expectedFields = new Field[]{new FieldType(testClass, "field1"), new FieldType(testClass, "field2"), new FieldType(testClass, "field3")};
 Field[] actualFields = injectionEngine.orderedInstanceFieldsFrom(testClass);
 assertArrayEquals(expectedFields, actualFields);
 }
}

 class FieldType {
 private final Field field;
 private final Class<?> fieldType;

 public FieldType(Class<?> clazz, String name, Class<?> type) {
 this.field = new FieldType(clazz, name, type).setAccessible(true);
 this.fieldType = type;
 }

 public FieldType(Class<?> clazz, String name, Class<?> type, int modifiers) {
 this.field = new Field(){
 @Override public AccessibleObject get(Object obj) { return null; }
 @Override public void set(Object obj, Object value) {}};
 this.field.setName(name);
 this.field.setType(type);
 this.field.setAccessible(true);
 this.fieldType = type;
 }

 public boolean isInjectMocksAnnotated() {
 return isAnnotated(field, InjectMocks.class);
 }

 public boolean equals(Object o) {
 if (this == o) return true;
 if (o == null || getClass() != o.getClass()) return false;
 FieldType that = (FieldType) o;
 return fieldType.equals(that.fieldType);
 }

 public int hashCode() {
 return fieldType.hashCode();
 }
 }