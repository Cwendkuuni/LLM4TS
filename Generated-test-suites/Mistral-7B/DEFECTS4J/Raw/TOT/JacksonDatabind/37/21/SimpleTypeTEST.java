import com.fasterxml.jackson.core.type.TypeBindings;
import com.fasterxml.jackson.core.type.SimpleType;
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals(intClass, intType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), intType.getBindings());
        assertNull(intType.getSuperClass());
        assertNull(intType.getSuperInterfaces());
        assertEquals(0, intType.getParameterCount());
        assertEquals(false, intType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithBindings() {
        Class<List<String>> stringListClass = List.class.getGenericType();
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(String.class, JavaType.constructCollectionType(List.class, String.class));
        JavaType stringListType = new SimpleType(stringListClass, bindings, null, null);
        assertEquals(stringListClass, stringListType.getRawClass());
        assertEquals(bindings, stringListType.getBindings());
        assertEquals(null, stringListType.getSuperClass());
        assertEquals(null, stringListType.getSuperInterfaces());
        assertEquals(0, stringListType.getParameterCount());
        assertEquals(false, stringListType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intTypeCopy = new SimpleType(intType);
        assertEquals(intClass, intTypeCopy.getRawClass());
        assertEquals(intType.getBindings(), intTypeCopy.getBindings());
        assertEquals(intType.getSuperClass(), intTypeCopy.getSuperClass());
        assertArrayEquals(intType.getSuperInterfaces(), intTypeCopy.getSuperInterfaces());
        assertEquals(intType.getParameterCount(), intTypeCopy.getParameterCount());
        assertEquals(intType.isStatic(), intTypeCopy.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidClassConstruction() {
        Class<Map<String, Integer>> mapClass = Map.class.getGenericType();
        new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeMethod() {
        Class<String> stringClass = String.class;
        SimpleType stringType = SimpleType.constructUnsafe(stringClass);
        assertEquals(stringClass, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertEquals(0, stringType.getParameterCount());
        assertEquals(false, stringType.isStatic());
    }

    @Test(expected = Deprecated.class)
    public void testConstructDeprecatedMethod() {
        Class<List<String>> stringListClass = List.class.getGenericType();
        SimpleType stringListType = SimpleType.construct(stringListClass);
        assertEquals(stringListClass, stringListType.getRawClass());
        // The bindings should be empty since we cannot create them here
        assertEquals(TypeBindings.emptyBindings(), stringListType.getBindings());
        assertNull(stringListType.getSuperClass());
        assertNull(stringListType.getSuperInterfaces());
        assertEquals(0, stringListType.getParameterCount());
        assertEquals(false, stringListType.isStatic());
    }

    @Test
    public void testNarrowMethod() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType intSubclassType = new SimpleType(Integer.class, TypeBindings.emptyBindings(), null, null);
        JavaType narrowedType = intType._narrow(intSubclassType.getRawClass());
        assertEquals(intSubclassType, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidClass() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        Class<String> stringClass = String.class;
        JavaType narrowedType = intType._narrow(stringClass);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType stringType = new SimpleType(String.class);
        assertEquals(null, stringType.withContentTypeHandler(null));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType intType = new SimpleType(Integer.class);
        SimpleType intTypeWithHandler = intType.withTypeHandler(new Object() {});
        assertEquals(intType.getRawClass(), intTypeWithHandler.getRawClass());
        assertNotSame(intType.getTypeHandler(), intTypeWithHandler.getTypeHandler());
        assertEquals(intType.getBindings(), intTypeWithHandler.getBindings());
        assertEquals(intType.getSuperClass(), intTypeWithHandler.getSuperClass());
        assertArrayEquals(intType.getSuperInterfaces(), intTypeWithHandler.getSuperInterfaces());
        assertEquals(intType.getParameterCount(), intTypeWithHandler.getParameterCount());
        assertEquals(intType.isStatic(), intTypeWithHandler.isStatic());
    }
}