package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.fieldname.FieldNameUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_whenFieldValueIsAnInstanceOfMockitoAnswer_thenReturnOngoingInjecter() {
        MockCandidateFilter filter = (collection, field, instance) -> new OngoingInjecter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Answer.class));

        Field fieldToBeInjected = Mockito.class.getDeclaredField("answer");
        fieldToBeInjected.setAccessible(true);
        Object fieldInstance = fieldToBeInjected.get(Mockito.class);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(result, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsNotNull_thenReturnNull() {
        MockCandidateFilter filter = MockCandidateFilter.DEFAULT;

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnInstanceOfMockitoInvocationOnMock_thenReturnOngoingInjecter() {
        MockCandidateFilter filter = (collection, field, instance) -> new OngoingInjecter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(InvocationOnMock.class));

        Field fieldToBeInjected = Mockito.class.getDeclaredField("invocationOnMock");
        fieldToBeInjected.setAccessible(true);
        Object fieldInstance = fieldToBeInjected.get(Mockito.class);

        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(result, filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnInstanceOfString_thenReturnNull() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        String fieldInstance = "foo";

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenFieldValueIsAnInstanceOfList_thenReturnNull() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);

        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        List list = new ArrayList<>();

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, list));
    }
}