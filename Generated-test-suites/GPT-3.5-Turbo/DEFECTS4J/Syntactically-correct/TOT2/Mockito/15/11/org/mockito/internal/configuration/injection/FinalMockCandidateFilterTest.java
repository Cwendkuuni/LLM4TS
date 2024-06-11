import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));

        MockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidateNoCandidateFound() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList();

        MockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateExceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));

        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(new MockitoException("Error in setting field")).when(fieldSetter).set(any());

        whenNew(FieldSetter.class).withArguments(fieldInstance, field).thenReturn(fieldSetter);

        MockCandidateFilter.OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }
}