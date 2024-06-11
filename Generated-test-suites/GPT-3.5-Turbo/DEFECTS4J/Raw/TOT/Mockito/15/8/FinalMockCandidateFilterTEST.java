import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void filterCandidate_singleMock_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void filterCandidate_singleMock_shouldInjectMatchingMock() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();

        assertEquals(mock, field.get(fieldInstance));
    }

    @Test
    public void filterCandidate_singleMock_shouldThrowMockitoExceptionOnInjectionFailure() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = new Object();

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        fieldSetter.setExpectedException(new MockitoException());

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        try {
            ongoingInjecter.thenInject();
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in " + field.getName(), e.getMessage());
        }
    }

    @Test
    public void filterCandidate_multipleMocks_shouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(ongoingInjecter);
    }

    @Test
    public void filterCandidate_multipleMocks_shouldNotInjectAnyMock() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);

        Field field = getField();
        Object fieldInstance = new Object();

        OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        ongoingInjecter.thenInject();

        assertNull(field.get(fieldInstance));
    }

    private Field getField() {
        Field field = null;
        try {
            field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            fail("Failed to get field mocks");
        }
        return field;
    }
}