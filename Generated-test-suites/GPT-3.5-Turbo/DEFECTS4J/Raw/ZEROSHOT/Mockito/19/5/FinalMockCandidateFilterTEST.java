import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithNoMocks() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field field = MyClass.class.getDeclaredField("myField");
        Object fieldInstance = new MyClass();
        
        assertNull(filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }
    
    @Test
    public void testFilterCandidateWithOneMock() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(MyClass.class));
        Field field = MyClass.class.getDeclaredField("myField");
        Object fieldInstance = new MyClass();
        
        assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }
    
    @Test
    public void testFilterCandidateWithOneMockAndSuccessfulInjection() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(MyClass.class));
        Field field = MyClass.class.getDeclaredField("myField");
        Object fieldInstance = new MyClass();
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        
        when(propertySetter.set(any())).thenReturn(true);
        doNothing().when(fieldSetter).set(any());
        
        new Reporter().cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
        
        assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance).thenInject());
        
        verify(propertySetter).set(mocks.get(0));
        verify(fieldSetter, never()).set(mocks.get(0));
        verify(new Reporter()).cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
    }
    
    @Test
    public void testFilterCandidateWithOneMockAndFailedInjection() throws Exception {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singletonList(mock(MyClass.class));
        Field field = MyClass.class.getDeclaredField("myField");
        Object fieldInstance = new MyClass();
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        
        when(propertySetter.set(any())).thenReturn(false);
        doNothing().when(fieldSetter).set(any());
        
        new Reporter().cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
        
        assertEquals(mocks.get(0), filter.filterCandidate(mocks, field, fieldInstance).thenInject());
        
        verify(propertySetter).set(mocks.get(0));
        verify(fieldSetter).set(mocks.get(0));
        verify(new Reporter(), never()).cannotInjectDependency(field, mocks.get(0), any(RuntimeException.class));
    }
    
    private static class MyClass {
        private MyField myField;
        
        public void setMyField(MyField myField) {
            this.myField = myField;
        }
        
        public MyField getMyField() {
            return myField;
        }
    }
    
    private static class MyField {
        
    }
}