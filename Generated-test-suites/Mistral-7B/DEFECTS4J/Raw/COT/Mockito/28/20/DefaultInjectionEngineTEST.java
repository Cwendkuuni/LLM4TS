package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 mocks = new HashSet<>();
 testClassInstance = spy(new Object() {});
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Verify that each injected mock is present in the mocks set
 for (Field field : injectMocksFields) {
 Object mock = mocks.iterator().next();
 Assert.assertNotNull("Injected mock is null for field " + field.getName(), mock);
 Assert.assertEquals("Injected mock is not the expected type for field " + field.getName(), field.getType(), mock.getClass());

 // Verify that all mocks are injected only once
 Assert.assertEquals("Multiple mocks injected for field " + field.getName(), 1, mocks.size());
 }
 }

 @Test(expected = MockitoException.class)
 public void testCannotInitializeInjectMocksField() {
 when(testClassInstance.getClass().getField("fieldToFail")).thenThrow(new RuntimeException("Cannot initialize field"));
 Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMockCandidateByType() {
 // Arrange
 Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
 Object mock = mock(TypeBasedCandidateFilter.class);
 when(mocks.iterator().next()).thenReturn(mock);

 // Act
 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // Assert
 verifyZeroInteractions(mock);
 }

 @Test
 public void testInjectMockCandidateByName() {
 // Arrange
 Class<?> awaitingInjectionClazz = MockitoJUnitRunner.class;
 Object mock1 = mock(TypeBasedCandidateFilter.class);
 Object mock2 = mock(TypeBasedCandidateFilter.class);
 when(mocks.iterator().next()).thenReturn(mock1);
 when(mocks.iterator().next()).thenReturn(mock2);

 // Act
 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // Assert
 verifyZeroInteractions(mock2);
 }
}