package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>();
        Object mock = new Object();
        mocks.add(mock);
        
        Field field = getField();
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        
        boolean result = injecter.thenInject();
        assertTrue(result);
        
        // Verify the mock is injected into the field
        try {
            Field actualField = fieldInstance.getClass().getDeclaredField(field.getName());
            actualField.setAccessible(true);
            Object actualMock = actualField.get(fieldInstance);
            assertEquals(mock, actualMock);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = getField();
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        
        boolean result = injecter.thenInject();
        assertFalse(result);
        
        // Verify the mock is not injected into the field
        try {
            Field actualField = fieldInstance.getClass().getDeclaredField(field.getName());
            actualField.setAccessible(true);
            Object actualMock = actualField.get(fieldInstance);
            assertNull(actualMock);
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
        }
    }
    
    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("filter");
        } catch (Exception e) {
            fail("Exception was thrown: " + e.getMessage());
            return null;
        }
    }
}