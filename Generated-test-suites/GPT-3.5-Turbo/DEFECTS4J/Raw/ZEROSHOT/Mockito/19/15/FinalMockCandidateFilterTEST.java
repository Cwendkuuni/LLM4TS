import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.*;
import org.mockito.internal.configuration.injection.filter.*;
import java.lang.reflect.Field;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    
    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = mock(Collection.class);
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }
    
    @Test
    public void testFilterCandidateWithSingleMock() {
        when(mocks.size()).thenReturn(1);
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object mock = mock(Object.class);
        when(mocks.iterator().next()).thenReturn(mock);
        
        ongoingInjecter.thenInject();
        
        verifyZeroInteractions(mockitoReporter);
        verifyZeroInteractions(beanPropertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(beanPropertySetter).set(fieldInstance, field, mock);
        verifyNoMoreInteractions(mockitoReporter);
        verifyNoMoreInteractions(beanPropertySetter);
        verifyNoMoreInteractions(fieldSetter);
    }
    
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        when(mocks.size()).thenReturn(2);
        
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        ongoingInjecter.thenInject();
        
        verifyZeroInteractions(mockitoReporter);
        verifyZeroInteractions(beanPropertySetter);
        verifyZeroInteractions(fieldSetter);
        verifyNoMoreInteractions(mockitoReporter);
        verifyNoMoreInteractions(beanPropertySetter);
        verifyNoMoreInteractions(fieldSetter);
    }
}