import com.google.gson.*;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private Field field;
    private Class<?> classDefiningF;

    @Before
    public void setUp() throws Exception {
        field = TypeInfoFactory.class.getDeclaredField("getTypeInfoForField");
        classDefiningF = TypeInfoFactory.class;
    }

    @Test
    public void getTypeInfoForArray_PositiveCase_ReturnsTypeInfoArrayInstance() {
        Type type = new Object[10].getClass();

        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);

        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeInfoForArray_NegativeCase_ThrowsIllegalArgumentException() {
        Type type = String.class;

        TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test
    public void getTypeInfoForField_PositiveCase_ReturnsTypeInfoInstance() {
        TypeVariable<?> typeVariable = field.getGenericType();
        Type actualType = TypeInfoFactory.getActualType(typeVariable, null, classDefiningF);

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, actualType);

        assertNotNull(typeInfo);
        assertEquals(actualType, typeInfo.getType());
    }

    @Test
    public void getTypeInfoForField_NegativeCase_ReturnsTypeInfoInstance() {
        Type type = TypeInfoFactory.class;

        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type);

        assertNotNull(typeInfo);
        assertNotEquals(type, typeInfo.getType());
    }

    @Test
    public void getActualType_PositiveCase_TypeToEvaluateIsClass_ReturnsTypeToEvaluate() {
        Class<?> typeToEvaluate = String.class;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);

        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void getActualType_PositiveCase_TypeToEvaluateIsParameterizedType_ReturnsParameterizedType() {
        TypeVariable<?> typeVariable = field.getGenericType();
        Type ownerType = classDefiningF;

        Type actualType = TypeInfoFactory.getActualType(typeVariable, ownerType, classDefiningF);

        assertNotNull(actualType);
        assertTrue(actualType instanceof ParameterizedType);
    }

    @Test
    public void getActualType_PositiveCase_TypeToEvaluateIsGenericArrayType_ReturnsGenericArrayType() {
        Type typeToEvaluate = new Object[10].getClass();

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);

        assertNotNull(actualType);
        assertTrue(actualType instanceof GenericArrayType);
    }

    @Test
    public void getActualType_PositiveCase_TypeToEvaluateIsTypeVariable_ReturnsTypeVariableActualType() {
        Field[] fields = classDefiningF.getDeclaredFields();
        Field f = fields[1];
        Type type = f.getType();
        Type typeToEvaluate = ((TypeVariable<?>) type).getBounds()[0];
        Type parentType = classDefiningF;
        Class<?> rawParentClass = classDefiningF;

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);

        assertNotNull(actualType);
        assertNotEquals(typeToEvaluate, actualType);
    }

    @Test
    public void getActualType_PositiveCase_TypeToEvaluateIsWildcardType_ReturnsActualType() {
        Type typeToEvaluate = TypeUtils.getParameterizedType(List.class, String.class);

        Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, null, null);

        assertNotNull(actualType);
        assertNotEquals(typeToEvaluate, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getActualType_NegativeCase_TypeToEvaluateIsInvalid_ThrowsIllegalArgumentException() {
        Type typeToEvaluate = new Type() {
            @Override
            public String getName() {
                return null;
            }
        };

        TypeInfoFactory.getActualType(typeToEvaluate, null, null);
    }

    @Test
    public void extractRealTypes_PositiveCase_ReturnsArrayWithActualTypes() {
        Type[] actualTypeArguments = new Type[] {String.class, Integer.class};
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;

        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);

        assertNotNull(realTypes);
        assertEquals(actualTypeArguments.length, realTypes.length);
        assertNotEquals(actualTypeArguments, realTypes);
    }

    @Test(expected = NullPointerException.class)
    public void extractRealTypes_NegativeCase_ActualTypeArgumentsNull_ThrowsNullPointerException() {
        Type[] actualTypeArguments = null;
        Type parentType = TypeInfoFactory.class;
        Class<?> rawParentClass = TypeInfoFactory.class;

        TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
    }

    @Test
    public void getIndex_PositiveCase_ReturnsIndexOfTypeVariable() {
        TypeVariable<?>[] types = classDefiningF.getTypeParameters();
        TypeVariable<?> type = types[0];

        int index = TypeInfoFactory.getIndex(types, type);

        assertNotEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void getIndex_NegativeCase_TypeNotPresentInClassDeclaration_ThrowsIllegalStateException() {
        TypeVariable<?>[] types = classDefiningF.getTypeParameters();
        TypeVariable<?> type = new TypeVariable<TypeVariable<?>>() {
            @Override
            public Type[] getBounds() {
                return new Type[0];
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public TypeVariable<? extends Class<?>> getGenericDeclaration() {
                return null;
            }

            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public Annotation[] getDeclaredAnnotations() {
                return new Annotation[0];
            }

            @Override
            public String getTypeName() {
                return null;
            }
        };
        TypeInfoFactory.getIndex(types, type);
    }
}