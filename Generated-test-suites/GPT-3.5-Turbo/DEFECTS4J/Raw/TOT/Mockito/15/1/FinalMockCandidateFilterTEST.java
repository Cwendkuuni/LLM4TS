import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter mockCandidateFilter;

    @Before
    public void setUp() {
        mockCandidateFilter = new FinalMockCandidateFilter();
    }

    @Test
    public void filterCandidate_WithSingleMock_ShouldReturnInjecterSetToTrue() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        
        Field field = getFieldFromClass("fieldInstance");
        Object fieldInstance = new Object();
        
        // Act
        MockCandidateFilter.OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertTrue(result.thenInject());
    }

    @Test
    public void filterCandidate_WithMultipleMocks_ShouldReturnInjecterSetToFalse() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mocks.add(mock1);
        mocks.add(mock2);
        
        Field field = getFieldFromClass("fieldInstance");
        Object fieldInstance = new Object();
        
        // Act
        MockCandidateFilter.OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        
        // Assert
        assertFalse(result.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void filterCandidate_WithExceptionOnFieldSetter_ShouldThrowMockitoException() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        
        Field field = getFieldFromClass("fieldInstance");
        Object fieldInstance = new Object();
        
        // Act
        MockCandidateFilter.OngoingInjecter result = mockCandidateFilter.filterCandidate(mocks, field, fieldInstance);
        result.thenInject();
        
        // Assert is done by expected exception annotation
    }

    private Field getFieldFromClass(String fieldName) {
        Field[] fields = FinalMockCandidateFilter.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }
}