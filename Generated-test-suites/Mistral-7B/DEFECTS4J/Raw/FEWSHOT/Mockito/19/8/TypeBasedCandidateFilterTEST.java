package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.collections.CollectionUtils.newArrayList;
import static org.mockito.internal.util.reflection.FieldUtils.getFieldValue;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TypeBasedCandidateFilterTest {

    private static final Class<?> MOCK_CLASS = Mockito.class;
    private static final Class<?> OTHER_CLASS = String.class;

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() throws Exception {
        nextMock = Mockito.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_WithMocksOfMatchingType_ShouldReturnFilteredMock() throws Exception {
        // Given
        List<Object> mocks = newArrayList(Mockito.mock(MOCK_CLASS), Mockito.mock(OTHER_CLASS));
        Field field = Mockito.fieldFilesDescedants(MOCK_CLASS, "field").get(0);
        Object fieldInstance = getFieldValue(Mockito.mock(MOCK_CLASS), field);

        // When
        OngoingInjecter filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(MOCK_CLASS.getName(), filteredMock.getMock().getClass().getName());
    }

    @Test
    public void testFilterCandidate_WithMocksOfNonMatchingType_ShouldDelegateToNextFilter() throws Exception {
        // Given
        List<Object> mocks = newArrayList(Mockito.mock(MOCK_CLASS), Mockito.mock(OTHER_CLASS));
        Field field = Mockito.fieldFilesDescedants(MOCK_CLASS, "field").get(0);
        Object fieldInstance = getFieldValue(Mockito.mock(MOCK_CLASS), field);

        // When
        Mockito.when(nextMock.filterCandidate(Mockito.anyList(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenReturn(Mockito.mock(OngoingInjecter.class));

        OngoingInjecter filteredMock = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        Mockito.verify(nextMock, Mockito.times(1)).filterCandidate(Mockito.anyList(), Mockito.eq(field), Mockito.eq(fieldInstance));
        assertEquals(OTHER_CLASS.getName(), filteredMock.getMock().getClass().getName());
    }

    @Test
    public void testFilterCandidate_WithNullMocks_ShouldPassThrough() throws Exception {
        // Given
        Field field = Mockito.fieldFilesDescedants(MOCK_CLASS, "field").get(0);
        Object fieldInstance = getFieldValue(Mockito.mock(MOCK_CLASS), field);

        // When
        OngoingInjecter filteredMock = filter.filterCandidate(null, field, fieldInstance);

        // Then
        assertNull(filteredMock);
    }

    @Test
    public void testFilterCandidate_WithEmptyMocks_ShouldPassThrough() throws Exception {
        // Given
        Field field = Mockito.fieldFilesDescedants(MOCK_CLASS, "field").get(0);
        Object fieldInstance = getFieldValue(Mockito.mock(MOCK_CLASS), field);

        // When
        OngoingInjecter filteredMock = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);

        // Then
        assertNull(filteredMock);
    }
}