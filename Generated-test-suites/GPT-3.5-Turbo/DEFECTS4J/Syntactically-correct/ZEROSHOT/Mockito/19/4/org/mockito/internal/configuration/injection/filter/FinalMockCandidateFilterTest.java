import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;
    private List<Reporter> reporters;

    @Before
    public void setUp() throws Exception {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = TestClass.class.getField("field");
        fieldInstance = new TestClass();
        reporters = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        filter = null;
        mocks = null;
        field = null;
        fieldInstance = null;
        reporters = null;
    }

    @Test
    public void testFilterCandidate_singleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock, injecter.thenInject());

        verify(mock, times(1)).getClass();
        verify(mock, times(1)).hashCode();
        verify(mock, times(1)).equals(any());

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        MockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(null, injecter.thenInject());

        verifyZeroInteractions(mocks);
    }

    public class TestClass {
        public Object field;
    }
}