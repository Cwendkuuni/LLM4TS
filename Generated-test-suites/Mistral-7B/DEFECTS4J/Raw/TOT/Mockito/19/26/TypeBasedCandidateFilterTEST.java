 package org.mockito.internal.configuration.injection.filter;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private Object mock1;
    private Object mock2;
    private Object mock3;

    @Before
    public void setup() throws Exception {
        filter = new TypeBasedCandidateFilter(null);
        mock1 = mock(String.class);
        mock2 = mock(Number.class);
        mock3 = mock(List.class);
    }

    @Test
    public void testFilterCandidate_ReturnsExpectedMock_WhenTypeMatches() {
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);
        Field field = new Field("testField", Integer.class, null, null);
        Object fieldInstance = new Object();

        List<Object> mockTypeMatches = Arrays.asList(mock1);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mock1).equals(result);
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenNoMockMatches() {
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);
        Field field = new Field("testField", Integer.class, null, null);
        Object fieldInstance = new Object();

        List<Object> mockTypeMatches = new ArrayList<>();

        Object result = filter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_ThrowsException_WhenNullField() {
        Collection<Object> mocks = Arrays.asList(mock1, mock2, mock3);
        Field field = null;
        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, field, fieldInstance);
    }
}
