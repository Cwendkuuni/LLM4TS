package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_EmptyMockCollection_NullInputFields() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        assertNull(filter.filterCandidate(new ArrayList<>(), null, null));
    }

    @Test
    public void testFilterCandidate_EmptyMockCollection_NonAssignableType() {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        assertNull(filter.filterCandidate(new ArrayList<>(), Mockito.mock(Field.class), Mockito.mock(Object.class)));
    }

    @Test
    public void testFilterCandidate_SingleMockMatches_ReturnsMatchingMock(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        Object mock = mock(Object.class);
        Object fieldInstance = Mockito.spy(new Object());
        Field field = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return mock.getClass();
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        Mockito.verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksMatch_ReturnsFirstMatchingMock(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object fieldInstance = Mockito.spy(new Object());
        Field field = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return mock1.getClass();
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(result);
        Mockito.verify(mock1).equals(result);
        Mockito.verify(nextMock).filterCandidate(new ArrayList<>(), field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMockCollection_ThrowsException(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        filter.filterCandidate(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField_ThrowsException(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        filter.filterCandidate(new ArrayList<>(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance_ThrowsException(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        filter.filterCandidate(new ArrayList<>(), new Field(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NonAssignableType_ThrowsException(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        Object mock = mock(String.class);
        Object fieldInstance = Mockito.spy(new Object());
        Field field = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MockFilterNext_ReturnsNull(MockCandidateFilter nextMock) {
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(nextMock);
        Object mock = mock(Object.class);
        Object fieldInstance = Mockito.spy(new Object());
        Field field = Mockito.spy(new Field() {
            @Override
            public Class<?> getType() {
                return mock.getClass();
            }
        });
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Mockito.when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

}