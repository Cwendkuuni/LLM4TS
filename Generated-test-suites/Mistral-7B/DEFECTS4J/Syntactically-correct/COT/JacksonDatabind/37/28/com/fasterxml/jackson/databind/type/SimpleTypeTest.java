import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Test constructor with Class argument
    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test constructor with Class, TypeBindings, superClass, and superInterfaces arguments
    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<Integer> integerClass = Integer.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType simpleType = new SimpleType(integerClass, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertNotNull(simpleType);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(Object.class, simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
    }

    // Test copy constructor
    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType original = new SimpleType(stringClass);
        SimpleType copy = new SimpleType(original);
        assertNotNull(copy);
        assertEquals(original.getRawClass(), copy.getRawClass());
    }

    // Test constructUnsafe method
    @Test
    public void testSimpleTypeConstructUnsafe() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(stringClass);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    // Test construct method (deprecated)
    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructDeprecated() {
        Class<String> stringClass = String.class;
        SimpleType.construct(stringClass);
    }

    // Test _narrow method
    @Test
    public void testSimpleTypeNarrow() {
        Class<String> stringClass = String.class;
        Class<StringBuilder> stringBuilderClass = StringBuilder.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType narrowedType = simpleType._narrow(stringBuilderClass);
        assertNotNull(narrowedType);
        assertEquals(StringBuilder.class, narrowedType.getRawClass());
    }

    // Test isContainerType method
    @Test
    public void testSimpleTypeIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    // Test getErasedSignature method
    @Test
    public void testSimpleTypeGetErasedSignature() {
        Class<Integer[][]> matrixClass = Integer[][].class;
        SimpleType simpleType = new SimpleType(matrixClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String expected = "[I[][]";
        assertEquals(expected, sb.toString());
    }

    // Test getGenericSignature method
    @Test
    public void testSimpleTypeGetGenericSignature() {
        Class<Map<String, List<Integer>>> mapClass = Map.class.getTypeParameters()[0].getRawType();
        JavaType mapType = TypeFactory.defaultInstance().constructType(mapClass);
        SimpleType simpleType = new SimpleType(mapType);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String expected = "<KS<KS extends JavaType & ParameterizedType>K, V extends JavaType & ParameterizedType>V>";
        assertEquals(expected, sb.toString());
    }

    // Test toString method
    @Test
    public void testSimpleTypeToString() {
        Class<List<List<List<String>>>> nestedListsClass = List.class.getGenericType();
        JavaType nestedListsType = TypeFactory.defaultInstance().constructType(nestedListsClass);
        SimpleType simpleType = new SimpleType(nestedListsType);
        String expected = "[java.util.List, java.util.List, java.util.List, java.lang.String]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    // Test equals method
    @Test
    public void testSimpleTypeEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        SimpleType simpleType3 = new SimpleType(stringClass1);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
    }
}