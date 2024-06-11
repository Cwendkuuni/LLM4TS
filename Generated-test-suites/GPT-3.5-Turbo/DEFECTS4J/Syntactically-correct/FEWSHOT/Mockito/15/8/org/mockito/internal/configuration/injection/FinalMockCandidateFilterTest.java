package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertTrue(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Object mock2 = new Object();
        mocks.add(mock2);
        
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertFalse(injecter.thenInject());
    }
}