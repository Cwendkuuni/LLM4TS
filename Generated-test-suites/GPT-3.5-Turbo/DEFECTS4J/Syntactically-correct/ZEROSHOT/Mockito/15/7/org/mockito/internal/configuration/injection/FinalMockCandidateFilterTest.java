import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = null;
        fieldInstance = null;
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Object mock = new Object();
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        mocks.add(new Object());
        fieldInstance = new Object();
        field = getField("invalidField");

        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName);
        }
    }
}