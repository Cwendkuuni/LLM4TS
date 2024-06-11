import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Field testField;

    @Before
    public void setUp() {
        nextMock = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        testField = spy(new Field("testField", String.class, Object.class));
    }

    @Test
    public void testFilterCandidate_ReturnsExpectedMock() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        List<Object> expectedMockTypeMatches = Arrays.asList(mock1);

        Object fieldInstance = "testInstance";

        Object actualMock = filter.filterCandidate(mocks, testField, fieldInstance);

        assertEquals(expectedMockTypeMatches.get(0), actualMock);
    }

    @Test
    public void testFilterCandidate_ReturnsNull_WhenNoMockMatches() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(Integer.class);
        Collection<Object> mocks = Arrays.asList(mock1, mock2);

        Object fieldInstance = "testInstance";

        Object actualMock = filter.filterCandidate(mocks, testField, fieldInstance);

        assertNull(actualMock);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_ThrowsException_WhenFieldIsNull() {
        Object mock1 = mock(String.class);
        Collection<Object> mocks = Arrays.asList(mock1);

        Object fieldInstance = "testInstance";

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_ThrowsException_WhenFieldInstanceIsNull() {
        Object mock1 = mock(String.class);
        Collection<Object> mocks = Arrays.asList(mock1);

        Object actualMock = filter.filterCandidate(mocks, testField, null);

        assertNull(actualMock);
    }
}