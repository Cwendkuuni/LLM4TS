import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_typeMatches() throws NoSuchFieldException {
        // Setup
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        MyMockClass mock1 = mock(MyMockClass.class);
        mocks.add(mock1);
        MyMockClass mock2 = mock(MyMockClass.class);
        mocks.add(mock2);
        Field field = MyTestClass.class.getField("field");

        // Execute
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, null);

        // Verify
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(null));
        assertEquals(2, result.mocks.size());
        assertTrue(result.mocks.contains(mock1));
        assertTrue(result.mocks.contains(mock2));
        assertEquals(field, result.field);
        assertNull(result.fieldInstance);
    }

    @Test
    public void testFilterCandidate_typeDoesNotMatch() throws NoSuchFieldException {
        // Setup
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        MyMockClass mock1 = mock(MyMockClass.class);
        mocks.add(mock1);
        Field field = MyTestClass.class.getField("field");

        // Execute
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, null);

        // Verify
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(null));
        assertEquals(0, result.mocks.size());
        assertEquals(field, result.field);
        assertNull(result.fieldInstance);
    }

    @Test
    public void testFilterCandidate_emptyMocksList() throws NoSuchFieldException {
        // Setup
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        TypeBasedCandidateFilter typeBasedCandidateFilter = new TypeBasedCandidateFilter(mockCandidateFilter);
        Collection<Object> mocks = new ArrayList<Object>();
        Field field = MyTestClass.class.getField("field");

        // Execute
        OngoingInjecter result = typeBasedCandidateFilter.filterCandidate(mocks, field, null);

        // Verify
        verify(mockCandidateFilter).filterCandidate(anyCollection(), eq(field), eq(null));
        assertEquals(0, result.mocks.size());
        assertEquals(field, result.field);
        assertNull(result.fieldInstance);
    }
}

class MyMockClass {}
class MyTestClass { public MyMockClass field; }