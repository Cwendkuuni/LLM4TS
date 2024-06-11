import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);

        assertEquals(simpleClass.getName(), simpleType.getRawClass().getName());
        assertFalse(simpleType.isContainerType());

        JavaType simpleTypeWithBindings = new SimpleType(simpleClass, TypeBindings.emptyBindings());
        assertEquals(simpleClass.getName(), simpleTypeWithBindings.getRawClass().getName());
        assertFalse(simpleTypeWithBindings.isContainerType());

        JavaType simpleTypeWithSuperClass = new SimpleType(simpleClass, null, JavaType.constructType(List.class, Integer.class), null);
        assertEquals(simpleClass.getName(), simpleTypeWithSuperClass.getRawClass().getName());
        assertFalse(simpleTypeWithSuperClass.isContainerType());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType narrowedType = simpleType._narrow(String[].class);

        assertTrue(narrowedType instanceof SimpleType);
        assertEquals(String[].class, narrowedType.getRawClass());
        assertFalse(narrowedType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeNarrowingInvalidClass() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass);
        JavaType narrowedType = simpleType._narrow(Integer.class);
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<?> simpleClass = String.class;
        SimpleType simpleType = new SimpleType(simpleClass, null, null, null, null, null, false);
        Object handler = new Object() {};

        JavaType typeWithHandler = simpleType.withTypeHandler(handler);
        assertSame(simpleType, typeWithHandler);
        assertNull(typeWithHandler.getTypeHandler());

        JavaType typeWithHandlerSet = new SimpleType(simpleClass, null, null, null, null, handler, false);
        assertNotSame(simpleType, typeWithHandlerSet);
        assertSame(handler, typeWithHandlerSet.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSimpleTypeConstruction() {
        Class<?> simpleClass = Map.class;
        new SimpleType(simpleClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructInvalidClass() {
        new SimpleType(int[].class);
    }
}