import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
import org.mockito.internal.util.collections.ListUtil;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.collections.Sets.newMockSafeHashSet;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injection;
 private Object testObject;
 private Set<Object> mockCandidates;

 @Before
 public void setup() {
 injection = new PropertyAndSetterInjection();
 testObject = new Object() {};
 mockCandidates = newMockSafeHashSet(Arrays.asList(new MockitoTestMock(), new MockitoTestMock2(), new MockitoTestMock3()));
 }

 @Test
 public void testProcessInjection_whenFieldIsAnnotatedWithInjectMocks_thenMockIsInjected() {
 Field injectMocksField = new FieldAnnotationTestClass().getClass().getDeclaredField("injectMocksField");
 injectMocksField.setAccessible(true);
 injectMocksField.set(testObject, new Object());

 FieldInitializationReport report = new FieldInitializer(testObject, injectMocksField).initialize();
 assertTrue(injection.processInjection(injectMocksField, testObject, mockCandidates));
 assertNotNull(injectMocksField.get(testObject));
 }

 @Test
 public void testProcessInjection_whenFieldIsNotAnnotatedWithInjectMocks_thenNothingHappens() {
 Field notAnnotatedField = new ClassWithNoInjectMocksAnnotation().getClass().getDeclaredField("notAnnotatedField");
 notAnnotatedField.setAccessible(true);

 boolean injectionOccurred = injection.processInjection(notAnnotatedField, testObject, mockCandidates);
 assertFalse(injectionOccurred);
 assertNull(notAnnotatedField.get(testObject));
 }

 @Test(expected = InvocationTargetException.class)
 public void testProcessInjection_whenInitializationThrowsException_thenExceptionIsThrown() {
 Field initializingFieldWithException = new ClassWithInitializationException().getClass()
 .getDeclaredField("initializingFieldWithException");
 initializingFieldWithException.setAccessible(true);

 FieldInitializationReport report = new FieldInitializer(testObject, initializingFieldWithException).initialize();
 assertNull(report);
 }

 @Test
 public void testProcessInjection_whenNoMockCandidatesFound_thenNothingHappens() {
 mockCandidates.clear();
 boolean injectionOccurred = injection.processInjection(testObject.getClass().getDeclaredField("testField"), testObject, mockCandidates);
 assertFalse(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidates_whenMockIsInjectedByType_thenMockIsInjected() {
 testObject = new TypeBasedTestClass();
 testObject.setInjectedField1(new MockitoTestMock());
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testObject.getClass());
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 assertNotNull(testObject.getInjectedField1());
 }

 @Test
 public void testInjectMockCandidates_whenMockIsInjectedByName_thenMockIsInjected() {
 testObject = new NameBasedTestClass();
 testObject.setInjectedField1(new MockitoTestMock());
 testObject.setInjectedField2(new MockitoTestMock2());
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testObject.getClass());
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 assertNotNull(testObject.getInjectedField2());
 }

 @Test
 public void testInjectMockCandidates_whenNoMockCandidatesFound_thenNothingHappens() {
 testObject = new NoMockCandidatesTestClass();
 mockCandidates.clear();
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 }

 @Test
 public void testInjectMockCandidates_whenTwoPassesNeeded_thenMockIsInjected() {
 testObject = new TwoPassesTestClass();
 testObject.setInjectedField1(new MockitoTestMock());
 testObject.setInjectedField2(new MockitoTestMock2());
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testObject.getClass());
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 assertNotNull(testObject.getInjectedField1());
 assertNotNull(testObject.getInjectedField2());
 }

 @Test(expected = IllegalAccessException.class)
 public void testInjectMockCandidates_whenAccessToFieldIsDenied_thenExceptionIsThrown() {
 testObject = new AccessDeniedTestClass();
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testObject.getClass());
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 assertNull(testObject.getInjectedField());
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidates_whenMoreThanOneMockCandidateFoundByName_thenExceptionIsThrown() {
 testObject = new MultipleMockCandidatesTestClass();
 testObject.setInjectedField(new MockitoTestMock());
 List<Field> orderedInstanceFields = injection.orderedInstanceFieldsFrom(testObject.getClass());
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 assertNotNull(testObject.getInjectedField());

 // Second call to injectMockCandidates should throw an exception
 assertTrue(injection.injectMockCandidates(testObject.getClass(), mockCandidates, testObject));
 }
}

class MockitoTestMock {
}

class MockitoTestMock2 {
}

class MockitoTestMock3 {
}

class FieldAnnotationTestClass {
 @InjectMocks
 private Object injectMocksField;
}

class ClassWithNoInjectMocksAnnotation {
 // No @InjectMocks annotation
 private Object notAnnotatedField;
}

class ClassWithInitializationException {
 @InjectMocks
 private Object initializingFieldWithException;

 public Object getInitializingFieldWithException() {
 throw new RuntimeException("Initialization exception");
 }
}

class NoMockCandidatesTestClass {
 @InjectMocks
 private Object testField;
}

class TwoPassesTestClass {
 @InjectMocks
 private Object injectedField1;
 @InjectMocks
 private Object injectedField2;
}

class AccessDeniedTestClass {
 @InjectMocks
 private Object injectedField;

 private Object deniedField;

 public Object getDeniedField() {
 throw new IllegalAccessException();
 }
}

class MultipleMockCandidatesTestClass {
 @InjectMocks
 private Object injectedField;

 @SuppressWarnings("unused")
 private Object anotherMockCandidate;
}