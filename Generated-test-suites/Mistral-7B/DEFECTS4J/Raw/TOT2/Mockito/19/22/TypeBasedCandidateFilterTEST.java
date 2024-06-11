import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.mockito3.mock.MockComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field field;

    @Before
    public void setUp() {
        filter = new TypeBasedCandidateFilter(null);
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
    }

    @Test
    public void testFilterCandidate_ReturnsExpectedMocksWithGivenFieldType() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);

        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        when(field.getType()).thenReturn(Object.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);
        expectedMocks.add(mock3);

        verify(filter).filterCandidate(expectedMocks, field, fieldInstance);
        assertEquals(expectedMocks, result.getMocks());
    }
}