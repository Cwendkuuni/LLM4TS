import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    private ReferenceType referenceType;

    @Before
    public void setup() {
        referenceType = new ReferenceType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(referenceType);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType refdType = null;
        try {
            referenceType.upgradeFrom(baseType, refdType);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing referencedType", e.getMessage());
        }
    }

    @Test
    public void testConstruct() {
        Class<?> cls = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = null;
        ReferenceType result = referenceType.construct(cls, bindings, superClass, superInts, refType);
        assertNotNull(result);
    }

    @Test
    public void testConstruct2() {
        Class<?> cls = null;
        JavaType refType = null;
        ReferenceType result = referenceType.construct(cls, refType);
        assertNotNull(result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        referenceType.withContentType(contentType);
        // TODO: Add assertion here
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        referenceType.withTypeHandler(h);
        // TODO: Add assertion here
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        referenceType.withContentTypeHandler(h);
        // TODO: Add assertion here
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        referenceType.withValueHandler(h);
        // TODO: Add assertion here
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        referenceType.withContentValueHandler(h);
        // TODO: Add assertion here
    }

    @Test
    public void testWithStaticTyping() {
        referenceType.withStaticTyping();
        // TODO: Add assertion here
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        referenceType.refine(rawType, bindings, superClass, superInterfaces);
        // TODO: Add assertion here
    }

    @Test
    public void testGetContentType() {
        referenceType.getContentType();
        // TODO: Add assertion here
    }

    @Test
    public void testGetReferencedType() {
        referenceType.getReferencedType();
        // TODO: Add assertion here
    }

    @Test
    public void testHasContentType() {
        referenceType.hasContentType();
        // TODO: Add assertion here
    }

    @Test
    public void testIsReferenceType() {
        referenceType.isReferenceType();
        // TODO: Add assertion here
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = null;
        referenceType.getErasedSignature(sb);
        // TODO: Add assertion here
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = null;
        referenceType.getGenericSignature(sb);
        // TODO: Add assertion here
    }

    @Test
    public void testGetAnchorType() {
        referenceType.getAnchorType();
        // TODO: Add assertion here
    }

    @Test
    public void testIsAnchorType() {
        referenceType.isAnchorType();
        // TODO: Add assertion here
    }

    @Test
    public void testEquals() {
        Object o = null;
        referenceType.equals(o);
        // TODO: Add assertion here
    }
}