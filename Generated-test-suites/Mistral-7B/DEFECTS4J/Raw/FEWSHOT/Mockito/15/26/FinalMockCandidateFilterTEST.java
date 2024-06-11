package org.mockito.internal.configuration.injection;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.mockitostub.MockitoStubs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithSingleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = MockitoStubs.mock(String.class);
        mocks.add(mock);

        Field field = MockitoConfiguration.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(injecter instanceof OngoingInjecter);

        injecter.thenInject();

        try {
            new FieldSetter(fieldInstance, field).set(mock);
            Assert.fail();
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = MockitoStubs.mock(String.class);
        Object mock2 = MockitoStubs.mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = MockitoConfiguration.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Assert.assertTrue(injecter instanceof OngoingInjecter);

        // Property setter should not be called as there is no unique mock
        try {
            new FieldSetter(fieldInstance, field).set(mock1);
        } catch (IllegalAccessException e) {
            // expected
        }

        Assert.assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidateWithNullMocks() {
        Field field = MockitoConfiguration.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(null, field, fieldInstance);

        Assert.assertTrue(injecter instanceof OngoingInjecter);

        Assert.assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithException() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = MockitoStubs.mock(String.class);
        mocks.add(mock);

        Field field = MockitoConfiguration.class.getDeclaredField("fieldToInject");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
            throw new MockitoException("Should have thrown exception during injection");
        } catch (MockitoException e) {
            // expected
        }
    }
}