package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_successfullyInjected() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList("mockObject");
        Field field = Field.class;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noInjection() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList("mockObject1", "mockObject2");
        Field field = Field.class;
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList("mockObject");
        Field field = null; // Intentionally setting field as null to trigger exception
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }
}