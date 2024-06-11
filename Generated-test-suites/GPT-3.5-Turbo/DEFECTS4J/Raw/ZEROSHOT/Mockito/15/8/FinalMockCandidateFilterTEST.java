package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = new Field();
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        mocks.add(new Object());

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();

        Assert.assertTrue(result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        boolean result = ongoingInjecter.thenInject();

        Assert.assertFalse(result);
    }
}