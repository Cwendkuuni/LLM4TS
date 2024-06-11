import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Arrays;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_whenMocksSizeIsOne_thenReturnOngoingInjecter() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof OngoingInjecter);
    }

    @Test
    public void testFilterCandidate_whenMocksSizeIsOne_thenInjectMatchingMockIntoField() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Object matchingMock = mock(Object.class);
        Collection<Object> mocks = Arrays.asList(matchingMock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        result.thenInject();

        // Assert
        verify(new FieldSetter(fieldInstance, field)).set(matchingMock);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_whenMocksSizeIsOneAndInjectionFails_thenThrowMockitoException() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        doThrow(new Exception()).when(new FieldSetter(fieldInstance, field)).set(anyObject());
        result.thenInject();

        // Assert
        // MockitoException should be thrown
    }

    @Test
    public void testFilterCandidate_whenMocksSizeIsNotOne_thenReturnOngoingInjecterWithFalseResponse() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = FinalMockCandidateFilter.class.getDeclaredField("mocks");
        Object fieldInstance = new FinalMockCandidateFilter();

        // Act
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(result);
        assertFalse(result.thenInject());
    }
}