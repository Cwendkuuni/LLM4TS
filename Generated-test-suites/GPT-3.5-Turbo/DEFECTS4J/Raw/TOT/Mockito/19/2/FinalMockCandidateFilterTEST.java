import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    
    private FinalMockCandidateFilter filter;
    
    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }
    
    @Test
    public void testFilterCandidate_singleMock_returnOngoingInjecterWithMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(mock, ongoingInjecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_multipleMocks_returnOngoingInjecterWithNullInjection() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(ongoingInjecter.thenInject());
    }
    
    @Test
    public void testFilterCandidate_singleMock_propertySetterSuccess_returnMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(fieldInstance, field)).thenReturn(true);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(mock, ongoingInjecter.thenInject());
        
        verify(propertySetter).set(fieldInstance, field);
        verifyZeroInteractions(new FieldSetter(fieldInstance, field));
        verifyZeroInteractions(new Reporter());
    }
    
    @Test
    public void testFilterCandidate_singleMock_propertySetterFailure_fieldSetterSuccess_returnMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        when(propertySetter.set(fieldInstance, field)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertEquals(mock, ongoingInjecter.thenInject());
        
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verifyZeroInteractions(new Reporter());
    }
    
    @Test
    public void testFilterCandidate_singleMock_propertySetterFailure_fieldSetterFailure_reporterCalled() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        Reporter reporter = mock(Reporter.class);
        RuntimeException exception = new RuntimeException();
        when(propertySetter.set(fieldInstance, field)).thenReturn(false);
        whenNew(BeanPropertySetter.class).withArguments(fieldInstance, field).thenReturn(propertySetter);
        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);
        whenNew(Reporter.class).thenReturn(reporter);
        
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        
        assertNull(ongoingInjecter.thenInject());
        
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verify(reporter).cannotInjectDependency(field, mock, exception);
    }
}