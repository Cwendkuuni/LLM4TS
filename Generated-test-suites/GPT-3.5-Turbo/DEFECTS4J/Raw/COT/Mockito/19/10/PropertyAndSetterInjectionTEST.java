
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

    private PropertyAndSetterInjection propertyAndSetterInjection;

    @Before
    public void setUp() {
        propertyAndSetterInjection = new PropertyAndSetterInjection();
    }

    @Test
    public void processInjection_shouldInitializeInjectMocksField() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        field.setAccessible(true);
        Set<Object> mockCandidates = (Set<Object>) field.get(propertyAndSetterInjection);

        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(propertyAndSetterInjection, field);

        Object injectMocksFieldOwner = new Object();

        // Act
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(result);
    }

    @Test
    public void processInjection_shouldInjectMockCandidates_onPass1() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        field.setAccessible(true);
        Set<Object> mockCandidates = (Set<Object>) field.get(propertyAndSetterInjection);

        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(propertyAndSetterInjection, field);

        Object injectMocksFieldOwner = new Object();

        // Act
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(result);
    }

    @Test
    public void processInjection_shouldInjectMockCandidates_onPass2() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        field.setAccessible(true);
        Set<Object> mockCandidates = (Set<Object>) field.get(propertyAndSetterInjection);

        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(propertyAndSetterInjection, field);

        Object injectMocksFieldOwner = new Object();

        // Act
        boolean result = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

        // Assert
        assertTrue(result);
    }

    @Test
    public void injectMockCandidatesOnFields_shouldInjectMockCandidates() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Field field = PropertyAndSetterInjection.class.getDeclaredField("mockCandidates");
        field.setAccessible(true);
        Set<Object> mockCandidates = (Set<Object>) field.get(propertyAndSetterInjection);

        Field injectMocksField = PropertyAndSetterInjection.class.getDeclaredField("injectMocksField");
        injectMocksField.setAccessible(true);
        injectMocksField.set(propertyAndSetterInjection, field);

        Object injectMocksFieldOwner = new Object();

        boolean injectionOccurred = false;
        List<Field> orderedInstanceFields = new ArrayList<>();
        orderedInstanceFields.add(PropertyAndSetterInjection.class.getDeclaredField("mockCandidateFilter"));

        // Act
        boolean result = propertyAndSetterInjection.injectMockCandidatesOnFields(mockCandidates, injectMocksFieldOwner, injectionOccurred, orderedInstanceFields);

        // Assert
        assertTrue(result);
    }

    @Test
    public void orderedInstanceFieldsFrom_shouldReturnOrderedInstanceFields() {
        // Act
        List<Field> result = propertyAndSetterInjection.orderedInstanceFieldsFrom(PropertyAndSetterInjection.class);

        // Assert
        assertEquals(1, result.size());
        assertEquals("mockCandidateFilter", result.get(0).getName());
    }

}
