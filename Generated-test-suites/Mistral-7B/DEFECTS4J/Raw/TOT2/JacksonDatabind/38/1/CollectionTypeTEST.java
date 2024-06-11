package com.example.junit;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeHandler;
import com.fasterxml.jackson.databind.type.impl.StdClassIdResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionTypeTest {
    @Test
    public void testToString() {
        Class<?> listClass = ArrayList.class;
        JavaType elemType = IntegerType.instance;
        CollectionType collectionType = CollectionType.construct(listClass, null, null, null, elemType);
        String actual = collectionType.toString();
        String expected = "[collection type; class " + listClass.getName() + ", contains " + elemType + "]";
        assertEquals(expected, actual);
    }

    @Test
    public void testNullArguments() {
        assertNull(CollectionType.construct(null, null, null, null, null));
        assertNull(CollectionType.construct(new TypeBase(), null));
    }

    @Test
    public void testInvalidClassType() {
        assertNull(CollectionType.construct(String.class, null, null, null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTypeBindings() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, null);
        collectionType.withContentTypeHandler(null);
    }

    @Test
    public void testIncorrectlyTypedArguments() {
        JavaType incorrectType = Object.class;
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, null, incorrectType);
        try {
            collectionType.withContentTypeHandler(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Wrong type for content type handler", e.getMessage());
        }
    }

    @Test
    public void testEmptySuperInterfacesArray() {
        CollectionType collectionType = CollectionType.construct(ArrayList.class, null, null, new JavaType[0], IntegerType.instance);
        assertEquals(new JavaType[0], collectionType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSuperClass() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, null);
        collectionType._narrow(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSuperInterfaces() {
        CollectionType collectionType = new CollectionType(ArrayList.class, null, null, null, null);
        collectionType.refine(ArrayList.class, null, null, null);
    }
}