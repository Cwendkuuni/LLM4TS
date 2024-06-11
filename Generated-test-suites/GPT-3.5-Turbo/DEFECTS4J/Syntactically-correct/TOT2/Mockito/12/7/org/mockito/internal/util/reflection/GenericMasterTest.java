import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericTypeBasic() {
        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(SomeClass.class.getDeclaredField("someField"));
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericTypeGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(SomeClass.class.getDeclaredField("genericField"));
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericTypeNestedGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        Class result = genericMaster.getGenericType(SomeClass.class.getDeclaredField("nestedGenericField"));
        assertEquals(Object.class, result);
    }
}

class SomeClass {
    List<String> genericField;
    List<List<Integer>> nestedGenericField;
}