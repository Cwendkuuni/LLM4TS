import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() throws Exception {
        mockCandidateFilter = new FinalMockCandidateFilter();
        field = SomeClass.class.getDeclaredField("someField");
        fieldInstance = new SomeClass();
    }

    @Test
    public void testFilterCandidate_withValidMockListAndSingleMockSize_returnsOngoingInjecterWithTrue() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        boolean result = injecter.thenInject();
        assertTrue(result);
    }

    @Test
    public void testFilterCandidate_withValidMockListAndSingleMockSize_injectsMatchingMockIntoField() throws Exception {
        Collection<Object> mocks = new ArrayList<>();
        Object matchingMock = new Object();
        mocks.add(matchingMock);

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        assertNull(fieldGetter(fieldInstance, field));

        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        boolean result = injecter.thenInject();
        assertTrue(result);

        assertEquals(matchingMock, fieldGetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_withEmptyMockList_returnsOngoingInjecterWithFalse() {
        Collection<Object> mocks = new ArrayList<>();

        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        boolean result = injecter.thenInject();
        assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withExceptionDuringInjection_throwsMockitoException() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());

        FinalMockCandidateFilter.OngoingInjecter injecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        FieldSetter faultyFieldSetter = new FieldSetter(fieldInstance, "nonExistentField");
        faultyFieldSetter.set(injecter);
    }

    private Object fieldGetter(Object object, Field field) throws Exception {
        field.setAccessible(true);
        return field.get(object);
    }

    private static class SomeClass {
        public String someField;
    }
}