import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class NameBasedCandidateFilterTest {

    private final MockUtil mockUtil = new MockUtil();

    @Test
    public void testFilterCandidate_singleMock() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next, times(1)).filterCandidate(mocks, field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertEquals(result, next.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks() throws NoSuchFieldException {
        // Arrange
        MockCandidateFilter next = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = Object.class.getDeclaredField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        List<Object> mockNameMatches = new ArrayList<>();
        when(mockUtil.getMockName(mock1)).thenReturn("field");
        when(mockUtil.getMockName(mock2)).thenReturn("otherField");
        mockNameMatches.add(mock1);
        verify(next, times(1)).filterCandidate(mockNameMatches, field, fieldInstance);
        verifyNoMoreInteractions(next);
        assertEquals(result, next.filterCandidate(mockNameMatches, field, fieldInstance));
    }
}