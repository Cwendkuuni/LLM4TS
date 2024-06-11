import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Field;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        mocks = Arrays.asList(mock(Object.class), mock(Object.class), mock(Object.class));
        field = mock(Field.class);
        fieldInstance = mock(Object.class);
        filter = new TypeBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_ShouldFilterMocksBasedOnType() {
        // Given

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertEquals(mocks.size(), filter.next.filterCandidate(filter.mockTypeMatches(mocks, field), field, fieldInstance).size());
    }
}