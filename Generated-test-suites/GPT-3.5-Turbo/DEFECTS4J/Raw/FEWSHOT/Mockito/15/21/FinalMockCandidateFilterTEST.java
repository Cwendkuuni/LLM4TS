package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

import static org.junit.Assert.*;
import org.junit.Test;


public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        
        Field field = new Field();
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = new Field();
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertFalse(injecter.thenInject());
    }
}