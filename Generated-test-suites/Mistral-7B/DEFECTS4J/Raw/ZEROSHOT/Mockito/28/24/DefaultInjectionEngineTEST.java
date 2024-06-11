package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.runners.MockitoJUnitRunnerExtension;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 for (Field injectMocksField : injectMocksFields) {
 Field injectedField = Whitebox.getInternalState(testClassInstance, injectMocksField.getName());
 assertNotNull(injectedField);
 }
 }

 @Test(expected = MockitoException.class)
 public void testExceptionWhenCannotInitializeInjectMocksAnnotation() {
 Field fieldToFail = mock(Field.class);
 doThrow(new RuntimeException("Mockito Exception")).when(fieldToFail).initialize();

 try {
 injectionEngine.injectMocksOnFields(Arrays.asList(fieldToFail), mocks, testClassInstance);
 } catch (MockitoException e) {
 verify(fieldToFail).initialize();
 throw e;
 }
 }

 @Test
 public void testInjectMocksCandidateByType() {
 Mock mock = mock(MockCandidate.class);
 mocks.add(mock);

 Class<?> awaitingInjectionClazz = TestClassWithMocks.class;
 Field awaitingInjectionField = awaitingInjectionClazz.getDeclaredField("mock1");
 awaitingInjectionField.setAccessible(true);

 when(awaitingInjectionField.get(any()))
 .thenReturn(testClassInstance);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField), mocks, testClassInstance);

 Object injectedMock = WhiteboxImpl.getInternalState(awaitingInjectionField.get(testClassInstance), "mock1");
 assertNotNull(injectedMock);
 assertSame(mock, injectedMock);
 }

 @Test
 public void testInjectMocksCandidateByName() {
 Mock mock1 = mock(MockCandidate1.class);
 Mock mock2 = mock(MockCandidate2.class);
 mocks.add(mock1);
 mocks.add(mock2);

 Class<?> awaitingInjectionClazz = TestClassWithTwoMocks.class;
 Field awaitingInjectionField = awaitingInjectionClazz.getDeclaredField("mock1");
 awaitingInjectionField.setAccessible(true);

 when(awaitingInjectionField.get(any()))
 .thenReturn(testClassInstance);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField), mocks, testClassInstance);

 Object injectedMock1 = WhiteboxImpl.getInternalState(awaitingInjectionField.get(testClassInstance), "mock1");
 assertNotNull(injectedMock1);
 assertSame(mock1, injectedMock1);

 awaitingInjectionField = awaitingInjectionClazz.getDeclaredField("mock2");
 awaitingInjectionField.setAccessible(true);

 WhiteboxImpl.setInternalState(awaitingInjectionField.get(testClassInstance), "mock2", mock2);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField), mocks, testClassInstance);

 Object injectedMock2 = WhiteboxImpl.getInternalState(awaitingInjectionField.get(testClassInstance), "mock2");
 assertNotNull(injectedMock2);
 assertSame(mock2, injectedMock2);
 }

 @Test
 public void testInjectMocksCandidateByTypeAndByName() {
 Mock mock1 = mock(MockCandidate1.class);
 Mock mock2 = mock(MockCandidate2.class);
 Mock mock3 = mock(MockCandidate3.class);
 mocks.add(mock1);
 mocks.add(mock2);
 mocks.add(mock3);

 Class<?> awaitingInjectionClazz = TestClassWithTwoMocks.class;
 Field awaitingInjectionField1 = awaitingInjectionClazz.getDeclaredField("mock1");
 awaitingInjectionField1.setAccessible(true);

 when(awaitingInjectionField1.get(any()))
 .thenReturn(testClassInstance);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField1), mocks, testClassInstance);

 Object injectedMock1 = WhiteboxImpl.getInternalState(awaitingInjectionField1.get(testClassInstance), "mock1");
 assertNotNull(injectedMock1);
 assertSame(mock1, injectedMock1);

 awaitingInjectionField1 = awaitingInjectionClazz.getDeclaredField("mock2");
 awaitingInjectionField1.setAccessible(true);

 WhiteboxImpl.setInternalState(awaitingInjectionField1.get(testClassInstance), "mock2", mock3);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField1), mocks, testClassInstance);

 Object injectedMock2 = WhiteboxImpl.getInternalState(awaitingInjectionField1.get(testClassInstance), "mock2");
 assertNotNull(injectedMock2);
 assertSame(mock3, injectedMock2);
 }

 @Test(expected = MockitoException.class)
 public void testExceptionWhenMultipleCandidatesHaveTheSameTypeAndName() {
 Mock mock1 = mock(MockCandidate1.class);
 Mock mock2 = mock(MockCandidate1.class);
 mocks.add(mock1);
 mocks.add(mock2);

 Class<?> awaitingInjectionClazz = TestClassWithMultipleMocks.class;
 Field awaitingInjectionField1 = awaitingInjectionClazz.getDeclaredField("mock1");
 awaitingInjectionField1.setAccessible(true);

 when(awaitingInjectionField1.get(any()))
 .thenReturn(testClassInstance);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField1), mocks, testClassInstance);

 Object injectedMock1 = WhiteboxImpl.getInternalState(awaitingInjectionField1.get(testClassInstance), "mock1");
 assertNotNull(injectedMock1);
 assertSame(mock1, injectedMock1);

 awaitingInjectionField1 = awaitingInjectionClazz.getDeclaredField("mock1");
 awaitingInjectionField1.setAccessible(true);

 WhiteboxImpl.setInternalState(awaitingInjectionField1.get(testClassInstance), "mock1", mock2);

 injectionEngine.injectMocksOnFields(Arrays.asList(awaitingInjectionField1), mocks, testClassInstance);
 }
}

class TestClass {
 @InjectMocks
 private Field injectMocksField;
}

class TestClassWithMocks {
 @InjectMocks
 private MockCandidate mock1;
}

class TestClassWithTwoMocks {
 @InjectMocks
 private MockCandidate1 mock1;
 @InjectMocks
 private MockCandidate2 mock2;
}

class TestClassWithMultipleMocks {
 @InjectMocks
 private MockCandidate1 mock1;
 @InjectMocks
 private MockCandidate1 mock2;
}

interface MockCandidate {}

interface MockCandidate1 {}

interface MockCandidate2 {}