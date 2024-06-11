import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<List<Integer>> listClass = List.class.getGenericType();
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.emptyBindings());
        assertEquals(List.class, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertEquals(Collection.class, javaType.getRawClass());

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertEquals(SimpleType.class, javaType.getRawClass());

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);

        assertEquals(intClass, simpleType.getRawClass());
        assertEquals(intClass, copiedSimpleType.getRawClass());
        assertNotSame(simpleType, copiedSimpleType);

        assertSame(simpleType.getValueHandler(), copiedSimpleType.getValueHandler());
        assertSame(simpleType.getTypeHandler(), copiedSimpleType.getTypeHandler());
        assertEquals(simpleType.isStatic(), copiedSimpleType.isStatic());

        JavaType simpleTypeSuperclass = simpleType.getSuperClass();
        JavaType copiedSimpleTypeSuperclass = copiedSimpleType.getSuperClass();
        if (simpleTypeSuperclass != null) {
            assertEquals(simpleTypeSuperclass, copiedSimpleTypeSuperclass);
        }

        JavaType simpleTypeGenericSuperclass = simpleType.getGenericSuperclass();
        JavaType copiedSimpleTypeGenericSuperclass = copiedSimpleType.getGenericSuperclass();
        if (simpleTypeGenericSuperclass != null) {
            assertEquals(simpleTypeGenericSuperclass, copiedSimpleTypeGenericSuperclass);
        }

        JavaType simpleTypeInterfaces = simpleType.getInterfaces();
        JavaType copiedSimpleTypeInterfaces = copiedSimpleType.getInterfaces();
        if (simpleTypeInterfaces != null) {
            assertEquals(simpleTypeInterfaces, copiedSimpleTypeInterfaces);
        }

        JavaType simpleTypeComponentType = simpleType.getComponentType();
        JavaType copiedSimpleTypeComponentType = copiedSimpleType.getComponentType();
        if (simpleTypeComponentType != null) {
            assertEquals(simpleTypeComponentType, copiedSimpleTypeComponentType);
        }

        JavaType simpleTypeContentType = simpleType.getContentType();
        JavaType copiedSimpleTypeContentType = copiedSimpleType.getContentType();
        if (simpleTypeContentType != null) {
            assertEquals(simpleTypeContentType, copiedSimpleTypeContentType);
        }
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass, null, null, null, new IntValueHandler(), null, false);
        assertEquals(intClass, simpleType.getRawClass());
        assertSame(new IntValueHandler(), simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass, null, null, null, null, new IntTypeHandler(), false);
        assertEquals(intClass, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertSame(new IntTypeHandler(), simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeWithValueAndTypeHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass, null, null, null, new IntValueHandler(), new IntTypeHandler(), false);
        assertEquals(intClass, simpleType.getRawClass());
        assertSame(new IntValueHandler(), simpleType.getValueHandler());
        assertSame(new IntTypeHandler(), simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass, null, null, null, null, null, true);
        assertEquals(intClass, simpleType.getRawClass());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidConstructorCall() {
        Class<Integer> intClass = Integer.class;
        new SimpleType(intClass, null, null, null, null, null, true, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructUnsafe() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = SimpleType.constructUnsafe(intClass);
        assertNotNull(simpleType);
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstruct() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = SimpleType.construct(intClass);
        assertNotNull(simpleType);
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());

        JavaType javaType = simpleType.getSuperClass();
        assertNull(javaType);

        javaType = simpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = simpleType.getInterfaces();
        assertNull(javaType);

        javaType = simpleType.getComponentType();
        assertNull(javaType);

        javaType = simpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeWithNarrowing() {
        Class<Integer> intClass = Integer.class;
        Class<Short> shortClass = Short.class;
        SimpleType intSimpleType = new SimpleType(intClass);
        SimpleType shortSimpleType = intSimpleType.narrow(shortClass);
        assertEquals(shortClass, shortSimpleType.getRawClass());
        assertSame(intSimpleType.getValueHandler(), shortSimpleType.getValueHandler());
        assertSame(intSimpleType.getTypeHandler(), shortSimpleType.getTypeHandler());
        assertFalse(shortSimpleType.isStatic());

        JavaType javaType = shortSimpleType.getSuperClass();
        assertSame(TypeFactory.unknownType().getRawClass(), javaType.getRawClass());

        javaType = shortSimpleType.getGenericSuperclass();
        assertNull(javaType);

        javaType = shortSimpleType.getInterfaces();
        assertNull(javaType);

        javaType = shortSimpleType.getComponentType();
        assertNull(javaType);

        javaType = shortSimpleType.getContentType();
        assertNull(javaType);
    }

    @Test
    public void testSimpleTypeToString() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType = new SimpleType(intClass);
        String simpleTypeString = simpleType.toString();
        String expected = "[simple type, class " + intClass.getName() + "]";
        assertEquals(expected, simpleTypeString);
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<Integer> intClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(intClass);
        SimpleType simpleType2 = new SimpleType(intClass);
        SimpleType simpleType3 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
        assertFalse(simpleType1.equals(null));
    }
}