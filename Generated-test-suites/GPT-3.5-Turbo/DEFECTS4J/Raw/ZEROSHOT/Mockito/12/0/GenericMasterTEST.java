import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {
    
    @Test
    public void testGetGenericType_whenFieldIsNull_thenReturnObjectClass() {
        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(null);
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_whenFieldIsNotGeneric_thenReturnObjectClass() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("nonGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_whenFieldIsGeneric_thenReturnParameterizedClass() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("genericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(ParameterizedClass.class, result);
    }
}