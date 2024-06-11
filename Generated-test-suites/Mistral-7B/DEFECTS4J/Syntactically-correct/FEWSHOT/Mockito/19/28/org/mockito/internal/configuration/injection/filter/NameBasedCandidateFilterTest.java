package org.mockito.internal.configuration.injection.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Stubber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_emptyMocks() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_noNameMatch() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(field.getName()).thenReturn("not_matching_name");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate_singleNameMatch() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class, "mockName"));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(field.getName()).thenReturn("mockName");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Stubber stubber = mock(Stubber.class);
        when(mocks.get(0).getClass()).thenReturn(stubber.answerStub(Object.class));
        MethodInterceptor interceptor = stubber.createMock(MethodInterceptor.class);
        when(mocks.get(0).getClass().getMethod("getName", new Class[0])).thenReturn(interceptor);
        when(interceptor.invoke(mock(Object.class), new Object[0])).thenReturn("mockName");

        Assert.assertEquals(mocks.get(0), result.getCandidate());
    }

    @Test
    public void testFilterCandidate_multipleNameMatches() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class, "mockName1"));
        mocks.add(mock(Object.class, "mockName2"));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(field.getName()).thenReturn("mockName1");

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mocks.get(0), result.getCandidate());
    }

    @Test
    public void testFilterCandidate_conflictingNameMatches() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class, "mockName1"));
        mocks.add(mock(Object.class, "mockName2"));
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(field.getName()).thenReturn("mockName1");

        NameBasedCandidateFilter nextFilter = spy(new NameBasedCandidateFilter(null));
        OngoingInjecter nextResult = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNull(nextResult);

        when(nextFilter.filterCandidate(new ArrayList<Object>(), field, fieldInstance)).thenReturn(mock(OngoingInjecter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mocks.get(1), result.getCandidate());
    }
}