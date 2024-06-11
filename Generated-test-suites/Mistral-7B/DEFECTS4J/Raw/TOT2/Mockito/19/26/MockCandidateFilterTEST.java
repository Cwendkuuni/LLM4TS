import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.quality.Strictness;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class MockCandidateFilterTest {

    @Mock
    private OngoingInjecter ongoingInjecterMock;

    @InjectMocks
    private MockCandidateFilter filter;

    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Before
    public void setup() {
        mocks = mock(Collection.class);
        fieldToBeInjected = mock(Field.class);
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_ValidArguments() {
        when(ongoingInjecterMock.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(Answers.delegateTo(filter));

        Object result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        verify(ongoingInjecterMock).filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertEquals(fieldInstance, result);
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        assertThrows(UnsupportedOperationException.class, () -> filter.filterCandidate(new ArrayList<>(), fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstance() {
        assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

    @Test
    public void testFilterCandidate_NullMocks() {
        assertNull(filter.filterCandidate(null, fieldToBeInjected, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldToBeInjected() {
        assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_NullFieldInstanceAndEmptyCollection() {
        assertNull(filter.filterCandidate(null, fieldToBeInjected, null));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstanceWithNonEmptyCollection() {
        filter.filterCandidate(mocks, fieldToBeInjected, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullMocksWithNonEmptyCollection() {
        filter.filterCandidate(null, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldToBeInjectedWithNonEmptyCollection() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_NullFieldInstanceAndNullFieldToBeInjectedWithNonEmptyCollection() {
        filter.filterCandidate(null, null, fieldInstance);
    }

    @Test(expected = RuntimeException.class)
    public void testFilterCandidate_OngoingInjecterThrowingRuntimeException() {
        doThrow(new RuntimeException("MockCandidateFilter Test Error")).when(ongoingInjecterMock).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertThrows(RuntimeException.class, () -> filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = Exception.class)
    public void testFilterCandidate_OngoingInjecterThrowingException() {
        doThrow(new Exception("MockCandidateFilter Test Error")).when(ongoingInjecterMock).filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertThrows(Exception.class, () -> filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }
}