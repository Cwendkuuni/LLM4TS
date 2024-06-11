import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = new Field(TestClass.class, "fieldName", int.class);
        Class expectedType = Integer.TYPE;
        Class actualType = genericMaster.getGenericType(field);
        Assert.assertEquals(expectedType, actualType);
    }
}