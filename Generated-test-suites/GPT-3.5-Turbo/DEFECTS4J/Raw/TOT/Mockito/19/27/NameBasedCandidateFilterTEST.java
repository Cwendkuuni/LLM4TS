import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter mockFilter;
    private MockCandidateFilter mockNextFilter;
    private Field mockField;
    private Object mockFieldInstance;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockNextFilter = mock(MockCandidateFilter.class);
        mockField = mock(Field.class);
        mockFieldInstance = mock(Object.class);
        mockUtil = mock(MockUtil.class);
        mockFilter = new NameBasedCandidateFilter(mockNextFilter, mockUtil);
    }

    @Test
    public void filterCandidate_withMultipleMocksAndMatchingName_shouldReturnFilteredCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        Object mock4 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        mocks.add(mock4);
        String fieldName = "mockField";
        when(mockField.getName()).thenReturn(fieldName);
        when(mockUtil.getMockName(mock1)).thenReturn(fieldName);

        // Act
        OngoingInjecter result = mockFilter.filterCandidate(mocks, mockField, mockFieldInstance);

        // Assert
        verify(mockNextFilter).filterCandidate(anyList(), any(Field.class), any(Object.class));
    }

    @Test
    public void filterCandidate_withSingleMock_shouldReturnFilteredCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        mocks.add(mock1);

        // Act
        OngoingInjecter result = mockFilter.filterCandidate(mocks, mockField, mockFieldInstance);

        // Assert
        verify(mockNextFilter).filterCandidate(anyList(), any(Field.class), any(Object.class));
    }

    @Test
    public void filterCandidate_withMultipleMocksAndNoMatchingName_shouldReturnFilteredCandidate() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        Object mock4 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);
        mocks.add(mock4);
        String fieldName = "mockField";
        when(mockField.getName()).thenReturn(fieldName);
        when(mockUtil.getMockName(mock1)).thenReturn("differentName");

        // Act
        OngoingInjecter result = mockFilter.filterCandidate(mocks, mockField, mockFieldInstance);

        // Assert
        verify(mockNextFilter).filterCandidate(mocks, mockField, mockFieldInstance);
    }
}