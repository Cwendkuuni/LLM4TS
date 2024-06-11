package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.mockitoSession;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {
    private static final Class<DefaultInjectionEngine> TESTED_CLASS = DefaultInjectionEngine.class;
    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() {
        mocks = new HashSet<>();
        testClassInstance = mock(TestClass.class);
        initMocks(this);
        injectionEngine = new DefaultInjectionEngine();
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field field : injectMocksFields) {
            verify(testClassInstance).getClass().getDeclaredField(field.getName());
            Object fieldInstance = field.get(testClassInstance);
            if (field.getType() == String.class) {
                verify(mocks).contains(fieldInstance);
            } else {
                verify(mocks).contains(spy(field.getType()));
            }
        }
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = TestClassWithNestedMocks.class;
        Set<Object> mocksCopy = new HashSet<>(mocks);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object candidate = mock(field.getType());
            verify(mocksCopy).remove(candidate);
            verify(testClassInstance).getClass().getDeclaredField(field.getName()).set(testClassInstance, candidate);
        }
    }

    @Test
    public void testNoMockCandidateFound() {
        Class<?> awaitingInjectionClazz = TestClassWithNoMockCandidate.class;

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

        // no verification is performed as no mocks were injected
    }

    private static Class<?>[] orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return awaitingInjectionClazz.getDeclaredFields();
    }

    static class TestClass {
        @InjectMocks
        private String stringField;

        @InjectMocks
        private Foo fooField;
    }

    static class TestClassWithNestedMocks {
        @InjectMocks
        private Bar bar;

        class Bar {
            @InjectMocks
            private Baz baz;
        }

        class Baz {}
    }

    static class TestClassWithNoMockCandidate {
        @InjectMocks
        private Object objectField;
    }
}