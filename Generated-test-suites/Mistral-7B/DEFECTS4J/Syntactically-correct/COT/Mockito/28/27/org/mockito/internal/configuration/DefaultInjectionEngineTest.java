package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.exceptions.BaseMockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;

 @Before
 public void setUp() {
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test(expected = AssertionError.class)
 public void testInjectMocksOnNullInjectMocksFields() {
 Set<Field> injectMocksFields = null;
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new Object();
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test(expected = AssertionError.class)
 public void testInjectMocksOnEmptyInjectMocksFields() {
 Set<Field> injectMocksFields = new HashSet<>();
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new Object();
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithSingleMockCandidate() throws Exception {
 Class<MockTestClass> testClass = MockTestClass.class;
 Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new MockTestClass();

 // Given
 Field fieldToBeInjected = injectMocksFields.iterator().next();
 Object mock = Mockito.mock(fieldToBeInjected.getType());
 mocks.add(mock);

 // When
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Then
 Assert.assertNotNull(testClassInstance.getInjectedField());
 Assert.assertEquals(mock, testClassInstance.getInjectedField());
 }

 @Test(expected = BaseMockitoException.class)
 public void testInjectMocksOnFieldsWithDuplicateMockCandidates() throws Exception {
 Class<MockTestClass> testClass = MockTestClass.class;
 Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new MockTestClass();

 // Given
 Field fieldToBeInjected = injectMocksFields.iterator().next();
 Object mock1 = Mockito.mock(fieldToBeInjected.getType());
 Object mock2 = Mockito.mock(fieldToBeInjected.getType());
 mocks.add(mock1);
 mocks.add(mock2);

 // When & Then
 Assert.assertThrows(BaseMockitoException.class, () -> defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance));
 }

 @Test
 public void testInjectMocksOnFieldsWithMockCandidatesByName() throws Exception {
 Class<MockTestClass> testClass = MockTestClass.class;
 Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new MockTestClass();

 // Given
 Field field1ToBeInjected = injectMocksFields.stream()
 .filter(field -> field.getName().equals("field1"))
 .findFirst()
 .orElseThrow(() -> new AssertionError("Field 'field1' not found"))
 .get();
 Object mock1 = Mockito.mock(field1ToBeInjected.getType());
 mocks.add(mock1);

 Field field2ToBeInjected = injectMocksFields.stream()
 .filter(field -> field.getName().equals("field2"))
 .findFirst()
 .orElseThrow(() -> new AssertionError("Field 'field2' not found"))
 .get();
 Object mock2 = Mockito.mock(field2ToBeInjected.getType());
 mocks.add(mock2);

 // When
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Then
 Assert.assertNotNull(testClassInstance.getInjectedField1());
 Assert.assertEquals(mock1, testClassInstance.getInjectedField1());
 Assert.assertNotNull(testClassInstance.getInjectedField2());
 Assert.assertEquals(mock2, testClassInstance.getInjectedField2());
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithNoMockCandidates() throws Exception {
 Class<MockTestClass> testClass = MockTestClass.class;
 Set<Field> injectMocksFields = Arrays.asList(testClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = new MockTestClass();

 // When & Then
 Assert.assertThrows(MockitoException.class, () -> defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance));
 }

 private static class MockTestClass {
 @InjectMocks
 private static Field injectedField;
 private static Field field1;
 private static Field field2;

 static {
 field1 = new Field("field1", String.class, MockTestClass.class, null, null);
 field2 = new Field("field2", Integer.class, MockTestClass.class, null, null);
 }

 public static Object getInjectedField() throws IllegalAccessException {
 return field1.get(null);
 }
 }

}