package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.MockCandidateFilter;
import org.mockito.internal.configuration.NameBasedCandidateFilter;
import org.mockito.internal.configuration.FinalMockCandidateFilter;
import org.mockito.internal.configuration.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.assertThrows;

@RunWith(org.mockito.runners.JUnit4.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = mock(TestClass.class);
 }

 @Test
 public void testInjectMocksOnFields() {
 Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
 .collect(Collectors.toSet());

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // Testing that each injected field has exactly one mock
 for (Field field : injectMocksFields) {
 Object fieldInstance = Whitebox.getInternalState(testClassInstance, field);
 Object injectedMock = mocks.iterator().next();
 verify(fieldInstance).equals(injectedMock);
 mocks.remove(injectedMock);
 }
 }

 @Test
 public void testInjectMocksOnFieldWithMultipleCandidates() {
 TestClass testClass = spy(TestClass.class);
 TestClass innerClass = new InnerClass();
 TestClass innerClassWithSameName = new InnerClass();
 mocks.add(Mockito.mock(InnerClass.class));
 mocks.add(Mockito.mock(InnerClass.class));

 try {
 injectionEngine.injectMocksOnFields(Arrays.asList(TestClass.class.getDeclaredField("innerClass")), mocks, testClass);
 assertThrows(MockitoException.class, () -> Whitebox.getInternalState(testClass, "innerClass")).equals(Mockito.mock(InnerClass.class));
 } catch (IllegalAccessException e) {
 // Expected exception
 }
 }

 @Test
 public void testInjectMocksOnFieldWithNoCandidates() {
 TestClass testClass = spy(TestClass.class);
 mocks.clear();

 try {
 injectionEngine.injectMocksOnFields(Arrays.asList(TestClass.class.getDeclaredField("nonExistentField")), mocks, testClass);
 } catch (MockitoException e) {
 // Expected exception
 }
 }
}

class TestClass {
 @InjectMocks
 private InnerClass innerClass;
}

class InnerClass {}