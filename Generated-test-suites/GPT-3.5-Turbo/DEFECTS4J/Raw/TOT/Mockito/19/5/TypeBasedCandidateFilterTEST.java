import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    private MockCandidateFilter nextMockCandidateFilter;

    @Before
    public void setUp() {
        nextMockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(nextMockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_typicalUseCase() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(1, 2, 3, "mock");
        Field field = getFieldMock(Integer.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(nextMockCandidateFilter).filterCandidate(anyList(), any(Field.class), any(Object.class));
    }

    @Test
    public void testFilterCandidate_noMockTypeMatches() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(1, 2, 3);
        Field field = getFieldMock(String.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(nextMockCandidateFilter).filterCandidate(Collections.emptyList(), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_emptyMocks() {
        // Arrange
        Collection<Object> mocks = Collections.emptyList();
        Field field = getFieldMock(Object.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(nextMockCandidateFilter).filterCandidate(Collections.emptyList(), field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_differentMockTypeMatches() {
        // Arrange
        Collection<Object> mocks = Arrays.asList(1, 2, "mock");
        Field field = getFieldMock(Integer.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        verify(nextMockCandidateFilter).filterCandidate(Collections.singletonList(1), field, fieldInstance);
    }

    private Field getFieldMock(Class<?> fieldType) {
        Field field = mock(Field.class);
        when(field.getType()).thenReturn(fieldType);
        return field;
    }
}