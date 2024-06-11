package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Instantiators;
import org.mockito.internal.util.reflection.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = ReflectUtils.findField(fieldInstance.getClass(), "fieldToMock");
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        MockCandidateFilter candidateFilter = spy(filter);
        Object result = candidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertNotNull(result);
        assertEquals(mock, result);
        verify(candidateFilter).new OngoingInjecter(new BeanPropertySetter(fieldInstance, field).set(mock));
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        MockCandidateFilter candidateFilter = spy(filter);
        Object result = candidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();

        assertNull(result);
        verify(candidateFilter).new OngoingInjecter(new Object[]{});
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidateWithException() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FieldSetter fieldSetter = Instantiators.newFieldSetter(field);

        MockCandidateFilter candidateFilter = spy(filter);
        Object result = candidateFilter.filterCandidate(mocks, field, fieldInstance).thenInject();

        try {
            new BeanPropertySetter(fieldInstance, field).set(mock);
            fail("Should have thrown exception");
        } catch (RuntimeException e) {
            // Expected exception
            throw e;
        }

        assertNull(result);
        verify(candidateFilter, times(2)).new OngoingInjecter(new Object[]{});
    }
}