import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

        Field field = getFieldInstance("field");
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        mocks.add(mock(Object.class));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        verify(mockBeanPropertySetter, times(1)).set(result);
        verify(mockFieldSetter, never()).set(result);
        verify(mockReporter, never()).cannotInjectDependency(field, result, null);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

        Field field = getFieldInstance("field");
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNull(result);
        verify(mockBeanPropertySetter, never()).set(result);
        verify(mockFieldSetter, never()).set(result);
        verify(mockReporter, never()).cannotInjectDependency(field, result, null);
    }

    @Test
    public void testFilterCandidate_withCannotInjectDependencyException() {
        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

        Field field = getFieldInstance("field");
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        mocks.add(mock(Object.class));

        when(mockBeanPropertySetter.set(any())).thenReturn(false);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();

        assertNotNull(result);
        verify(mockBeanPropertySetter, times(1)).set(result);
        verify(mockFieldSetter, times(1)).set(result);
        verify(mockReporter, times(1)).cannotInjectDependency(field, result, null);
    }

    private Field getFieldInstance(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}