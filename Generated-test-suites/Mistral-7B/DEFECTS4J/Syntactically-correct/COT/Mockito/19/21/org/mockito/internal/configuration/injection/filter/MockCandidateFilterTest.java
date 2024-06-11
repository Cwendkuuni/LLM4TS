import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = Mockito.mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate_NullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = spy(new Object() {});

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullField() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = new Object();

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = null;

        assertNull(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_ValidInput() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(fieldInstance, result);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullMock() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalAccessException.class)
    public void testFilterCandidate_InaccessibleField() throws IllegalAccessException {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        doThrow(IllegalAccessException.class).when(fieldToBeInjected).setAccessible(true);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_FieldAlreadyInjected() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();
        fieldInstance = mock(Object.class);

        assertSame(fieldInstance, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidate_AnswerInvoked() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        Answer<Object> answer = invocation -> {
            Object result = invocation.getArgument(2);
            return result;
        };

        Mockito.doAnswer(answer).when(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertSame(fieldInstance, result);
    }

}