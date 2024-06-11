package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class NameBasedCandidateFilterTest {
    private MockUtil mockUtil;
    private NameBasedCandidateFilter filter;
    private MockCandidateFilter next;

    @Before
    public void setup() {
        mockUtil = Mockito.spy(new MockUtil());
        next = mock(MockCandidateFilter.class);
        filter = new NameBasedCandidateFilter(next);
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));

        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        field.setName("testField");

        String mockName = "testMock";
        stub(mockUtil.getMockName(Mockito.any(Object.class))).toReturn(mockName);

        Object result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(result, fieldInstance);
        assertEquals(1, mocks.size());
        assertEquals(mockName, field.getName());
    }

    @Test
    public void testFilterCandidate_multipleMocksWithMatchingNames() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class, "mock1"));
        mocks.add(Mockito.mock(String.class, "mock1"));

        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        field.setName("testField");

        String mockName = "testMock";
        stub(mockUtil.getMockName(Mockito.any(Object.class))).toReturn(mockName);

        List<Object> result = new ArrayList<>();
        Answer<Object> answer = inv -> {
            result.add(inv.getArgument(0));
            return fieldInstance;
        };

        when(next.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(answer);

        filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(1, result.size());
        assertEquals(mocks.get(0), result.get(0));
        assertEquals(1, mocks.size());
    }

    @Test
    public void testFilterCandidate_multipleMocksWithDifferentNames() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class, "mock1"));
        mocks.add(Mockito.mock(String.class, "mock2"));

        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        field.setName("testField");

        String mockName1 = "mock1";
        String mockName2 = "mock2";
        stub(mockUtil.getMockName(Mockito.eq(mocks.get(0)))).toReturn(mockName1);
        stub(mockUtil.getMockName(Mockito.eq(mocks.get(1)))).toReturn(mockName2);

        List<Object> result = new ArrayList<>();
        Answer<Object> answer = inv -> {
            result.add(inv.getArgument(0));
            return fieldInstance;
        };

        when(next.filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class)))
                .thenAnswer(answer);

        filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(1, result.size());
        assertEquals(mocks.get(1), result.get(0));
        assertEquals(2, mocks.size());
    }

    @Test
    public void testFilterCandidate_noMatchingMocks() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(String.class));
        mocks.add(Mockito.mock(Integer.class));

        Object fieldInstance = mock(Object.class);
        Field field = Mockito.spy(Mockito.mock(Field.class));
        field.setName("testField");

        String mockName = "testMock";
        stub(mockUtil.getMockName(Mockito.any(Object.class))).toReturn(mockName);

        filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(0, mocks.size());
    }
}