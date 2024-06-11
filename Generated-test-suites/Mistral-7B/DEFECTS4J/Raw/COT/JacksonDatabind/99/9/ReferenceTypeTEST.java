import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithArguments() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals("ReferenceType with correct arguments", refType, referenceType.getReferencedType());
        assertEquals("Class is correctly set", cls, referenceType.getRawClass());
    }

    @Test
    public void testConstructorUpgradedFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refdType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        assertEquals("Referenced type is correctly set", refdType, referenceType.getReferencedType());
        assertEquals("Base type is correctly set", baseType, referenceType.getSuperClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorUpgradedFromWithNullRefdType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);

        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        assertEquals("Referenced type is correctly set", refType, referenceType.getReferencedType());
        assertEquals("Class is correctly set", cls, referenceType.getRawClass());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals("GetContentType returns the correct referenced type", refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals("GetReferencedType returns the correct referenced type", refType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertTrue("HasContentType returns true since it has a referenced type", referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertTrue("IsReferenceType returns true since it is a ReferenceType", referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals("GetErasedSignature returns the correct signature", "Ljava/lang/String;", referenceType.getErasedSignature(sb).toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

        assertEquals("GetGenericSignature returns the correct signature", "Lcom/fasterxml/jackson/databind/type/ReferenceType<Ljava/lang/Integer;>;", referenceType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertNull("GetAnchorType returns null since it is not set", referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertFalse("IsAnchorType returns false since it is not the anchor type", referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<?> cls = new Class<?>[] { String.class }[0];
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);

        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

        assertEquals("ToString returns the expected String representation", "[reference type, class [Ljava/lang/String;<Ljava/lang/Integer;>]", referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = new Class<?>[] { String.class }[0];
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts1 = new JavaType[0];
        JavaType refType1 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType1 = null;
        Object valueHandler1 = null;
        Object typeHandler1 = null;
        boolean asStatic1 = false;

        Class<?> cls2 = new Class<?>[] { String.class }[0];
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType[] superInts2 = new JavaType[0];
        JavaType refType2 = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType2 = null;
        Object valueHandler2 = null;
        Object typeHandler2 = null;
        boolean asStatic2 = false;

        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, anchorType1, valueHandler1, typeHandler1, asStatic1);
        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInts2, refType2, anchorType2, valueHandler2, typeHandler2, asStatic2);

        assertTrue("Equals returns true when comparing equal objects", referenceType1.equals(referenceType1));
        assertTrue("Equals returns true when comparing two equal objects", referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, anchorType1, valueHandler1, typeHandler1, asStatic1);
        ReferenceType referenceType4 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType2, anchorType1, valueHandler1, typeHandler1, asStatic1);

        assertFalse("Equals returns false when comparing different objects", referenceType1.equals(referenceType3));
        assertFalse("Equals returns false when comparing different objects", referenceType1.equals(referenceType4));
    }
}