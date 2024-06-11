import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.test.util.PowerMockTestRule;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockCandidateFilterTest {

    private MockCandidateFilter filter;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Field fieldToBeInjected;

    @Before
    public void setUp() throws NoSuchFieldException {
        filter = PowerMockito.spy(PowerMockito.doNothing().when(filter));
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        fieldInstance = mock(Object.class);
        fieldToBeInjected = PowerMockito.spy(PowerMockito.spyField(this, "fieldToBeInjected"));
    }

    @Test
    public void testFilterCandidate_returnsOngoingInjecter() {
        OngoingInjecter result = filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
        assertNotNull(result);
    }

    @Test
    public void testFilterCandidate_injectsMocks() throws IllegalAccessException {
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                Object mock = invocation.getArgument(0);
                fieldToBeInjected.setAccessible(true);
                fieldToBeInjected.set(fieldInstance, mock);
                return null;
            }
        }).when(filter).filterCandidate(any(Collection.class), any(Field.class), any(Object.class));

        filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        for (Object mock : mocks) {
            verify(mock).someMethod(); // replace 'someMethod' with a method that is only called by the code under test
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCandidate_throwsIllegalArgumentException_whenFieldIsFinal() throws NoSuchFieldException {
        Field finalField = PowerMockito.spyField(this, "finalField");
        finalField.setAccessible(true);
        finalField.setFinal(true);

        try {
            filter.filterCandidate(mocks, finalField, fieldInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        filter.filterCandidate(mocks, finalField, fieldInstance);
    }

}