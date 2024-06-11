import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.EmptyList;
import org.mockito. invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = mock(MockCandidateFilter.class);
    }

    @Test
    public void testFilterCandidate_whenNullMocks_thenReturnNull() {
        Collection<Object> mocks = null;
        assertNull(mockCandidateFilter.filterCandidate(mocks, mock(Field.class), new Object()));
    }

    @Test
    public void testFilterCandidate_whenNullFieldInstance_thenReturnNull() {
        Collection<Object> mocks = EmptyList.asMockitoList();
        assertNull(mockCandidateFilter.filterCandidate(mocks, mock(Field.class), null));
    }

    @Test
    public void testFilterCandidate_whenNullFieldToBeInjected_thenReturnNull() {
        Collection<Object> mocks = EmptyList.asMockitoList();
        assertNull(mockCandidateFilter.filterCandidate(mocks, null, new Object()));
    }

    @Test
    public void testFilterCandidate_whenFilterCandidateIsCalled_thenCallFieldSetter() {
        Collection<Object> mocks = EmptyList.asMockitoList();
        Field fieldToBeInjected = mock(Field.class);
        Object fieldInstance = new Object();

        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                fieldInstance.set(fieldToBeInjected, invocation.getArguments()[0]);
                return null;
            }
        };

        when(mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(answer);

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, new Object());

        verify(fieldToBeInjected).set(fieldInstance, any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_whenNullMocksOrFieldToBeInjected_thenThrowIllegalArgumentException() {
        Collection<Object> mocks = null;
        Field fieldToBeInjected = null;
        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, new Object());
    }

    @Test
    public void testFilterCandidate_whenMocksCollectionIsEmpty_thenReturnNull() {
        Collection<Object> mocks = EmptyList.asMockitoList();
        assertNull(mockCandidateFilter.filterCandidate(mocks, mock(Field.class), new Object()));
    }
}