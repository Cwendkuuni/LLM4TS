import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<List<String>> cls = List.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        SimpleType simpleType = new SimpleType(cls, bindings);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertArrayEquals(new JavaType[0], simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType original = new SimpleType(cls);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafe() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructDeprecated() {
        SimpleType.construct(String.class);
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        Class<StringBuffer> stringBufferClass = StringBuffer.class;
        SimpleType stringType = new SimpleType(stringClass);
        SimpleType stringBufferType = new SimpleType(stringBufferClass);
        SimpleType narrowed = stringType._narrow(StringBuffer.class);
        assertSame(stringBufferType, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        stringType._narrow(Integer.class);
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        Object handler = new Object();
        SimpleType newType = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, newType);
        assertEquals(handler, newType._typeHandler);
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType staticType = simpleType.withStaticTyping();
        assertTrue(staticType._asStatic);
        assertFalse(simpleType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        String canonicalName = simpleType.buildCanonicalName();
        assertTrue(canonicalName.contains(List.class.getName()));
        assertTrue(canonicalName.contains("<String>"));
    }

    @Test
    public void testIsContainerType() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        String erasedSignature = sb.toString();
        assertTrue(erasedSignature.contains(List.class.getName()));
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<List<String>>> listListClass = List.class;
        SimpleType simpleType = new SimpleType(listListClass);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        String genericSignature = sb.toString();
        assertTrue(genericSignature.contains(List.class.getName()));
        assertTrue(genericSignature.contains("<List<String>>"));
    }

    @Test
    public void testToString() {
        Class<List<List<String>>> listListClass = List.class;
        SimpleType simpleType = new SimpleType(listListClass);
        String toString = simpleType.toString();
        assertTrue(toString.contains("[simple type, class "));
        assertTrue(toString.contains("[List, List<String>]"));
    }

    @Test
    public void testEquals() {
        Class<List<List<String>>> listListClass = List.class;
        SimpleType simpleType1 = new SimpleType(listListClass);
        SimpleType simpleType2 = new SimpleType(listListClass);
        assertTrue(simpleType1.equals(simpleType2));
        SimpleType simpleType3 = new SimpleType(ArrayList.class);
        assertFalse(simpleType1.equals(simpleType3));
    }
}