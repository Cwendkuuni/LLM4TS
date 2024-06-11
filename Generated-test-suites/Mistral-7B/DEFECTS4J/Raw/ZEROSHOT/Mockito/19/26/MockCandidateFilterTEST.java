import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() throws NoSuchFieldException {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate_whenFieldIsNotAnnotated_thenReturnNull() {
        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return false;
            }
        });

        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        verify(filter, Mockito.times(1)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotated_thenReturnMock() {
        MockitoAnnotations.initMocks(this);

        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(mock(Object.class));

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(filter, Mockito.times(1)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(result, mock(Object.class));
    }

    @Test
    public void testFilterCandidate_whenFieldIsAnnotatedWithMultipleAnnotations_thenReturnLastAnnotation() {
        MockitoAnnotations.initMocks(this);

        MockitoAnnotations.Mock annotation1 = MockitoAnnotations.mock(MockitoAnnotations.Mock.class);
        MockitoAnnotations.Mock annotation2 = MockitoAnnotations.mock(MockitoAnnotations.Mock.class);
        when(fieldToBeInjected.getAnnotations()).thenReturn(new Object[]{annotation1, annotation2});
        when(annotation1.value()).thenReturn("Mock1");
        when(annotation2.value()).thenReturn("Mock2");

        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(mock(Object.class));

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(filter, Mockito.times(1)).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(result, mock(Object.class));
        verify(annotation2, Mockito.times(1)).value();
        assertEquals(result.getClass().getName(), verify(annotation2, Mockito.capture()).value().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testFilterCandidate_whenFieldIsAnnotatedWithInvalidAnnotation_thenThrowIllegalStateException() {
        MockitoAnnotations.initMocks(this);

        when(fieldToBeInjected.isAnnotationPresent(MockitoAnnotations.Mock.class)).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return true;
            }
        });
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenThrow(new IllegalStateException("Invalid annotation"));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}