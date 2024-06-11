import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void filterCandidate_shouldFilterBasedOnType_WhenCandidateTypesMatch() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Mock1");
        mocks.add("Mock2");
        mocks.add("Mock3");

        Field field = this.getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals("Mock1", result.getMockTypeMatches().get(0));
        assertEquals("Mock2", result.getMockTypeMatches().get(1));
        assertEquals("Mock3", result.getMockTypeMatches().get(2));

        verify(next).filterCandidate(result.getMockTypeMatches(), field, fieldInstance);
    }

    @Test
    public void filterCandidate_shouldNotFilterBasedOnType_WhenCandidateTypesDoNotMatch() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("Mock1");
        mocks.add(123);
        mocks.add(true);

        Field field = this.getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(result.getMockTypeMatches().isEmpty());

        verify(next).filterCandidate(result.getMockTypeMatches(), field, fieldInstance);
    }

    @Test
    public void filterCandidate_shouldCallNextFilterWithEmptyCollection_WhenMocksEmpty() throws NoSuchFieldException {
        // Arrange
        Collection<Object> mocks = Collections.emptyList();

        Field field = this.getField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertEquals(Collections.emptyList(), result.getMockTypeMatches());

        verify(next).filterCandidate(result.getMockTypeMatches(), field, fieldInstance);
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        return TypeBasedCandidateFilter.class.getDeclaredField(fieldName);
    }
}