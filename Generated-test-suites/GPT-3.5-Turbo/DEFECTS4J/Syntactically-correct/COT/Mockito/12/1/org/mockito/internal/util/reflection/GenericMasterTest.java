import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    private GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType_FieldWithParameterizedType_ReturnsActualType() {
        Field field = getSampleField();

        Class<?> actualType = genericMaster.getGenericType(field);

        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithoutParameterizedType_ReturnsObjectClass() {
        Field field = getSampleFieldWithoutParameterizedType();

        Class<?> actualType = genericMaster.getGenericType(field);

        assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericType_NullField_ReturnsObjectClass() {
        Class<?> actualType = genericMaster.getGenericType(null);
        assertEquals(Object.class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithMultipleNestedGenerics_ReturnsActualType() {
        Field field = getFieldWithMultipleNestedGenerics();
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(Double.class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithPrimitiveType_ReturnsPrimitiveClass() {
        Field field = getFieldWithPrimitiveType();
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(int.class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithArrayType_ReturnsActualType() {
        Field field = getFieldWithArrayType();
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(String[].class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithCollectionType_ReturnsActualType() {
        Field field = getFieldWithCollectionType();
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(Integer.class, actualType);
    }

    @Test
    public void testGetGenericType_FieldWithWildcardType_ReturnsWildcardClass() {
        Field field = getFieldWithWildcardType();
        Class<?> actualType = genericMaster.getGenericType(field);
        assertEquals(Object.class, actualType);
    }

    private Field getSampleField() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            // Other methods of ParameterizedType

        };
        return getFieldWithGenericType(type);
    }

    private Field getSampleFieldWithoutParameterizedType() {
        return getFieldWithGenericType(null);
    }

    private Field getFieldWithGenericType(Type type) {
        Field field = null;
        try {
            field = GenericMaster.class.getDeclaredField("generic");
            field.setAccessible(true);
            field.setGenericType(type);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return field;
    }

    private Field getFieldWithMultipleNestedGenerics() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                ParameterizedType nestedGenericType = new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[] { Double.class };
                    }

                    // Other methods of ParameterizedType
                };
                return new Type[] { nestedGenericType };
            }

            // Other methods of ParameterizedType
        };
        return getFieldWithGenericType(type);
    }

    private Field getFieldWithPrimitiveType() {
        return getFieldWithGenericType(int.class);
    }

    private Field getFieldWithArrayType() {
        return getFieldWithGenericType(String[].class);
    }

    private Field getFieldWithCollectionType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { Integer.class };
            }

            // Other methods of ParameterizedType
        };
        return getFieldWithGenericType(type);
    }

    private Field getFieldWithWildcardType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { Object.class };
            }

            // Other methods of ParameterizedType
        };
        return getFieldWithGenericType(type);
    }
}