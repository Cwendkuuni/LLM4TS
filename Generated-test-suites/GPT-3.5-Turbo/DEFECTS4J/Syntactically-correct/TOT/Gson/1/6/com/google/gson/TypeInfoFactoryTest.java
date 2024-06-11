import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TypeInfoFactoryTest {

    private TypeInfoFactory typeInfoFactory;

    @Before
    public void setUp() {
        typeInfoFactory = new TypeInfoFactory();
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type[]{String.class};
        TypeInfoArray expected = new TypeInfoArray(type);

        TypeInfoArray actual = typeInfoFactory.getTypeInfoForArray(type);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInformationForField_withTypeVariable() {
        Field field = getClass().getDeclaredFields()[0];
        Type type = field.getGenericType();
        Type parentType = getClass().getGenericSuperclass();
        TypeInfo expected = new TypeInfo(((ParameterizedType) parentType).getActualTypeArguments()[0]);

        TypeInfo actual = typeInfoFactory.getTypeInfoForField(field, parentType);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeInformationForField_withWildcardType() {
        Field field = getClass().getDeclaredFields()[1];
        Type type = field.getGenericType();
        Type parentType = getClass().getGenericSuperclass();
        TypeInfo expected = new TypeInfo(((ParameterizedType) parentType).getActualTypeArguments()[0]);

        TypeInfo actual = typeInfoFactory.getTypeInfoForField(field, parentType);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInformationForField_withInvalidType() {
        Field field = getClass().getDeclaredFields()[2];
        Type type = field.getGenericType();
        Type parentType = getClass().getGenericSuperclass();

        typeInfoFactory.getTypeInfoForField(field, parentType);
    }

    @Test
    public void testGetActualType_withClassType() {
        Type type = String.class;
        Type parentType = getClass().getGenericSuperclass();
        Class<?> rawParentClass = getClass().getSuperclass();
        Type expected = type;

        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_withParameterizedType() {
        Type type = new ParameterizedTypeImpl(ArrayList.class, new Type[]{String.class}, null);
        Type parentType = new ParameterizedTypeImpl(List.class, new Type[]{String.class}, null);
        Class<?> rawParentClass = List.class;
        Type expected = type;

        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_withGenericArrayType() {
        Type type = new GenericArrayTypeImpl(String.class);
        Type parentType = ArrayList.class;
        Class<?> rawParentClass = ArrayList.class;
        Type expected = type;

        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_withTypeVariable() {
        TypeVariable<?> typeVariable = getClass().getTypeParameters()[0];
        Type type = new ParameterizedTypeImpl(String.class, new Type[]{typeVariable}, null);
        Type parentType = new ParameterizedTypeImpl(List.class, new Type[]{type}, null);
        Class<?> rawParentClass = List.class;
        Type expected = typeVariable;

        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetActualType_withWildcardType() {
        Type type = new ParameterizedTypeImpl(List.class, new Type[]{String.class}, null);
        Type parentType = new WildcardTypeImpl(new Type[]{type}, new Type[]{Object.class});
        Class<?> rawParentClass = List.class;
        Type expected = type;

        Type actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withInvalidType() {
        Type type = int.class;
        Type parentType = getClass().getGenericSuperclass();
        Class<?> rawParentClass = getClass().getSuperclass();

        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> rawType;
        private final Type[] actualTypeArguments;
        private final Type ownerType;

        public ParameterizedTypeImpl(Class<?> rawType, Type[] actualTypeArguments, Type ownerType) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
            this.ownerType = ownerType;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }
    }

    private static class GenericArrayTypeImpl implements GenericArrayType {
        private final Type genericComponentType;

        public GenericArrayTypeImpl(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }
    }

    private static class WildcardTypeImpl implements WildcardType {
        private final Type[] upperBounds;
        private final Type[] lowerBounds;

        public WildcardTypeImpl(Type[] upperBounds, Type[] lowerBounds) {
            this.upperBounds = upperBounds;
            this.lowerBounds = lowerBounds;
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds;
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds;
        }
    }
}