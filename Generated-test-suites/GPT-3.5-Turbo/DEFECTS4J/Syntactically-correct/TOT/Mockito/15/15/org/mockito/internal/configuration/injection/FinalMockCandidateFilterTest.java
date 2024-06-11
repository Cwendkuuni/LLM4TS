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
    
    private MockCandidateFilter filter;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_singleMock_returnOngoingInjecter() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        assertTrue(injecter.thenInject());
        assertSame(mock, getField(fieldInstance, field));
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnOngoingInjecterWithFalseInject() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(new Object());
        mocks.add(new Object());
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert
        assertNotNull(injecter);
        assertFalse(injecter.thenInject());
        assertNull(getField(fieldInstance, field));
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidate_exceptionDuringInjection_throwMockitoException() {
        // Arrange
        Collection<Object> mocks = new ArrayList<>();
        Object mock = new Object();
        mocks.add(mock);
        Field field = getField("field");
        Object fieldInstance = new Object();

        // Act
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(injecter);
        assertTrue(injecter.thenInject());

        // Injecting a null value to throw an exception
        FieldSetter.setField(fieldInstance, field, null);

        // Assert
        // Expected MockitoException
    }

    // Helper method to get the Field object by name
    private Field getField(String fieldName) {
        try {
            return FinalMockCandidateFilter.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Field " + fieldName + " not found", e);
        }
    }

    // Helper method to get the value of a Field from an object instance
    private Object getField(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new AssertionError("Unable to access field", e);
        }
    }
}