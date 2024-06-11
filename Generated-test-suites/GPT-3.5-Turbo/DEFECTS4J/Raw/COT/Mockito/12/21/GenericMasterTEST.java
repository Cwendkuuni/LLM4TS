
import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {
    
    @Test
    public void testGetGenericType_returnsObjectClass_ifFieldNotGeneric() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("field");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_returnsActualTypeArgument_ifFieldIsGeneric() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("genericField");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericType_returnsNestedGenericType_ifFieldIsNestedGeneric() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = genericMaster.getClass().getDeclaredField("nestedGenericField");
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Integer.class, result);
    }
    
}

