import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
    }

    @Test
    public void testFilterCandidateWithNullCollection() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = null;
        Object fieldInstance = mock(Object.class);

        assertEquals(fieldInstance, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        verify(mockCandidateFilter, Mockito.never()).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));
    }

    @Test
    public void testFilterCandidateWithEmptyCollection() {
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = new Field("testField", String.class) {};
        Object fieldInstance = mock(Object.class);

        assertEquals(fieldInstance, mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
        verify(mockCandidateFilter, Mockito.never()).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMocksAndFieldWithoutAnnotatedInject() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = new Field("testField", String.class) {};
        Object fieldInstance = mock(Object.class);

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(fieldInstance, result);
        verify(mockCandidateFilter, Mockito.never()).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test
    public void testFilterCandidateWithMocksAndFieldAnnotatedWithInject() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        Field fieldToBeInjected = new Field("testField") {
            @SuppressWarnings("unchecked")
            @Override
            public Object get(Object obj) {
                return "testValue";
            }

            @Override
            public void setAccessible(boolean b) {
                // no-op
            }
        };
        Object fieldInstance = mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> answer = Mockito.answer(invocation);
        Mockito.doAnswer(answer).when(mocks.get(0)).someMethod();

        Object result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(fieldInstance, result);
        verify(mockCandidateFilter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        verify(mocks.get(0)).someMethod();
        assertEquals("testValue", invocation.getArguments()[0]);
    }

}