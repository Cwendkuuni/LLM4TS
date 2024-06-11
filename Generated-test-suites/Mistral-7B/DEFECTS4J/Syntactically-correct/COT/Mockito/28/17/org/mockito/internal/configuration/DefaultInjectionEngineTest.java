package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Field fieldAnnotatedWithInjectMocks;
 private Field fieldToBeInjected;
 private Field fieldToIgnore;

 @Before
 public void setup() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = mock(TestClass.class);

 fieldAnnotatedWithInjectMocks = TestClass.class.getDeclaredField("fieldAnnotatedWithInjectMocks");
 fieldAnnotatedWithInjectMocks.setAccessible(true);

 fieldToBeInjected = TestSubClass.class.getDeclaredField("fieldToBeInjected");
 fieldToBeInjected.setAccessible(true);

 fieldToIgnore = TestIgnoreClass.class.getDeclaredField("fieldToIgnore");
 fieldToIgnore.setAccessible(true);
 }

 @Test
 public void testInjectMocksOnFields() {
 // Test basic functionality
 mockCandidateFilterTest();

 // Test edge cases and exceptions
 testInjectMocksOnFieldsWithMultipleCandidates();
 testInjectMocksOnFieldsWithNoCandidate();
 testInjectMocksOnFieldsWithException();
 }

 // Basic functionality test
 private void mockCandidateFilterTest() {
 mocks.add(mock(TypeA.class));
 mocks.add(mock(TypeB.class));
 mocks.add(mock(TypeC.class));

 injectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, testClassInstance);

 verify(fieldAnnotatedWithInjectMocks).set(testClassInstance, mocks.iterator().next());
 verifyNoMoreInteractions(mocks.subSet(1, mocks.size()));
 }

 // Edge cases and exceptions tests

 @Test(expected = MockitoException.class)
 private void testInjectMocksOnFieldsWithMultipleCandidates() {
 mocks.add(mock(TypeA.class));
 mocks.add(mock(TypeB.class));
 mocks.add(mock(TypeC.class));
 mocks.add(mock(TypeA.class));

 injectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFieldsWithNoCandidate() {
 mocks.add(mock(TypeA.class));
 mocks.add(mock(TypeB.class));

 injectionEngine.injectMocksOnFields(Arrays.asList(fieldToIgnore), mocks, testClassInstance);

 verifyNoInteractions(mocks);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithException() {
 mocks.add(mock(TypeA.class));
 mocks.add(mock(TypeB.class));

 injectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, null);
 }

 // Helper methods

 private void verifyNoInteractions(Set<Object> mocks) {
 for (Object mock : mocks) {
 verifyNoMoreInteractions(mock);
 }
 }

 private void verify(Field field, Object mock) {
 try {
 field.set(testClassInstance, mock);
 } catch (IllegalAccessException e) {
 verify(field).set(testClassInstance, FieldInitializer.createFieldSetter(mock, field.getName()));
 }
 }
}

class TestClass {
 @InjectMocks
 private Field fieldAnnotatedWithInjectMocks;
}

class TestSubClass extends TestClass {
 @InjectMocks
 private Field fieldToBeInjected;
}

class TestIgnoreClass {
 @InjectMocks
 private Field fieldToIgnore;
}

class TypeA {
}

class TypeB {
}

class TypeC {
}