import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.*;
import org.mockito.Mock;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextFilter;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidate_WithMatchingType_SingleMatch() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        AClass mockInstance = mock(AClass.class);
        mocks.add(mockInstance);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_WithNonMatchingType_NoMatch() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        BClass mockInstance = mock(BClass.class);
        mocks.add(mockInstance);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter, never()).filterCandidate(anyCollection(), any(Field.class), any(Object.class));
        assertNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_WithMultipleMatchingTypes_MultipleMatches() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        AClass mockInstance1 = mock(AClass.class);
        AClass mockInstance2 = mock(AClass.class);
        mocks.add(mockInstance1);
        mocks.add(mockInstance2);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_WithNoMocks_EmptyMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(anyCollection(), eq(field), eq(fieldInstance));
        assertNotNull(ongoingInjecter);
    }

    @Test
    public void testFilterCandidate_WithNullMocks_NullMocks() throws Exception {
        // Arrange
        Collection<Object> mocks = null;

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mockInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextFilter).filterCandidate(isNull(), eq(field), eq(fieldInstance));
        assertNotNull(ongoingInjecter);
    }
}