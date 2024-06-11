import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Setup
        MockCandidateFilter next = Mockito.mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        mocks.add(mock);

        // Execute
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        Mockito.verify(next).filterCandidate(mocks, field, fieldInstance);
        assertEquals(next, result);
    }

    @Test
    public void testFilterCandidate_multipleMocks_matchingName() {
        // Setup
        MockCandidateFilter next = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Stubbing
        Mockito.when(mock1.getName()).thenReturn("mockName");
        Mockito.when(mock2.getName()).thenReturn("mockName");
        Mockito.when(field.getName()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock1).toString()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock2).toString()).thenReturn("mockName");

        // Execute
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        Mockito.verify(next).filterCandidate(Mockito.argThat((Collection<Object> arg) -> arg.size() == 2), argumentCaptor.capture(), Mockito.eq(fieldInstance));
        assertEquals(next, result);
        assertEquals(mock1, argumentCaptor.getValue());

        // Cleanup
        Mockito.reset(mock1, mock2, field, mockUtil, next);
    }

    @Test
    public void testFilterCandidate_multipleMocks_noMatchingName() {
        // Setup
        MockCandidateFilter next = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Stubbing
        Mockito.when(mock1.getName()).thenReturn("mockName1");
        Mockito.when(mock2.getName()).thenReturn("mockName2");
        Mockito.when(field.getName()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock1).toString()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock2).toString()).thenReturn("mockName");

        // Execute
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        Mockito.verify(next).filterCandidate(Mockito.argThat((Collection<Object> arg) -> arg.size() == 2), argumentCaptor.capture(), Mockito.eq(fieldInstance));
        assertEquals(next, result);
        assertEquals(mock1, argumentCaptor.getValue());

        // Cleanup
        Mockito.reset(mock1, mock2, field, mockUtil, next);
    }

    @Test
    public void testFilterCandidate_multipleMocks_conflictingNames() {
        // Setup
        MockCandidateFilter next = Mockito.mock(MockCandidateFilter.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(next);
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        // Stubbing
        Mockito.when(mock1.getName()).thenReturn("mockName1");
        Mockito.when(mock2.getName()).thenReturn("mockName2");
        Mockito.when(field.getName()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock1).toString()).thenReturn("mockName");
        Mockito.when(mockUtil.getMockName(mock2).toString()).thenReturn("mockName");

        // Execute
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Verify
        Mockito.verify(next).filterCandidate(Mockito.argThat((Collection<Object> arg) -> arg.size() == 2), argumentCaptor.capture(), Mockito.eq(fieldInstance));
        assertEquals(next, result);
        assertEquals(mock1, argumentCaptor.getValue());

        // Cleanup
        Mockito.reset(mock1, mock2, field, mockUtil, next);
    }
}