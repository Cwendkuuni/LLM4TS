
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void filterCandidate_shouldReturnOngoingInjecter_withSetterInjection() throws Exception {
        // Arrange
        Object mock = mock(Object.class);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(injecter.thenInject());

        FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
        assertSame(mock, fieldSetter.getField());

        verifyNoMoreInteractions(mock);
    }

    @Test
    public void filterCandidate_shouldReturnOngoingInjecter_withoutInjection() throws Exception {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(injecter.thenInject());
    }

    @Test
    public void filterCandidate_shouldThrowException_whenInjectingDependencyFails() throws Exception {
        // Arrange
        Object mock = mock(Object.class);
        Field field = FinalMockCandidateFilter.class.getDeclaredField("fieldInstance");
        Object fieldInstance = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        doThrow(new Exception()).when(mock).getName();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        try {
            injecter.thenInject();
            fail("Exception expected");
        } catch (MockitoException e) {
            assertEquals("Problems injecting dependency in fieldInstance", e.getMessage());
            assertNotNull(e.getCause());
        }
        verify(mock).getName();
    }
}

