import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType expectedSimpleType = new SimpleType(stringClass, TypeBindings.emptyBindings(), null, null);

        assertEquals(expectedSimpleType.getClass(), simpleType.getClass());
        assertEquals(expectedSimpleType._class, simpleType._class);
        assertEquals(expectedSimpleType._bindings, simpleType._bindings);
        assertEquals(expectedSimpleType._superClass, simpleType._superClass);
        assertEquals(expectedSimpleType._superInterfaces, simpleType._superInterfaces);
        assertEquals(expectedSimpleType._valueHandler, simpleType._valueHandler);
        assertEquals(expectedSimpleType._typeHandler, simpleType._typeHandler);
        assertEquals(expectedSimpleType._asStatic, simpleType._asStatic);
    }
}