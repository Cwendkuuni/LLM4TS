
package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_Success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = mock.getClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        assertTrue(result);
    }

    @Test
    public void testFilterCandidate_multipleMocks_Failure() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = mocks.iterator().next().getClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionThrown() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = mock.getClass().getDeclaredFields()[0];
        Object fieldInstance = new Object();

        // Force an exception to be thrown during injection
        FieldSetter.setField(fieldInstance, field, null);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }

}
