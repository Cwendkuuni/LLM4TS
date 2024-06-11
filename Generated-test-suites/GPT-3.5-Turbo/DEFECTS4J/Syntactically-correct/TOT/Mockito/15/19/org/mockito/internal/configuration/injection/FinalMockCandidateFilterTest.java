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
    private FieldSetter mockFieldSetter;
    private Field mockField;
    private Collection<Object> mocks;
    private Object mockInstance;

    @Before
    public void setUp() throws Exception {
        mockCandidateFilter = new FinalMockCandidateFilter();
        mockField = getClass().getDeclaredField("mockInstance");
        mockInstance = new Object();
        mocks = new ArrayList<>();
        mocks.add(mockInstance);
    }

    @Test
    public void testFilterCandidate_singleMock() throws Exception {
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, mockField, mockInstance);
        assertTrue(result.thenInject());
        assertSame(mockInstance, mockField.get(mockInstance));
    }

    @Test
    public void testFilterCandidate_multipleMocks() throws Exception {
        mocks.add(new Object());
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, mockField, mockInstance);
        assertFalse(result.thenInject());
        assertNull(mockField.get(mockInstance));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        mockInstance = null;
        OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, mockField, null);
        result.thenInject();
    }
}