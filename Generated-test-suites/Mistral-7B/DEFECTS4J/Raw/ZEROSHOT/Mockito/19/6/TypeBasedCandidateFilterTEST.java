package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.CollectionUtils;
import org.mockito.internal.util.reflection.FieldAccessible;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
        mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        mocks.add(mock(List.class));

        field = new FieldAccessible().getOnlyPrivateField(TypeBasedCandidateFilter.class, "next");
        field.setAccessible(true);
        field.set(filter, nextFilter);

        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsAssignableFromMockType_ReturnNextFilter() {
        // given
        Field mockField = new FieldAccessible().getPrivateField(Object.class, "field");
        when(nextFilter.filterCandidate(CollectionUtils.newArrayList(mocks.get(0)), mockField, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verify(nextFilter).filterCandidate(CollectionUtils.newArrayList(mocks.get(0)), field, fieldInstance);
        assertSame(result, mock(OngoingInjecter.class));
    }

    @Test
    public void testFilterCandidate_WhenFieldTypeIsNotAssignableFromMockType_ReturnEmptyCollection() {
        // given
        Field mockField = new FieldAccessible().getPrivateField(Object.class, "field");

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertTrue(result == null || CollectionUtils.isEmpty(result.getMocks()));
    }

    @Test
    public void testFilterCandidate_WhenNextFilterReturnsEmptyCollection_ReturnEmptyCollection() {
        // given
        Field mockField = new FieldAccessible().getPrivateField(Object.class, "field");
        when(nextFilter.filterCandidate(CollectionUtils.newArrayList(mocks.get(1)), mockField, fieldInstance)).thenReturn(mock(OngoingInjecter.class, "nextFilterResult"));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        assertSame(result, mock(OngoingInjecter.class, "nextFilterResult"));
        assertTrue(CollectionUtils.isEmpty(result.getMocks()));
    }
}