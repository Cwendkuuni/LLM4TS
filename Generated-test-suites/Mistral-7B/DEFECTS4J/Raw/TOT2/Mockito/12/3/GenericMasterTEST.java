import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    void testGetGenericTypeWithGenericField() {
        Field field = FieldTestUtil.getField(GenericMaster.class, "someField"); // Assuming "someField" is a generic field
        Class expectedType = String.class; // Assuming "someField" is of type List<String>
        Class actualType = new GenericMaster().getGenericType(field);
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    void testGetGenericTypeWithNonGenericField() {
        Field field = FieldTestUtil.getField(GenericMaster.class, "someNonGenericField"); // Assuming "someNonGenericField" is not a generic field
        Class expectedType = Object.class;
        Class actualType = new GenericMaster().getGenericType(field);
        Assertions.assertEquals(expectedType, actualType);
    }

    @Test
    void testGetGenericTypeWithNullField() {
        Field field = null;
        Class expectedType = Object.class;
        Class actualType = new GenericMaster().getGenericType(field);
        Assertions.assertEquals(expectedType, actualType);
    }
}