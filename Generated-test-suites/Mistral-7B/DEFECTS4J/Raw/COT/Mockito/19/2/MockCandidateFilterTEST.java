package org.mockito.internal.configuration.injection.filter.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MockCandidateFilterTest {

    private MockCandidateFilter filter;

    @SuppressWarnings("unchecked")
    private <T> T mockObject() {
        return mock(T.class);
    }

    @Test
    public void testFilterCandidate_nullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_nullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = mockObject();
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_nullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = null;
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_fieldAlreadyInjected() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        Object existingInjected = mockObject();
        when(fieldInstance.getClass()).thenReturn(Object.class);
        when(fieldToBeInjected.get(this)).thenReturn(existingInjected);
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(filter, never()).inject(fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_newInjectedObject() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        Object newInjected = mockObject();
        when(fieldInstance.getClass()).thenReturn(Object.class);
        when(fieldToBeInjected.get(this)).thenReturn(null);
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(newInjected, fieldToBeInjected.get(this));
        verify(filter).inject(fieldToBeInjected, fieldInstance);
        verifyNoMoreInteractions(filter);
    }

    @Test
    public void testFilterCandidate_existingMockInCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Object existingMock = mockObject();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        mocks.add(existingMock);
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(existingMock, fieldToBeInjected.get(this));
        verify(filter).inject(fieldToBeInjected, existingMock);
    }

    @Test
    public void testFilterCandidate_newMockInCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Object newMock = mockObject();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(newMock, fieldToBeInjected.get(this));
        verify(filter).inject(fieldToBeInjected, newMock.getClass().getConstructor().newInstance());
        mocks.add(newMock);
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidate_illegalAccessException() throws NoSuchFieldException, IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        when(fieldInstance.getClass()).thenThrow(new IllegalAccessException());
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = InstantiationException.class)
    public void testFilterCandidate_instantiationException() throws NoSuchFieldException, IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", Object.class, this);
        Object fieldInstance = mockObject();
        when(fieldInstance.getClass()).thenThrow(new InstantiationException());
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class, Strictness.STRICT));
        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

}