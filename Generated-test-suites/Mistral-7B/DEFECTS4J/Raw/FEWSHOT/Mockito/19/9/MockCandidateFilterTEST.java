package org.mockito.internal.configuration.injection.filter;

import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.reflection.Whitebox.getInternalState;
import static org.mockito.internal.util.reflection.Whitebox.setInternalState;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.MockInjector;
import org.mockito.internal.configuration.injection.InjectableField;
import org.mockito.internal.configuration.injection.MockFactory;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private MockFactory mockFactory;
    private MockInjector mockInjector;

    @Before
    public void setUp() {
        mockFactory = Mockito.mock(MockFactory.class);
        mockInjector = Mockito.mock(MockInjector.class);
        filter = Mockito.spy(new MockCandidateFilter() {
            @Override
            public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
                return mockInjector;
            }
        });
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFilterCandidateNullMockCollection() {
        FilterCandidateResult result = filter.filterCandidate(null, new InjectableField("testField", String.class, null), new Object());
        assertNull(result.getMock());
        assertNull(result.getCandidate());
    }

    @Test
    public void testFilterCandidateEmptyMockCollection() {
        FilterCandidateResult result = filter.filterCandidate(Collections.emptyList(), new InjectableField("testField", String.class, null), new Object());
        assertNull(result.getMock());
        assertNull(result.getCandidate());
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Object mock = Mockito.mock(String.class);
        FilterCandidateResult result = filter.filterCandidate(Collections.singletonList(mock), new InjectableField("testField", String.class, null), new Object());
        Mockito.verify(mockFactory, Mockito.times(1)).createMock(String.class);
        assertEquals(mock, result.getMock());
        assertNull(result.getCandidate());
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        FilterCandidateResult result1 = filter.filterCandidate(Arrays.asList(mock1, mock2), new InjectableField("testField", String.class, null), new Object());
        Mockito.verify(mockFactory, Mockito.times(1)).createMock(String.class);
        assertEquals(mock1, result1.getMock());
        assertNull(result1.getCandidate());

        Field field = Reflections.getField(MockInjector.class, "mocksToInject");
        List<Object> mocks = (List<Object>) getInternalState(mockInjector, field);
        assertEquals(1, mocks.size());
        assertEquals(mock1, mocks.get(0));

        FilterCandidateResult result2 = filter.filterCandidate(Arrays.asList(mock1, mock2), new InjectableField("testField2", String.class, null), new Object());
        Mockito.verify(mockFactory, Mockito.times(1)).createMock(String.class);
        assertEquals(mock2, result2.getMock());
        assertNull(result2.getCandidate());

        assertEquals(2, mocks.size());
        assertEquals(mock1, mocks.get(0));
        assertEquals(mock2, mocks.get(1));
    }

    @Test
    public void testFilterCandidateSameMock() {
        Object mock = Mockito.mock(String.class);
        FilterCandidateResult result1 = filter.filterCandidate(Collections.singletonList(mock), new InjectableField("testField", String.class, null), new Object());
        Mockito.verify(mockFactory, Mockito.times(1)).createMock(String.class);
        assertEquals(mock, result1.getMock());
        assertNull(result1.getCandidate());

        FilterCandidateResult result2 = filter.filterCandidate(Collections.singletonList(mock), new InjectableField("testField2", String.class, null), new Object());
        assertSame(mock, result2.getMock());
        assertNull(result2.getCandidate());
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        FilterCandidateResult result = filter.filterCandidate(Collections.singletonList(Mockito.mock(String.class)), null, new Object());
        assertNull(result.getMock());
        assertNull(result.getCandidate());
    }

    @Test
    public void testFilterCandidateNullFieldToBeInjected() {
        FilterCandidateResult result = filter.filterCandidate(Collections.singletonList(Mockito.mock(String.class)), null, new Object());
        assertNull(result.getMock());
        assertNull(result.getCandidate());
    }

}

class FilterCandidateResult {
    private Object mock;
    private Object candidate;

    public Object getMock() {
        return mock;
    }

    public void setMock(Object mock) {
        this.mock = mock;
    }

    public Object getCandidate() {
        return candidate;
    }

    public void setCandidate(Object candidate) {
        this.candidate = candidate;
    }
}