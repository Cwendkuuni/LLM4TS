import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    // Basic test cases for each constructor

    @Test
    public void testSimpleTypeConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        assertNotNull(simpleType);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        SimpleType source = new SimpleType(String.class);
        SimpleType target = new SimpleType(source);
        assertNotNull(target);
        assertEquals(source.getRawClass(), target.getRawClass());
    }

    @Test
    public void testSimpleTypePassThroughConstructor() {
        SimpleType simpleType = new SimpleType(ReferenceType.class);
        assertNotNull(simpleType);
        assertEquals(ReferenceType.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafe() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        SimpleType.construct(Map.class);
    }

    // Test methods for _narrow()

    @Test
    public void testNarrowWithSameType() {
        SimpleType source = new SimpleType(String.class);
        SimpleType target = source.narrow(String.class);
        assertEquals(source, target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithDifferentType() {
        SimpleType source = new SimpleType(String.class);
        SimpleType target = source.narrow(Integer.class);
        assertNotSame(source, target);
    }

    // Test methods for withContentType(), withTypeHandler(), withValueHandler()

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(new JavaType(int[].class));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(new Object());
        assertNotSame(simpleType, newSimpleType);
    }

    @Test
    public void testWithTypeHandlerSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withTypeHandler(new Object());
        SimpleType newSimpleType2 = simpleType.withTypeHandler(new Object());
        assertEquals(newSimpleType, newSimpleType2);
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withValueHandler(new Object());
        assertNotSame(simpleType, newSimpleType);
    }

    @Test
    public void testWithValueHandlerSameHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType newSimpleType = simpleType.withValueHandler(new Object());
        SimpleType newSimpleType2 = simpleType.withValueHandler(new Object());
        assertEquals(newSimpleType, newSimpleType2);
    }

    // Test methods for withStaticTyping()

    @Test
    public void testWithStaticTypingFalse() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        assertFalse(staticSimpleType.isStatic());
    }

    @Test
    public void testWithStaticTypingTrue() {
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType staticSimpleType = simpleType.withStaticTyping();
        SimpleType staticSimpleType2 = simpleType.withStaticTyping();
        assertTrue(staticSimpleType.isStatic());
        assertEquals(staticSimpleType, staticSimpleType2);
    }

    // Test method for refine()

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String[].class, TypeBindings.emptyBindings(), null, null);
        assertNull(refinedType);
    }

    // Test methods for buildCanonicalName()

    @Test
    public void testBuildCanonicalNameSimpleType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }

    @Test
    public void testBuildCanonicalNameSimpleTypeWithGenericBindings() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.listBindings(Integer.class));
        assertEquals("java.util.List<java.lang.Integer>", simpleType.buildCanonicalName());
    }

    // Test methods for getErasedSignature() and getGenericSignature()

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.getErasedSignature(new StringBuilder()).toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(List.class, TypeBindings.listBindings(Integer.class));
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("java.util.List<java.lang.Integer>", sb.toString());
    }
}