package org.mockito.internal.util.reflection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {
    
    private GenericMaster genericMaster;
    
    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }
    
    @Test
    public void testGetGenericTypeNonGenericField() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("nonGenericField");
        Class<?> result = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericTypeGenericField() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("genericField");
        Class<?> result = genericMaster.getGenericType(field);
        Assert.assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericTypeNestedGenericField() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("nestedGenericField");
        Class<?> result = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, result);
    }
}