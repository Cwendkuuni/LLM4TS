package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ArrayUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = Mockito.mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = new Field("testField", Object.class, Mockito.class);
        Object fieldInstance = new Object();

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertNull(result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class, Mockito.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = new Field("testField", Object.class, Mockito.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidateWithInaccessibleField() throws IllegalAccessException {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = new Field("privateTestField", Object.class, Mockito.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, Mockito.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldType() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = new Field("testField", null, Mockito.class);
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateReturnsInjectedMock() {
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field fieldToBeInjected = new Field("testField", Object.class, Mockito.class);
        Object fieldInstance = mock(Object.class);
        Object expectedMock = mock(Object.class);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance))
                .thenReturn(expectedMock);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(expectedMock, result);
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

}