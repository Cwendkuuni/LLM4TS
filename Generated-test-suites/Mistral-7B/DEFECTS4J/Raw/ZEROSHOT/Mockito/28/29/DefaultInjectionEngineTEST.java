package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.reflect.Whitebox2;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.spyAnnotations;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.MockCandidate.NONE;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.MockCandidate.ONE;
import static org.mockito.internal.configuration.injection.MockCandidateFilter.MockCandidate.MULTIPLE;
import static org.mockito.internal.util.reflection.Whitebox.getPrivateFieldValue;
import static org.mockito.internal.util.reflection.Whitebox.setPrivateFieldValue;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testClassInstance;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 testClassInstance = new Object() {
 private final SomeClass someField = mock(SomeClass.class);
 private final AnotherClass anotherField = mock(AnotherClass.class);
 private final InnerClass innerField = spy(new InnerClass());
 };
 mocks = Arrays.asList(someField, anotherField).<Object>toSet();
 injectMocksFields = Arrays.asList(TestClassInstance.class.getDeclaredFields())
 .stream()
 .filter(Field::isAnnotationPresent)
 .map(field -> (Field) field.getAnnotation(InjectMocks.class))
 .map(annotatedField -> (Field) annotatedField.value())
 .collect(Collectors.toSet());
 }

 @Test
 public void testInjectMocksOnFields() {
 initMocks(testClassInstance); // Mockito initialization for comparison
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 Set<Object> mocksAfterEngineInvocation = new HashSet<>(mocks);

 for (Field field : injectMocksFields) {
 Object fieldInstance = null;
 try {
 fieldInstance = new FieldInitializer(testClassInstance, field).initialize();
 } catch (MockitoException e) {
 fail("Unexpected exception during mocks initialization: " + e.getMessage());
 }

 assertNotNull(fieldInstance);

 Class<?> fieldClass = fieldInstance.getClass();
 while (!Object.class.equals(fieldClass)) {
 Object mockCandidate = null;
 for (Field candidateField : orderedInstanceFieldsFrom(fieldClass)) {
 mockCandidate = injectionEngine.mockCandidateFilter.filterCandidate(mocksAfterEngineInvocation, candidateField, fieldInstance).getMockCandidate();
 if (mockCandidate != null) {
 break;
 }
 }

 assertNotNull(mockCandidate);
 if (MULTIPLE == mockCandidate) {
 fail("Unexpected multiple mock candidates");
 }
 if (NONE == mockCandidate) {
 fail("No mock candidate found for field " + field.getName());
 }

 setPrivateFieldValue(fieldInstance, field, mockCandidate);
 mocksAfterEngineInvocation.remove(mockCandidate);
 }
 }
 }

 private Set<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
 return Arrays.stream(awaitingInjectionClazz.getDeclaredFields())
 .sorted(Comparator.comparing(field -> getSuperclassRank(awaitingInjectionClazz, field)))
 .collect(Collectors.toSet());
 }

 private int getSuperclassRank(Class<?> awaitingInjectionClazz, Field field) {
 return awaitingInjectionClazz.getSuperclass() == null ? 0 : getSuperclassRank(awaitingInjectionClazz.getSuperclass(), field) + 1;
 }
}

class SomeClass {
}

class AnotherClass {
}

class InnerClass {
}

class TestClassInstance {
 @InjectMocks
 SomeClass someField;
 @InjectMocks
 AnotherClass anotherField;
}