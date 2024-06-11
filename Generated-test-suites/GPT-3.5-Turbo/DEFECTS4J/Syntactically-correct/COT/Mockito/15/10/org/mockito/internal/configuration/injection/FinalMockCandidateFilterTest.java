import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertTrue(injecter.thenInject());
        assertEquals(fieldInstance, field.getDeclaringClass());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.emptyList();
        Field field = null;
        Object fieldInstance = new Object();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        // Given
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = null;
        Object fieldInstance = new Object();

        // When
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, null);

        // Then
        injecter.thenInject();
    }

}