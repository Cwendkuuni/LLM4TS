import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidate_singleMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertTrue(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exception() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Object mock = mock(Object.class);
        mocks.add(mock);
        Field field = mock(Field.class);
        Object fieldInstance = new Object();
        when(field.getName()).thenReturn("fieldName");

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        injecter.thenInject();
    }
}