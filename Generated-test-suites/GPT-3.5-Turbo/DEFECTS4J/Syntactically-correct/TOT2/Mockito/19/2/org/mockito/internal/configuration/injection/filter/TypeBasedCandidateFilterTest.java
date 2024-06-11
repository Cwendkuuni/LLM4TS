import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    MockCandidateFilter mockCandidateFilter;
    TypeBasedCandidateFilter typeBasedCandidateFilter;

    @Test
    public void testFilterCandidate() {
        // Given
        mockCandidateFilter = new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
                return new OngoingInjecter();
            }
        };
        typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = null;
        Object fieldInstance = null;

        // When
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertNotNull(result);
    }
}