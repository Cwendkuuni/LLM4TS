
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter mockCandidateFilter;
    
    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }
    
    @Test
    public void testFilterCandidate_singleMock_shouldReturnInjecterWithThenInjectTrue() {
        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks_shouldReturnInjecterWithThenInjectFalse() {
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        assertFalse(injecter.thenInject());
    }
    
    @Test(expected = MockitoException.class)
    public void testFilterCandidate_injectFailure_shouldThrowMockitoException() {
        Collection<Object> mocks = Arrays.asList(new Object());
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(null);
        
        injecter.thenInject();
    }
    
    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException should not occur.");
        }
        return null;
    }
    
}
