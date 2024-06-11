import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void filterCandidate_singleMock_successfulInjection() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        verify(mock, times(1)).set(fieldInstance);
        verifyZeroInteractions(fieldInstance);
        assertEquals(mock, result);
    }

    @Test
    public void filterCandidate_singleMock_unsuccessfulInjection() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        doReturn(false).when(mock).set(any());
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        verify(mock).set(fieldInstance);
        verify(fieldInstance, never()).set(any());
        assertEquals(mock, result);
    }

    @Test
    public void filterCandidate_multipleMocks_noInjection() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        verifyZeroInteractions(mock1, mock2, fieldInstance);
        assertNull(result);
    }
}