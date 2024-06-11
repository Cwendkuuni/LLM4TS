import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = null;
        CollectionType expected = new CollectionType(rawType, bindings, superClass, superInts, elemT, null, null, false);
        CollectionType actual = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        assertEquals(expected, actual);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = String.class;
        CollectionType baseType = null;
        JavaType expected = new CollectionType(subclass, baseType._bindings, baseType._superClass, baseType._superInterfaces, baseType._elementType, null, null, baseType._asStatic);
        JavaType actual = baseType._narrow(subclass);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, contentType, _valueHandler, _typeHandler, _asStatic);
        CollectionType actual = CollectionType.withContentType(contentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, _elementType, _valueHandler, h, _asStatic);
        CollectionType actual = CollectionType.withTypeHandler(h);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, _elementType.withTypeHandler(h), _valueHandler, _typeHandler, _asStatic);
        CollectionType actual = CollectionType.withContentTypeHandler(h);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, _elementType, h, _typeHandler, _asStatic);
        CollectionType actual = CollectionType.withValueHandler(h);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, _elementType.withValueHandler(h), _valueHandler, _typeHandler, _asStatic);
        CollectionType actual = CollectionType.withContentValueHandler(h);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType expected = new CollectionType(_class, _bindings, _superClass, _superInterfaces, _elementType.withStaticTyping(), _valueHandler, _typeHandler, true);
        CollectionType actual = CollectionType.withStaticTyping();
        assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType contentType = null;
        CollectionType baseType = null;
        JavaType expected = new CollectionType(rawType, bindings, superClass, superInterfaces, contentType, _valueHandler, _typeHandler, _asStatic);
        JavaType actual = CollectionType.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "[collection type; class "+_class.getName()+", contains "+_elementType+"]";
        String actual = CollectionType.toString();
        assertEquals(expected, actual);
    }
}