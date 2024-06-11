import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithClassAndBindingsAndSuperClassAndSuperInterfacesAndRefTypeAndAnchorTypeAndValueHandlerAndTypeHandlerAndAsStatic() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(int.class, TypeBindings.emptyBindings());
        JavaType refType = new SimpleType(String.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType((TypeBase) baseType.getClass(), refType);
        ReferenceType actual = ReferenceType.upgradeFrom(baseType, refType);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNullRefType() {
        JavaType baseType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(int[].class, TypeBindings.emptyBindings());
        JavaType refType = new SimpleType(String.class, TypeBindings.emptyBindings());
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testConstruct() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = ReferenceType.construct(int[].class, TypeBindings.emptyBindings(), null, null, referencedType);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullRefType() {
        ReferenceType.construct(int[].class, TypeBindings.emptyBindings(), null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidClass() {
        JavaType refType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType.construct(double[].class, TypeBindings.emptyBindings(), null, null, refType);
    }

    @Test
    public void testWithContentType() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        JavaType newContentType = new SimpleType(double.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withContentType(newContentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithTypeHandler() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        Object handler = new Object();
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withTypeHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        Object handler = new Object();
        JavaType newContentType = new SimpleType(double.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withContentTypeHandler(handler).withContentType(newContentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithValueHandler() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        Object handler = new Object();
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withValueHandler(handler);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        Object handler = new Object();
        JavaType newContentType = new SimpleType(double.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withContentValueHandler(handler).withContentType(newContentType);
        assertEquals(expected, actual);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        ReferenceType actual = expected.withStaticTyping();
        assertEquals(expected, actual);
    }

    @Test
    public void testRefine() {
        JavaType rawType = int[].class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType expected = new ReferenceType(rawType, bindings, null, null, referencedType, null, null, null, false);
        ReferenceType actual = new ReferenceType(rawType, bindings, superClass, superInterfaces).refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetContentType() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        assertEquals(referencedType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, null, null, null, null, false);
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        assertEquals(referencedType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = int[].class;
        JavaType referencedType = new SimpleType(int.class, TypeBindings.emptyBindings());
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, null, referencedType, null, null, null, false);
        String expected = "[reference type, class [" + cls.getName() + "]<" + referencedType.toCanonical() + ">]";
        String actual = referenceType.buildCanonicalName();
        assertEquals(expected, actual);
    }
}