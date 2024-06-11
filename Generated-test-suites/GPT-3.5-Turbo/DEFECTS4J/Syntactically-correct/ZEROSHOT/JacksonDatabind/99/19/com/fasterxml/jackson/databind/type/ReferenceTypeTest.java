import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refType = null;
        ReferenceType result = ReferenceType.construct(cls, bindings, superClass, superInterfaces, refType);
        assertNotNull(result);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType refdType = null;
        ReferenceType result = ReferenceType.upgradeFrom(baseType, refdType);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.withContentType(contentType);
        assertNotNull(result);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withContentTypeHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withContentValueHandler(h);
        assertNotNull(result);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        ReferenceType result = instance.withStaticTyping();
        assertNotNull(result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType refType = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.refine(rawType, bindings, superClass, superInterfaces);
        assertNotNull(result);
    }

    @Test
    public void testBuildCanonicalName() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        String result = instance.buildCanonicalName();
        assertNotNull(result);
    }

    @Test
    public void testGetContentType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.getContentType();
        assertNotNull(result);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.getReferencedType();
        assertNotNull(result);
    }

    @Test
    public void testHasContentType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean result = instance.hasContentType();
        assertTrue(result);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean result = instance.isReferenceType();
        assertTrue(result);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        StringBuilder sb = null;
        StringBuilder result = instance.getErasedSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        StringBuilder sb = null;
        StringBuilder result = instance.getGenericSignature(sb);
        assertNotNull(result);
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        JavaType result = instance.getAnchorType();
        assertNotNull(result);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean result = instance.isAnchorType();
        assertTrue(result);
    }

    @Test
    public void testToString() {
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        ReferenceType instance = new ReferenceType(null, null, null, null, null, null, null, null, false);
        boolean result = instance.equals(o);
        assertFalse(result);
    }
}