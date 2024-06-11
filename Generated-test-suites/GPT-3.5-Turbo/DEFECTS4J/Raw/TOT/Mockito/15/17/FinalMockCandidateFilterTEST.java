
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private OngoingInjecter ongoingInjecter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mocks = Collections.emptyList();
        field = getField("fieldName");
        fieldInstance = new Object();
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Test
    public void testFilterCandidate_WithOneMock_ShouldInjectMock() {
        Object mock = new Object();
        mocks = Collections.singleton(mock);

        ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(true, ongoingInjecter.thenInject());
        assertEquals(mock, fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithMultipleMocks_ShouldNotInject() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks = Arrays.asList(mock1, mock2);

        ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(false, ongoingInjecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_WithOneMockAndExceptionWhileInjecting_ShouldThrowException() throws Exception {
        Object mock = new Object();
        mocks = Collections.singleton(mock);
        fieldInstance = new ObjectWithPrivateField();

        ongoingInjecter = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();
    }

    private class ObjectWithPrivateField {
        private Object fieldName;
    }
}
