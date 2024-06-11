import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;

    @Before
    public void setup() {
        mockCandidateFilter = new MockCandidateFilterImpl();
    }

    @Test
    public void testFilterCandidate_WithMocksAndFieldInstance_ReturnsOngoingInjecter() {
        List<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getFieldToBeInjected();
        Object fieldInstance = getFieldInstance();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(OngoingInjecter.class, result.getClass());
    }

    @Test
    public void testFilterCandidate_WithoutMocksAndFieldInstance_ReturnsNull() {
        List<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getFieldToBeInjected();
        Object fieldInstance = getFieldInstance();

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidate_WithMocksAndNullFieldInstance_ReturnsOngoingInjecter() {
        List<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = getFieldToBeInjected();
        Object fieldInstance = null;

        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);

        assertEquals(OngoingInjecter.class, result.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WithNullMocksAndFieldInstance_ThrowsNullPointerException() {
        List<Object> mocks = null;
        Field fieldToBeInjected = getFieldToBeInjected();
        Object fieldInstance = getFieldInstance();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_WithMocksAndNullFieldToBeInjected_ThrowsNullPointerException() {
        List<Object> mocks = new ArrayList<>();
        Field fieldToBeInjected = null;
        Object fieldInstance = getFieldInstance();

        mockCandidateFilter.filterCandidate(mocks, fieldToBeInjected, fieldInstance);
    }

    private Field getFieldToBeInjected() {
        try {
            return MockCandidateFilterImpl.class.getDeclaredField("fieldToBeInjected");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object getFieldInstance() {
        return mock(Object.class);
    }
}