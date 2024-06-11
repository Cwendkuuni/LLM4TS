
package org.mockito.internal.configuration.injection.filter;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private static final String TEST_FIELD_NAME = "testField";
    private MockCandidateFilter nextMock;
    private TypeBasedCandidateFilter filter;
    private Field field;
    private Object fieldInstance;
    private Collection<Object> mocks;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        field = new Field(TEST_FIELD_NAME, String.class, null, null);
        fieldInstance = "fieldInstance";
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_EmptyMocksCollection() {
        // Given

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_NextFilterReturnsNull() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNull(result);
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertSame(mock1, result.getMock());
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_NextFilterThrowsException() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenThrow(new RuntimeException("Test Exception"));

        // When & Then
        expectException(RuntimeException.class);
        filter.filterCandidate(mocks, field, fieldInstance);

        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MockTypeMatches_NextFilterReturnsResult() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Answer<OngoingInjecter> answer = new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return new OngoingInjecter(mock2);
            }
        };

        when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(answer);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertSame(mock2, result.getMock());
        verify(nextMock).filterCandidate(mocks, field, fieldInstance);
    }
}