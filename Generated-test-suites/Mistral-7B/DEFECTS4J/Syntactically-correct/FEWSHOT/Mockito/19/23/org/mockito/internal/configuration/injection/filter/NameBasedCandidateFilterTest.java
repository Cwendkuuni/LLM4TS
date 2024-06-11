package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.mock.MockMethod;
import org.mockito.mock.MockType;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.MockUtil.mock;
import static org.mockito.internal.util.MockUtil.spy;

public class NameBasedCandidateFilterTest {
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        nextMock = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(nextMock);
    }

    @Test
    public void testFilterCandidate_NoConflictingFields_ReturnsNextFilter() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = new Field() {{
            setName("testField");
        }};

        Answer<Object> answer = answer(new Object());

        Object mock1 = mock(MockType.CLASS, answer, new Class[0]);
        mocks.add(mock1);

        Object mock2 = mock(MockType.CLASS, answer, new Class[0]);
        mocks.add(mock2);

        Object expectedNextFilterResult = nextMock.filterCandidate(mocks, field, fieldInstance);

        Object filterResult = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(expectedNextFilterResult, filterResult);
    }

    @Test
    public void testFilterCandidate_ConflictingFields_ReturnsMatchingMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = new Field() {{
            setName("testField");
        }};

        Object mock1 = mock(MockType.CLASS, new Object[0]);
        mocks.add(mock1);
        String mockName1 = getMockName(mock1);

        Object mock2 = mock(MockType.CLASS, new Object[0]);
        mocks.add(mock2);
        String mockName2 = getMockName(mock2);

        if (mockName1.equals(mockName2)) {
            Object expectedMatchingMock = mock1;
            Object filterResult = filter.filterCandidate(mocks, field, fieldInstance);
            assertEquals(expectedMatchingMock, filterResult);
        } else {
            Object expectedNextFilterResult = nextMock.filterCandidate(mocks, field, fieldInstance);
            Object filterResult = filter.filterCandidate(mocks, field, fieldInstance);
            assertEquals(expectedNextFilterResult, filterResult);
        }
    }

    @Test
    public void testFilterCandidate_NullMocks_ReturnsNull() {
        Object fieldInstance = new Object();
        Field field = new Field() {{
            setName("testField");
        }};

        Object expectedNextFilterResult = nextMock.filterCandidate(null, field, fieldInstance);
        Object filterResult = filter.filterCandidate(null, field, fieldInstance);
        assertNull(filterResult);
        assertNull(expectedNextFilterResult);
    }

    @Test
    public void testFilterCandidate_NullField_ReturnsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();

        Field nullField = null;

        Object expectedNextFilterResult = nextMock.filterCandidate(mocks, nullField, fieldInstance);
        Object filterResult = filter.filterCandidate(mocks, nullField, fieldInstance);
        assertNull(filterResult);
        assertNull(expectedNextFilterResult);
    }

    @Test
    public void testFilterCandidate_NullFieldInstance_ReturnsNull() {
        Collection<Object> mocks = new ArrayList<>();
        Field field = new Field() {{
            setName("testField");
        }};

        Object nullFieldInstance = null;

        Object expectedNextFilterResult = nextMock.filterCandidate(mocks, field, nullFieldInstance);
        Object filterResult = filter.filterCandidate(mocks, field, nullFieldInstance);
        assertNull(filterResult);
        assertNull(expectedNextFilterResult);
    }

    @Test
    public void testFilterCandidate_EmptyMocks_ReturnsNull() {
        Object fieldInstance = new Object();
        Field field = new Field() {{
            setName("testField");
        }};

        Object expectedNextFilterResult = nextMock.filterCandidate(new ArrayList<>(), field, fieldInstance);
        Object filterResult = filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
        assertNull(filterResult);
        assertNull(expectedNextFilterResult);
    }
}