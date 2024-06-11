
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Collection<Object> mocks = Collections.singletonList(new Object());
        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(ongoingInjecter.thenInject());
        assertEquals(fieldInstance, getFieldInstanceValue(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        Collection<Object> mocks = Arrays.asList(new Object(), new Object());
        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionOnInjection() {
        Collection<Object> mocks = Collections.singletonList("InvalidMock");
        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getFieldInstanceValue(Object fieldInstance, Field field) {
        try {
            return field.get(fieldInstance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}