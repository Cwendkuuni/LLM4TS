package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;

    @Before
    public void setup() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_oneMock() {
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        Object mock = new Object();
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        boolean result = ongoingInjecter.thenInject();
        assertTrue(result);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        boolean result = ongoingInjecter.thenInject();
        assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        Field field = getField("fieldName");
        Object fieldInstance = new Object();
        mocks.add(new Object());

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);

        ongoingInjecter.thenInject();
    }

    private Field getField(String name) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}