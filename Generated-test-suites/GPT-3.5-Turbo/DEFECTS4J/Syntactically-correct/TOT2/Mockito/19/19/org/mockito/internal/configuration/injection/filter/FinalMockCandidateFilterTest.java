import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter;

    @Before
    public void setUp() {
        finalMockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_WhenSingleMock_ShouldReturnOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertEquals(mockObject, result.thenInject());
    }

    @Test
    public void testFilterCandidate_WhenMultipleMocks_ShouldReturnNullOngoingInjecter() {
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        OngoingInjecter result = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        assertNull(result.thenInject());
    }

    @Test
    public void testFilterCandidate_WhenBeanPropertySetterFails_ShouldFallbackToFieldSetter() {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);
        Field field = null;
        Object fieldInstance = new Object();

        finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(mockObject).hashCode();
    }

    @Test
    public void testFilterCandidate_WhenDependencyInjectionFails_ShouldCatchException() {
        Collection<Object> mocks = new ArrayList<>();
        Object mockObject = mock(Object.class);
        mocks.add(mockObject);
        Field field = null;
        Object fieldInstance = new Object();

        finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);

        verify(new Reporter()).cannotInjectDependency(any(Field.class), any(Object.class), any(RuntimeException.class));
    }
}