import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate_emptyMocks() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        List<Object> mocks = Collections.emptyList();
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testFilterCandidate_multipleFieldsWithSameName() {
        String mockName1 = "mock1";
        String mockName2 = "mock2";
        String fieldName = "fieldName";

        MockCandidateFilter mockFilter = mock(MockCandidateFilter.class);
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockFilter);
        Object fieldInstance = mock(Object.class);
        List<Object> mocks = Arrays.asList(mock1(mockName1, fieldName), mock1(mockName2, fieldName));
        Field field = mock(Field.class);

        when(field.getName()).thenReturn(fieldName);

        InvocationOnMock answer = new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getMock();
            }
        };

        when(mockFilter.filterCandidate(eq(mocks), eq(field), eq(fieldInstance))).thenAnswer(answer);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        verify(mockFilter).filterCandidate(eq(mockNameMatches(mockName1, mockName2)), eq(field), eq(fieldInstance));

        assertSame(mock1(mockName1, fieldName), result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullMocks() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        List<Object> mocks = null;
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullField() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        List<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = null;
        Object fieldInstance = mock(Object.class);

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_nullFieldInstance() {
        NameBasedCandidateFilter filter = new NameBasedCandidateFilter(null);
        List<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = null;

        filter.filterCandidate(mocks, field, fieldInstance);
    }

    private Object mock1(String name, String fieldName) {
        Object mock = mock(Object.class);
        doReturn(name).when(mock).toString();
        doReturn(fieldName).when(mock).getClass().getField(fieldName);
        return mock;
    }

    private List<Object> mockNameMatches(String... names) {
        List<Object> list = new ArrayList<>();
        for (String name : names) {
            Object mock = mock(Object.class);
            doReturn(name).when(mock).toString();
            list.add(mock);
        }
        return list;
    }
}