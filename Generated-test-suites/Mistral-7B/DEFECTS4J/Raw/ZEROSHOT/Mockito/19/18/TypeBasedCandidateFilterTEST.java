package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.ExtendedArrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidateWithCompatibleMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = ExtendedArrays.asList(mock(Object.class));

        Object expectedFilteredMock = mock(Object.class);
        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(expectedFilteredMock);

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(filteredMock);
        verify(nextMock).filterCandidate(argThat(mocks -> mocks.contains(expectedFilteredMock)), field, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithIncompatibleMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Collection<Object> mocks = ExtendedArrays.asList(mock(String.class), mock(Object.class));

        Object incompatibleMock = mock(String.class);

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(incompatibleMock);

        List<Object> mockTypeMatches = new ArrayList<>();
        for (Object mock : mocks) {
            if (field.getType().isAssignableFrom(mock.getClass())) {
                mockTypeMatches.add(mock);
            }
        }

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(filteredMock);
        verifyZeroInteractions(nextMock);
        assertThat(mockTypeMatches).containsExactly(filteredMock);
    }

    @Test
    public void testFilterCandidateWithEmptyMocksCollection() {
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        Collection<Object> mocks = ExtendedArrays.emptyList();

        Object filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(filteredMock);
        verifyZeroInteractions(nextMock);
    }
}