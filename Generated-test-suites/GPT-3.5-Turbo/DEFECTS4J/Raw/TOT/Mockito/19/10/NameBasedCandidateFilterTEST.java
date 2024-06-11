import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
        mockUtil = new MockUtil();
    }

    @Test
    public void testFilterCandidate_singleMockNoMatchingName() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));

        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_singleMockMatchingName() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        String mockName = "mockObject";
        when(mockUtil.getMockName(mockObject)).thenReturn(mockName);
        mocks.add(mockObject);

        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMocksMatchingName() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);
        String mockName = "mockObject";
        when(mockUtil.getMockName(mockObject1)).thenReturn(mockName);
        when(mockUtil.getMockName(mockObject2)).thenReturn(mockName);
        mocks.add(mockObject1);
        mocks.add(mockObject2);

        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_multipleMocksNoMatchingName() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);
        String mockName = "mockObject1";
        when(mockUtil.getMockName(mockObject1)).thenReturn(mockName);
        when(mockUtil.getMockName(mockObject2)).thenReturn(mockName);
        mocks.add(mockObject1);
        mocks.add(mockObject2);

        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    // edge case - empty mocks collection
    @Test
    public void testFilterCandidate_noMocks() throws Exception {
        Collection<Object> mocks = new ArrayList<>();

        Field field = getClass().getDeclaredField("dummyField");
        Object fieldInstance = new Object();

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(next).filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    // edge case - empty field instance
    @Test
    public void testFilterCandidate_emptyFieldInstance() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        String mockName = "mockObject";
        when(mockUtil.getMockName(mockObject)).thenReturn(mockName);
        mocks.add(mockObject);

        Field field = getClass().getDeclaredField("dummyField");

        OngoingInjecter result = filter.filterCandidate(mocks, field, null);

        verify(next).filterCandidate(mocks, field, null);
        assertNull(result);
    }

    // error scenario - null field
    @Test(expected = NoSuchFieldException.class)
    public void testFilterCandidate_nullField() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        String mockName = "mockObject";
        when(mockUtil.getMockName(mockObject)).thenReturn(mockName);
        mocks.add(mockObject);

        Object fieldInstance = new Object();

        filter.filterCandidate(mocks, null, fieldInstance);
    }
}