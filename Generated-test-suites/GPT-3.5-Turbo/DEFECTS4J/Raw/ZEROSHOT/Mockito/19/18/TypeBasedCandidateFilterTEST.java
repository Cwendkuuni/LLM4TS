import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter typeBasedCandidateFilter;
    private MockCandidateFilter nextMockCandidateFilter;

    @Before
    public void setUp() {
        nextMockCandidateFilter = mock(MockCandidateFilter.class);
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(nextMockCandidateFilter);
    }

    @Test
    public void testFilterCandidate() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = SomeClass.class.getDeclaredField("someField");
        Object fieldInstance = new Object();
        List<Object> mockTypeMatches = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mockTypeMatches.add(mock1);
        mockTypeMatches.add(mock2);
        mockTypeMatches.add(mock3);

        // Act
        when(field.getType().isAssignableFrom(mock1.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock2.getClass())).thenReturn(true);
        when(field.getType().isAssignableFrom(mock3.getClass())).thenReturn(false);

        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        verify(nextMockCandidateFilter).filterCandidate(mockTypeMatches, field, fieldInstance);
        assertThat(result, is(notNullValue()));
    }

    public class SomeClass {
        private Object someField;
    }
}