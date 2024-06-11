package org.mockito.internal.configuration.injection;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Object mock;
    private Object anotherMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filter = new FinalMockCandidateFilter();
        fieldInstance = new Object();
        field = new Field(FinalMockCandidateFilterTest.class, "mock") {
            @Override
            public Class<Object> getType() {
                return Object.class;
            }
        };
        mock = Mockito.mock(Object.class);
        anotherMock = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidateOneMock() {
        Collection<Object> mocks = Arrays.asList(mock);
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(result.thenInject());
        verify(mock, times(1)).setField(fieldInstance);
    }

    @Test
    public void testFilterCandidateNoMock() {
        Collection<Object> mocks = Collections.emptyList();
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertInstanceOf(OngoingInjecter.class, result);
        verify(mock, never()).setField(fieldInstance);
    }

    @Test
    public void testFilterCandidateNullFieldInstance() {
        Collection<Object> mocks = Arrays.asList(mock);
        OngoingInjecter result = filter.filterCandidate(mocks, field, null);
        assertInstanceOf(OngoingInjecter.class, result);
        verify(mock, never()).setField(fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateException() {
        Collection<Object> mocks = Arrays.asList(mock, anotherMock);
        when(mock.setField(fieldInstance)).thenThrow(new RuntimeException());
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(result.thenInject());
        verify(mock, never()).setField(fieldInstance);
    }
}