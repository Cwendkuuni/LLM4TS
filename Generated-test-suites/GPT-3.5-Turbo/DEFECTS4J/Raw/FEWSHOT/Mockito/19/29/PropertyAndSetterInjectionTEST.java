package org.mockito.internal.configuration.injection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_whenInjectMocksFieldIsNull_shouldReturnFalse() {
        assertFalse(propertyAndSetterInjection.processInjection(null, null, null));
    }

    @Test
    public void testProcessInjection_whenInjectMocksFieldIsInitialized_shouldReturnTrue() throws NoSuchFieldException {
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mocksField");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = new HashSet<>();

        assertTrue(propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates));
    }

    @Test
    public void testProcessInjection_whenInjectMocksFieldIsNotInitialized_shouldThrowException() throws NoSuchFieldException {
        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("mocksField");
        Object injectMocksFieldOwner = new PropertyAndSetterInjection();
        Set<Object> mockCandidates = null;

        try {
            propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Cannot initialize for @InjectMocks annotation", e.getMessage());
        }
    }

    @Test
    public void testInjectMockCandidates_whenMocksIsEmptyAndInstanceIsNotNull_shouldReturnFalse() {
        Set<Object> mocks = new HashSet<>();
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();

        assertFalse(propertyAndSetterInjection.injectMockCandidates(instance.getClass(), mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_whenMocksIsEmptyAndInstanceIsNull_shouldReturnFalse() {
        Set<Object> mocks = new HashSet<>();
        Object instance = null;
        List<Field> orderedInstanceFields = new ArrayList<>();

        assertFalse(propertyAndSetterInjection.injectMockCandidates(instance.getClass(), mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_whenMocksContainsOneMockAndInstanceIsNotNull_shouldReturnTrue() {
        Set<Object> mocks = new HashSet<>();
        Object mock = mock(MockClass.class);
        mocks.add(mock);
        Object instance = new Object();
        List<Field> orderedInstanceFields = new ArrayList<>();

        assertTrue(propertyAndSetterInjection.injectMockCandidates(instance.getClass(), mocks, instance));
    }

    @Test
    public void testInjectMockCandidates_whenMocksContainsOneMockAndInstanceIsNull_shouldReturnTrue() {
        Set<Object> mocks = new HashSet<>();
        Object mock = mock(MockClass.class);
        mocks.add(mock);
        Object instance = null;
        List<Field> orderedInstanceFields = new ArrayList<>();

        assertTrue(propertyAndSetterInjection.injectMockCandidates(instance.getClass(), mocks, instance));
    }

    private class MockClass {

    }
}