import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Collection;

import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Arrange
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mocks.size()).thenReturn(1);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        verify(mocks, times(1)).size();
        verify(field, times(1)).getName();
        verify(fieldInstance, times(1)).getClass();
        verify(mocks.iterator().next(), times(1)).getClass();
        verify(mocks.iterator().next(), times(1)).equals(any());
        verify(new BeanPropertySetter(fieldInstance, field), times(1)).set(any());
        verify(new FieldSetter(fieldInstance, field), times(1)).set(any());
        verify(new Reporter(), times(1)).cannotInjectDependency(any(), any(), any());
        assertEquals(mocks.iterator().next(), result);
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Arrange
        Collection<Object> mocks = mock(Collection.class);
        Field field = mock(Field.class);
        Object fieldInstance = mock(Object.class);

        when(mocks.size()).thenReturn(2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        verify(mocks, times(1)).size();
        verify(field, times(1)).getName();
        verify(fieldInstance, never()).getClass();
        verify(mocks.iterator().next(), never()).getClass();
        verify(mocks.iterator().next(), never()).equals(any());
        verify(new BeanPropertySetter(fieldInstance, field), never()).set(any());
        verify(new FieldSetter(fieldInstance, field), never()).set(any());
        verify(new Reporter(), never()).cannotInjectDependency(any(), any(), any());
        assertNull(result);
    }

}