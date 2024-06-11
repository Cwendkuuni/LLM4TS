import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("Expected simple type instance with class: Integer", intClass, intType.getType());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<Integer> intClass = Integer.class;
        Class<Long> longClass = Long.class;
        SimpleType intType = new SimpleType(intClass);
        SimpleType longType = intType.narrow(longClass);
        assertFalse("Expected new instance after narrowing", intType == longType);
        assertEquals("Expected long type instance", longClass, longType.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        Class<List<String>> listClass = ArrayList.class;
        new SimpleType(listClass); // This should throw an IllegalArgumentException
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        Object handler = new Object();
        SimpleType newIntType = intType.withTypeHandler(handler);
        assertEquals("Expected new instance after setting type handler", handler, newIntType.getTypeHandler());
    }

    @Test
    public void testSimpleTypeToString() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType = new SimpleType(intClass);
        assertEquals("Expected '[simple type, class java.lang.Integer]'", intType.toString(), intType.toString());
    }

    @Test
    public void testSimpleTypeEquals() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = new SimpleType(intClass);
        SimpleType intType2 = new SimpleType(intClass);
        assertTrue("Expected true for equal instances", intType1.equals(intType2));
    }

    @Test
    public void testSimpleTypeNotEquals() {
        Class<Integer> intClass = Integer.class;
        SimpleType intType1 = new SimpleType(intClass);
        SimpleType stringType = new SimpleType(String.class);
        assertFalse("Expected false for unequal instances", intType1.equals(stringType));
    }
}