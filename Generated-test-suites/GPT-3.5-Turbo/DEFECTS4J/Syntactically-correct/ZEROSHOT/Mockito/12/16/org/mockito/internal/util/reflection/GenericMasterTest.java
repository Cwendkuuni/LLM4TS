import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setup() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericTypeShouldReturnObjectClassWhenFieldIsNotGeneric() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("notGenericField");
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericTypeShouldReturnGenericTypeWhenFieldIsGeneric() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("genericField");
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(GenericType.class, result);
    }

    @Test
    public void testGetGenericTypeShouldReturnNestedGenericTypeWhenFieldHasNestedGenericType() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("nestedGenericField");
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(NestedGenericType.class, result);
    }
}