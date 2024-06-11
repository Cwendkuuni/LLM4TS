import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);

        assertEquals("class [Ljava.lang.String;]", stringType.buildCanonicalName());
        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);

        SimpleType copiedStringType = new SimpleType(stringType);

        assertEquals("class [Ljava.lang.String;]", copiedStringType.buildCanonicalName());
        assertEquals(String.class, copiedStringType.getRawClass());
        assertFalse(copiedStringType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithSuperClassAndInterfaces() {
        Class<List<Integer>> listIntClass = java.util.List.class.getTypeParameters()[0].getType();
        JavaType superClass = TypeFactory.unknownType();
        JavaType[] superInterfaces = new JavaType[0];
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.emptyBindings(), superClass, superInterfaces);

        assertEquals("class java.util.List<java.lang.Integer>", listIntType.buildCanonicalName());
        assertEquals(java.util.List.class, listIntType.getRawClass());
        assertFalse(listIntType.isContainerType());
    }

    @Test
    public void testSimpleTypeWithValuesAndTypeHandlers() {
        Class<String> stringClass = String.class;
        Object stringValueHandler = new Object();
        Object stringTypeHandler = new Object();

        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null, stringValueHandler, stringTypeHandler, false);

        assertEquals("class [Ljava.lang.String;]", stringType.buildCanonicalName());
        assertEquals(String.class, stringType.getRawClass());
        assertFalse(stringType.isContainerType());
        assertEquals(stringValueHandler, stringType.getValueHandler());
        assertEquals(stringTypeHandler, stringType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeUnsupportedConstruct() {
        Class<Map<String, Integer>> mapClass = new ClassFactory().createMapClass(String.class, Integer.class);
        SimpleType mapType = new SimpleType(mapClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeWithInvalidSuperClass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass, TypeBindings.emptyBindings(), Arrays.asList(Integer.class).getType(), null);
    }
}