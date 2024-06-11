import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NameBasedCandidateFilterTest {
    private MockCandidateFilter mockCandidateFilter;

    @Test
    public void testFilterCandidate_SingleMock() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        mockCandidateFilter = new NameBasedCandidateFilter(null);
        MockCandidateFilter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result);
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        mocks.add(mock3);

        when(mock1.getName()).thenReturn("mock1");
        when(mock2.getName()).thenReturn("mock2");
        when(mock3.getName()).thenReturn("mock1");

        mockCandidateFilter = new NameBasedCandidateFilter(null);
        MockCandidateFilter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(result);
    }

    // Additional test cases for edge cases and error scenarios should also be added here

}