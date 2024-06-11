package org.mockito.internal.configuration.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;

 @Before
 public void setup() {
 mocks = new HashSet<>();
 testClassInstance = new Object() {
 private final String name = "testClass";
 private FinalMock mock1;
 private FinalMock mock2;
 };
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnSimpleClass() {
 // given
 Field injectMocksField = TestClass.class.getDeclaredField("mock1");
 injectMocksField.setAccessible(true);
 injectMocksField.annotatedWith(InjectMocks.class);

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);

 // then
 assertNotNull(testClassInstance.mock1);
 assertEquals(mock1.getClass(), testClassInstance.mock1.getClass());
 mocks.remove(testClassInstance.mock1);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldWithMultipleCandidates() {
 // given
 Field injectMocksField1 = TestClass.class.getDeclaredField("mock1");
 injectMocksField1.setAccessible(true);
 injectMocksField1.annotatedWith(InjectMocks.class);

 Field injectMocksField2 = TestClass.class.getDeclaredField("mock2");
 injectMocksField2.setAccessible(true);
 injectMocksField2.annotatedWith(InjectMocks.class);

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField1, injectMocksField2), mocks, testClassInstance);

 // then - exception should be thrown
 }

 @Test
 public void testInjectMocksOnFieldWithNoCandidate() {
 // given
 Field injectMocksField = TestClass.class.getDeclaredField("nonExistentMock");
 injectMocksField.setAccessible(true);
 injectMocksField.annotatedWith(InjectMocks.class);
 mocks.add(mock(String.class)); // a dummy mock to prevent real mock injection

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);

 // then
 assertNotNull(testClassInstance); // no exception should be thrown
 }

 @Test
 public void testInjectMocksOnFieldWithSuperClassCandidate() {
 // given
 TestSuperClass superClass = new TestSuperClass();
 Field injectMocksField = TestClass.class.getDeclaredField("mock1");
 injectMocksField.setAccessible(true);
 injectMocksField.annotatedWith(InjectMocks.class);

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClassInstance);

 // then
 assertNotNull(superClass.mock1);
 assertEquals(mock1.getClass(), superClass.mock1.getClass());
 mocks.remove(superClass.mock1);
 }

 @Test
 public void testInjectMocksOnFieldWithSuperClassCandidateAndMultipleSubClassCandidates() {
 // given
 TestSubClass subClass = new TestSubClass();
 TestClass testClass = new TestClass();
 Field injectMocksField = TestClass.class.getDeclaredField("mock1");
 injectMocksField.setAccessible(true);
 injectMocksField.annotatedWith(InjectMocks.class);
 mocks.add(mock(String.class)); // a dummy mock to prevent real mock injection

 // when
 injectionEngine.injectMocksOnFields(Arrays.asList(injectMocksField), mocks, testClass);

 // then
 assertNotNull(subClass.mock1);
 assertEquals(mock1.getClass(), subClass.mock1.getClass());
 mocks.remove(subClass.mock1);
 }
}

class TestClass {
 private FinalMock mock1;
}

class TestSuperClass extends TestClass {
 private FinalMock mock1;
}

class TestSubClass extends TestSuperClass {
 // empty
}

class FinalMock {
}