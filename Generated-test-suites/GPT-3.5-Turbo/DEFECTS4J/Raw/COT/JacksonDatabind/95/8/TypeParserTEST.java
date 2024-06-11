// Generated JUnit 4 test cases for TypeParser class

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeParser;

public class TypeParserTest {

    private TypeParser typeParser;

    @Before
    public void setup() {
        typeParser = new TypeParser(TypeFactory.defaultInstance());
    }

    @Test
    public void testParse() {
        // Test case 1: Valid canonical type
        String canonical1 = "java.util.List";
        JavaType javaType1 = typeParser.parse(canonical1);
        assertEquals(JavaType.class, javaType1.getClass());
        assertEquals("java.util.List", javaType1.getRawClass().getName());
        assertNull(javaType1.getBindings());

        // Test case 2: Invalid canonical type
        String canonical2 = "InvalidType";
        try {
            typeParser.parse(canonical2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test case 3: Canonical type with generics
        String canonical3 = "java.util.Map<java.lang.String,java.util.List<T>>";
        JavaType javaType3 = typeParser.parse(canonical3);
        assertEquals(JavaType.class, javaType3.getClass());
        assertEquals("java.util.Map", javaType3.getRawClass().getName());
        assertNotNull(javaType3.getBindings());
        assertEquals("java.lang.String", javaType3.getBindings().getBoundType(0).getRawClass().getName());
        assertEquals("java.util.List", javaType3.getBindings().getBoundType(1).getRawClass().getName());
        assertEquals("T", javaType3.getBindings().getBoundType(1).getBindings().getVariable(0));
    }
}