package org.mockito.internal.configuration.injection.filter;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws Exception {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        
        Field fieldToBeInjected = MockCandidateFilterTest.class.getDeclaredField("mocks");
        Object fieldInstance = new ArrayList<>();
        
        OngoingInjecter injecter = MockCandidateFilter.OngoingInjecter.builder()
                .injectClass(MockCandidateFilterTest.class)
                .injectObject(this)
                .fieldInstance(fieldInstance);
        
        Mockito.when(filter.filterCandidate(Mockito.any(Collection.class), Mockito.any(Field.class), Mockito.any(Object.class))).thenReturn(injecter);
        
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        
        assertEquals(MockCandidateFilterTest.class, result.getInjectClass());
        assertEquals(this, result.getInjectObject());
        assertEquals(fieldInstance, result.getFieldInstance());
    }

}