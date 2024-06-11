import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNotNull(simpleType.getName());
        assertEquals("java.lang.String", simpleType.getCanonicalName());
        assertEquals("java.lang.String", simpleType.getErasedSignature().toString());
        assertEquals("java.lang.String[]", simpleType.getGenericSignature().toString());
        assertEquals(false, simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = simpleType.narrow(String[].class);
        assertNotNull(narrowedType);
        assertEquals(cls, narrowedType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowingWithInvalidSubClass() {
        Class<?> cls = String[].class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = simpleType.narrow(Integer.class);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object() {};
        SimpleType typeWithHandler = simpleType.withValueHandler(handler);
        assertEquals(handler, typeWithHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType staticType = simpleType.withStaticTyping();
        assertEquals(true, staticType.isStatic());
    }

    @Test
    public void testSimpleTypeRefine() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        JavaType refinedType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidClass2() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidClass3() {
        SimpleType.construct(int[].class);
    }
}