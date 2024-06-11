import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private TypeInfoFactory typeInfoFactory;

    @Before
    public void setUp() {
        typeInfoFactory = new TypeInfoFactory();
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            // implementation for Type interface
        };

        TypeInfoArray typeInfo = TypeInfoFactory.getTypeInfoForArray(type);

        assertNotNull(typeInfo);
        assertEquals(type, typeInfo.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        Field f = YourClass.class.getDeclaredField("yourField");
        Type typeDefiningF = YourClass.class;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);

        assertNotNull(typeInfo);
        assertEquals(expectedType, typeInfo.getActualType());
    }

    @Test
    public void testGetActualType() {
        Type parentType = new Type() {
            // implementation for Type interface
        };
        Class<?> rawParentClass = YourClass.class;
        Type typeToEvaluate = new Type() {
            // implementation for Type interface
        };

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertEquals(expectedType, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualTypeWithUnsupportedTypeVariable() {
        Type parentType = new Type() {
            // implementation for Type interface
        };
        Class<?> rawParentClass = YourClass.class;
        Type typeToEvaluate = new TypeVariable<>() {
            // implementation for TypeVariable
        };

        TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[] {
            new Type() {
                // implementation for Type interface
            },
            new Type() {
                // implementation for Type interface
            }
        };

        Type[] expectedTypes = new Type[] {
            new Type() {
                // implementation for Type interface
            },
            new Type() {
                // implementation for Type interface
            }
        };

        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments);

        assertArrayEquals(expectedTypes, realTypes);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndexWithMissingTypeVariable() {
        TypeVariable<?>[] types = new TypeVariable<?>[] {
            new TypeVariable<>() {
                // implementation for TypeVariable
            }
        };
        TypeVariable<?> type = new TypeVariable<>() {
            // implementation for TypeVariable
        };

        TypeInfoFactory.getIndex(types, type);
    }
}