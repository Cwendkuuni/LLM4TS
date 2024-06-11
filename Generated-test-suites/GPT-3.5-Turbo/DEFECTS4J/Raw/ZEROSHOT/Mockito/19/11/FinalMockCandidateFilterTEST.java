import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock_success() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);

        Field field = getField();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertEquals(mock, result);

        verify(mockitoReporter, never()).cannotInjectDependency(eq(field), eq(mock), any(RuntimeException.class));
        verify(beanPropertySetter, times(1)).set(eq(mock));
        verify(fieldSetter, never()).set(eq(mock));
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnNull() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock(Object.class));
        mocks.add(mock(Object.class));

        Field field = getField();
        Object fieldInstance = new Object();

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);

        Object result = injecter.thenInject();
        assertNull(result);
    }

    private Field getField() {
        return getClass().getDeclaredFields()[0];
    }

    private Reporter mockitoReporter = mock(Reporter.class);
    private BeanPropertySetter beanPropertySetter = mock(BeanPropertySetter.class);
    private FieldSetter fieldSetter = mock(FieldSetter.class);
}