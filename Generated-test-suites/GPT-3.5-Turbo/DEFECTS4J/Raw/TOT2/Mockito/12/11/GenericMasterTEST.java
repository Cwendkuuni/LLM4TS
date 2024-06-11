import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = getField(); // assume a method to obtain a field
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
}