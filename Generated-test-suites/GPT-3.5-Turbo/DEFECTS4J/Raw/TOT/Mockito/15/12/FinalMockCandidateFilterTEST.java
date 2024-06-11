import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {
    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void filterCandidate_withOneMock_shouldReturnOngoingInjecterThatInjectsMock() {
        Collection<Object> mocks = Collections.singleton("mock");

        Field field = createField("fieldName", Object.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
        assertSame("mock", getField(fieldInstance, field));
    }

    @Test
    public void filterCandidate_withMultipleMocks_shouldReturnOngoingInjecterThatDoesNotInject() {
        Collection<Object> mocks = Arrays.asList("mock1", "mock2");

        Field field = createField("fieldName", Object.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
        assertNull(getField(fieldInstance, field));
    }

    @Test(expected = MockitoException.class)
    public void filterCandidate_withExceptionDuringInjection_shouldThrowMockitoException() {
        Collection<Object> mocks = Collections.singleton("mock");

        Field field = createField("fieldName", Object.class);
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        injecter.thenInject();
    }

    private Field createField(String name, Class<?> type) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getField(Object fieldInstance, Field field) {
        try {
            field.setAccessible(true);
            return field.get(fieldInstance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}