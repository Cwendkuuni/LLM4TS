import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<Integer> intClass = Integer.class;
        SimpleType intSimpleType = new SimpleType(intClass);
        JavaType javaIntType = TypeFactory.defaultInstance().constructType(intClass);

        assertEquals(intSimpleType.getTypeId(), javaIntType.getTypeId());
        assertEquals(intSimpleType.getClass(), javaIntType.getClass());
        assertEquals(intSimpleType.toString(), javaIntType.toString());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<Integer> intClass = Integer.class;
        SimpleType intSimpleType = new SimpleType(intClass);
        SimpleType intSimpleTypeCopy = new SimpleType(intSimpleType);

        assertEquals(intSimpleType, intSimpleTypeCopy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeInvalidConstruction() {
        SimpleType simpleType = new SimpleType(new ArrayList<>());
    }

    @Test
    public void testSimpleTypeWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType stringSimpleType = new SimpleType(stringClass);
        Object stringTypeHandler = new Object() {
            @Override
            public String getName() {
                return "StringTypeHandler";
            }
        };
        SimpleType stringSimpleTypeWithTypeHandler = stringSimpleType.withTypeHandler(stringTypeHandler);

        assertNotNull(stringSimpleTypeWithTypeHandler.getTypeHandler());
        assertEquals(stringTypeHandler, stringSimpleTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testSimpleTypeWithValueHandler() {
        Class<Integer> intClass = Integer.class;
        SimpleType intSimpleType = new SimpleType(intClass);
        Object intValueHandler = new Object() {
            @Override
            public Integer deserialize(byte[] bytes) throws IOException {
                return 42;
            }
        };
        SimpleType intSimpleTypeWithValueHandler = intSimpleType.withValueHandler(intValueHandler);

        assertEquals(intValueHandler, intSimpleTypeWithValueHandler.getValueHandler());
    }

    @Test
    public void testSimpleTypeWithContentTypeHandler() {
        // Not applicable as SimpleType does not have a content type
    }

    @Test
    public void testSimpleTypeWithContentValueHandler() {
        // Not applicable as SimpleType does not have a content type
    }

    @Test
    public void testSimpleTypeWithStaticTyping() {
        Class<List<Integer>> listClass = new ArrayList<Integer>().getClass();
        SimpleType listSimpleType = new SimpleType(listClass);
        SimpleType listSimpleTypeStatic = listSimpleType.withStaticTyping();

        assertFalse(listSimpleType.isStatic());
        assertTrue(listSimpleTypeStatic.isStatic());
    }

    @Test
    public void testSimpleTypeRefine() {
        // Not applicable as SimpleType is not a base type that can be refined
    }
}