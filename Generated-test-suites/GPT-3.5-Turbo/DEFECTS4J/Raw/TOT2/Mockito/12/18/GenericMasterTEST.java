import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;

public class GenericMasterTest {

    GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType() {
        Field field = SampleClass.class.getDeclaredField("genericListField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericTypeNonGenericField() {
        Field field = SampleClass.class.getDeclaredField("nonGenericField");
        Class result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericTypeNullField() {
        Class result = genericMaster.getGenericType(null);
        assertEquals(Object.class, result);
    }
    
    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericTypeNoSuchField() throws NoSuchFieldException {
        Field field = SampleClass.class.getDeclaredField("nonExistentField");
        genericMaster.getGenericType(field);
    }
    
    private class SampleClass {
        List<String> genericListField;
        String nonGenericField;
    }
}