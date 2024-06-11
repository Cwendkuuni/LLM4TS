import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setUp() {
        filter = Mockito.spy(Mockito.mock(MockCandidateFilter.class));
        mocks = new ArrayList<>();
        fieldToBeInjected = Mockito.spy(Mockito.mock(Field.class));
        fieldInstance = mock(Object.class);
    }

    @Test
    public void testFilterCandidate() {
        Object ongOingInjecter = new Object();
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongOingInjecter);

        assertNotNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));

        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullCollection() {
        when(filter.filterCandidate(null, fieldToBeInjected, fieldInstance)).thenCallRealMethod();

        filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullField() {
        when(filter.filterCandidate(mocks, null, fieldInstance)).thenCallRealMethod();

        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        when(filter.filterCandidate(mocks, fieldToBeInjected, null)).thenCallRealMethod();

        filter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test
    public void testFilterCandidateWithMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Object ongOingInjecter = new Object();
        when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenReturn(ongOngoingInjecter);

        assertNotNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));

        verify(mock1).isInstance(fieldInstance.getClass());
        verify(mock2).isInstance(fieldInstance.getClass());
        verify(filter).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    private void when(MockCandidateFilter filter, Answer answer) {
        Mockito.doAnswer(answer).when(filter);
    }

}