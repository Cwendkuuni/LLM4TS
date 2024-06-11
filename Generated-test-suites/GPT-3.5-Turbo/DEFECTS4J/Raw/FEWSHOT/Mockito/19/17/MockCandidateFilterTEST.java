package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

public class MockCandidateFilterTest {
    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;
    
    @Before
    public void setUp() {
        filter = new MockCandidateFilterImpl();
        mocks = createMocks();
        fieldToBeInjected = createField();
        fieldInstance = createFieldInstance();
    }
    
    private Collection<Object> createMocks() {
        Collection<Object> mocks = new ArrayList<>();
        // add mock objects to the collection
        return mocks;
    }
    
    private Field createField() {
        // create and return a Field instance
    }
    
    private Object createFieldInstance() {
        // create and return a field instance
    }

    @Test
    public void testFilterCandidate() {
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        // assert the ongoing injecter returned by the filter
    }
}