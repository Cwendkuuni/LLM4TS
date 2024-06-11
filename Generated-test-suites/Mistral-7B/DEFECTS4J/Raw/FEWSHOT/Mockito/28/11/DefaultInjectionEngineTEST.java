package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    private static final Class<?> TEST_CLASS = TestClassUnderTest.class;
    private static final Field TEST_FIELD = TestClassUnderTest.class.getDeclaredField("testField");

    @Rule
    public MockitoJUnit runner = MockitoJUnit.rule();

    private DefaultInjectionEngine injectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setUp() {
        injectionEngine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        mocks.add(mock(MockType1.class));
        mocks.add(mock(MockType2.class));
        mocks.add(mock(MockType3.class));
        testClassInstance = spy(TEST_CLASS);
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = Arrays.asList(TestClassUnderTest.class.getDeclaredFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(org.mockito.InjectMocks.class))
                .collect(Collectors.toSet());

        injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Test that all mocks have been injected
        for (Field field : injectMocksFields) {
            Object injectedMock = Whitebox.getInternalState(field, testClassInstance);
            assertThat(injectedMock).isNotNull();
            assertThat(mocks.contains(injectedMock)).isTrue();
        }

        // Test that no duplicates have been injected
        for (Field field : injectMocksFields) {
            Object injectedMock1 = Whitebox.getInternalState(field, testClassInstance);
            Object injectedMock2 = Whitebox.getInternalState(field, testClassInstance);
            assertThat(injectedMock1).isSameAs(injectedMock2);
        }

        // Test that mocks have been removed from the mocks set
        for (Object mock : mocks) {
            assertThat(mocks.contains(mock)).isFalse();
        }
    }

    @Test
    public void testInjectMockCandidate() {
        Class<?> awaitingInjectionClazz = TestTypeWithMockField.class;
        Set<Object> mocksCopy = new HashSet<>(mocks);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        // Test that one mock has been injected
        for (Field field : orderedInstanceFieldsFrom(awaitingInjectionClazz)) {
            Object injectedMock = Whitebox.getInternalState(field, testClassInstance);
            Object mockCandidate = mockCandidateFilter.filterCandidate(mocksCopy, field, fieldInstance).getMock();
            assertThat(injectedMock).isSameAs(mockCandidate);
        }

        // Test that the mock has been removed from the mocks copy
        for (Object mock : mocksCopy) {
            assertThat(mocks.contains(mock)).isFalse();
        }

        // Test that no exceptions have been thrown
        verify(testClassInstance, never()).getCausesRecursionException();
    }

    @Test(expected = MockitoException.class)
    public void testInjectMockCandidateMultipleCandidates() {
        Class<?> awaitingInjectionClazz = TestTypeWithTwoMockCandidates.class;
        Set<Object> mocksCopy = new HashSet<>(mocks);

        injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        // This test should fail with a MockitoException as there are multiple candidates for the same field
    }

    private Comparator<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        return injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
    }
}

class TestClassUnderTest {
    @InjectMocks
    private MockType1 testField;
}

class TestTypeWithMockField {
    @InjectMocks
    private MockType1 testField;
}

class TestTypeWithTwoMockCandidates {
    @InjectMocks
    private MockType1 testField;

    @InjectMocks
    private MockType2 testField;
}

interface MockType1 {
}

interface MockType2 {
}