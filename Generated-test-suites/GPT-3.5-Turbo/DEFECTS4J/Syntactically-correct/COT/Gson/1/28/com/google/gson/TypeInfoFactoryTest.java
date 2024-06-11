
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TypeInfoFactoryTest {

    @Test
    void getTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(new int[5]);
        assertNotNull(typeInfoArray);
        assertEquals(int[].class, typeInfoArray.getType());
    }

    @Test
    void getTypeInfoForField() {
        class TestClass {
            private String strField;
            private int intField;
            private double doubleField;
        }

        TestClass testClass = new TestClass();
        TypeInfo typeInfoStrField = TypeInfoFactory.getTypeInfoForField(
                testClass.getClass().getDeclaredFields()[0], TestClass.class);
        assertNotNull(typeInfoStrField);
        assertEquals(String.class, typeInfoStrField.getType());

        TypeInfo typeInfoIntField = TypeInfoFactory.getTypeInfoForField(
                testClass.getClass().getDeclaredFields()[1], TestClass.class);
        assertNotNull(typeInfoIntField);
        assertEquals(int.class, typeInfoIntField.getType());

        TypeInfo typeInfoDoubleField = TypeInfoFactory.getTypeInfoForField(
                testClass.getClass().getDeclaredFields()[2], TestClass.class);
        assertNotNull(typeInfoDoubleField);
        assertEquals(double.class, typeInfoDoubleField.getType());
    }
}
