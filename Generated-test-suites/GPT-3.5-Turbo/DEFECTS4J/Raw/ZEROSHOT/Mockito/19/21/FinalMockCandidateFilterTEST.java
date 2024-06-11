import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new HashSet<Object>();
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_withOneMock() {
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertEquals(mockObject, result);
        
        verify(fieldInstance, times(1)).set(mockObject);
        verify(field, times(1)).set(fieldInstance, mockObject);
    }
    
    @Test
    public void testFilterCandidate_withMultipleMocks() {
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNull(result);
        
        verifyZeroInteractions(fieldInstance, field);
    }

    @Test
    public void testFilterCandidate_withException() {
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);
        
        BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
        when(beanPropertySetter.set(mockObject)).thenThrow(new RuntimeException());

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertEquals(mockObject, result);
        
        verify(beanPropertySetter, times(1)).set(mockObject);
        verify(fieldInstance, never()).set(mockObject);
        verify(field, never()).set(fieldInstance, mockObject);
        
        verify(new Reporter(), times(1)).cannotInjectDependency(field, mockObject, new RuntimeException());
    }

}