import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = FieldTestUtil.getField(GenericMaster.class, "genericField");
        Class expectedType = String.class;
        Class actualType = new GenericMaster().getGenericType(field);

        assertEquals(expectedType, actualType);
    }
}