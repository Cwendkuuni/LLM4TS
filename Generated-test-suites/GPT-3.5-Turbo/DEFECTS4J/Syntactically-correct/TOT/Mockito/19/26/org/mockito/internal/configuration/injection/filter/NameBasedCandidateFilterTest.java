
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void filterCandidate_shouldFilterByNameAndDelegateToNext() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        when(next.filterCandidate(mock1, field, fieldInstance)).thenReturn(mock1);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        assertEquals(mock1, result);
    }

    @Test
    public void filterCandidate_shouldNotFilterIfOnlyOneMock() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;
        mocks.add(mock);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(result);
    }

    @Test
    public void filterCandidate_shouldFilterByNameIfMultipleMocks() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        when(next.filterCandidate(mock1, field, fieldInstance)).thenReturn(mock1);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        assertEquals(mock1, result);
    }

    @Test
    public void filterCandidate_shouldNotFilterByNameIfNoMatch() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = null;
        mocks.add(mock1);
        mocks.add(mock2);

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(result);
    }
}

