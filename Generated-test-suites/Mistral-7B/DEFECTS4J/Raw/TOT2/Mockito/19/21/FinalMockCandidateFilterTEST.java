import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Field field;
    private Object fieldInstance;
    private Object mock1;
    private Object mock2;
    private Object mock3;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        field = Mockito.mock(Field.class);
        fieldInstance = Mockito.mock(Object.class);
        mock1 = Mockito.mock(Object.class);
        mock2 = Mockito.mock(Object.class);
        mock3 = Mockito.mock(Object.class);
    }

    @Test
    public void testFilterCandidate_SingleMock() {
        when(mocks.iterator().next()).thenReturn(mock1);

        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
        verify(mock1, times(1)).set(ArgumentMatchers.any(field.getType()), ArgumentMatchers.eq(fieldInstance));
    }

    @Test
    public void testFilterCandidate_MultipleMocks() {
        when(mocks.iterator().next()).thenReturn(mock1);

        OngoingInjecter injecter = filter.filterCandidate(Collections.singletonList(mock1), field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_BeanPropertySetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mock1);

        doAnswer(invocation -> {
            BeanPropertySetter setter = new BeanPropertySetter(fieldInstance, field);
            setter.set(invocation.getArgument(0));
            return null;
        }).when(mock1).set(field, fieldInstance);

        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
        verify(mock1, times(1)).set(field, fieldInstance);
    }

    @Test
    public void testFilterCandidate_BeanPropertySetterFailure() {
        when(mocks.iterator().next()).thenReturn(mock1);

        doThrow(new RuntimeException("Test exception")).when(mock1).set(field, fieldInstance);

        try {
            filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testFilterCandidate_FieldSetterSuccess() {
        when(mocks.iterator().next()).thenReturn(mock1);

        doAnswer(invocation -> {
            FieldSetter setter = new FieldSetter(fieldInstance, field);
            setter.set(invocation.getArgument(0));
            return null;
        }).when(mock1).setAccessible(true);
        doAnswer(invocation -> {
            FieldSetter setter = new FieldSetter(fieldInstance, field);
            setter.set(invocation.getArgument(0), fieldInstance);
            return null;
        }).when(mock1).set(field, ArgumentMatchers.any());

        OngoingInjecter injecter = filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mock1, injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_FieldSetterFailure() {
        when(mocks.iterator().next()).thenReturn(mock1);

        doThrow(new RuntimeException("Test exception")).when(mock1).setAccessible(true);

        try {
            filter.filterCandidate(Arrays.asList(mock1, mock2, mock3), field, fieldInstance);
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }
}