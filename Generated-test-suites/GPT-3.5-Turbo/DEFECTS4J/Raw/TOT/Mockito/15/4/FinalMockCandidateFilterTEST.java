package org.mockito.internal.configuration.injection;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_withSingleMock_shouldReturnSuccessfulInjecter() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        // Assert
        assertTrue(result);
        verify(new FieldSetter(fieldInstance, field), times(1)).set(mock);
    }

    @Test
    public void testFilterCandidate_withNoMock_shouldReturnUnsuccessfulInjecter() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        boolean result = injecter.thenInject();

        // Assert
        assertFalse(result);
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_withFailedInjection_shouldThrowMockitoException() throws Exception {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();
        doThrow(new Exception("Injection failed")).when(new FieldSetter(fieldInstance, field)).set(mock);

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();
    }
}