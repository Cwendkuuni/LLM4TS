import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter next;
    private Object mock1;
    private Object mock2;
    private Object mock3;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(next);
        mock1 = mock(Object.class);
        mock2 = mock(String.class);
        mock3 = mock(Integer.class);
    }

    @Test
    public void filterCandidate_WithCompatibleMocks_ReturnsOngoingInjecter() throws Exception {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mock1");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(eq(mocks), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock1);

        assertEquals(ongoingInjecter, result);
        verify(next).filterCandidate(eq(mocks), eq(field), eq(mock1));
    }

    @Test
    public void filterCandidate_WithoutCompatibleMocks_ReturnsOngoingInjecter() throws Exception {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock3);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mock2");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(eq(mocks), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock2);

        assertEquals(ongoingInjecter, result);
        verify(next).filterCandidate(eq(mocks), eq(field), eq(mock2));
    }

    @Test
    public void filterCandidate_EmptyMocksList_ReturnsOngoingInjecter() throws Exception {
        List<Object> mocks = new ArrayList<>();

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mock3");

        OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
        when(next.filterCandidate(eq(mocks), any(Field.class), any(Object.class))).thenReturn(ongoingInjecter);

        OngoingInjecter result = filter.filterCandidate(mocks, field, mock3);

        assertEquals(ongoingInjecter, result);
        verify(next).filterCandidate(eq(mocks), eq(field), eq(mock3));
    }

    @Test
    public void filterCandidate_NoNextFilter_ThrowsNullPointerException() throws Exception {
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = TypeBasedCandidateFilterTest.class.getDeclaredField("mock1");

        assertNull(filter.next);

        try {
            filter.filterCandidate(mocks, field, mock1);
            fail("Expected NullPointerException to be thrown.");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

}