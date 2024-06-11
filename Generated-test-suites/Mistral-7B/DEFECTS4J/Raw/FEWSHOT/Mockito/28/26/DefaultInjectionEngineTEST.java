package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitostub.MockitoStubs;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = MockitoStubs.spy(new TestClass());
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
 .stream()
 .filter(Field::isAnnotationPresent)
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 assertNotNull(testClassInstance.getInjectedField1());
 assertNotNull(testClassInstance.getInjectedField2());
 }

 @Test(expected = MockitoException.class)
 public void testFailedToInitializeFieldAnnotatedWithInjectMocks() {
 Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
 .stream()
 .filter(Field::isAnnotationPresent)
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toCollection(HashSet::new));

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 testClassInstance.getClass().getSuperclass().getDeclaredField("superField").setAccessible(true);
 try {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 } catch (IllegalAccessException e) {
 throw new MockitoException("Failed to initialize field annotated with @InjectMocks", e);
 }
 }

 @Test
 public void testMockCandidateFiltering() {
 final Class<MockType> mockType = MockType.class;
 final Class<RealType> realType = RealType.class;

 mocks.add(mock(mockType));
 mocks.add(mock(RealType.class));
 mocks.add(mock(String.class));

 Field mockField = TestClass.class.getDeclaredField("mockField");
 mockField.setAccessible(true);

 Field realField = TestClass.class.getDeclaredField("realField");
 realField.setAccessible(true);

 mockCandidateFilteringTests(mockField, mockType, realType);
 mockCandidateFilteringTests(realField, realType, mockType);
 }

 private void mockCandidateFilteringTests(Field field, Class<?> awaitingInjectionClazz, Class<?> mockType) {
 Object fieldInstance = null;
 try {
 fieldInstance = field.get(testClassInstance);
 } catch (IllegalAccessException e) {
 throw new IllegalStateException("Unexpected exception", e);
 }

 DefaultInjectionEngine.InjectedMockResult result = injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, fieldInstance);

 if (result.isMockInjected()) {
 assertNotNull(field.get(testClassInstance));
 } else {
 assertNull(field.get(testClassInstance));
 }
 }
}

class TestClass {
 @InjectMocks
 private InjectedField1 injectedField1;
 private InjectedField2 injectedField2;

 @InjectMocks
 private static class InnerClass {
 @InjectMocks
 private InjectedField3 injectedField3;
 }

 private InjectedField1 mockField;
 private InjectedField2 realField;
 private Object superField;

 // getters and setters
}

class InjectedField1 {
}

class InjectedField2 {
}

class InjectedField3 {
}

class MockType {
}

class RealType {
}