
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_WhenMocksSizeIsOne_ThenInjectMatchingMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("MockObject");
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
        assertEquals("MockObject", fieldInstance);
    }

    @Test
    public void testFilterCandidate_WhenMocksSizeIsGreaterThanOne_ThenDoNotInject() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("MockObject1");
        mocks.add("MockObject2");
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertFalse(injecter.thenInject());
        assertNotEquals("MockObject1", fieldInstance);
        assertNotEquals("MockObject2", fieldInstance);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_WhenExceptionOccursDuringInjection_ThenThrowMockitoException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add("MockObject");
        Field field = getField("fieldInstance");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        FieldSetter.setField((FieldSetter) new Object(), "field", null);

        injecter.thenInject();
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}
