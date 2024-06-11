package org.mockito.internal.configuration.injection.filter;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidateWithSingleMock() {
        // Setup
        Collection<Object> mocks = new HashSet<>();
        final Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        final Object fieldInstance = Util.class;

        // Test
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        assertNotNull(injecter);
        assertTrue(injecter instanceof OngoingInjecter);

        Object result = injecter.thenInject();
        assertNotNull(result);

        verify(fieldInstance, times(1)).set(matchingMock);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Setup
        Collection<Object> mocks = new HashSet<>();
        mocks.add(mock(Objecet.class));
        mocks.add(mock(Objecet.class));
        final Field field = Util.class.getDeclaredField("EMPTY_STRING_ARRAY");
        final Object fieldInstance = Util.class;

        // Test
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        assertNotNull(injecter);
        assertTrue(injecter instanceof OngoingInjecter);

        Object result = injecter.thenInject();
        assertNull(result);

        verify(fieldInstance, never()).set(any());
    }
}