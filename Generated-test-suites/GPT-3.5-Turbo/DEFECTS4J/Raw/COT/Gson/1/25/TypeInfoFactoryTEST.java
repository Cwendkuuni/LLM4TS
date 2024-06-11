
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private TypeInfoFactory factory;

    @Before
    public void setUp() {
        factory = new TypeInfoFactory();
    }

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new String[]{}.getClass();
        TypeInfoArray typeInfoArray = factory.getTypeInfoForArray(type);
        assertNotNull(typeInfoArray);
        assertEquals(type, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        try {
            class TestClass<T> {
                T field;
            }
            Type typeDefiningF = TestClass.class;
            Field field = typeDefiningF.getDeclaredField("field");
            TypeInfo typeInfo = factory.getTypeInfoForField(field, typeDefiningF);
            assertNotNull(typeInfo);
            assertEquals(field.getGenericType(), typeInfo.getType());
        } catch (NoSuchFieldException e) {
            fail("Field not found");
        }
    }

    @Test
    public void testGetActualType_withClassType() {
        Class<?> typeToEvaluate = String.class;
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = factory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withParameterizedType() {
        ParameterizedType typeToEvaluate = new ParameterizedTypeImpl(String.class, new Type[0], null);
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = factory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withGenericArrayType() {
        GenericArrayType typeToEvaluate = new GenericArrayTypeImpl(String.class);
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = factory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(typeToEvaluate, actualType);
    }

    @Test
    public void testGetActualType_withTypeVariable() {
        TypeVariable<?> typeToEvaluate = new TypeVariableImpl();
        ParameterizedType parentType = new ParameterizedTypeImpl(TestClass.class,
                new Type[]{typeToEvaluate}, null);
        Class<?> rawParentClass = TestClass.class;
        Type actualType = factory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_withTypeVariable_noParentType() {
        TypeVariable<?> typeToEvaluate = new TypeVariableImpl();
        factory.getActualType(typeToEvaluate, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_withUnsupportedType() {
        factory.getActualType(new Object(), null, null);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{String.class};
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type[] retTypes = factory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(retTypes);
        assertEquals(actualTypeArguments.length, retTypes.length);
        assertEquals(String.class, retTypes[0]);
    }

    @Test
    public void testGetIndex_withTypePresent() {
        TypeVariable<?>[] types = new TypeVariable<?>[]{new TypeVariableImpl()};
        TypeVariable<?> type = new TypeVariableImpl();
        int index = factory.getIndex(types, type);
        assertEquals(0, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetIndex_withTypeNotPresent() {
        TypeVariable<?>[] types = new TypeVariable<?>[]{new TypeVariableImpl()};
        TypeVariable<?> type = new TypeVariableImpl();
        factory.getIndex(types, type);
    }
}

class TypeVariableImpl implements TypeVariable {

    @Override
    public Type[] getBounds() {
        return new Type[]{String.class};
    }

    @Override
    public String getName() {
        return "T";
    }

    @Override
    public TypeVariable[] getAnnotatedBounds() {
        return new TypeVariable[0];
    }

    @Override
    public <T extends java.lang.annotation.Annotation> T getAnnotation(Class<T> annotationClass) {
        return null;
    }

    @Override
    public java.lang.annotation.Annotation[] getAnnotations() {
        return new java.lang.annotation.Annotation[0];
    }

    @Override
    public java.lang.annotation.Annotation[] getDeclaredAnnotations() {
        return new java.lang.annotation.Annotation[0];
    }

    @Override
    public java.lang.reflect.AnnotatedType getAnnotatedOwnerType() {
        return null;
    }

    @Override
    public Type[] getLowerBounds() {
        return new Type[0];
    }

    @Override
    public TypeVariable[] getTypeParameters() {
        return new TypeVariable[0];
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

class ParameterizedTypeImpl implements ParameterizedType {

    private final Type rawType;
    private final Type[] actualTypeArguments;
    private final Type ownerType;

    public ParameterizedTypeImpl(Type rawType, Type[] actualTypeArguments, Type ownerType) {
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(rawType.getTypeName());
        if (actualTypeArguments.length > 0) {
            stringBuilder.append("<");
            for (int i = 0; i < actualTypeArguments.length; i++) {
                Type actualTypeArgument = actualTypeArguments[i];
                stringBuilder.append(actualTypeArgument.getTypeName());
                if (i != actualTypeArguments.length - 1) {
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(">");
        }
        return stringBuilder.toString();
    }
}

class GenericArrayTypeImpl implements GenericArrayType {

    private final Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        this.genericComponentType = genericComponentType;
    }

    @Override
    public Type getGenericComponentType() {
        return genericComponentType;
    }

    @Override
    public String toString() {
        return genericComponentType.getTypeName() + "[]";
    }
}

class TypeInfoArray {
    private final Type type;

    public TypeInfoArray(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

class TypeInfo {

    private final Type type;

    public TypeInfo(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

class Preconditions {
    public static void checkArgument(boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }
}

class TypeUtils {

    public static boolean isArray(Type type) {
        return type instanceof Class && ((Class<?>) type).isArray();
    }

    public static Class<?> toRawClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            throw new IllegalArgumentException("Type is not a Class");
        }
    }

    public static Class<?>[] wrapWithArray(Class<?>... classes) {
        return classes;
    }
}
