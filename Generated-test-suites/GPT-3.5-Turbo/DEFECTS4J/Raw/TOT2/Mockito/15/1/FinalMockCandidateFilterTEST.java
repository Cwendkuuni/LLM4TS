
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = getClass().getDeclaredFields()[0];
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateSingleMock() {
        mocks.add(new Object());
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        mocks.add(new Object());
        mocks.add(new Object());
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateExceptionHandling() {
        Collection<Object> emptyMocks = new ArrayList<>();
        filter.filterCandidate(emptyMocks, field, fieldInstance);
    }

}
