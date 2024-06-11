
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertEquals(mocks.iterator().next(), injecter.thenInject());
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(field);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = mock(Collection.class);
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(field);
    }

    @Test
    public void testFilterCandidate_propertySetterException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        doThrow(RuntimeException.class).when(propertySetter).set(any());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(field);
        verify(propertySetter, times(1)).set(any());
    }

    @Test
    public void testFilterCandidate_fieldSetterException() {
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Collections.singleton(mock(Object.class));
        Field field = getField("fieldName");
        Object fieldInstance = mock(Object.class);

        BeanPropertySetter propertySetter = mock(BeanPropertySetter.class);
        when(propertySetter.set(any())).thenReturn(false);

        FieldSetter fieldSetter = mock(FieldSetter.class);
        doThrow(RuntimeException.class).when(fieldSetter).set(any());

        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter);
        assertNull(injecter.thenInject());
        verifyZeroInteractions(fieldInstance);
        verifyZeroInteractions(field);
        verify(propertySetter, times(1)).set(any());
        verify(fieldSetter, times(1)).set(any());
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}
