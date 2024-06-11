import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubbing.OngoingInjecter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_WithSingleMock_ShouldInjectMockUsingPropertySetter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertEquals(mock, result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);
        verifyNoMoreInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithSingleMock_ShouldInjectMockUsingFieldAccessIfPropertySetterFails() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = getField("field");
        Object fieldInstance = new Object();

        doReturn(false).when(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertEquals(mock, result);
        verify(new BeanPropertySetter(fieldInstance, field)).set(matchingMock);
        verify(new FieldSetter(fieldInstance, field)).set(matchingMock);
        verifyNoMoreInteractions(fieldInstance);
    }

    @Test
    public void testFilterCandidate_WithMultipleMocks_ShouldNotInjectAnything() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        // Assert
        assertNull(result);
        verifyZeroInteractions(fieldInstance);
    }

    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

}