import org.junit.*;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(null);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructor2() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = {new JavaType()};
        SimpleType simpleType = new SimpleType(null, bindings, superClass, superInts);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructor3() {
        TypeBase base = new TypeBase();
        SimpleType simpleType = new SimpleType(base);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructor4() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = {new JavaType()};
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(null, bindings, superClass, superInts, valueHandler, typeHandler, true);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructor5() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = {new JavaType()};
        int extraHash = 0;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(null, bindings, superClass, superInts, extraHash, valueHandler, typeHandler, true);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(null);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType simpleType = SimpleType.construct(null);
            assertNotNull(simpleType);
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: null)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(null);
        JavaType subclass = new JavaType();
        JavaType result = simpleType._narrow(subclass);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(null);
        JavaType contentType = new JavaType();
        try {
            simpleType.withContentType(contentType);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(null);
        Object h = new Object();
        SimpleType result = simpleType.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(null);
        Object h = new Object();
        try {
            simpleType.withContentTypeHandler(h);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(null);
        Object h = new Object();
        SimpleType result = simpleType.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(null);
        Object h = new Object();
        try {
            simpleType.withContentValueHandler(h);
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(null);
        SimpleType result = simpleType.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(null);
        Class<?> rawType = new Class<?>();
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {new JavaType()};
        JavaType result = simpleType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(null);
        String result = simpleType.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(null);
        String result = simpleType.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(null);
        assertTrue(simpleType.equals(simpleType));
    }

    @Test
    public void testEquals2() {
        SimpleType simpleType = new SimpleType(null);
        SimpleType other = new SimpleType(null);
        assertTrue(simpleType.equals(other));
    }

    @Test
    public void testEquals3() {
        SimpleType simpleType = new SimpleType(null);
        SimpleType other = new SimpleType(null);
        other._class = new Class<?>();
        assertFalse(simpleType.equals(other));
    }

    @Test
    public void testEquals4() {
        SimpleType simpleType = new SimpleType(null);
        SimpleType other = new SimpleType(null);
        other._bindings.put(new Binding());
        assertFalse(simpleType.equals(other));
    }
    
    @Test
    public void testContentSignature() {
        SimpleType simpleType = new SimpleType(null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.buildCanonicalName(sb);
        assertNotNull(result);
    }

    @Test
    public void testPublicAPI() {
        SimpleType simpleType = new SimpleType(null);
        assertFalse(simpleType.isContainerType());
        assertFalse(simpleType.hasContentType());
        assertNull(simpleType.getContentType());
    }

}