import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock() {
        // Setup
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Mocking
        Object mock = mock(Object.class);
        mocks.add(mock);

        // Execution
        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verification
        assertTrue(actual == mock);
        verifyNoInteractions(mock);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        // Setup
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Mocking
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Execution
        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verification
        assertTrue(actual == null);
        verifyNoInteractions(mock1, mock2);
    }

    @Test
    public void testFilterCandidate_cannotInjectDependency() {
        // Setup
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Mocking
        Object mock = mock(Object.class);
        mocks.add(mock);

        doThrow(new RuntimeException()).when(mock).methodName();

        // Execution
        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verification
        assertTrue(actual == mock);
        verify(mock, times(1)).methodName();
        verify(new Reporter()).cannotInjectDependency(field, mock, new RuntimeException());
    }

    @Test
    public void testFilterCandidate_setByPropertySetter() {
        // Setup
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Mocking
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(true);

        // Execution
        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verification
        assertTrue(actual == mock);
        verify(new BeanPropertySetter(fieldInstance, field)).set(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testFilterCandidate_setByFieldSetter() {
        // Setup
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        // Mocking
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).set(mock)).thenReturn(true);

        // Execution
        FinalMockCandidateFilter.OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Object actual = result.thenInject();

        // Verification
        assertTrue(actual == mock);
        verify(new BeanPropertySetter(fieldInstance, field)).set(mock);
        verify(new FieldSetter(fieldInstance, field)).set(mock);
        verifyNoMoreInteractions(mock);
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}