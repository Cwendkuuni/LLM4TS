import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.Field;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeBasic() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(String[].class, "fieldName", String.class);
        Class expectedType = String[].class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        genericMaster.getGenericType(field);
    }

    @Test
    public void testGetGenericTypeNonGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(int.class, "fieldName", int.class);
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericTypeNestedField() {
        GenericMaster genericMaster = new GenericMaster();
        Type nestedType = new TypeToken<List<List<String>>>() {}.getType();
        Field field = new Field(nestedType, "fieldName", List.class);
        Class expectedType = List.class;
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }

    @Test(expected = ClassCastException.class)
    public void testGetGenericTypeWithInvalidType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(int[].class, "fieldName", Integer.class);
        genericMaster.getGenericType(field);
    }
}