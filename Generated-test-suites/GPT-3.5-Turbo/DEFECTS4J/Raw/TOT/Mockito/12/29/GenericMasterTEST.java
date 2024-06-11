import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_FieldIsNull_ReturnsObjectClass() {
        Class type = genericMaster.getGenericType(null);
        assertEquals(Object.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsNotGeneric_ReturnsObjectClass() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("notGenericField"));
        assertEquals(Object.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsGeneric_ReturnsGenericTypeClass() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("genericField"));
        assertEquals(String.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsNestedGeneric_ReturnsNestedGenericTypeClass() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("nestedGenericField"));
        assertEquals(Integer.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsParameterizedType_ReturnsActualTypeArgument() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("parameterizedField"));
        assertEquals(Long.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsWildcardType_ReturnsObjectClass() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("wildcardField"));
        assertEquals(Object.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsGenericArray_ReturnsArrayType() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("genericArrayField"));
        assertEquals(ArrayList.class, type);
    }

    @Test
    public void testGetGenericType_FieldIsGenericArrayType_ReturnsComponentType() throws NoSuchFieldException {
        Class type = genericMaster.getGenericType(SampleClass.class.getDeclaredField("genericArrayField"));
        assertEquals(String.class, type.getComponentType());
    }

    private static class SampleClass {
        private String notGenericField;
        private List<String> genericField;
        private List<List<Integer>> nestedGenericField;
        private List<Long> parameterizedField;
        private List<?> wildcardField;
        private List<String>[] genericArrayField;
    }
}