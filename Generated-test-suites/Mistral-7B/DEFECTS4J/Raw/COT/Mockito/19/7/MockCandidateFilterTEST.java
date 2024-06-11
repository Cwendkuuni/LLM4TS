import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class MockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Mockito.when(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return Mockito.mock(OngoingInjecter.class);
            }
        });

        Assert.assertNotNull(filter.filterCandidate(mocks, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullMocks() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Assert.assertNull(filter.filterCandidate(null, fieldToBeInjected, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldToBeInjected() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Object fieldInstance = Mockito.mock(Object.class);

        Assert.assertNull(filter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidateWithNullFieldInstance() {
        MockCandidateFilter filter = Mockito.mock(MockCandidateFilter.class);
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Field fieldToBeInjected = Mockito.mock(Field.class);

        Assert.assertNull(filter.filterCandidate(mocks, fieldToBeInjected, null));
    }

}