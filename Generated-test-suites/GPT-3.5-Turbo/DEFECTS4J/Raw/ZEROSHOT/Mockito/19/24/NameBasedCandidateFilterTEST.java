import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter mockCandidateFilter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(mockCandidateFilter);
        mockUtil = Mockito.mock(MockUtil.class);
        Mockito.when(mockUtil.getMockName(Mockito.any())).thenReturn("mock");
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoNameMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, null);
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NameMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.getName()).thenReturn("mock");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMockNameMatches = new ArrayList<>();
        expectedMockNameMatches.add(mock1);
        expectedMockNameMatches.add(mock2);
        Mockito.verify(mockCandidateFilter).filterCandidate(expectedMockNameMatches, field, fieldInstance);
        assertEquals(result, null);
    }

    @Test
    public void testFilterCandidate_SingleMock_NoNameMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockCandidateFilter).filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, null);
    }

    @Test
    public void testFilterCandidate_SingleMock_NameMatch() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        mocks.add(mock1);
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.getName()).thenReturn("mock");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMockNameMatches = new ArrayList<>();
        expectedMockNameMatches.add(mock1);
        Mockito.verify(mockCandidateFilter).filterCandidate(expectedMockNameMatches, field, fieldInstance);
        assertEquals(result, null);
    }

}