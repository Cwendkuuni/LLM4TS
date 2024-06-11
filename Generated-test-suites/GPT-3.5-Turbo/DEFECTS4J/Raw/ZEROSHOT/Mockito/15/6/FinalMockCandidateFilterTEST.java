import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        // Arrange
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        boolean result = ongoingInjecter.thenInject();

        // Assert
        verify(field, times(1)).getName();
        verify(fieldInstance, times(1)).getClass();
        verify(field, times(1)).set(fieldInstance, matchingMock);
        assertTrue(result);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        // Arrange
        mocks.add(new Object());
        mocks.add(new Object());

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        boolean result = ongoingInjecter.thenInject();

        // Assert
        assertFalse(result);
    }
}