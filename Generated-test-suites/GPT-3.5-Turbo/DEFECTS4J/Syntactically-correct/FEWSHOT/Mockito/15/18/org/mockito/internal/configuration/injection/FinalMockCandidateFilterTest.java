package org.mockito.internal.configuration.injection;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.configuration.injection.MockCandidateFilter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = Arrays.asList(mock(Object.class));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_withSingleMock() {
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result.thenInject());
        verify(fieldInstance, times(1)).set(any(Object.class));
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        Collection<Object> multipleMocks = Arrays.asList(mock(Object.class), mock(Object.class));
        OngoingInjecter result = filter.filterCandidate(multipleMocks, field, fieldInstance);

        assertFalse(result.thenInject());
        verify(fieldInstance, never()).set(any(Object.class));
    }
}