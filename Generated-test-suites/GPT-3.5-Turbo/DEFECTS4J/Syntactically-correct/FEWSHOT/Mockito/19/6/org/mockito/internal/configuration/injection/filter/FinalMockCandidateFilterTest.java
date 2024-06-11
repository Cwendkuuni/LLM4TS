package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_SingleMock() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = injecter.thenInject();
        
        assertEquals(mocks.iterator().next(), result);
        verify(mockingDetails(mocks.iterator().next()), never()).isMock();
        verify(mockingDetails(mocks.iterator().next()), never()).isSpy();
        verify(mockingDetails(mocks.iterator().next()), never()).getMockCreationSettings();
        verify(mockingDetails(mocks.iterator().next()), never()).getMatchers();
        verify(mockingDetails(mocks.iterator().next()), never()).getInvocationContainer();
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(mocks.iterator().next());
        verify(new FieldSetter(fieldInstance, field), never()).set(mocks.iterator().next());
        verify(new Reporter(), never()).cannotInjectDependency(field, mocks.iterator().next());
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = injecter.thenInject();
        
        assertEquals(null, result);
        verify(new Reporter(), never()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }
    
    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("mocks");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}