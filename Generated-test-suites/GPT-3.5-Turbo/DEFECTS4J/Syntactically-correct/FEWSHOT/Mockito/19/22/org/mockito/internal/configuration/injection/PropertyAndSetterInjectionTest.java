package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class PropertyAndSetterInjectionTest {
    private PropertyAndSetterInjection injection;

    @Before
    public void setUp() {
        injection = new PropertyAndSetterInjection();
    }

    @Test
    public void testProcessInjection_OneField_Success() throws NoSuchFieldException {
        // Prepare mock candidates
        Set<Object> mockCandidates = new HashSet<>();
        MockCandidate mock1 = new MockCandidate();
        mockCandidates.add(mock1);

        // Prepare injectMocksField
        Field injectMocksField = MyClass.class.getDeclaredField("mockField");
        MyClass injectMocksFieldOwner = new MyClass();

        // Perform injection
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Verify injection occurred
        assertTrue(result);
        assertEquals(mock1, injectMocksFieldOwner.getMockField());
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testProcessInjection_MultipleFields_Success() throws NoSuchFieldException {
        // Prepare mock candidates
        Set<Object> mockCandidates = new HashSet<>();
        MockCandidate mock1 = new MockCandidate();
        MockCandidate mock2 = new MockCandidate();
        mockCandidates.add(mock1);
        mockCandidates.add(mock2);

        // Prepare injectMocksField
        Field injectMocksField1 = MyClass.class.getDeclaredField("mockField");
        Field injectMocksField2 = MyClass.class.getDeclaredField("anotherMockField");
        MyClass injectMocksFieldOwner = new MyClass();

        // Perform injection
        boolean result = injection.processInjection(injectMocksField1, injectMocksFieldOwner, mockCandidates);
        result |= injection.processInjection(injectMocksField2, injectMocksFieldOwner, mockCandidates);

        // Verify injection occurred
        assertTrue(result);
        assertEquals(mock1, injectMocksFieldOwner.getMockField());
        assertEquals(mock2, injectMocksFieldOwner.getAnotherMockField());
        assertTrue(mockCandidates.isEmpty());
    }

    @Test
    public void testProcessInjection_NoCandidates_NoInjection() throws NoSuchFieldException {
        // Prepare mock candidates
        Set<Object> mockCandidates = new HashSet<>();

        // Prepare injectMocksField
        Field injectMocksField = MyClass.class.getDeclaredField("mockField");
        MyClass injectMocksFieldOwner = new MyClass();

        // Perform injection
        boolean result = injection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Verify no injection occurred
        assertFalse(result);
        assertNull(injectMocksFieldOwner.getMockField());
        assertTrue(mockCandidates.isEmpty());
    }
    
    public static class MyClass {
        private MockCandidate mockField;
        private MockCandidate anotherMockField;

        public MockCandidate getMockField() {
            return mockField;
        }

        public void setMockField(MockCandidate mockField) {
            this.mockField = mockField;
        }

        public MockCandidate getAnotherMockField() {
            return anotherMockField;
        }

        public void setAnotherMockField(MockCandidate anotherMockField) {
            this.anotherMockField = anotherMockField;
        }
    }

    public static class MockCandidate {
    }
}