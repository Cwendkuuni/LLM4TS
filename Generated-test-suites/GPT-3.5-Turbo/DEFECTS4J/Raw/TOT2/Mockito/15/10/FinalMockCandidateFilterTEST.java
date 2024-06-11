package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Typical use case
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = mock.getClass().getField("testField");
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());

        // Edge case: Multiple mocks
        mocks.add(new Object());
        injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());

        // Error scenario: Field not found
        try {
            Field notFoundField = mock.getClass().getField("nonExistentField");
            filter.filterCandidate(mocks, notFoundField, fieldInstance);
        } catch (NoSuchFieldException e) {
            // Expected exception
            assertTrue(e instanceof NoSuchFieldException);
        }
    }
}