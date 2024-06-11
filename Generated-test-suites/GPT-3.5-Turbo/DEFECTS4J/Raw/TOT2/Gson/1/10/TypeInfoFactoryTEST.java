import static org.junit.Assert.*;
import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new TypeToken<String[]>() {}.getType();
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        Field field = SampleClass.class.getDeclaredField("sampleField");
        Type type = new TypeToken<Integer>() {}.getType();
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type);
        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForFieldWithInvalidType() {
        Field field = SampleClass.class.getDeclaredField("sampleField");
        Type type = new TypeToken<String>() {}.getType();
        TypeInfoFactory.getTypeInfoForField(field, type);
    }
}

class SampleClass {
    private Integer sampleField;
}