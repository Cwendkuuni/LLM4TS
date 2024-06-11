import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_withSingleMock_returnsOngoingInjecter() {
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = getField("field");
        Object fieldInstance = new Object();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        assertNotNull(fieldSetter.get());
        assertEquals(mocks.iterator().next(), fieldSetter.get());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_returnsOngoingInjecter() {
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = getField("field");
        Object fieldInstance = new Object();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
        assertNull(fieldSetter.get());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withException_throwsMockitoException() {
        Collection<Object> mocks = Collections.singleton(new Object());
        Field field = getField("field");
        Object fieldInstance = null; // Setting fieldInstance as null to trigger exception

        mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            fail("Failed to retrieve field: " + fieldName);
            return null;
        }
    }
}