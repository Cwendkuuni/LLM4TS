import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter finalMockCandidateFilter;
    
    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }
    
    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object matchingMock = mock(Object.class);
        
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator()).thenReturn(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public Object next() {
                return matchingMock;
            }
            
            @Override
            public void remove() {
            }
        });
        
        finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        
        verify(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);
        verify(new FieldSetter(fieldInstance, field)).set(matchingMock);
    }
    
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        when(mocks.size()).thenReturn(2);
        
        Object result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        
        assertNull(result);
    }
    
    @Test(expected = Reporter.cannotInjectDependency.class)
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object matchingMock = mock(Object.class);
        
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator()).thenReturn(new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }
            
            @Override
            public Object next() {
                return matchingMock;
            }
            
            @Override
            public void remove() {
            }
        });
        
        when(new BeanPropertySetter(fieldInstance, field).set(matchingMock)).thenThrow(new RuntimeException());
        
        finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
    }
}