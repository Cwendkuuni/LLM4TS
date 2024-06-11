
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class GenericMasterTest {
    
    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMasterTest.class.getDeclaredField("dummyField");
        Class expected = Object.class;
        Class actual = genericMaster.getGenericType(field);
        assertEquals(expected, actual);
    }
}
