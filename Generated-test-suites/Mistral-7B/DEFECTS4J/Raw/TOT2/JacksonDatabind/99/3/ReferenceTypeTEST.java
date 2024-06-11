import com.fasterxml.jackson.core.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeWithNoArguments() {
        JavaType refType = TypeFactory.defaultInstance().construct(ReferenceType.class);
        ReferenceType referenceType = (ReferenceType) refType;
        assertNotNull(referenceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullBaseType() {
        JavaType baseType = null;
        JavaType refdType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullReferencedType() {
        JavaType baseType = TypeFactory.defaultInstance().construct(ReferenceType.class);
        JavaType refdType = null;
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().construct(int.class);
        JavaType refdType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullClass() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(null, bindings, superClass, superInts, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullTypeBindings() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(cls, null, superClass, superInts, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullSuperClass() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, null, superInts, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullReferencedType() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeWithNullContentType() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        JavaType result = referenceType.withContentType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandlerWithNullHandler() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        ReferenceType result = referenceType.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerWithNullHandler() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        Object result = referenceType.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerWithNullHandler() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        Object result = referenceType.withValueHandler(null);
    }

    @Test
    public void testWithContentValueHandlerWithValidHandler() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        Object result = referenceType.withContentValueHandler(handler);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTypingWhenNotStatic() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertFalse(result.isStatic());

        ReferenceType referenceTypeWithStatic = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, true);
        assertTrue(referenceTypeWithStatic.isStatic());
    }

    @Test
    public void testReferenceTypeToString() {
        Class<?> cls = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType referencedType = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, referencedType, null, null, null, false);
        String result = referenceType.toString();
        String expected = "[reference type, class [I, class java.lang.String]";
        assertEquals(expected, result);
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<?> cls1 = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts1 = new JavaType[0];
        JavaType referencedType1 = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInts1, referencedType1, null, null, null, false);

        Class<?> cls2 = TypeFactory.defaultInstance().construct(Integer.class);
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = TypeFactory.defaultInstance().construct(Object.class);
        JavaType[] superInts2 = new JavaType[0];
        JavaType referencedType2 = TypeFactory.defaultInstance().construct(String.class);
        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInts2, referencedType2, null, null, null, false);

        assertTrue(referenceType1.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(referenceType1);
        assertTrue(referenceType1.equals(referenceType3));
    }
}