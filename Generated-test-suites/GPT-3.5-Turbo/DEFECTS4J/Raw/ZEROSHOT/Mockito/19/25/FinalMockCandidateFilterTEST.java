import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {
    
    @InjectMocks
    private FinalMockCandidateFilter filter;
    
    @Mock
    private Collection<Object> mocks;
    
    @Mock
    private Field field;
    
    @Mock
    private Object fieldInstance;
    
    @Mock
    private Object matchingMock;
    
    @Before
    public void setUp() throws Exception {
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(matchingMock);
    }
    
    @Test
    public void testFilterCandidate_singleMock() {
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        Assert.assertEquals(matchingMock, result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyNoMoreInteractions(new Reporter());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        when(mocks.size()).thenReturn(2);
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        Object result = ongoingInjecter.thenInject();
        
        Assert.assertNull(result);
        verifyNoMoreInteractions(new BeanPropertySetter(fieldInstance, field));
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
        verifyNoMoreInteractions(new Reporter());
    }
}