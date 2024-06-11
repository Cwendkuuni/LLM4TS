import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.WildcardType;

public class TypeInfoFactoryTest {
    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            public String getTypeName() {
                return "String[]";
            }
        };

        TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(info);
        assertEquals("String[]", info.getTypeName());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Type typeDefiningF = TypeInfoFactoryTest.class;
        Field f = TypeInfoFactoryTest.class.getField("testField");

        TypeInfo info = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        assertNotNull(info);
        assertEquals(TestField.class, info.getActualType());
    }

    @Test
    public void testGetActualType_Class() {
        Type type = String.class;
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        Type type = new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return new Type[] {String.class};
            }

            public Type getOwnerType() {
                return null;
            }

            public Type getRawType() {
                return TypeInfoFactoryTest.class;
            }

            public String getTypeName() {
                return "ParameterizedType<String>";
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertNotNull(actualType);
        assertEquals(ParameterizedType.class, actualType.getClass());
        assertEquals("ParameterizedType<String>", actualType.getTypeName());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        Type type = new GenericArrayType() {
            public Type getGenericComponentType() {
                return String.class;
            }

            public String getTypeName() {
                return "String[]";
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(ParameterizedType.class, actualType.getClass());
        assertEquals("ParameterizedType<String[]>", actualType.getTypeName());
    }

    @Test
    public void testGetActualType_TypeVariable() {
        Type type = new TypeVariable<Class<?>>() {
            public String getName() {
                return "T";
            }

            public Type[] getBounds() {
                return new Type[] {String.class};
            }

            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            public Annotation[] getDeclaredAnnotations() {
                return new Annotation[0];
            }

            public Type[] getLowerBounds() {
                return new Type[0];
            }

            public Type[] getUpperBounds() {
                return getBounds();
            }

            public boolean equals(Object obj) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "T";
            }

            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
                return null;
            }

            public <T extends Annotation> T[] getDeclaredAnnotationsByType(
                    Class<T> annotationClass) {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariableWithoutParameterizedType() {
        Type type = new TypeVariable<Class<?>>() {
            public String getName() {
                return "T";
            }

            public Type[] getBounds() {
                return new Type[0];
            }

            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }

            public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
                return null;
            }

            public Annotation[] getAnnotations() {
                return new Annotation[0];
            }

            public Annotation[] getDeclaredAnnotations() {
                return new Annotation[0];
            }

            public Type[] getLowerBounds() {
                return new Type[0];
            }

            public Type[] getUpperBounds() {
                return getBounds();
            }

            public boolean equals(Object obj) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            public String toString() {
                return "T";
            }

            public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
                return null;
            }

            public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
                return null;
            }

            public <T extends Annotation> T[] getDeclaredAnnotationsByType(
                    Class<T> annotationClass) {
                return null;
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }

    @Test
    public void testGetActualType_WildcardType() {
        Type type = new WildcardType() {
            public Type[] getLowerBounds() {
                return new Type[0];
            }

            public Type[] getUpperBounds() {
                return new Type[] {String.class};
            }

            public String getTypeName() {
                return "<?> extends String";
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
        assertEquals(String.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        Type type = new Type() {
            public String getTypeName() {
                return "InvalidType";
            }
        };
        Type parentType = TypeInfoFactoryTest.class;
        Class<?> rawParentClass = TypeInfoFactoryTest.class;

        TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    }
}