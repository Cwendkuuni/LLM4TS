package org.mockito.internal.configuration.injection.filter;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.invocation.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_FieldTypeIsAssignableFromMockType_ReturnMock() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(String.class);
        Field field = Mockito.spy(String.class.getDeclaredField("value"));
        field.setAccessible(true);

        Object mockType1 = Mockito.mock(Number.class);
        mocks.add(mockType1);
        Object mockType2 = Mockito.mock(String.class);
        mocks.add(mockType2);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Assert.assertEquals(mockType2, result.getCandidate());
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getInstance());
    }

    @Test
    public void testFilterCandidate_FieldTypeIsNotAssignableFromMockType_ReturnNextFilter() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(String.class);
        Field field = Mockito.spy(int.class.getDeclaredField("value"));
        field.setAccessible(true);

        Object mockType1 = Mockito.mock(Number.class);
        mocks.add(mockType1);
        Object mockType2 = Mockito.mock(String.class);
        mocks.add(mockType2);

        TypeBasedCandidateFilter nextFilter = Mockito.spy(new TypeBasedCandidateFilter(null));

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Assert.assertNotNull(result);
        Assert.assertNull(result.getCandidate());
        Assert.assertSame(nextFilter, result.getFilter());
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsNull_ReturnNull() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(String.class);
        Field field = Mockito.spy(String.class.getDeclaredField("value"));
        field.setAccessible(true);

        Object mockType1 = Mockito.mock(Number.class);
        mocks.add(mockType1);
        Object mockType2 = Mockito.mock(String.class);
        mocks.add(mockType2);

        TypeBasedCandidateFilter nextFilter = Mockito.spy(new TypeBasedCandidateFilter(null));
        Mockito.doReturn(null).when(nextFilter).filterCandidate(mocks, field, fieldInstance);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsMock_ReturnMock() {
        // given
        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = Mockito.mock(String.class);
        Field field = Mockito.spy(String.class.getDeclaredField("value"));
        field.setAccessible(true);

        Object mockType1 = Mockito.mock(Number.class);
        mocks.add(mockType1);
        Object mockType2 = Mockito.mock(String.class);
        mocks.add(mockType2);

        TypeBasedCandidateFilter nextFilter = Mockito.spy(new TypeBasedCandidateFilter(null));
        Object nextMock = Mockito.mock(String.class);
        Mockito.doReturn(nextMock).when(nextFilter).filterCandidate(mocks, field, fieldInstance);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        Assert.assertEquals(nextMock, result.getCandidate());
        Assert.assertEquals(field, result.getField());
        Assert.assertEquals(fieldInstance, result.getInstance());
        Assert.assertSame(nextFilter, result.getFilter());
    }
}