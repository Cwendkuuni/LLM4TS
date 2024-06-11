import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithClassAndBindingsAndSuperClassAndSuperInterfacesAndRefTypeAndAnchorTypeAndValueHandlerAndTypeHandlerAndAsStatic() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(Integer.class);
        JavaType anchorType = SimpleType.construct(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType, anchorType,
                valueHandler, typeHandler, asStatic);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertEquals(valueHandler, referenceType.getValueHandler());
        assertEquals(typeHandler, referenceType.getTypeHandler());
        assertEquals(asStatic, referenceType.isStatic());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(anchorType, referenceType.getAnchorType());
        assertEquals(String.class, referenceType.getAnchorType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = SimpleType.construct(Integer.class);
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        assertNotNull(referenceType);
        assertEquals(baseType.getClass(), referenceType.getClass());
        assertEquals(baseType, referenceType.getBaseType());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(refType, referenceType.getAnchorType());
    }

    @Test
    public void testConstruct() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = ReferenceType.construct(
                cls, bindings, superClass, superInts, refType);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType.getRawClass());
        assertEquals(bindings, referenceType.getTypeBindings());
        assertEquals(superClass, referenceType.getSuperClass());
        assertNull(referenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), referenceType.getTypeId());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
        assertEquals(false, referenceType.isStatic());
        assertEquals(refType, referenceType.getReferencedType());
        assertEquals(refType, referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType baseType = Boolean.TYPE;
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType.construct(baseType, refType);
    }

    @Test
    public void testWithContentType() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);
        JavaType contentType = SimpleType.construct(Integer.class);

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        ReferenceType newReferenceType = referenceType.withContentType(contentType);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(contentType, newReferenceType.getReferencedType());
        assertNotSame(referenceType.getAnchorType(), newReferenceType.getAnchorType());
        assertEquals(contentType, newReferenceType.getAnchorType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);
        Object typeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(typeHandler, newReferenceType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);
        Object contentTypeHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        JavaType newContentType = referenceType.withValueHandler(contentTypeHandler);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandler);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(contentTypeHandler, newReferenceType.getValueHandler());
        assertEquals(contentTypeHandler, newReferenceType.getReferencedType().getValueHandler());
        assertEquals(contentTypeHandler, newContentType.getValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);
        Object valueHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);
        Object contentValueHandler = new Object() {};

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        JavaType newContentType = referenceType.withValueHandler(contentValueHandler);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandler);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(contentValueHandler, newReferenceType.getValueHandler());
        assertEquals(contentValueHandler, newContentType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<String> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                cls, bindings, superClass, superInts, refType);

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(referenceType, newReferenceType.getBaseType());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertTrue(newReferenceType.isStatic());
    }

    @Test
    public void testRefine() {
        Class<String> rawType = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType[] superInterfaces = new JavaType[0];
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                rawType, bindings, superClass, superInterfaces, refType);

        JavaType newReferenceType = referenceType.refine(String[].class, bindings, superClass, superInterfaces);

        assertNotNull(newReferenceType);
        assertEquals(referenceType.getClass(), newReferenceType.getClass());
        assertEquals(rawType, newReferenceType.getRawClass());
        assertEquals(bindings, newReferenceType.getTypeBindings());
        assertEquals(superClass, newReferenceType.getSuperClass());
        assertNull(newReferenceType.getSuperInterfaces());
        assertEquals(refType.hashCode(), newReferenceType.getTypeId());
        assertNull(newReferenceType.getValueHandler());
        assertNull(newReferenceType.getTypeHandler());
        assertEquals(false, newReferenceType.isStatic());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(refType, newReferenceType.getAnchorType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> cls = String.class;
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType);

        StringBuilder result = new StringBuilder();
        referenceType.getErasedSignature(result);

        assertEquals("Ljava/lang/String;", result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> cls = String.class;
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType);

        StringBuilder result = new StringBuilder();
        referenceType.getGenericSignature(result);

        assertEquals("[Ljava/lang/String;<T:Ljava/lang/Object;>", result.toString());
    }

    @Test
    public void testGetCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                cls, TypeBindings.emptyBindings(), null, new JavaType[0], refType);

        assertEquals("[Ljava/lang/String;", referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<String> cls = String.class;
        JavaType superClass = SimpleType.construct(Integer.class);
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                cls, TypeBindings.emptyBindings(), superClass, new JavaType[0], refType);

        JavaType narrowedType = referenceType._narrow(String[].class);

        assertNotNull(narrowedType);
        assertEquals(referenceType.getClass(), narrowedType.getClass());
        assertEquals(cls, narrowedType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), narrowedType.getTypeBindings());
        assertEquals(superClass, narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertEquals(refType.hashCode(), narrowedType.getTypeId());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertEquals(false, narrowedType.isStatic());
        assertEquals(refType, narrowedType.getReferencedType());
        assertEquals(refType, narrowedType.getAnchorType());
    }

    @Test
    public void testGetClass() {
        JavaType refType = SimpleType.construct(String.class);

        ReferenceType referenceType = new ReferenceType(
                null, null, null, new JavaType[0], refType);

        assertEquals(ReferenceType.class, referenceType.getClass());
    }
}