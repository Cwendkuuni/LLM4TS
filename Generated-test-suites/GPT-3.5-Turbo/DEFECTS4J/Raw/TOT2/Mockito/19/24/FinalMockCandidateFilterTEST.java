import org.junit.Test;
import org.mockito.exceptions.Reporter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidateNoCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList();
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidateMultipleCandidates() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(null, result);
    }

    @Test
    public void testFilterCandidateExceptionHandling() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Mocking BeanPropertySetter and FieldSetter to throw an exception
        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenThrow(new RuntimeException());
        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(new RuntimeException()).when(fieldSetter).set(any());

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        assertEquals(null, result);
        verify(propertySetter, times(1)).set(any());
        verify(fieldSetter, times(1)).set(any());
        verify(new Reporter(), times(1)).cannotInjectDependency(eq(field), any(), any());
    }
}