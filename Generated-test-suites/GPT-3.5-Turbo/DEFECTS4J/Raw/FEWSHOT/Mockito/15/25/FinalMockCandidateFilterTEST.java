
package org.mockito.internal.configuration.injection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() throws NoSuchFieldException {
        filter = new FinalMockCandidateFilter();
        field = FinalMockCandidateFilter.class.getDeclaredField("field");
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        assertEquals(mock, field.get(fieldInstance));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }
}
