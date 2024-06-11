import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        this.mockUtil = Mockito.spy(new MockUtil());
        this.filter = new NameBasedCandidateFilter(new StubFilter()) {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks,
                                                  Field field, Object fieldInstance) {
                return super.filterCandidate(mocks, field, fieldInstance);
            }
        };
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidate_oneMock_matchingName() {
        // given
        String mockName = "mockName";
        Object mock = mock(Object.class, MockUtil.MATCH_ALL);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mockName;
            }
        }).when(mockUtil).getMockName(any(Object.class));

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, null, null);

        // then
        assertEquals(mock, result.getMock());
    }

    @Test
    public void testFilterCandidate_oneMock_differentName() {
        // given
        String mockName1 = "mockName1";
        Object mock1 = mock(Object.class, MockUtil.MATCH_ALL);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mockName1;
            }
        }).when(mockUtil).getMockName(eq(mock1));

        String mockName2 = "mockName2";
        Object mock2 = mock(Object.class, MockUtil.MATCH_ALL);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mockName2;
            }
        }).when(mockUtil).getMockName(eq(mock2));

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, null, null);

        // then
        assertEquals(mock1, result.getMock());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_noMatchingMocks() {
        // given
        Collection<Object> mocks = new ArrayList<>();

        // when
        filter.filterCandidate(mocks, null, null);

        // then
        throw new AssertionError("Test expected an exception. No exception thrown.");
    }

    @Test
    public void testFilterCandidate_conflictingNames() {
        // given
        String mockName1 = "mockName1";
        Object mock1 = mock(Object.class, MockUtil.MATCH_ALL);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mockName1;
            }
        }).when(mockUtil).getMockName(eq(mock1));

        String mockName2 = "mockName1"; // same name
        Object mock2 = mock(Object.class, MockUtil.MATCH_ALL);
        Mockito.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mockName2;
            }
        }).when(mockUtil).getMockName(eq(mock2));

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // when
        OngoingInjecter result = filter.filterCandidate(mocks, null, null);

        // then
        assertEquals(mock2, result.getMock());
    }

    private static class StubFilter implements MockCandidateFilter {
        @Override
        public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
            throw new UnsupportedOperationException();
        }
    }
}