import org.junit.*;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_OneMockMatch() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object mock1 = mock(Object.class);
        mocks.add(mock1);
        when(field.getName()).thenReturn("fieldName");
        when(mockUtil.getMockName(mock1)).thenReturn(mock(mockClassName.class));

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        verify(next, never()).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMockMatches() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        when(field.getName()).thenReturn("fieldName");
        when(mockUtil.getMockName(mock1)).thenReturn(mock(mockClassName.class));
        when(mockUtil.getMockName(mock2)).thenReturn(mock(mockClassName.class));

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
        verify(next, never()).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_NoMockMatch() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        Object mock1 = mock(Object.class);
        mocks.add(mock1);
        when(field.getName()).thenReturn("fieldName");
        when(mockUtil.getMockName(mock1)).thenReturn(mock(mockClassName.class));

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_EmptyMocks() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);
        when(field.getName()).thenReturn("fieldName");

        // Act
        OngoingInjecter result = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }
}