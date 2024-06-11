package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;

/**
 * NameBasedCandidateFilterTest class to test NameBasedCandidateFilter methods.
 */
public class NameBasedCandidateFilterTest {

    private MockCandidateFilter nextFilter;
    private NameBasedCandidateFilter nameBasedCandidateFilter;
    private MockUtil mockUtil;
    private Object mock1;
    private Object mock2;
    private Object mock3;
    private Object fieldInstance;

    @Before
    public void setup() {
        nextFilter = mock(MockCandidateFilter.class);
        mockUtil = spy(new MockUtil());
        mock1 = mock(Object.class, "mock1");
        mock2 = mock(Object.class, "mock2");
        mock3 = mock(Object.class, "mock3");
        fieldInstance = mock(Object.class);
        nameBasedCandidateFilter = new NameBasedCandidateFilter(nextFilter);
    }

    @Test
    public void testFilterCandidateOneMock() {
        // Given
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);

        // When
        Collection<Object> filteredMocks = nameBasedCandidateFilter.filterCandidate(mocks, null, fieldInstance);

        // Then
        assertEquals(mock1, filteredMocks.iterator().next());
    }

    @Test
    public void testFilterCandidateMultipleMocksNameMatches() {
        // Given
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        Collection<Object> filteredMocks = nameBasedCandidateFilter.filterCandidate(mocks, new Field("nameField", fieldInstance.getClass()), fieldInstance);

        // Then
        assertEquals(mock1, filteredMocks.iterator().next());
    }

    @Test
    public void testFilterCandidateMultipleMocksNameDoesNotMatch() {
        // Given
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);

        // When
        Collection<Object> filteredMocks = nameBasedCandidateFilter.filterCandidate(mocks, new Field("otherField", fieldInstance.getClass()), fieldInstance);

        // Then
        assertEquals(mock2, nextFilter.filterCandidate(mocks, new Field("otherField", fieldInstance.getClass()), fieldInstance).iterator().next());
    }

    @Test
    public void testFilterCandidateMultipleFieldsNameMatches() {
        // Given
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        InvocationOnMock mockInvocation1 = mock(InvocationOnMock.class);
        InvocationOnMock mockInvocation2 = mock(InvocationOnMock.class);
        InvocationOnMock mockInvocation3 = mock(InvocationOnMock.class);
        Stubber stubber = mock(Stubber.class);

        stub(nextFilter.getClass()).getMockForStatic().spy(eq(MockUtil.class));
        stub(nextFilter.getClass()).getMockForStatic().spy(eq(Stubber.class));

        stub(mockUtil.getClass()).getMockForStatic().getMockName(eq(mock1)).thenReturn("mock1Name");
        stub(mockUtil.getClass()).getMockName(eq(mock2)).thenReturn("mock1Name");
        stub(mockUtil.getClass()).getMockName(eq(mock3)).thenReturn("mock3Name");

        // When
        Collection<Object> filteredMocks = nameBasedCandidateFilter.filterCandidate(mocks, new Field("nameField", fieldInstance.getClass()), fieldInstance);

        // Then
        assertEquals(mock1, filteredMocks.iterator().next());
    }

    @Test
    public void testFilterCandidateMultipleFieldsNameDoesNotMatch() {
        // Given
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        InvocationOnMock mockInvocation1 = mock(InvocationOnMock.class);
        InvocationOnMock mockInvocation2 = mock(InvocationOnMock.class);
        InvocationOnMock mockInvocation3 = mock(InvocationOnMock.class);
        Stubber stubber = mock(Stubber.class);

        stub(nextFilter.getClass()).getMockForStatic().spy(eq(MockUtil.class));
        stub(nextFilter.getClass()).getMockForStatic().spy(eq(Stubber.class));

        stub(mockUtil.getClass()).getMockForStatic().getMockName(eq(mock1)).thenReturn("mock1Name");
        stub(mockUtil.getClass()).getMockName(eq(mock2)).thenReturn("mock2Name");
        stub(mockUtil.getClass()).getMockName(eq(mock3)).thenReturn("mock3Name");

        // When
        Collection<Object> filteredMocks = nameBasedCandidateFilter.filterCandidate(mocks, new Field("otherField", fieldInstance.getClass()), fieldInstance);

        // Then
        assertEquals(mock3, nextFilter.filterCandidate(mocks, new Field("otherField", fieldInstance.getClass()), fieldInstance).iterator().next());
    }
}