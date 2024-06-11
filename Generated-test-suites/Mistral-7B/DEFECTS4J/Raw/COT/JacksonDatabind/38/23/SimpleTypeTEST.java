import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import static com.fasterxml.jackson.databind.type.SimpleType.*;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeConstructorWithClassBindingsSuperClassSuperInterfaces() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertEquals(cls, simpleType.getRawClass());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType originalSimpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
        assertEquals(originalSimpleType, copiedSimpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafe() {
        Class<?> raw = String[].class;
        SimpleType simpleType = SimpleType.constructUnsafe(raw);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct() {
        Class<?> cls = Map.class;
        SimpleType simpleType = SimpleType.construct(cls);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithSubclass() {
        Class<?> baseClass = Object.class;
        Class<?> subclass = String.class;
        SimpleType baseType = new SimpleType(baseClass, TypeBindings.emptyBindings(), null, null);
        SimpleType subType = new SimpleType(subclass, TypeBindings.emptyBindings(), null, null);
        JavaType result = baseType._narrow(subclass);
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        Object handler = new Object();
        SimpleType withHandler = simpleType.withTypeHandler(handler);
        assertNotSame(simpleType, withHandler);
        assertEquals(handler, withHandler.getTypeHandler());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = List.class;
        GenericType<?> innerType = new GenericTypeImpl<Integer>(Integer.class);
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { innerType });
        String result = simpleType.buildCanonicalName();
        String expected = "[java.util.List, class java.util.List<java.lang.Integer>]";
        assertEquals(expected, result);
    }

    @Test
    public void testIsContainerType() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("java.util.List");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        StringBuilder expected = new StringBuilder("java.util.List<T>");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls, TypeBindings.emptyBindings(), null, null);
        String result = simpleType.toString();
        String expected = "[simple type, class java.util.List]";
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = String.class;
        Class<?> cls2 = Integer.class;
        SimpleType simpleType1 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        SimpleType simpleType2 = new SimpleType(cls2, TypeBindings.emptyBindings(), null, null);
        assertFalse(simpleType1.equals(simpleType2));

        SimpleType simpleType3 = new SimpleType(cls1, TypeBindings.emptyBindings(), null, null);
        assertTrue(simpleType1.equals(simpleType3));
    }
}