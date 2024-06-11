import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMatchingMock_thenInject() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>();
        Field field = null;
        Object fieldInstance = new Object();

        Object mockCandidate = mock(Object.class);
        mocks.add(mockCandidate);

        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMatchingMocks_thenNotInject() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList(mock(Object.class), mock(Object.class)));
        Field field = null;
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_fieldSetterException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new HashSet<>(Arrays.asList(mock(Object.class)));
        Field field = null;
        Object fieldInstance = new Object();

        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.set(null);
        injecter.thenInject();
    }
}