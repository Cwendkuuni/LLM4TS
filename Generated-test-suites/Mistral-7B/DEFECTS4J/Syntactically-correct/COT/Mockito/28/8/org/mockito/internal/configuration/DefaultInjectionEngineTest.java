package org.mockito.internal.configuration.DefaultInjectionEngine; // update the package name in your project

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.junit.Runners;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine engine;
    private Set<Object> mocks;
    private Object testClassInstance;
    private Class<?> testClassWithInjectMocks;

    @Before
    public void setup() {
        engine = new DefaultInjectionEngine();
        mocks = new HashSet<>();
        testClassInstance = new Object() {};
        testClassWithInjectMocks = new Class<TestClassWithInjectMocks>() {};
    }

    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = getInjectMocksFields(testClassWithInjectMocks);
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Test if injected mocks are set in the testClassInstance
        for (Field field : injectMocksFields) {
            Object injectedMock = mocks.iterator().next();
            Assert.assertNotNull(field.get(testClassInstance));
            Assert.assertEquals(injectedMock, field.get(testClassInstance));
            mocks.remove(injectedMock);
        }

        Assert.assertEquals(0, mocks.size());
    }

    @Test(expected = MockitoException.class)
    public void testInjectMocksOnFieldsWithException() {
        Set<Field> injectMocksFields = getInjectMocksFields(testClassWithInjectMocks);
        engine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        // Test if an exception is thrown when no mock candidate is found
        Field fieldWithoutMock = TestClassWithInjectMocks.class.getDeclaredField("fieldWithoutMock");
        Set<Field> updatedInjectMocksFields = new HashSet<>(injectMocksFields);
        updatedInjectMocksFields.remove(fieldWithoutMock);
        engine.injectMocksOnFields(updatedInjectMocksFields, mocks, testClassInstance);
    }

    @Test
    public void testInjectMockCandidate() {
        // Test if a mock candidate is injected based on type
        Class<?> typeWithMockCandidate = new Class<TypeWithMockCandidate>() {};
        Object mockCandidate = mock(TypeWithMockCandidate.class);
        mocks.add(mockCandidate);

        Field fieldToInject = TypeWithMockCandidate.class.getDeclaredField("fieldToMock");
        engine.injectMockCandidate(typeWithMockCandidate.getClass(), mocks, testClassInstance);
        Object injectedMock = fieldToInject.get(testClassInstance);
        Assert.assertNotNull(injectedMock);
        Assert.assertEquals(mockCandidate, injectedMock);

        // Test if a mock candidate is injected based on name
        Class<?> typeWithMultipleMockCandidates = new Class<TypeWithMultipleMockCandidates>() {};
        Object mockCandidate1 = mock(TypeWithMultipleMockCandidates.class);
        Object mockCandidate2 = mock(TypeWithMultipleMockCandidates.class);
        mocks.addAll(Arrays.asList(mockCandidate1, mockCandidate2));

        Field fieldToMockByName = TypeWithMultipleMockCandidates.class.getDeclaredField("fieldToMockByName");
        engine.injectMockCandidate(typeWithMultipleMockCandidates.getClass(), mocks, testClassInstance);
        Object injectedMockByName = fieldToMockByName.get(testClassInstance);
        Assert.assertNotNull(injectedMockByName);
        Assert.assertEquals(mockCandidate1, injectedMockByName);
    }

    private Set<Field> getInjectMocksFields(Class<?> testClass) {
        return Arrays.stream(testClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());
    }
}