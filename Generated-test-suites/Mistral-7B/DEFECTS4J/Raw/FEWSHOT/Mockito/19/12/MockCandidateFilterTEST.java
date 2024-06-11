package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.mockito.internal.configuration.injection.MockInjector;
import org.mockito.internal.invocation.ReflectUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate_nullCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(null, new Field("fieldName", String.class, null, null), spy(Object.class)));
    }

    @Test
    public void testFilterCandidate_emptyCollection() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        assertNull(filter.filterCandidate(mocks, new Field("fieldName", String.class, null, null), spy(Object.class)));
    }

    @Test
    public void testFilterCandidate_nullField() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(new ArrayList<Object>() {{ add(mock(Object.class)); }}, null, spy(Object.class)));
    }

    @Test
    public void testFilterCandidate_nullFieldInstance() {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        assertNull(filter.filterCandidate(new ArrayList<Object>() {{ add(mock(Object.class)); }}, new Field("fieldName", String.class, null, null), null));
    }

    @Test
    public void testFilterCandidate_returnsInjectedMock() throws IllegalAccessException {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<Object>() {{ add(mock(Object.class)); add(mock(Object.class)); }};
        Field fieldToBeInjected = new Field("fieldName", String.class, null, null);
        Object fieldInstance = mock(String.class);

        ReflectUtils.makeAccessible(fieldToBeInjected);
        Object injectedMock = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        MockInjector mockInjector = mock(MockInjector.class);
        assert(injectedMock == mockInjector.createMock(fieldToBeInjected.getType()));
    }

    @Test
    public void testFilterCandidate_returnsFieldInstance_whenMockCannotBeCreated() throws IllegalAccessException {
        MockCandidateFilter filter = mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<Object>() {{ add(mock(Object.class)); add(mock(Object.class)); }};
        Field fieldToBeInjected = new Field("fieldName", String.class, null, null);
        Object fieldInstance = "someValue";

        ReflectUtils.makeAccessible(fieldToBeInjected);
        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assert(result == fieldInstance);
    }

}