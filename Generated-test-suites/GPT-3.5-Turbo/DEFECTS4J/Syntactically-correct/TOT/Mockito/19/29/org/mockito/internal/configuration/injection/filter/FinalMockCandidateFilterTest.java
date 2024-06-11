import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setup() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_successfulInjection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = getTestClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();
        Object matchingMock = mock(Object.class);
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(matchingMock);
        
        // When
        Object result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        
        // Then
        assertTrue(result instanceof Object);
        verify(matchingMock, times(1)).hashCode();
        verify(new BeanPropertySetter(fieldInstance, field), times(0)).set(matchingMock);
        verify(new FieldSetter(fieldInstance, field), times(1)).set(matchingMock);
    }

    @Test
    public void testFilterCandidate_multipleCandidates() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = getTestClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();
        when(mocks.size()).thenReturn(2);

        // When
        Object result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();

        // Then
        assertNull(result);
        verify(new BeanPropertySetter(fieldInstance, field), times(0)).set(any());
        verify(new FieldSetter(fieldInstance, field), times(0)).set(any());
    }

    @Test
    public void testFilterCandidate_exceptionOnInjection() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Field field = getTestClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();
        Object matchingMock = mock(Object.class);
        when(mocks.size()).thenReturn(1);
        when(mocks.iterator().next()).thenReturn(matchingMock);
        doThrow(RuntimeException.class).when(new BeanPropertySetter(fieldInstance, field)).set(eq(matchingMock));
        
        // When
        final Reporter reporterMock = mock(Reporter.class);
        Object result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();
        
        // Then
        assertNotNull(result);
        verify(reporterMock).cannotInjectDependency(eq(field), eq(matchingMock), any(RuntimeException.class));
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(eq(matchingMock));
        verify(new FieldSetter(fieldInstance, field), times(1)).set(eq(matchingMock));
    }
}