import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("Expected class to be set correctly", stringClass, simpleType.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        SimpleType.construct(Map.class);
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String canonicalName = simpleType.buildCanonicalName();
        String expectedCanonicalName = "[simple type, class java.lang.String]";
        assertEquals("Expected canonical name to be set correctly", expectedCanonicalName, canonicalName);
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse("Expected simple type to not be container type", simpleType.isContainerType());
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String actualString = simpleType.toString();
        String expectedString = "[simple type, class java.lang.String]";
        assertEquals("Expected toString() to return correct string representation", expectedString, actualString);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        assertTrue("Expected simple types to be equal", simpleType1.equals(simpleType2));
    }

    @Test
    public void testNotEqualDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleTypeString = new SimpleType(stringClass);
        SimpleType simpleTypeInteger = new SimpleType(integerClass);
        assertFalse("Expected simple types to not be equal", simpleTypeString.equals(simpleTypeInteger));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNarrowInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Class<Integer> integerClass = Integer.class;
        simpleType.withNarrow(integerClass);
    }
}