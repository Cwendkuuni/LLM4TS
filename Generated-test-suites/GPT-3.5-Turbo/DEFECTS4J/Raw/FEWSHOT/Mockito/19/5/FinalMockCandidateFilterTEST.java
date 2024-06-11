
package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withOneMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock, never()).hashCode();
        verify(mock, never()).equals(any());
        verify(mock, never()).toString();

        assertEquals(mock, result);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock1, never()).hashCode();
        verify(mock1, never()).equals(any());
        verify(mock1, never()).toString();
        verify(mock2, never()).hashCode();
        verify(mock2, never()).equals(any());
        verify(mock2, never()).toString();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_withNullMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = null;

        Field field = getField();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_withNullField() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_withNullFieldInstance() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = null;

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock, never()).hashCode();
        verify(mock, never()).equals(any());
        verify(mock, never()).toString();

        assertNotNull(result);
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("mocks");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
