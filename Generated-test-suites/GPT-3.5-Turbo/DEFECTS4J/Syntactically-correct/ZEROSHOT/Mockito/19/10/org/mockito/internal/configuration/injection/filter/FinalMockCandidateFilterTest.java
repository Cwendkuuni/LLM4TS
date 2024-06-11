import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setUp() throws Exception {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = FinalMockCandidateFilter.class.getDeclaredField("field");
        fieldInstance = new Object();
    }

    @Test
    public void testFilterCandidateSingleMock() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        verify(mockitoReporter, never()).cannotInjectDependency(field, mock, any(RuntimeException.class));
        verify(mockitoBeanPropertySetter, times(1)).set(fieldInstance, field);
        verify(mockitoFieldSetter, never()).set(fieldInstance, field);
        assertSame(mock, result);
    }

    @Test
    public void testFilterCandidateMultipleMocks() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        verify(mockitoReporter, never()).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
        verify(mockitoBeanPropertySetter, never()).set(fieldInstance, field);
        verify(mockitoFieldSetter, never()).set(fieldInstance, field);
        assertNull(result);
    }

    @Test
    public void testFilterCandidateNoMocks() {
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        Object result = injecter.thenInject();

        verify(mockitoReporter, never()).cannotInjectDependency(field, any(Object.class), any(RuntimeException.class));
        verify(mockitoBeanPropertySetter, never()).set(fieldInstance, field);
        verify(mockitoFieldSetter, never()).set(fieldInstance, field);
        assertNull(result);
    }
}