import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type arrayType = new Type() {
            @Override
            public String getTypeName() {
                return "int[]";
            }
        };
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(arrayType);
        
        assertNotNull(arrayTypeInfo);
        assertEquals(arrayType, arrayTypeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        Field field = TypeInfoFactory.class.getDeclaredFields()[0];
        Type declaringType = TypeInfoFactory.class;
        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(field, declaringType);

        assertNotNull(fieldTypeInfo);
        assertEquals(field.getGenericType(), fieldTypeInfo.getType());
    }

    // Add more test cases for other methods in TypeInfoFactory class...

}