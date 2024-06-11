

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setUp() throws Exception {
        JavaType refType = SimpleType.construct(Integer.class);
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        referenceType = new ReferenceType(Integer.class, bindings, superClass, superInts,
                refType, null, valueHandler, typeHandler, asStatic);
    }

    /*
     * Test method for
     * 'com.fasterxml.jackson.databind.type.ReferenceType.upgradeFrom(JavaType, JavaType)'
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_RawTypeNull() {
        ReferenceType.upgradeFrom(null, SimpleType.construct(Integer.class));
    }

    /*
     * Test method for
     * 'com.fasterxml.jackson.databind.type.ReferenceType.upgradeFrom(JavaType, JavaType)'
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_RefdTypeNull() {
        ReferenceType.upgradeFrom(SimpleType.construct(Integer.class), null);
    }

    /*
     * Test method for
     * 'com.fasterxml.jackson.databind.type.ReferenceType.upgradeFrom(JavaType, JavaType)'
     */
    @Test
    public void testUpgradeFrom_ValidRawTypeRefdType_ExpectNonNullReferenceType() {
        ReferenceType newReferenceType = ReferenceType.upgradeFrom(SimpleType.construct(Integer.class),
                SimpleType.construct(String.class));
        assertFalse(referenceType == newReferenceType);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.construct(Class<?>,
     * TypeBindings, JavaType, JavaType[], JavaType)'
     */
    @Test
    public void testConstruct_ValidParameters_ExpectNonNullReferenceType() {
        ReferenceType newReferenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null, SimpleType.construct(Long.class));
        assertEquals(newReferenceType.getClass(), ReferenceType.class);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.construct(Class<?>,
     * TypeBindings, JavaType, JavaType[], JavaType)'
     */
    @Test
    public void testConstruct_ValidParameters_ExpectNonNullReferencedType() {
        ReferenceType newReferenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null, SimpleType.construct(Long.class));
        assertFalse(newReferenceType.getReferencedType() == null);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withContentType(JavaType)'
     */
    @Test
    public void testWithContentType_ExpectContentTypesToMatch() {
        ReferenceType newReferenceType = referenceType.withContentType(SimpleType.construct(Long.class));
        assertEquals(newReferenceType.getContentType(), SimpleType.construct(Long.class));
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withTypeHandler(Object)'
     */
    @Test
    public void testWithTypeHandler_ExpectTypeHandlersToMatch() {
        ReferenceType newReferenceType = referenceType.withTypeHandler(new Object());
        assertEquals(newReferenceType.getTypeHandler(), new Object());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withContentTypeHandler(Object)'
     */
    @Test
    public void testWithContentTypeHandler_ExpectContentTypeHandlersToMatch() {
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(new Object());
        assertEquals(newReferenceType.getContentType().getTypeHandler(), new Object());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withValueHandler(Object)'
     */
    @Test
    public void testWithValueHandler_ExpectValueHandlersToMatch() {
        ReferenceType newReferenceType = referenceType.withValueHandler(new Object());
        assertEquals(newReferenceType.getValueHandler(), new Object());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withContentValueHandler(Object)'
     */
    @Test
    public void testWithContentValueHandler_ExpectContentValueHandlersToMatch() {
        ReferenceType newReferenceType = referenceType.withContentValueHandler(new Object());
        assertEquals(newReferenceType.getContentType().getValueHandler(), new Object());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.withStaticTyping()'
     */
    @Test
    public void testWithStaticTyping_ExpectStaticTypingToBeTrue() {
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        assertTrue(newReferenceType.isStaticTyping());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.refine(Class<?>,
     * TypeBindings, JavaType, JavaType[])'
     */
    @Test
    public void testRefine_ValidParameters_ExpectParamsToMatch() {
        ReferenceType newReferenceType = referenceType.refine(Integer.class, TypeBindings.emptyBindings(),
                SimpleType.construct(Object.class), null);
        assertEquals(newReferenceType.getRawClass(), Integer.class);
        assertEquals(newReferenceType.getBindings(), TypeBindings.emptyBindings());
        assertEquals(newReferenceType.getSuperClass(), SimpleType.construct(Object.class));
        assertEquals(newReferenceType.getInterfaces(), null);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.buildCanonicalName()'
     */
    @Test
    public void testBuildCanonicalName() {
        assertEquals(referenceType.buildCanonicalName(), "java.lang.Integer<java.lang.Integer>");
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.getContentType()'
     */
    @Test
    public void testGetContentType_ExpectNonNullContentType() {
        assertFalse(referenceType.getContentType() == null);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.getReferencedType()'
     */
    @Test
    public void testGetReferencedType_ExpectNonNullReferencedType() {
        assertFalse(referenceType.getReferencedType() == null);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.hasContentType()'
     */
    @Test
    public void testHasContentType_ExpectTrue() {
        assertTrue(referenceType.hasContentType());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.isReferenceType()'
     */
    @Test
    public void testIsReferenceType_ExpectTrue() {
        assertTrue(referenceType.isReferenceType());
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.toString()'
     */
    @Test
    public void testToString_ExpectNonNullString() {
        assertFalse(referenceType.toString() == null);
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.equals(Object)'
     */
    @Test
    public void testEquals_ExpectEqualObjects() {
        assertTrue(referenceType.equals(referenceType));
    }

    /*
     * Test method for 'com.fasterxml.jackson.databind.type.ReferenceType.equals(Object)'
     */
    @Test
    public void testEquals_ExpectDifferentObjectsToBeUnequal() {
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(new Object()));
    }

}
