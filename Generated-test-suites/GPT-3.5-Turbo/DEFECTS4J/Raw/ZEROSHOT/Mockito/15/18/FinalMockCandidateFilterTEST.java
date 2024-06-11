import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import static org.junit.Assert.*;
import org.mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock() {
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(result.thenInject());
        Mockito.verify(fieldInstance, Mockito.times(1)).set(Mockito.any());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks() {
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Field field = Mockito.mock(Field.class);
        Object fieldInstance = Mockito.mock(Object.class);

        FinalMockCandidateFilter mockCandidateFilter = new FinalMockCandidateFilter();
        FinalMockCandidateFilter.OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(result.thenInject());
        Mockito.verify(fieldInstance, Mockito.times(0)).set(Mockito.any());
    }
}