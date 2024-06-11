import org.junit.Test;
import org.mockito.internal.util.reflection.FieldTestHelper;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = new FieldTestHelper().getField("testField", Object.class);
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = String.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;

        genericMaster.getGenericType(field);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeNullGenericType() {
        Field field = new FieldTestHelper().getField("testField", String.class);
        GenericMaster genericMaster = new GenericMaster();
        Field testField = new FieldTestHelper().getField("testInnerField", List.class);
        Field enclosingField = new FieldTestHelper().getEnclosingField(testField);
        Type genericType = enclosingField.getGenericType();

        genericMaster.getGenericType(field);
        genericMaster.getGenericType(null);
    }

    @Test
    public void testGetGenericTypeArray() {
        Field field = new FieldTestHelper().getField("testField", Object[].class);
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = int[].class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypePrimitive() {
        Field field = new FieldTestHelper().getField("testField", int.class);
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = int.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeRawType() {
        Field field = new FieldTestHelper().getField("testField", List.class);
        GenericMaster genericMaster = new GenericMaster();
        Class expectedType = List.class;
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(expectedType, actualType);
    }
}
//