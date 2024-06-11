import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        next = mock(MockCandidateFilter.class);
    }

    @Test
    public void filterCandidate_WhenNoMatchingMocks_ShouldReturnNextFilterCandidate() {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = getFieldMock();
        Object fieldInstance = getFieldInstanceMock();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void filterCandidate_WhenOneMatchingMock_ShouldReturnNextFilterCandidate() {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        mocks.add(getMatchingMock());
        Field field = getFieldMock();
        Object fieldInstance = getFieldInstanceMock();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_WhenMultipleMocksWithMatchingNames_ShouldReturnNextFilterCandidateWithMatchingMocks() {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        mocks.add(getMatchingMock());
        mocks.add(getMatchingMock());
        mocks.add(getNonMatchingMock());
        Field field = getFieldMock();
        Object fieldInstance = getFieldInstanceMock();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(eq(mocks.subList(0, 2)), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_WhenMultipleMocksWithNoMatchingNames_ShouldReturnNextFilterCandidateWithAllMocks() {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        mocks.add(getNonMatchingMock());
        mocks.add(getNonMatchingMock());
        Field field = getFieldMock();
        Object fieldInstance = getFieldInstanceMock();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_WhenNoMocks_ShouldReturnNextFilterCandidate() {
        // Arrange
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        List<Object> mocks = new ArrayList<>();
        Field field = getFieldMock();
        Object fieldInstance = getFieldInstanceMock();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
    }

    private Object getMatchingMock() {
        Object mock = mock(Object.class);
        when(mockUtil.getMockName(mock)).thenReturn(getFieldMock().getName());
        return mock;
    }

    private Object getNonMatchingMock() {
        Object mock = mock(Object.class);
        when(mockUtil.getMockName(mock)).thenReturn("nonMatching");
        return mock;
    }

    private Field getFieldMock() {
        return mock(Field.class);
    }

    private Object getFieldInstanceMock() {
        return mock(Object.class);
    }
}