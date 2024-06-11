package org.mockito.internal.configuration.DefaultInjectionEngine; // adjust the package name if necessary

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultInjectionEngineTest {

    private DefaultInjectionEngine defaultInjectionEngine;
    private Set<Object> mocks;
    private Object testClassInstance;

    @Before
    public void setup() throws Exception {
        mockCandidateFilterSetup();
        defaultInjectionEngine = new DefaultInjectionEngine();
        testClassInstance = new Object() {};
        mocks = new HashSet<>();
        mocks.add(mock(ClassA.class));
        mocks.add(mock(ClassB.class));
    }

    // Test for injectMocksOnFields method
    @Test
    public void testInjectMocksOnFields() {
        Set<Field> injectMocksFields = Arrays.asList(testClassInstance.getClass().getFields())
                .stream()
                .filter(field -> field.isAnnotationPresent(InjectMocks.class))
                .collect(Collectors.toSet());

        defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

        for (Field injectMocksField : injectMocksFields) {
            Object injectMocksFieldInstance = Whitebox.getInternalState(testClassInstance, injectMocksField.getName());
            Assert.assertNotNull(injectMocksFieldInstance);
        }
    }

    // Test for injectMockCandidate method with a mock candidate found by type
    @Test
    public void testInjectMockCandidateFoundByType() throws Exception {
        Class<?> awaitingInjectionClazz = ClassC.class;
        Class<?> expectedMockType = ClassD.class;

        Set<Object> mocksCopy = new HashSet<>(mocks);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        Object fieldInstance = Whitebox.getInternalState(testClassInstance, "fieldToBeInjected"); // replace with actual field name
        Object injectedMock = mocksCopy.iterator().next();
        Assert.assertEquals(expectedMockType, fieldInstance.getClass());
        Assert.assertEquals(injectedMock, Whitebox.getInternalState(fieldInstance, "mock")); // replace with actual field name to be injected

        mocksCopy.remove(injectedMock);
    }

    // Test for injectMockCandidate method with a mock candidate not found by type
    // but found by name
    @Test
    public void testInjectMockCandidateFoundByName() throws Exception {
        Class<?> awaitingInjectionClazz = ClassC.class;
        String expectedMockName = "mockName";
        Object expectedMockInstance = mock(ClassE.class);

        Set<Object> mocksCopy = new HashSet<>(mocks);

        doReturn(expectedMockInstance).when(mockCandidateFilter).filterCandidate(any(Set.class), any(Field.class), any(Object.class));

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        Object fieldInstance = Whitebox.getInternalState(testClassInstance, "fieldToBeInjected"); // replace with actual field name
        Object injectedMock = expectedMockInstance;
        Assert.assertEquals(injectedMock, Whitebox.getInternalState(fieldInstance, "mock")); // replace with actual field name to be injected

        mocksCopy.remove(injectedMock);
    }

    // Test for injectMockCandidate method without finding any mock candidate
    @Test(expected = MockitoException.class)
    public void testInjectMockCandidateNotFound() throws Exception {
        Class<?> awaitingInjectionClazz = ClassC.class;

        Set<Object> mocksCopy = new HashSet<>(mocks);

        defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testClassInstance);

        fail("InjectMockCandidateNotFoundException should have been thrown.");
    }

    // Helper method to set up mockCandidateFilter
    private void mockCandidateFilterSetup() throws Exception {
        MockCandidateFilter nameBasedFilter = new NameBasedCandidateFilter(new FinalMockCandidateFilter());
        MockCandidateFilter typeBasedFilter = new TypeBasedCandidateFilter(nameBasedFilter);
        mockCandidateFilter = new TypeBasedCandidateFilter(typeBasedFilter);
    }
}