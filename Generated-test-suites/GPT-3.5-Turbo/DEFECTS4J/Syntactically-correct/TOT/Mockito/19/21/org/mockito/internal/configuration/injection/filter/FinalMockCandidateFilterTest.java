import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnOngoingInjecterWithMatchingMockSetUsingPropertySetter() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField("field");
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        ongoingInjecter.thenInject();
        
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(any());
        verify(new FieldSetter(fieldInstance, field), never()).set(any());
    }

    @Test
    public void testFilterCandidate_withSingleMock_andFailedPropertySetter_shouldSetMatchingMockUsingFieldSetter() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField("field");
        Object fieldInstance = new Object();
        
        when(new BeanPropertySetter(fieldInstance, field).set(any())).thenReturn(false);
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        ongoingInjecter.thenInject();
        
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(any());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(any());
    }

    @Test
    public void testFilterCandidate_withSingleMock_andFailedPropertySetter_andFailedFieldSetter_shouldCallReporterCannotInjectDependency() {
        Collection<Object> mocks = Collections.singletonList(mock(Object.class));
        Field field = getField("field");
        Object fieldInstance = new Object();
        
        when(new BeanPropertySetter(fieldInstance, field).set(any())).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).set(any())).thenThrow(new RuntimeException());
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Reporter reporter = mock(Reporter.class);
        ongoingInjecter.thenInject();
        
        verify(reporter, times(1)).cannotInjectDependency(eq(field), any(), any(RuntimeException.class));
    }
    
    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnOngoingInjecterAsNull() {
        Collection<Object> mocks = Collections.nCopies(2, mock(Object.class));
        Field field = getField("field");
        Object fieldInstance = new Object();
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        assertNull(result);
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}