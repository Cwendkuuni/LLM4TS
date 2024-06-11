import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstructorWithClassBindings() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings());
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithIllegalClass() {
        new SimpleType(Map.class);
    }

    @Test
    public void testNarrowMethod() {
        SimpleType simpleTypeInt = new SimpleType(Integer.class);
        SimpleType simpleTypeString = new SimpleType(String.class);

        SimpleType narrowedTypeInt = simpleTypeInt._narrow(Integer.class);
        SimpleType narrowedTypeString = simpleTypeString._narrow(String.class);

        assertEquals(simpleTypeInt, narrowedTypeInt);
        assertEquals(simpleTypeString, narrowedTypeString);

        SimpleType narrowedTypeStringToInt = simpleTypeString._narrow(Integer.class);
        assertNotEquals(simpleTypeString, narrowedTypeStringToInt);
        assertNotEquals(simpleTypeInt, narrowedTypeStringToInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithIllegalClass() {
        SimpleType simpleTypeInt = new SimpleType(Integer.class);
        simpleTypeInt._narrow(String.class);
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();

        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);

        assertNotEquals(simpleType, withTypeHandler);
        assertEquals(handler, withTypeHandler.getTypeHandler());
    }
}