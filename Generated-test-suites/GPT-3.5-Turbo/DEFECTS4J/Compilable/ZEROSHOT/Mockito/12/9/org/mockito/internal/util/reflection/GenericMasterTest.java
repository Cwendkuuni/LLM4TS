package org.mockito.internal.util.reflection;
import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field field;

    @Before
    public void setup() {
        genericMaster = new GenericMaster();
        field = null; // Initialize field here
    }

    @Test
    public void testGetGenericType() {
        // Test with generic field
        field = getField("fieldGenericType");
        Class<?> expectedGenericType = String.class;
        Class<?> actualGenericType = genericMaster.getGenericType(field);
        assertEquals(expectedGenericType, actualGenericType);

        // Test with non-generic field
        field = getField("fieldNonGeneric");
        expectedGenericType = Object.class;
        actualGenericType = genericMaster.getGenericType(field);
        assertEquals(expectedGenericType, actualGenericType);

        // Add more test cases for different scenarios
    }

    private Field getField(String fieldName) {
        try {
            return GenericMaster.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }
}