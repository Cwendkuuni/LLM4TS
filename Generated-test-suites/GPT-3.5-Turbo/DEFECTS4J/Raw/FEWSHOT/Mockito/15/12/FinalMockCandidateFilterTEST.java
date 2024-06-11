package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.exceptions.base.MockitoException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = new Field();
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        mocks.add(new Object());
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(ongoingInjecter.thenInject());
        Assert.assertNotNull(FieldSetter.getField(fieldInstance, field));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertFalse(ongoingInjecter.thenInject());
        Assert.assertNull(FieldSetter.getField(fieldInstance, field));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithInjectionException() {
        mocks.add(new Object());
        field.setAccessible(false);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();
    }
}