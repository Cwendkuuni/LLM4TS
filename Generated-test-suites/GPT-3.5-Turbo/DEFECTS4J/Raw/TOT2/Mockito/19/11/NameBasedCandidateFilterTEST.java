import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;

public class NameBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() throws NoSuchFieldException, IllegalAccessException {
        MockCandidateFilter mockCandidateFilter = mock(MockCandidateFilter.class);
        Field field = NameBasedCandidateFilter.class.getDeclaredField("next");
        Object fieldInstance = new Object();
        NameBasedCandidateFilter nameBasedCandidateFilter = new NameBasedCandidateFilter(mockCandidateFilter);

        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(Arrays.asList(mock1), field, fieldInstance);

        // Test edge case: mocks.size() = 1
        Collection<Object> singleMock = new ArrayList<>();
        singleMock.add(new Object());
        ongoingInjecter = nameBasedCandidateFilter.filterCandidate(singleMock, field, fieldInstance);
        verify(mockCandidateFilter).filterCandidate(singleMock, field, fieldInstance);

        // Test exception handling scenario
        try {
            Field nonExistingField = NameBasedCandidateFilter.class.getDeclaredField("nonExistingField");
            ongoingInjecter = nameBasedCandidateFilter.filterCandidate(mocks, nonExistingField, fieldInstance);
            fail("Exception not thrown for non-existing field");
        } catch (NoSuchFieldException e) {
            // Expected exception
        } catch (Exception e) {
            fail("Incorrect exception thrown for non-existing field");
        }
    }
}