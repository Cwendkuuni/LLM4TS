package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.Collection;

import org.junit.Test;

public class CollectionLikeTypeTest {

    @Test
    public void testIsContainerType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        JavaType intCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intType);

        assertTrue(intType.isContainerType());
        assertTrue(intCollectionType.isContainerType());
        assertTrue(intCollectionLikeType.isContainerType());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType stringCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, stringType);

        assertTrue(stringType.isContainerType());
        assertTrue(stringCollectionType.isContainerType());
        assertTrue(stringCollectionLikeType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        JavaType intCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intType);

        assertTrue(intType.isContainerType());
        assertTrue(intCollectionType.isContainerType());
        assertTrue(intCollectionLikeType.isCollectionLikeType());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType stringCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, stringType);

        assertTrue(stringType.isContainerType());
        assertTrue(stringCollectionType.isContainerType());
        assertTrue(stringCollectionLikeType.isCollectionLikeType());
    }

    @Test
    public void testGetContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        JavaType intCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intType);

        assertEquals(intType, intCollectionLikeType.getContentType());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType stringCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, stringType);

        assertEquals(stringType, stringCollectionLikeType.getContentType());
    }

    @Test
    public void testIsTrueCollectionType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        JavaType intCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intType);

        assertFalse(intType.isTrueCollectionType());
        assertTrue(intCollectionType.isTrueCollectionType());
        assertFalse(intCollectionLikeType.isTrueCollectionType());

        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType stringCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, String.class);
        JavaType stringCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, stringType);

        assertFalse(stringType.isTrueCollectionType());
        assertTrue(stringCollectionType.isTrueCollectionType());
        assertFalse(stringCollectionLikeType.isTrueCollectionType());
    }

    @Test
    public void testRefine() {
        Class<List> listClass = List.class;
        JavaType listIntType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        JavaType listIntCollectionType = TypeFactory.defaultInstance().constructType(List.class, int.class);

        JavaType listIntCollectionLikeType = CollectionLikeType.upgradeFrom(listIntType, TypeFactory.defaultInstance().constructType(Collection.class, int.class));

        JavaType refinedListIntCollectionLikeType = listIntCollectionLikeType.refine(listClass, TypeBindings.emptyBindings(), null, null);

        assertEquals(listIntCollectionType, refinedListIntCollectionLikeType);
    }

    @Test
    public void testUpgradeFrom() {
        Class<List> listClass = List.class;
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);
        JavaType intCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, intArrayType);

        JavaType intListType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, intArrayType);
        JavaType intListCollectionLikeType = CollectionLikeType.upgradeFrom(intCollectionLikeType, intListType);

        assertEquals(intListType, intListCollectionLikeType);
    }

    @Test
    public void testConstruct() {
        Class<List> listClass = List.class;
        JavaType intArrayType = TypeFactory.defaultInstance().constructType(int[].class);
        JavaType intCollectionType = TypeFactory.defaultInstance().constructType(Collection.class, int[].class);

        JavaType intListType = CollectionLikeType.construct(listClass, TypeBindings.emptyBindings(), null, null, intArrayType);

        JavaType intListCollectionLikeType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), intListType, null, intArrayType);

        assertEquals(intListType, intListCollectionLikeType.getRawType());
        assertEquals(intArrayType, intListCollectionLikeType.getContentType());
    }

    @Test
    public void testConstructWithTypeBindings() {
        Class<?> rawType = new ArrayList<String>().getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };

        JavaType listType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, elemType);

        assertEquals(rawType, listType.getRawType());
        assertEquals(Collection.class, listType.getSuperClass());
        assertEquals(elemType, listType.getContentType());
    }

    @Test
    public void testConstructWithCustomBindings() {
        Class<?> rawType = new ArrayList<String>() {}.getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        TypeBindings bindings = TypeBindings.create(rawType, elemType);

        JavaType listType = CollectionLikeType.construct(rawType, bindings, superClass, superInterfaces, elemType);

        assertEquals(rawType, listType.getRawType());
        assertEquals(Collection.class, listType.getSuperClass());
        assertEquals(elemType, listType.getContentType());
        assertEquals(bindings, listType._bindings);
    }

    @Test
    public void testConstructWithCustomSuperInterfaces() {
        Class<?> rawType = new ArrayList<String>() {}.getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = List.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class, List.class };

        JavaType listType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, elemType);

        assertEquals(rawType, listType.getRawType());
        assertEquals(List.class, listType.getSuperClass());
        assertEquals(Collection.class, listType.getSuperInterface(0));
        assertEquals(elemType, listType.getContentType());
    }

    @Test
    public void testConstructWithCustomValueHandler() {
        Class<?> rawType = new ArrayList<String>() {}.getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        Object valueHandler = new Object() {
            public String foo(String s) {
                return s.toUpperCase();
            }
        };

        JavaType listType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, elemType, valueHandler);

        assertEquals(rawType, listType.getRawType());
        assertEquals(Collection.class, listType.getSuperClass());
        assertEquals(elemType, listType.getContentType());
        assertEquals(valueHandler, listType._valueHandler);
    }

    @Test
    public void testConstructWithCustomTypeHandler() {
        Class<?> rawType = new ArrayList<String>() {}.getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        Object typeHandler = new Object() {
            public JavaType foo(JavaType t) {
                return TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
            }
        };

        JavaType listType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, elemType, null, typeHandler);

        assertEquals(rawType, listType.getRawType());
        assertEquals(Collection.class, listType.getSuperClass());
        assertEquals(elemType, listType.getContentType());
        assertEquals(typeHandler, listType._typeHandler);
    }

    @Test
    public void testConstructWithCustomValueHandlerAndTypeHandler() {
        Class<?> rawType = new ArrayList<String>() {}.getClass();
        JavaType elemType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType superClass = Object.class;
        JavaType[] superInterfaces = new JavaType[] { Collection.class };
        Object valueHandler = new Object() {
            public String foo(String s) {
                return s.toUpperCase();
            }
        };
        Object typeHandler = new Object() {
            public JavaType foo(JavaType t) {
                return TypeFactory.defaultInstance().constructType(ArrayList.class, String.class);
            }
        };

        JavaType listType = CollectionLikeType.construct(rawType, TypeBindings.emptyBindings(), superClass, superInterfaces, elemType, valueHandler, typeHandler);

        assertEquals(rawType, listType.getRawType());
        assertEquals(Collection.class, listType.getSuperClass());
        assertEquals(elemType, listType.getContentType());
        assertEquals(valueHandler, listType._valueHandler);
        assertEquals(typeHandler, listType._typeHandler);
    }

    @Test
    public void testNarrow() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        JavaType intArrayListType = TypeFactory.defaultInstance().constructType(ArrayList.class, int.class);

        JavaType narrowedListType = listType._narrow(ArrayList.class);

        assertEquals(intArrayListType, narrowedListType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidBaseType() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        JavaType stringType = TypeFactory.defaultInstance().constructType(String.class);

        listType._narrow(stringType);
    }

    @Test
    public void testWithContentType() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        JavaType intArrayListType = TypeFactory.defaultInstance().constructType(ArrayList.class, int.class);

        JavaType newListType = listType.withContentType(intArrayListType);

        assertEquals(intArrayListType, newListType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        Object intHandler = new Object() {
            public int foo(Collection<?> c) {
                return ((Collection<Integer>) c).size();
            }
        };

        JavaType newListType = listType.withTypeHandler(intHandler);

        assertEquals(intHandler, newListType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        Object intHandler = new Object() {
            public int foo(Collection<?> c) {
                return ((Collection<Integer>) c).size();
            }
        };
        Object intContentTypeHandler = new Object() {
            public JavaType foo(JavaType t) {
                return TypeFactory.defaultInstance().constructType(ArrayList.class, int.class);
            }
        };

        JavaType newListType = listType.withContentTypeHandler(intContentTypeHandler);

        assertEquals(intContentTypeHandler, newListType._contentTypeHandler);
    }

    @Test
    public void testWithValueHandler() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        Object intHandler = new Object() {
            public int foo(int i) {
                return i * 2;
            }
        };

        JavaType newListType = listType.withValueHandler(intHandler);

        assertEquals(intHandler, newListType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));
        Object intHandler = new Object() {
            public int foo(int i) {
                return i * 2;
            }
        };
        Object intContentValueHandler = new Object() {
            public int foo(int i) {
                return i + 1;
            }
        };

        JavaType newListType = listType.withContentValueHandler(intContentValueHandler);

        assertEquals(intContentValueHandler, newListType._contentValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));

        JavaType newListType = listType.withStaticTyping();

        assertTrue(newListType._asStatic);
    }

    @Test
    public void testCanonicalName() {
        JavaType listType = CollectionLikeType.construct(Collection.class, TypeBindings.emptyBindings(), null, null, TypeFactory.defaultInstance().constructType(int.class));

        assertEquals("[Ljava.util.Collection;Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;Z", listType.buildCanonicalName());
    }
}