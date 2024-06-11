
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private TypeBasedCandidateFilter nextMock;
    private List<Object> mocks;
    private Stubber stubber;

    @Before
    public void setup() {
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = new ArrayList<>();
        nextMock = mock(TypeBasedCandidateFilter.class);
        stubber = mock(Stubber.class);
    }

    @Test
    public void testFilterCandidate_MatchingMockType_ShouldReturnOngoingInjecter() {
        Object mock = Mockito.mock(String.class);
        mocks.add(mock);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(String.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertEquals(mock, result.getMock());
        verify(nextMock).filterCandidate(mocks, field, new Object());
    }

    @Test
    public void testFilterCandidate_NoMatchingMockType_ShouldReturnNull() {
        Object mock = Mockito.mock(Integer.class);
        mocks.add(mock);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(Long.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMatchingMockTypes_ShouldReturnOngoingInjecterOfTheFirstOne() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = mock(Field.class);
        when(field.getType()).thenReturn(String.class);

        OngoingInjecter result = filter.filterCandidate(mocks, field, new Object());

        assertEquals(mock1, result.getMock());
        verify(nextMock).filterCandidate(Collections.singletonList(mock2), field, new Object());
    }

    // Add test cases for error scenarios here
}