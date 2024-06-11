import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    @Test
    public void filterCandidate_whenMocksHasSingleElement_thenReturnsInjecterWithMatchingMock() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final Object mock = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = getFieldObject();
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
        assertSame(mock, getFieldData(fieldInstance, field));
    }

    @Test
    public void filterCandidate_whenMocksIsEmpty_thenReturnsEmptyInjecter() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = new ArrayList<>();
        Field field = getFieldObject();
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void filterCandidate_whenSettingFieldThrowsException_thenThrowsMockitoException() {
        // Arrange
        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        final Object mock = new Object();
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);
        Field field = getFieldObject();
        Object fieldInstance = new Object();

        // Act
        FinalMockCandidateFilter.OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        // Assert
        // MockitoException should be thrown
    }

    // Helper methods

    private Field getFieldObject() {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField("field");
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    private Object getFieldData(Object fieldInstance, Field field) {
        try {
            return new FieldSetter(fieldInstance, field).get();
        } catch (Exception e) {
            return null;
        }
    }
}