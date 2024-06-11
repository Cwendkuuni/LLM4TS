package org.mockito.internal.configuration.injection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Object mock4;
    private Field field;
    private Field field2;

    @Before
    public void setup() throws Exception {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        mock1 = mock(Object.class);
        mock2 = mock(Object.class);
        mock3 = mock(Object.class);
        mock4 = mock(Object.class);
        field = new Field("testField", Object.class, fieldInstance.getClass());
        field2 = new Field("testField2", Object.class, fieldInstance.getClass());
    }

    // Test cases from Expert 1
    @Test
    public void testSingleMock() {
        List<Object> mocks = Arrays.asList(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        new FieldSetter(fieldInstance, field).set(mock1);

        verify(mock1).equals(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjecting() {
        List<Object> mocks = Arrays.asList(mock1, mock2);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
        } catch (Exception e) {
            throw new MockitoException("Expected exception", e);
        }

        new FieldSetter(fieldInstance, field).set(mock2);
    }

    @Test
    public void testNoMockFound() {
        List<Object> mocks = Collections.emptyList();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // Do nothing, as no mock was found
    }

    // Test cases from Expert 2
    @Test
    public void testSingleMock_Expert2() {
        List<Object> mocks = Arrays.asList(mock1);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        new FieldSetter(fieldInstance, field).set(mock1);

        verify(mock1).equals(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjecting_Expert2() {
        List<Object> mocks = Arrays.asList(mock1, mock2);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        try {
            injecter.thenInject();
        } catch (Exception e) {
            throw new MockitoException("Expected exception", e);
        }

        new FieldSetter(fieldInstance, field).set(mock2);
    }

    @Test
    public void testNoMockFound_Expert2() {
        List<Object> mocks = Collections.emptyList();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        injecter.thenInject();

        // Do nothing, as no mock was found
    }

    // Test cases from Expert 3
    @Test
    public void testSingleMock_Expert3() {
        List<Object> mocks = Arrays.asList(mock3);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field2, fieldInstance);
        injecter.thenInject();

        new FieldSetter(fieldInstance, field2).set(mock3);

        verify(mock3).equals(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testExceptionWhenInjecting_Expert3() {
        List<Object> mocks = Arrays.asList(mock3, mock4);
        OngoingInjecter injecter = filter.filterCandidate(mocks, field2, fieldInstance);

        try {
            injecter.thenInject();
        } catch (Exception e) {
            throw new MockitoException("Expected exception", e);
        }

        new FieldSetter(fieldInstance, field2).set(mock4);
    }

    @Test
    public void testNoMockFound_Expert3() {
        List<Object> mocks = Collections.emptyList();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field2, fieldInstance);

        injecter.thenInject();

        // Do nothing, as no mock was found
    }
}