import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
        verify(mockitoException).setIgnore(true);
    }

    @Test
    public void testFilterCandidate_multipleMock() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = null;
        Object fieldInstance = null;

        OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
    }
}