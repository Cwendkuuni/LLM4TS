import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnInjecterWithMatchingMock() {
        Object mock = mock(Object.class);
        Collection<Object> mocks = Collections.singletonList(mock);
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertTrue(injecter.thenInject());

        verify(mockitoFieldSetter).set(matchingMock);
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnInjecterWithoutInjecting() {
        List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
    }
    
    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Failed to get field: " + fieldName);
        }
        return null; // Will never reach this line
    }
}