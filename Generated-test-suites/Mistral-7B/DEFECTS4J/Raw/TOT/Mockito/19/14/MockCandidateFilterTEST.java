import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Object fieldInstance;
    private Collection<Object> mocks;
    private Field fieldToBeInjected;

    @Before
    public void setUp() throws NoSuchFieldException {
        mockCandidateFilter = mock(MockCandidateFilter.class);
        fieldInstance = mock(Object.class);
        mocks = new ArrayList<>();
        fieldToBeInjected = mock(Field.class);
        fieldToBeInjected.set(fieldInstance, mockCandidateFilter);
    }

    @Test
    public void testFilterCandidate_WhenMocksAndFieldInstanceAreProvided_ThenFilterCandidateIsCalled() {
        Object testObject = new Object();
        mocks.add(testObject);

        Answer<OngoingInjecter> answer = new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                OngoingInjecter ongoingInjecter = mock(OngoingInjecter.class);
                mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
                verify(mockCandidateFilter).filterCandidate(ArgumentMatchers.eq(mocks), ArgumentMatchers.eq(fieldToBeInjected), ArgumentMatchers.eq(fieldInstance));
                return ongoingInjecter;
            }
        };

        Mockito.doAnswer(answer).when(mockCandidateFilter).filterCandidate(ArgumentMatchers.any(Collection.class), ArgumentMatchers.any(Field.class), ArgumentMatchers.any(Object.class));

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }
}