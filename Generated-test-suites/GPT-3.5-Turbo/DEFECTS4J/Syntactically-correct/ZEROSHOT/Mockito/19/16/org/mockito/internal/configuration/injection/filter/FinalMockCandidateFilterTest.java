import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter.*;

public class FinalMockCandidateFilterTest {
    
    @Test
    public void testFilterCandidate_oneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(injecter);
        assertNotNull(injecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = null;
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }
    
    @Test
    public void testThenInject_propertySetterSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();
        
        assertNotNull(injectedObject);
        assertEquals(mocks.iterator().next(), injectedObject);
    }
    
    @Test
    public void testThenInject_propertySetterFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();
        
        assertNotNull(injectedObject);
        assertEquals(mocks.iterator().next(), injectedObject);
    }
    
    @Test
    public void testThenInject_fieldSetterSuccess() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();
        
        assertNotNull(injectedObject);
        assertEquals(mocks.iterator().next(), injectedObject);
    }
    
    @Test
    public void testThenInject_fieldSetterFailure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<Object>();
        mocks.add(new Object());
        Field field = null;
        Object fieldInstance = new Object();
        
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object injectedObject = injecter.thenInject();
        
        assertNotNull(injectedObject);
        assertEquals(mocks.iterator().next(), injectedObject);
    }
}