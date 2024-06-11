import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for each public constructor

    @Test
    public void testSimpleTypeWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeWithClassBindingsSuperClassSuperInts() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeWithBase() {
        SimpleType simpleType = new SimpleType(new SimpleType(String.class));
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeWithClassBindingsSuperClassSuperIntsValueHandlerTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertNotNull(simpleType);
    }

    @Test
    public void testSimpleTypeWithClassBindingsSuperClassSuperIntsValueHandlerTypeHandlerAsStatic() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, true);
        assertNotNull(simpleType);
    }

    // Test methods for constructUnsafe
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeNull() {
        SimpleType.constructUnsafe(null);
    }

    @Test
    public void testConstructUnsafeString() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertInstanceOf(SimpleType.class, simpleType);
    }

    // Test methods for construct
    @Test(expected = IllegalArgumentException.class)
    public void testConstructArrayList() {
        SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructHashMap() {
        SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArrayType() {
        SimpleType.construct(int[].class);
    }

    @Test
    public void testConstructClass() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertInstanceOf(SimpleType.class, simpleType);
    }

    // Test methods for _narrow
    @Test
    public void testNarrowSameType() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrow = simpleType.narrow(String.class);
        assertEquals(simpleType, narrow);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowDifferentType() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType narrow = simpleType.narrow(Integer.class);
        fail("IllegalArgumentException expected");
    }

    // Test methods for isContainerType
    @Test
    public void testIsContainerTypeFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    // Test methods for getErasedSignature and getGenericSignature
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder expected = new StringBuilder("[simple type, class java.lang.String]");
        StringBuilder actual = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.emptyBindings(), null, null);
        StringBuilder expected = new StringBuilder("[simple type, class java.util.List<capture#1-of ? extends java.lang.Object>>");
        StringBuilder actual = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), actual.toString());
    }

    // Test methods for toString and equals
    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String expected = "[simple type, class java.lang.String]";
        String actual = simpleType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsReflexive() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType1));
    }

    @Test
    public void testEqualsReflexiveSameClassDifferentBindings() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(simpleType3));
    }

    @Test
    public void testEqualsSymmetric() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType2.equals(simpleType1));
    }

    @Test
    public void testEqualsTransitive() {
        SimpleType simpleType1 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType3 = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null, null, null, false);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertTrue(simpleType2.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType3));
    }

    // Test methods for withContentType, withTypeHandler, withContentTypeHandler, withValueHandler, withContentValueHandler
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeNull() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(null);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withTypeHandler(null);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler(null);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withValueHandler(null);
        fail("IllegalArgumentException expected");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerNull() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler(null);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testWithContentTypeSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType simpleContentType = new SimpleType(Integer.class);
        SimpleType withContentType = simpleType.withContentType(simpleContentType);
        assertEquals(simpleType._class, withContentType._class);
        JavaType contentType = withContentType.containedType(0);
        assertEquals(simpleContentType._class, contentType._class);
    }

    @Test
    public void testWithTypeHandlerObject() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        assertEquals(simpleType._class, withTypeHandler._class);
        Object actualHandler = withTypeHandler.getTypeHandler();
        assertEquals(handler, actualHandler);
    }

    @Test
    public void testWithContentTypeHandlerObject() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withContentTypeHandler = simpleType.withContentTypeHandler(handler);
        assertEquals(simpleType._class, withContentTypeHandler._class);
        Object actualHandler = withContentTypeHandler.getTypeHandler();
        assertEquals(handler, actualHandler);
    }

    @Test
    public void testWithValueHandlerObject() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        assertEquals(simpleType._class, withValueHandler._class);
        Object actualHandler = withValueHandler.getValueHandler();
        assertEquals(handler, actualHandler);
    }

    @Test
    public void testWithContentValueHandlerObject() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();
        SimpleType withContentValueHandler = simpleType.withContentValueHandler(handler);
        assertEquals(simpleType._class, withContentValueHandler._class);
        Object actualHandler = withContentValueHandler.getValueHandler();
        assertEquals(handler, actualHandler);
    }

    // Test methods for withStaticTyping
    @Test
    public void testWithStaticTypingFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertFalse(withStaticTyping._asStatic);
    }

    @Test
    public void testWithStaticTypingTrue() {
        SimpleType simpleType = new SimpleType(String.class, null, null, null, null, null, false);
        SimpleType withStaticTyping = simpleType.withStaticTyping();
        assertTrue(withStaticTyping._asStatic);
    }

    // Test methods for refine
    @Test(expected = NullPointerException.class)
    public void testRefineNullRawType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.refine(null, null, null, null);
        fail("NullPointerException expected");
    }

    @Test
    public void testRefineStringList() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refined = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertNull(refined);
    }
}