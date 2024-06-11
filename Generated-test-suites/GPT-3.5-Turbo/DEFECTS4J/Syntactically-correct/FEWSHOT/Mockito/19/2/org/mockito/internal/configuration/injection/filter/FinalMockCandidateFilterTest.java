package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.ArrayList;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<Object>();
        final Field field = null;
        final Object fieldInstance = null;

        mocks.add(new Object()); // Add a single mock

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = new ArrayList<Object>();
        final Field field = null;
        final Object fieldInstance = null;

        mocks.add(new Object());
        mocks.add(new Object()); // Add multiple mocks

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_injectProperty() {
        Collection<Object> mocks = new ArrayList<Object>();
        final Field field = null;
        final Object fieldInstance = new Object();

        mocks.add(new Object()); // Add a single mock

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_injectField() {
        Collection<Object> mocks = new ArrayList<Object>();
        final Field field = new Field();
        final Object fieldInstance = new Object();

        mocks.add(new Object()); // Add a single mock

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNotNull(result);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_cannotInject() {
        Collection<Object> mocks = new ArrayList<Object>();
        final Field field = new Field();
        final Object fieldInstance = new Object();

        mocks.add(new Object()); // Add a single mock

        FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);

        Object result = ongoingInjecter.thenInject();
        assertNull(result);
    }
}