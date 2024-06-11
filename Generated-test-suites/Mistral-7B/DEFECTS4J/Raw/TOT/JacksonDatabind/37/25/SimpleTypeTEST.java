import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType javaType = simpleType.withoutContentTypeHandler(null);

        assertEquals(String.class, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertNull(javaType.getSuperTypes());
        assertFalse(javaType.isContainerType());
        assertNull(javaType.withContentType(null));
        assertNull(javaType.withTypeHandler(null));
        assertNull(javaType.withContentTypeHandler(null));
        assertNull(javaType.withValueHandler(null));
        assertNull(javaType.withContentValueHandler(null));
        assertFalse(javaType.isStatic());
        assertEquals("[simple type, class java.lang.String]", javaType.toString());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<List<String>> listClass = List.class.getTypeParameters()[0].getType();
        SimpleType simpleType = new SimpleType(listClass, TypeBindings.mapType(String.class), null, null);
        JavaType javaType = simpleType.withoutContentTypeHandler(null);

        assertEquals(List.class, javaType.getRawClass());
        assertEquals(TypeBindings.mapType(String.class), javaType.getBindings());
        assertNull(javaType.getSuperClass());
        assertArrayEquals(new Class[]{Collection.class, List.class}, javaType.getSuperTypes());
        assertFalse(javaType.isContainerType());
        assertNull(javaType.withContentType(null));
        assertNull(javaType.withTypeHandler(null));
        assertNull(javaType.withContentTypeHandler(null));
        assertNull(javaType.withValueHandler(null));
        assertNull(javaType.withContentValueHandler(null));
        assertFalse(javaType.isStatic());
        assertEquals("[simple type, class java.util.List<java.lang.String>]", javaType.toString());
    }

    @Test
    public void testSimpleTypeConstructionWithSuperClass() {
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType = new SimpleType(integerClass, TypeBindings.emptyBindings(),
                JavaType.constructNumberType(Integer.TYPE), null);
        JavaType javaType = simpleType.withoutContentTypeHandler(null);

        assertEquals(Integer.class, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getBindings());
        assertEquals(JavaType.constructNumberType(Integer.TYPE), javaType.getSuperClass());
        assertNull(javaType.getSuperTypes());
        assertFalse(javaType.isContainerType());
        assertNull(javaType.withContentType(null));
        assertNull(javaType.withTypeHandler(null));
        assertNull(javaType.withContentTypeHandler(null));
        assertNull(javaType.withValueHandler(null));
        assertNull(javaType.withContentValueHandler(null));
        assertFalse(javaType.isStatic());
        assertEquals("[simple type, class java.lang.Integer]", javaType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMapConstruction() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCollectionConstruction() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArrayConstruction() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testNarrowing() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        SimpleType numberSimpleType = new SimpleType(Integer.TYPE);

        JavaType stringJavaType = stringSimpleType.withoutContentTypeHandler(null);
        JavaType numberJavaType = numberSimpleType.withoutContentTypeHandler(null);

        JavaType narrowedType = stringJavaType._narrow(Integer.TYPE);
        assertEquals(numberJavaType, narrowedType);
    }
}