package org.mockito.internal.configuration.injection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.MockDefinition;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldValueQuery;
import org.mockito.mock.MockitoCore;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = spy(new String("bar"));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        ongoingInjecter.thenInject();
    }

    @Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = spy(new String("bar"));

        doThrow(new RuntimeException("Test exception")).when(fieldInstance).setFoo(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessSuccess() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = spy(new String("bar"));

        doReturn(null).when(fieldInstance).getFoo();
        doAnswer(invocation -> {
            field.setAccessible(true);
            field.set(fieldInstance, invocation.getArgument(0));
            return null;
        }).when(fieldInstance).setFoo(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
        ongoingInjecter.thenInject();
    }

    @Test
    public void testFilterCandidate_singleMock_fieldAccessFailure() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = mock(String.class);

        doAnswer(invocation -> {
            throw new RuntimeException("Test exception");
        }).when(field).setAccessible(any());

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        assertThrows(MockitoException.class, () -> filter.filterCandidate(mocks, field, fieldInstance).thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatchingCandidate() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class), mock(Integer.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = mock(String.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, ongoingInjecter);
        assertFalse(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_matchingCandidate() {
        final Collection<Object> mocks = Arrays.asList(mock(String.class), mock(String.class));
        final Field field = FieldValueQuery.getDeclaredField(String.class, "foo");
        final Object fieldInstance = mocks.get(1);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertInstanceOf(OngoingInjecter.class, ongoingInjecter);
        assertNotNull(ongoingInjecter.thenInject());
    }
}