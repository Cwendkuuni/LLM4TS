import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MockCandidateFilterTest {

    private MockCandidateFilter mockCandidateFilter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() throws NoSuchFieldException {
        mockCandidateFilter = new MockCandidateFilterImpl();
        mocks = new ArrayList<>();
        field = MockCandidateFilterTest.class.getDeclaredField("fieldInstance");
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidate_returnsOngoingInjecter_whenMocksAndFieldAreValid() {
        assertNotNull(mockCandidateFilter.filterCandidate(mocks, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_returnsNull_whenMocksIsNull() {
        assertNull(mockCandidateFilter.filterCandidate(null, field, fieldInstance));
    }

    @Test
    public void testFilterCandidate_returnsNull_whenFieldIsNull() {
        assertNull(mockCandidateFilter.filterCandidate(mocks, null, fieldInstance));
    }

    @Test
    public void testFilterCandidate_returnsNull_whenFieldInstanceIsNull() {
        assertNull(mockCandidateFilter.filterCandidate(mocks, field, null));
    }

    @Test
    public void testFilterCandidate_returnsOngoingInjecterWithMocksEmpty_whenMocksIsEmpty() {
        Collection<Object> emptyMocks = new ArrayList<>();
        OngoingInjecter ongoingInjecter = mockCandidateFilter.filterCandidate(emptyMocks, field, fieldInstance);
        assertNotNull(ongoingInjecter);
        assertTrue(ongoingInjecter.getMocks().isEmpty());
    }
}

class MockCandidateFilterImpl implements MockCandidateFilter {

    @Override
    public OngoingInjecter filterCandidate(Collection<Object> mocks, Field fieldToBeInjected, Object fieldInstance) {
        if (mocks == null || fieldToBeInjected == null || fieldInstance == null) {
            return null;
        }

        OngoingInjecter ongoingInjecter = new OngoingInjecterImpl();
        ongoingInjecter.setMocks(mocks);
        ongoingInjecter.setFieldToBeInjected(fieldToBeInjected);
        ongoingInjecter.setFieldInstance(fieldInstance);
        return ongoingInjecter;
    }
}

class OngoingInjecterImpl implements OngoingInjecter {

    private Collection<Object> mocks;
    private Field fieldToBeInjected;
    private Object fieldInstance;

    @Override
    public OngoingInjecter setMocks(Collection<Object> mocks) {
        this.mocks = mocks;
        return this;
    }

    @Override
    public OngoingInjecter setFieldToBeInjected(Field fieldToBeInjected) {
        this.fieldToBeInjected = fieldToBeInjected;
        return this;
    }

    @Override
    public OngoingInjecter setFieldInstance(Object fieldInstance) {
        this.fieldInstance = fieldInstance;
        return this;
    }

    @Override
    public Collection<Object> getMocks() {
        return mocks;
    }

    @Override
    public Field getFieldToBeInjected() {
        return fieldToBeInjected;
    }

    @Override
    public Object getFieldInstance() {
        return fieldInstance;
    }
}