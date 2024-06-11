package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setup() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidateWithSubtypeMocks() {
        // Given
        Field field = new Field("testField", Integer.class, null);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Number.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertEquals(mocks.get(0), result.getMock());
    }

    @Test
    public void testFilterCandidateWithNonMatchingMocks() {
        // Given
        Field field = new Field("testField", Integer.class, null);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(String.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMock());
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithEmptyMockList() {
        // Given
        Field field = new Field("testField", Integer.class, null);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertNotNull(result.getMock());
        assertEquals(nextMock, result.getFilter());
    }

    @Test
    public void testFilterCandidateWithSingleMatchingMock() {
        // Given
        Field field = new Field("testField", Integer.class, null);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertEquals(mocks.get(0), result.getMock());
    }

    @Test
    public void testFilterCandidateWithMultipleMatchingMocks() {
        // Given
        Field field = new Field("testField", Integer.class, null);
        Object fieldInstance = spy(new Object() {});
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Integer.class));
        mocks.add(mock(Integer.class));

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
        assertEquals(mocks.get(0), result.getMock());
    }
}