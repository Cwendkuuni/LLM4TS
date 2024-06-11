package org.mockito.internal.configuration.DefaultInjectionEngine; // adjust the package name if necessary

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() throws Exception {
 // given
 Class mockClass = MockitoTestClass.class;
 Set<Field> injectMocksFields = Arrays.asList(mockClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = Whitebox.getInternalState(this, "defaultInjectionEngine").injectMocksOnFields(injectMocksFields, mocks, this);

 // when

 // check if all expected mocks were injected
 for (Field field : injectMocksFields) {
 Object expectedMock = mocks.stream()
 .filter(mock -> mock.getClass().getName().equals(field.getType().getName()))
 .findFirst()
 .orElseThrow(() -> new AssertionError("Expected mock of type " + field.getType().getName() + " was not injected"));
 Assert.assertSame(expectedMock, Whitebox.getInternalState(testClassInstance, field.getName()));
 }
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_Exception() throws Exception {
 // given
 Class mockClass = MockitoTestClassWithException.class;
 Set<Field> injectMocksFields = Arrays.asList(mockClass.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());
 Set<Object> mocks = new HashSet<>();
 Object testClassInstance = Whitebox.getInternalState(this, "defaultInjectionEngine").injectMocksOnFields(injectMocksFields, mocks, this);

 // when and then
 }

 @Test
 public void testInjectMockCandidate() throws Exception {
 // given
 Class mockClass = MockitoTestClassWithDependency.class;
 Set<Object> mocks = new HashSet<>();
 Object fieldInstance = new Object();
 Object testClassInstance = Whitebox.getInternalState(this, "defaultInjectionEngine").injectMocksOnFields(new HashSet<>(), mocks, this);

 // when
 Field dependencyField = mockClass.getDeclaredField("dependencyField");

 // then
 Whitebox.setInternalState(testClassInstance, "dependencyField", mocks.stream()
 .filter(mock -> mock.getClass().getName().equals(dependencyField.getType().getName()))
 .findFirst()
 .orElseThrow(() -> new AssertionError("Expected dependency of type " + dependencyField.getType().getName() + " was not injected")));

 Assert.assertSame(mocks.stream()
 .filter(mock -> mock.getClass().getName().equals(dependencyField.getType().getName()))
 .findFirst()
 .orElseThrow(() -> new AssertionError("Expected dependency of type " + dependencyField.getType().getName() + " was not injected")),
 Whitebox.getInternalState(testClassInstance, "dependencyField"));
 }

 @Test
 public void testInjectMockCandidate_MultipleCandidates() throws Exception {
 // given
 Class mockClass = MockitoTestClassWithMultipleCandidates.class;
 Set<Object> mocks = new HashSet<>();
 Object fieldInstance = new Object();
 Object testClassInstance = Whitebox.getInternalState(this, "defaultInjectionEngine").injectMocksOnFields(new HashSet<>(), mocks, this);

 // when
 Field dependencyField1 = mockClass.getDeclaredField("dependencyField1");
 Field dependencyField2 = mockClass.getDeclaredField("dependencyField2");

 // then
 Assert.assertEquals(dependencyField1.getType().getName(),
 Whitebox.getInternalState(testClassInstance, "dependencyField1").getClass().getName());
 Assert.assertEquals(dependencyField2.getType().getName(),
 Whitebox.getInternalState(testClassInstance, "dependencyField2").getClass().getName());
 }
}

class MockitoTestClass {
 // add any required annotated fields here
}

class MockitoTestClassWithException extends MockitoTestClass {
 // add any required annotated fields here
}

class MockitoTestClassWithDependency {
 public class Dependency {}
 @InjectMocks
 private Dependency dependencyField;
}

class MockitoTestClassWithMultipleCandidates {
 public class Candidate1 {}
 public class Candidate2 {}
 @InjectMocks
 private Candidate1 dependencyField1;
 @InjectMocks
 private Candidate2 dependencyField2;
}