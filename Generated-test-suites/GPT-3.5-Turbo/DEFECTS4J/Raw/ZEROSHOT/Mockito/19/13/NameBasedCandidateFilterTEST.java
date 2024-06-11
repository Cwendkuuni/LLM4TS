import org.junit.*;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockCandidateFilter mockNext;

    @Before
    public void setUp() {
        mockNext = Mockito.mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(mockNext);
    }

    @Test
    public void testFilterCandidate_singleMockNoMatch() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(MockCandidateFilter.class));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockNext).filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mockNext, result);
    }

    @Test
    public void testFilterCandidate_singleMockNameMatch() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = new MockUtil();
        Mockito.when(mockUtil.getMockName(mock).toString()).thenReturn("field");
        mocks.add(mock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        Mockito.verify(mockNext).filterCandidate(mocks, field, fieldInstance);
        Assert.assertEquals(mockNext, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksNoMatch() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(MockCandidateFilter.class);
        Object mock2 = Mockito.mock(MockCandidateFilter.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);
        Mockito.verify(mockNext).filterCandidate(expectedMocks, field, fieldInstance);
        Assert.assertEquals(mockNext, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksNameMatch() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(MockCandidateFilter.class);
        Object mock2 = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = new MockUtil();
        Mockito.when(mockUtil.getMockName(mock1).toString()).thenReturn("field");
        Mockito.when(mockUtil.getMockName(mock2).toString()).thenReturn("field");
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);
        Mockito.verify(mockNext).filterCandidate(expectedMocks, field, fieldInstance);
        Assert.assertEquals(mockNext, result);
    }

    @Test
    public void testFilterCandidate_multipleMocksNameMatchWithOtherMatches() throws Exception {
        Field field = TestClass.class.getDeclaredField("field");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(MockCandidateFilter.class);
        Object mock2 = Mockito.mock(MockCandidateFilter.class);
        Object mock3 = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = new MockUtil();
        Mockito.when(mockUtil.getMockName(mock1).toString()).thenReturn("field");
        Mockito.when(mockUtil.getMockName(mock2).toString()).thenReturn("field");
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        List<Object> expectedMocks = new ArrayList<>();
        expectedMocks.add(mock1);
        expectedMocks.add(mock2);
        Mockito.verify(mockNext).filterCandidate(expectedMocks, field, fieldInstance);
        Assert.assertEquals(mockNext, result);
    }
    
    private class TestClass {
        private Object field;
    }
}