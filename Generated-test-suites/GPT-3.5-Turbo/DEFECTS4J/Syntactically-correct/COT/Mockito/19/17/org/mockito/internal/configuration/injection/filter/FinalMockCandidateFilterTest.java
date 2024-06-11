
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    // Test filterCandidate method with a single mock
    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }

    // Test filterCandidate method with multiple mocks
    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = List.of(mock(Object.class), mock(Object.class));
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
        verifyZeroInteractions(field);
        verifyZeroInteractions(fieldInstance);
    }
    
    // Test filterCandidate method with a single mock and successful injection
    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulInjection() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(mock);
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        
        when(propertySetter.set(fieldInstance, field)).thenReturn(true);
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, injecter.thenInject());
        verify(propertySetter).set(fieldInstance, field);
        verifyZeroInteractions(fieldSetter);
    }

    // Test filterCandidate method with a single mock and successful injection using field access
    @Test
    public void testFilterCandidateWithSingleMockAndSuccessfulInjectionUsingFieldAccess() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(mock);
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        
        when(propertySetter.set(fieldInstance, field)).thenReturn(false);
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, injecter.thenInject());
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
    }
    
    // Test filterCandidate method with a single mock and failed injection
    @Test
    public void testFilterCandidateWithSingleMockAndFailedInjection() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singleton(mock);
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);
        
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        FieldSetter fieldSetter = mock(FieldSetter.class);
        
        when(propertySetter.set(fieldInstance, field)).thenReturn(false);
        doThrow(new RuntimeException()).when(fieldSetter).set(fieldInstance, field);
        
        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
        verify(propertySetter).set(fieldInstance, field);
        verify(fieldSetter).set(fieldInstance, field);
        verifyNew(Reporter.class).cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    // Test filterCandidate method with no mocks
    @Test
    public void testFilterCandidateWithNoMocks() {
        Collection<Object> mocks = Collections.emptyList();
        Field field =mock(Field.class);
        Object fieldInstance = mock(Object.class);

        OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(injecter.thenInject());
        assertNull(field);
        assertNull(fieldInstance);
    }
}
