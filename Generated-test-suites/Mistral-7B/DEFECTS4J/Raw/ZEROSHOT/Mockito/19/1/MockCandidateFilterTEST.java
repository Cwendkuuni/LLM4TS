package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Object fieldInstance;
    private Field fieldToBeInjected;

    @Before
    public void setup() throws NoSuchFieldException {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        fieldInstance = mock(Object.class);
        fieldToBeInjected = Mockito.spy(ReflectionUtils.getDeclaredField(this, "fieldInstance"));
    }

    @Test
    public void testFilterCandidate_whenMocksAreEmpty_thenReturnNull() {
        List<Object> mocks = Collections.emptyList();

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertSame(null, result);
    }

    @Test
    public void testFilterCandidate_whenMocksAreNotEmpty_thenReturnMockedInstance() {
        Object mocked = mock(Object.class);
        List<Object> mocks = Collections.singletonList(mocked);

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(mocked, result);
        verify(filter, Mockito.times(1)).filterCandidate(eq(mocks), eq(fieldToBeInjected), eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_whenMocksAreNotEmptyAndMockAlreadyExists_thenReturnExistingInstance() {
        Object existingMock = mock(Object.class);
        Object mocked = mock(Object.class);
        List<Object> mocks = Collections.singletonList(existingMock);

        Mockito.when(filter.filterCandidate(eq(mocks), eq(fieldToBeInjected), eq(fieldInstance)))
                .thenAnswer(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        return existingMock;
                    }
                });

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(existingMock, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenMocksIsNull_thenThrowIllegalArgumentException() {
        filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldToBeInjectedIsNull_thenThrowIllegalArgumentException() {
        filter.filterCandidate(Collections.emptyList(), null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenFieldInstanceIsNull_thenThrowIllegalArgumentException() {
        filter.filterCandidate(Collections.emptyList(), fieldToBeInjected, null);
    }
}