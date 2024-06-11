import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter finalMockCandidateFilter = new FinalMockCandidateFilter();


    @Test
    public void testFilterCandidate_singleMock_shouldReturnMatchingMock() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testFilterCandidate_multipleMocks_shouldReturnNull() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        mocks.add(Mockito.mock(Object.class));
        Field field = null;
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_unableToInjectPropertySetter_shouldUseFieldAccess() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();
        BeanPropertySetter propertySetterMock = Mockito.mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = Mockito.mock(FieldSetter.class);
        Mockito.when(propertySetterMock.set(mock)).thenReturn(false);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
        Mockito.verify(propertySetterMock).set(mock);
        Mockito.verify(fieldSetterMock).set(mock);
    }

    @Test
    public void testFilterCandidate_unableToInjectPropertySetterAndFieldAccess_shouldThrowException() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = null;
        Object fieldInstance = new Object();
        BeanPropertySetter propertySetterMock = Mockito.mock(BeanPropertySetter.class);
        FieldSetter fieldSetterMock = Mockito.mock(FieldSetter.class);
        Mockito.when(propertySetterMock.set(mock)).thenReturn(false);
        Mockito.when(fieldSetterMock.set(mock)).thenThrow(new RuntimeException());
        Reporter reporterMock = Mockito.mock(Reporter.class);

        // Act
        FinalMockCandidateFilter.OngoingInjecter ongoingInjecter = finalMockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        Object result = ongoingInjecter.thenInject();

        // Assert
        assertNotNull(result);
        assertEquals(mock, result);
        Mockito.verify(propertySetterMock).set(mock);
        Mockito.verify(fieldSetterMock).set(mock);
        Mockito.verify(reporterMock).cannotInjectDependency(field, mock, new RuntimeException());
    }

}