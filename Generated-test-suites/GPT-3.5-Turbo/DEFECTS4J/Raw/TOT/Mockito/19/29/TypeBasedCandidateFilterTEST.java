import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
    }

    @Test
    public void filterCandidate_singleMockOfType_properFiltering() throws NoSuchFieldException {
        // Arrange
        Object mock = mock(Object.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = TypeBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_multipleMocksMatched_properFiltering() throws NoSuchFieldException {
        // Arrange
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = TypeBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_noMocksOfType_emptyFiltering() throws NoSuchFieldException {
        // Arrange
        Object mock1 = mock(Integer.class);
        Object mock2 = mock(String.class);
        Object mock3 = mock(Double.class);
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        Field field = TypeBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMock).filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance));
    }

    @Test
    public void filterCandidate_emptyMocks_emptyFiltering() throws NoSuchFieldException {
        // Arrange
        List<Object> mocks = new ArrayList<>();
        Field field = TypeBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();

        // Act
        filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMock).filterCandidate(eq(new ArrayList<>()), eq(field), eq(fieldInstance));
    }
}