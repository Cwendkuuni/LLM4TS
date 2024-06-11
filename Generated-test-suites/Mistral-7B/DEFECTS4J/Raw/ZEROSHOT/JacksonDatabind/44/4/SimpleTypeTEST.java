package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, null, null, false);

        assertEquals(simpleType1._class, simpleType2._class);
        assertEquals(simpleType1._class, simpleType3._class);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertEquals(simpleType1.hashCode(), simpleType3.hashCode());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(simpleType1);

        assertEquals(simpleType1._class, simpleType2._class);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
    }

    @Test
    public void testSimpleTypeWithTypeBindings() {
        Class<List<Integer>> listOfIntegerClass = List.class.getTypeParameters()[0].getRawType().getComponentType();
        JavaType javaType = TypeFactory.defaultInstance().constructParametricType(List.class, Integer.class);
        SimpleType simpleType1 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        SimpleType simpleType3 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, null, null, null, false);
        TypeBindings typeBindings = TypeBindings.builder().bindValue("IntegerType", Integer.TYPE).build();
        SimpleType simpleType4 = new SimpleType(listOfIntegerClass, typeBindings, null, null);

        assertEquals(simpleType1._class, simpleType2._class);
        assertEquals(simpleType1._class, simpleType3._class);
        assertEquals(simpleType1.hashCode(), simpleType2.hashCode());
        assertEquals(simpleType1.hashCode(), simpleType3.hashCode());
        assertNotEquals(simpleType1.hashCode(), simpleType4.hashCode());
        assertEquals(simpleType1._bindings, TypeBindings.emptyBindings());
        assertEquals(simpleType4._bindings, typeBindings);
    }

    @Test
    public void testSimpleTypeWithSuperClass() {
        Class<List<Integer>> listOfIntegerClass = List.class.getTypeParameters()[0].getRawType().getComponentType();
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), integerClass, null);

        assertNotNull(simpleType2._superClass);
        assertEquals(integerClass, simpleType2._superClass);
    }

    @Test
    public void testSimpleTypeWithSuperInterfaces() {
        Class<List<Integer>> listOfIntegerClass = List.class.getTypeParameters()[0].getRawType().getComponentType();
        Class<Number> numberClass = Number.class;
        SimpleType simpleType1 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(listOfIntegerClass, TypeBindings.emptyBindings(), null, new JavaType[] { numberClass });

        assertNotNull(simpleType2._superInterfaces);
        assertEquals(Arrays.asList(numberClass), Arrays.asList(simpleType2._superInterfaces));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<Map<String, List<String>>> mapClass = Map.class.getTypeParameters()[0].getRawType();
        new SimpleType(mapClass, TypeBindings.emptyBindings(), null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeUnsafeConstruction() {
        Class<Map<String, List<String>>> mapClass = Map.class.getTypeParameters()[0].getRawType();
        SimpleType simpleType = SimpleType.constructUnsafe(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeDeprecatedConstruct() {
        Class<Map<String, List<String>>> mapClass = Map.class.getTypeParameters()[0].getRawType();
        SimpleType simpleType = SimpleType.construct(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithUnsupportedOperation() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withContentType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithUnsupportedOperation2() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithUnsupportedOperation3() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithUnsupportedOperation4() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withContentValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithUnsupportedOperation5() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withStaticTyping();
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleTypeWithNullBindings() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleTypeWithNullSuperClass() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withSuperClass(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleTypeWithNullSuperInterfaces() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withSuperInterfaces(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleTypeWithNullValueHandler() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withValueHandler(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSimpleTypeWithNullTypeHandler() {
        Class<SimpleType> simpleTypeClass = SimpleType.class;
        SimpleType simpleType1 = new SimpleType(simpleTypeClass, TypeBindings.emptyBindings(), null, null);
        simpleType1.withTypeHandler(null);
    }
}