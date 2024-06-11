package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.createMock;

public class NameBasedCandidateFilterTest {
    private MockCandidateFilter next;
    private NameBasedCandidateFilter filter;
    private MockUtil mockUtil;

    @Before
    public void setup() {
        next = mock(MockCandidateFilter.class);
        mockUtil = new MockUtil();
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidateWithOneMock() {
        Object mock = createMock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = mock.getClass().getDeclaredField("testField");

        Object fieldInstance = createMock(field.getType());

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock, result.getMock());
        assertNull(result.getNext());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndMatchedName() {
        Object mock1 = createMock(Object.class);
        Object mock2 = createMock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock1.getClass().getDeclaredField("testField");

        Object fieldInstance = createMock(field.getType());

        List<Object> expectedMockMatches = new ArrayList<>();
        expectedMockMatches.add(mock1);

        when(next.filterCandidate(expectedMockMatches, field, fieldInstance))
                .thenReturn(createOngoingInjecter(mock1));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock1, result.getMock());
        assertNull(result.getNext());
    }

    @Test
    public void testFilterCandidateWithMultipleMocksAndUnmatchedNames() {
        Object mock1 = createMock(Object.class);
        Object mock2 = createMock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock1.getClass().getDeclaredField("testField1");

        Object fieldInstance = createMock(field.getType());

        List<Object> expectedMockMatches = new ArrayList<>();
        expectedMockMatches.add(mock2);

        when(next.filterCandidate(expectedMockMatches, field, fieldInstance))
                .thenReturn(createOngoingInjecter(mock2));

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mock2, result.getMock());
        assertNull(result.getNext());
    }

    @Test
    public void testFilterCandidateWithConflictingNamingFields() {
        Object mock1 = createMock(Object.class);
        Object mock2 = createMock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field1 = mock1.getClass().getDeclaredField("testField1");
        Field field2 = mock2.getClass().getDeclaredField("testField1");

        Object fieldInstance = createMock(field1.getType());

        OngoingInjecter result1 = filter.filterCandidate(mocks, field1, fieldInstance);
        OngoingInjecter result2 = filter.filterCandidate(mocks, field2, fieldInstance);

        assertEquals(mock1, result1.getMock());
        assertNull(result1.getNext());

        assertEquals(mock2, result2.getMock());
        assertEquals(next, result2.getNext());
    }

    private OngoingInjecter createOngoingInjecter(Object mock) {
        return new OngoingInjecter(mock, next);
    }
}

class OngoingInjecter {
    private final Object mock;
    private final MockCandidateFilter next;

    public OngoingInjecter(Object mock, MockCandidateFilter next) {
        this.mock = mock;
        this.next = next;
    }

    public Object getMock() {
        return mock;
    }

    public MockCandidateFilter getNext() {
        return next;
    }
}