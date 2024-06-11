import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_oneMock() {
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = null; 
        Object fieldInstance = null; 
        
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verify(mock, times(1)).getClass();
        verifyNoMoreInteractions(mock);
        
        Assert.assertEquals(mock, result);
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = null; 
        Object fieldInstance = null; 
        
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        verifyZeroInteractions(mock1, mock2);
        
        Assert.assertNull(result);
    }
    
    @Test
    public void testFilterCandidate_exceptionThrown() {
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = null; 
        Object fieldInstance = null; 
        
        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(final Collection<Object> mocks, final Field field, final Object fieldInstance) {
                throw new RuntimeException("Test Exception");
            }
        };
        
        boolean exceptionThrown = false;
        
        try {
            FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        } catch (RuntimeException e) {
            exceptionThrown = true;
        }
        
        Assert.assertTrue(exceptionThrown);
    }
}