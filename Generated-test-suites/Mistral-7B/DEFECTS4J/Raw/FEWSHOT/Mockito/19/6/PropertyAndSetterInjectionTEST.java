package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

/**
 * Test class for {@link PropertyAndSetterInjection}
 */
public class PropertyAndSetterInjectionTest {

    @Test
    public void testProcessInjectionWithMockCandidates() {
        Class<?> clazzToInjectMocksInto = MockitoInjectMockTestClass.class;
        Object instance = mock(clazzToInjectMocksInto);
        Set<Object> mockCandidates = newHashSet(mock(DependentClass1.class), mock(DependentClass2.class), mock(DependentClass3.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field fieldAnnotatedWithInjectMocks = getFieldAnnotatedWithInjectMocks(clazzToInjectMocksInto);

        FieldInitializationReport report = propertyAndSetterInjection.initializeInjectMocksField(fieldAnnotatedWithInjectMocks, instance);

        assertTrue(propertyAndSetterInjection.processInjection(fieldAnnotatedWithInjectMocks, instance, mockCandidates));

        for (Object mock : mockCandidates) {
            verify(mock).isMock();
        }

        // Check that each field in the class hierarchy was tried to be injected with mocks
        Class<?> fieldClass = report.fieldClass();
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        while (fieldClass != Object.class) {
            List<Field> orderedInstanceFields = propertyAndSetterInjection.orderedInstanceFieldsFrom(fieldClass);
            for (Field field : orderedInstanceFields) {
                Object injectedMock = mockCandidateFilter.filterCandidate(mockCandidates, field, fieldInstanceNeedingInjection).thenInject();
                if (injectedMock != null) {
                    verify(injectedMock).isMock();
                }
            }
            fieldClass = fieldClass.getSuperclass();
        }
    }

    @Test
    public void testProcessInjectionWithoutMockCandidates() {
        Class<?> clazzToInjectMocksInto = MockitoInjectMockTestClass.class;
        Object instance = mock(clazzToInjectMocksInto);
        Set<Object> emptyMockCandidates = newHashSet();

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field fieldAnnotatedWithInjectMocks = getFieldAnnotatedWithInjectMocks(clazzToInjectMocksInto);

        propertyAndSetterInjection.processInjection(fieldAnnotatedWithInjectMocks, instance, emptyMockCandidates);

        // No verification needed as no mocks were injected
    }

    @Test(expected = MockitoException.class)
    public void testProcessInjectionWithNonFinalNonStaticFieldAnnotatedWithInjectMocksButNotInitialized() {
        Class<?> clazzToInjectMocksInto = MockitoInjectMockTestClass.class;
        Object instance = mock(clazzToInjectMocksInto);
        Set<Object> mockCandidates = newHashSet(mock(DependentClass1.class), mock(DependentClass2.class), mock(DependentClass3.class));

        PropertyAndSetterInjection propertyAndSetterInjection = new PropertyAndSetterInjection();
        Field fieldAnnotatedWithInjectMocks = getFieldAnnotatedWithInjectMocks(clazzToInjectMocksInto);
        doAnswer(invocation -> {
            throw new RuntimeException("Field not initialized");
        }).when(instance).getClass();

        propertyAndSetterInjection.processInjection(fieldAnnotatedWithInjectMocks, instance, mockCandidates);
    }

    private Field getFieldAnnotatedWithInjectMocks(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .findFirst()
                .orElseThrow(() -> new MockitoException("No field annotated with @InjectMocks found"));
    }

    private static Set<Object> newHashSet(Object... objects) {
        return new HashSet<>(Arrays.asList(objects));
    }

    static class MockitoInjectMockTestClass {
        @InjectMocks
        private DependentClass1 dependentClass1;
        @InjectMocks
        private DependentClass2 dependentClass2;
        @InjectMocks
        private DependentClass3 dependentClass3;
    }

    static class DependentClass1 {
        private DependentClass2 dependentClass2;
    }

    static class DependentClass2 {

    }

    static class DependentClass3 {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface InjectMocks {
    }
}