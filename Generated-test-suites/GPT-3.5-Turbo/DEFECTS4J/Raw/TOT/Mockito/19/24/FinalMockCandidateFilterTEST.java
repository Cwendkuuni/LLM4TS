import org.junit.Test;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();

    @Test
    public void testFilterCandidate_singleMock_matchingMock() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        Object result = ongoingInjecter.thenInject();

        verifyNoInteractions(fieldInstance);

        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidate_singleMock_noMatchingMock() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        when(new BeanPropertySetter(fieldInstance, field).set(any())).thenReturn(false);
        when(new FieldSetter(fieldInstance, field).set(any())).thenReturn(true);

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        Object result = ongoingInjecter.thenInject();

        verify(new BeanPropertySetter(fieldInstance, field)).set(any());
        verify(new FieldSetter(fieldInstance, field)).set(any());
        verifyNoMoreInteractions(fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        Object result = ongoingInjecter.thenInject();

        verifyNoInteractions(fieldInstance);

        assertNull(result);
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            fail("Failed to get field: " + name);
            return null;
        }
    }

    @Test
    public void testFilterCandidate_exceptionThrown() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        when(new BeanPropertySetter(fieldInstance, field).set(any())).thenThrow(new RuntimeException("Test Exception"));

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        Object result = ongoingInjecter.thenInject();

        verify(new BeanPropertySetter(fieldInstance, field)).set(any());
        verifyNoMoreInteractions(fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_noMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        Object result = ongoingInjecter.thenInject();

        verifyNoInteractions(fieldInstance);

        assertNull(result);
    }

}