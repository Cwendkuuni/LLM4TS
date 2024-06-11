package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final Collection<Object> mocks = new ArrayList<>();
        final Field field = null;
        final Object fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        final Field field = null;
        final Object fieldInstance = null;

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWhenExceptionOccurred() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        final Field field = null;
        final Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }
}