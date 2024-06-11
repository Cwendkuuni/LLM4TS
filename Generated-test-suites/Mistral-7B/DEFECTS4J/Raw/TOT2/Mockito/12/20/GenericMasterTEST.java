import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = FieldTestUtil.getField(GenericMaster.class, "fieldName"); // replace "fieldName" with actual field name
        Class expectedType = String.class; // replace with actual expected type
        Class actualType = genericMaster.getGenericType(field);
        assertEquals(expectedType, actualType);
    }
}