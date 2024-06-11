import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_noMocks() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result.thenInject());
    }
    
    @Test
    public void testFilterCandidate_oneMock() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock = Mockito.mock(Object.class);
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertSame(mock, result.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result.thenInject());
    }
    
    @Test
    public void testThenInject_successfulInjection() {
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = new FinalMockCandidateFilter.OngoingInjecter() {
            public Object thenInject() {
                return null;
            }
        };
        
        final Object mock = Mockito.mock(Object.class);
        Field field = null;
        
        BeanPropertySetter propertySetter = Mockito.mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = Mockito.mock(FieldSetter.class);
        
        Mockito.when(propertySetter.set(mock)).thenReturn(true);
        Mockito.when(fieldSetter.set(mock)).thenReturn(true);
        
        new Expectations() {{
            new BeanPropertySetter(any, any); result = propertySetter;
            new FieldSetter(any, any); result = fieldSetter;
        }};
        
        assertNull(ongoingInjecter.thenInject());
        
        new Verifications() {{
            propertySetter.set(mock);
            fieldSetter.set(mock);
        }};
    }
    
    @Test
    public void testThenInject_failedInjection() {
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = new FinalMockCandidateFilter.OngoingInjecter() {
            public Object thenInject() {
                return null;
            }
        };
        
        final Object mock = Mockito.mock(Object.class);
        Field field = null;
        
        BeanPropertySetter propertySetter = Mockito.mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = Mockito.mock(FieldSetter.class);
        Reporter reporter = Mockito.mock(Reporter.class);
        
        Mockito.when(propertySetter.set(mock)).thenReturn(false);
        Mockito.when(fieldSetter.set(mock)).thenReturn(false);
        
        new Expectations() {{
            new BeanPropertySetter(any, any); result = propertySetter;
            new FieldSetter(any, any); result = fieldSetter;
            new Reporter(); result = reporter;
        }};
        
        assertNull(ongoingInjecter.thenInject());
        
        new Verifications() {{
            propertySetter.set(mock);
            fieldSetter.set(mock);
            reporter.cannotInjectDependency(field, mock, any);
        }};
    }

}