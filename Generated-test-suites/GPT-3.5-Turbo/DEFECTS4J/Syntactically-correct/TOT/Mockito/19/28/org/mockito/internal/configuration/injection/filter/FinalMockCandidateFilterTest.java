import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnOngoingInjecter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
    }

    @Test
    public void testFilterCandidate_withSingleMockAndInjectSuccess_shouldReturnMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mockObject = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mockObject);
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertEquals(mockObject, result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(mockObject);
        verifyNoMoreInteractions(new FieldSetter(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_withSingleMockAndInjectFailure_shouldReturnMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object mockObject = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(mockObject);
        Field field = getField();
        Object fieldInstance = new Object();
        BeanPropertySetter propertySetterMock = mock(BeanPropertySetter.class);
        doReturn(false).when(propertySetterMock).set(mockObject);
        FieldSetter fieldSetterMock = mock(FieldSetter.class);

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertEquals(mockObject, result);
        verify(propertySetterMock).set(mockObject);
        verify(fieldSetterMock).set(mockObject);
        verifyNoMoreInteractions(new Reporter());
    }

    @Test
    public void testFilterCandidate_withMultipleMocks_shouldReturnNull() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = getField();
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNull(injecter);
    }

    private Field getField() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}