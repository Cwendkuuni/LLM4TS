import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = FieldTestUtil.getTestField();
        Class<String> expected = String.class;
        Class<Object> actual = new GenericMaster().getGenericType(field);
        assertEquals(expected, actual);
    }
}