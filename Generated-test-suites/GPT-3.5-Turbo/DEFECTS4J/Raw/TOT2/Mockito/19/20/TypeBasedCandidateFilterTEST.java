import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);

        Collection<Object> mocks = new ArrayList<>();
        mocks.add("mock1");
        mocks.add("mock2");
        mocks.add("mock3");

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter ongoingInjecter = typeBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockCandidateFilter.filterCandidate(anyCollection(), any(Field.class), any(Object.class)), ongoingInjecter);
    }

}