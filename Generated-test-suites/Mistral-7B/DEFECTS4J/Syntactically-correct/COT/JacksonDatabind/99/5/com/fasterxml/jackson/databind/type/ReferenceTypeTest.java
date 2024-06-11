import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBase;
import com.fasterxml.jackson.databind.TypeDescriptor;
import com.fasterxml.jackson.databind.TypeModifier;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithClassSuperclassRefType() {
        Class<String> cls = String.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), superClass, new JavaType[] { refType }, null);
        JavaType actual = referenceType.getContentType();
        JavaType expected = refType;
        assertEquals(expected, actual);
    }

    @Test
    public void testConstructorWithClassRefType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(cls, TypeBindings.emptyBindings(), null, new JavaType[] { refType });
        JavaType actual = referenceType.getContentType();
        JavaType expected = refType;
        assertEquals(expected, actual);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);
        JavaType actual = referenceType.getContentType();
        JavaType expected = refType;
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithoutRefType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testIsReferenceType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(intType instanceof ReferenceType);
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        assertTrue(referenceType instanceof ReferenceType);
    }

    @Test
    public void testHasContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        assertFalse(intType.hasContentType());
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        assertNull(intType.getContentType());
        JavaType referenceType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType expected = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actual = referenceType.getContentType();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType expected = TypeFactory.defaultInstance().constructType(Long.class);
        JavaType actual = intType.withContentType(expected);
        assertNotSame(intType, actual);
        assertEquals(expected, actual.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        Object handler = new Object() {};
        JavaType actual = intType.withTypeHandler(handler);
        assertNotSame(intType, actual);
        assertEquals(handler, actual.getTypeHandler());
    }

    @Test
    public void testUpgradeFromNullBaseType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        try {
            ReferenceType.upgradeFrom(null, refType);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Can not upgrade from an instance of null";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testUpgradeFromNonReferenceType() {
        JavaType intType = TypeFactory.defaultInstance().constructType(int.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        try {
            ReferenceType.upgradeFrom(intType, refType);
        } catch (IllegalArgumentException e) {
            String expectedMessage = "Can not upgrade from an instance of " + intType.getClass() + "";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testConstructWithNullTypeBindings() {
        Class<String> cls = String.class;
        JavaType superClass = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        try {
            ReferenceType.construct(cls, null, superClass, new JavaType[] { refType });
        } catch (NullPointerException e) {
            String expectedMessage = "typeBindings argument can not be null";
            String actualMessage = e.getMessage();
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @Test
    public void testBuildCanonicalName() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { refType }, null, null, null, null, false);
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        String actual = referenceType.buildCanonicalName();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAnchorType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { refType }, null, referenceType, null, null, false);
        assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testGetAnchorType() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { refType }, null, referenceType, null, null, false);
        JavaType expected = referenceType;
        JavaType actual = referenceType.getAnchorType();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        Class<String> cls = String.class;
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType referenceType = new ReferenceType(cls, TypeBindings.emptyBindings(), null, new JavaType[] { refType }, null, null, null, null, false);
        String expected = "[reference type, class java.lang.String<java.lang.Integer>]";
        String actual = referenceType.toString();
        assertEquals(expected, actual);
    }
}