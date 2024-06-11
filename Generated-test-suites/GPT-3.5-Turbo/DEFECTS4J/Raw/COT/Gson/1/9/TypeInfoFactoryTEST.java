import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "java.lang.String[]";
            }
        };
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals("java.lang.String[]", typeInfoArray.getType().getTypeName());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = MyClass.class.getField("myField");
        Type typeDefiningF = MyClass.class;
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertEquals(String.class, typeInfo.getType());
    }

    @Test
    public void testGetActualType_Class() {
        Type type = String.class;
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public String getTypeName() {
                return "java.util.List<java.lang.String>";
            }
        };
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(List.class, actualType);
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type type = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return String.class;
            }

            @Override
            public String getTypeName() {
                return "java.lang.String[]";
            }
        };
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String[].class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable() {
        Type type = new TypeVariable<>() {
            @Override
            public String getName() {
                return "T";
            }

            @Override
            public Type[] getBounds() {
                return new Type[] { String.class };
            }

            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedSuperclass() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedInterfaces() {
                return new AnnotatedType[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public AnnotatedType getAnnotatedOwnerType() {
                return null;
            }

            @Override
            public AnnotatedType[] getAnnotatedActualTypeArguments() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBoundsDirect() {
                return new AnnotatedType[0];
            }

            @Override
            public Class<?> getGenericDeclaration() {
                return null;
            }

            @Override
            public Type[] getAnnotatedLowerBounds() {
                return new Type[0];
            }

            @Override
            public Type[] getAnnotatedUpperBounds() {
                return new Type[0];
            }
        };
        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public String getTypeName() {
                return "java.util.List<java.lang.String>";
            }
        };
        Class<?> rawParentClass = List.class;
        
        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_WildcardType() {
        Type type = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[] { String.class };
            }

            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedUpperBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedLowerBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public AnnotatedType getAnnotatedOwnerType() {
                return null;
            }

            @Override
            public AnnotatedType[] getAnnotatedActualTypeArguments() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBoundsDirect() {
                return new AnnotatedType[0];
            }
        };
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    private static class MyClass {
        public List<String> myField;
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[] { new Type() {
            @Override
            public String getTypeName() {
                return "java.lang.String";
            }
        } };
        Type parentType = null;
        Class<?> rawParentClass = null;
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertEquals(String.class, realTypes[0]);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<Class<?>> typeVariable1 = new TypeVariable<Class<?>>() {
            @Override
            public String getName() {
                return "T";
            }

            @Override
            public Type[] getBounds() {
                return new Type[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedSuperclass() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedInterfaces() {
                return new AnnotatedType[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public AnnotatedType getAnnotatedOwnerType() {
                return null;
            }

            @Override
            public AnnotatedType[] getAnnotatedActualTypeArguments() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBoundsDirect() {
                return new AnnotatedType[0];
            }

            @Override
            public Class<?> getGenericDeclaration() {
                return null;
            }

            @Override
            public Type[] getAnnotatedLowerBounds() {
                return new Type[0];
            }

            @Override
            public Type[] getAnnotatedUpperBounds() {
                return new Type[0];
            }
        };
        TypeVariable<Class<?>> typeVariable2 = new TypeVariable<Class<?>>() {
            @Override
            public String getName() {
                return "U";
            }

            @Override
            public Type[] getBounds() {
                return new Type[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedSuperclass() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedInterfaces() {
                return new AnnotatedType[0];
            }

            @Override
            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            @Override
            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            @Override
            public AnnotatedType getAnnotatedOwnerType() {
                return null;
            }

            @Override
            public AnnotatedType[] getAnnotatedActualTypeArguments() {
                return new AnnotatedType[0];
            }

            @Override
            public AnnotatedType[] getAnnotatedBoundsDirect() {
                return new AnnotatedType[0];
            }

            @Override
            public Class<?> getGenericDeclaration() {
                return null;
            }

            @Override
            public Type[] getAnnotatedLowerBounds() {
                return new Type[0];
            }

            @Override
            public Type[] getAnnotatedUpperBounds() {
                return new Type[0];
            }
        };

        TypeVariable<?>[] types = new TypeVariable[] { typeVariable1, typeVariable2 };
        int index = TypeInfoFactory.getIndex(types, typeVariable2);
        assertEquals(1, index);
    }
}