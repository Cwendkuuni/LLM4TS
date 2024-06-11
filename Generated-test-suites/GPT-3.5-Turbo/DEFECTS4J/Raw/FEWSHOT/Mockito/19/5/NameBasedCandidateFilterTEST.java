package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {
    private MockCandidateFilter nextFilter;
    private NameBasedCandidateFilter candidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        nextFilter = mock(MockCandidateFilter.class);
        candidateFilter = new NameBasedCandidateFilter(nextFilter);
        mockUtil = spy(MockUtil.class);
        candidateFilter.setMockUtil(mockUtil);
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocksAndMatchFound_ReturnsNextFilterResult() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock1)).thenReturn("fieldName");
        when(mockUtil.getMockName(mock2)).thenReturn("fieldName");

        when(nextFilter.filterCandidate(any(), any(), any())).thenReturn(new OngoingInjecterImpl());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextFilter.filterCandidate(any(), any(), any()));
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocksAndMatchNotFound_ReturnsNextFilterResult() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock1)).thenReturn("fieldName1");
        when(mockUtil.getMockName(mock2)).thenReturn("fieldName2");

        when(nextFilter.filterCandidate(any(), any(), any())).thenReturn(new OngoingInjecterImpl());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextFilter.filterCandidate(any(), any(), any()));
    }

    @Test
    public void testFilterCandidate_WhenSingleMock_ReturnsNextFilterResult() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        when(mockUtil.getMockName(mock)).thenReturn("fieldName");

        when(nextFilter.filterCandidate(any(), any(), any())).thenReturn(new OngoingInjecterImpl());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextFilter.filterCandidate(any(), any(), any()));
    }

    @Test
    public void testFilterCandidate_WhenNoMocks_ReturnsNextFilterResult() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();

        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        when(nextFilter.filterCandidate(any(), any(), any())).thenReturn(new OngoingInjecterImpl());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertSame(result, nextFilter.filterCandidate(any(), any(), any()));
    }

    @Test
    public void testFilterCandidate_WhenNullMocks_ReturnsNextFilterResult() throws NoSuchFieldException {
        // Arrange
        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        when(nextFilter.filterCandidate(any(), any(), any())).thenReturn(new OngoingInjecterImpl());

        // Act
        OngoingInjecter result = candidateFilter.filterCandidate(null, field, fieldInstance);

        // Assert
        assertSame(result, nextFilter.filterCandidate(any(), any(), any()));
    }
}

class OngoingInjecterImpl implements OngoingInjecter {
    @Override
    public void set(Object value) {

    }

    @Override
    public void inject() {

    }
}