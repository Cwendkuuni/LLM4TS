package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = new Object() {{
 Field field1 = this.getClass().getDeclaredField("field1");
 Field field2 = this.getClass().getDeclaredField("field2");
 field1.setAccessible(true);
 field2.setAccessible(true);
 }};
 }

 @Test
 public void testInjectMocksOnFieldsWithNoMocks() {
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertNotNull(testClassInstance.field1);
 assertNotNull(testClassInstance.field2);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithMultipleMockCandidates() {
 Class<?> mockClass = Mockito.class;
 mocks.add(mock(mockClass));
 mocks.add(mock(mockClass));
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithMockCandidateByType() {
 Class<?> mockClass = Mockito.class;
 mocks.add(mockClass);
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertNotNull(Whitebox.getInternalState(testClassInstance, "field1"));
 }

 @Test
 public void testInjectMocksOnFieldsWithMockCandidateByName() {
 Class<?> anotherMockClass = Spy.class;
 mocks.add(mock(anotherMockClass));
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 assertNotNull(Whitebox.getInternalState(testClassInstance, "field2"));
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithNoMockCandidates() {
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithNullTestClassInstance() {
 injectionEngine.injectMocksOnFields(null, mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithNullInjectMocksFields() {
 Set<Field> injectMocksFields = null;
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithEmptyMocks() {
 Set<Object> emptyMocks = new HashSet<>();
 injectionEngine.injectMocksOnFields(Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new)), emptyMocks, testClassInstance);
 }
}