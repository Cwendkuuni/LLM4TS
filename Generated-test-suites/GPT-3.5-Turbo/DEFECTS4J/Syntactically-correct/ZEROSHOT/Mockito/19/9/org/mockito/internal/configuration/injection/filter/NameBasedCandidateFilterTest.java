import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_SingleMockWithNameMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        when(next.filterCandidate(anyCollection(), any(Field.class), anyObject())).thenReturn(mock);

        mocks.add(mock);
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.mock);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_SingleMockWithoutNameMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        when(next.filterCandidate(anyCollection(), any(Field.class), anyObject())).thenReturn(mock);

        mocks.add(mock);
        Field field = getField("differentName");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.mock);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithNameMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(next.filterCandidate(anyCollection(), any(Field.class), anyObject())).thenReturn(mock1);

        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result.mock);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_MultipleMocksWithoutNameMatch() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(next.filterCandidate(anyCollection(), any(Field.class), anyObject())).thenReturn(mock1);

        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("differentName");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result.mock);
        verify(next).filterCandidate(mocks, field, fieldInstance);
    }

    private Field getField(String fieldName) throws NoSuchFieldException {
        return NameBasedCandidateFilter.class.getDeclaredField(fieldName);
    }
}