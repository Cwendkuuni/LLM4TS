import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {
            @Override
            public String getTypeName() {
                return "TestType";
            }
        };
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertEquals(type, result.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Field field = TypeInfoFactory.class.getDeclaredField("typeDefiningF");
        Type typeDefiningF = field.getGenericType();

        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
        assertEquals(typeDefiningF, result.getType());
    }

    @Test
    public void testGetActualTypeClass() {
        Type type = TypeInfoFactory.getActualType(String.class, String.class, String.class);
        assertEquals(String.class, type);
    }

    @Test
    public void testGetActualTypeParameterizedType() {
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type getRawType() {
                return List.class;
            }
        };

        Type type = TypeInfoFactory.getActualType(parameterizedType, parameterizedType, List.class);
        assertEquals(parameterizedType, type);
    }

    @Test
    public void testGetActualTypeGenericArrayType() {
        GenericArrayType genericArrayType = new GenericArrayType() {
            @Override
            public Type getGenericComponentType() {
                return String.class;
            }
        };

        Type type = TypeInfoFactory.getActualType(genericArrayType, genericArrayType, List.class);
        assertEquals(genericArrayType, type);
    }

    @Test
    public void testGetActualTypeTypeVariable() {
        TypeVariable<?> typeVariable = new TypeVariable<Class<?>>() {
            @Override
            public Type[] getBounds() {
                return new Type[]{Object.class};
            }
            @Override
            public String getName() {
                return "T";
            }
            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }
            @Override
            public AnnotatedElement[] getAnnotatedBoundsJava7() {
                return new AnnotatedElement[0];
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
            public Type[] getUpperBounds() {
                return new Type[]{Object.class};
            }
            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }
            @Override
            public String toString() {
                return "TypeVariable";
            }
            @Override
            public boolean equals(Object obj) {
                return false;
            }
            @Override
            public int hashCode() {
                return 0;
            }
        };

        Type parentType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }
            @Override
            public Type getOwnerType() {
                return null;
            }
            @Override
            public Type getRawType() {
                return List.class;
            }
        };

        Class<?> rawParentClass = List.class;

        try {
            TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Expecting parameterized type, got " + parentType + ".\n Are you missing the use of TypeToken idiom?\n See " + "http://sites.google.com/site/gson/gson-user-guide#TOC-Serializing-and-Deserializing-Gener", e.getMessage());
        }
    }

    @Test
    public void testGetActualTypeWildcardType() {
        WildcardType wildcardType = new WildcardType() {
            @Override
            public Type[] getUpperBounds() {
                return new Type[]{Object.class};
            }
            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }
        };

        Type type = TypeInfoFactory.getActualType(wildcardType, wildcardType, List.class);
        assertEquals(wildcardType, type);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = new Type[]{String.class};

        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, actualTypeArguments, List.class);
        assertArrayEquals(actualTypeArguments, result);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<Class<?>>[] types = new TypeVariable[1];
        TypeVariable<Class<?>> typeVariable = new TypeVariable<Class<?>>() {
            @Override
            public Type[] getBounds() {
                return new Type[]{Object.class};
            }
            @Override
            public String getName() {
                return "T";
            }
            @Override
            public AnnotatedType[] getAnnotatedBounds() {
                return new AnnotatedType[0];
            }
            @Override
            public AnnotatedElement[] getAnnotatedBoundsJava7() {
                return new AnnotatedElement[0];
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
            public Type[] getUpperBounds() {
                return new Type[]{Object.class};
            }
            @Override
            public Type[] getLowerBounds() {
                return new Type[0];
            }
            @Override
            public String toString() {
                return "TypeVariable";
            }
            @Override
            public boolean equals(Object obj) {
                return false;
            }
            @Override
            public int hashCode() {
                return 0;
            }
        };
        types[0] = typeVariable;
        int index = TypeInfoFactory.getIndex(types, typeVariable);
        assertEquals(0, index);
    }
}