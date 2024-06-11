import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructSimpleType(String.class, null);
        JavaType refdType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);

        ReferenceType result = ReferenceType.upgradeFrom(baseType, refdType);

        assertNotNull(result);
        assertEquals(refdType, result.getReferencedType());
    }

    @Test
    public void testConstruct() {
        JavaType refType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);

        ReferenceType result = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType);

        assertNotNull(result);
        assertEquals(refType, result.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType);

        JavaType newContentType = TypeFactory.defaultInstance().constructSimpleType(Boolean.class, null);

        ReferenceType result = referenceType.withContentType(newContentType);

        assertNotNull(result);
        assertEquals(newContentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType);

        Object newHandler = new Object();

        ReferenceType result = referenceType.withTypeHandler(newHandler);

        assertNotNull(result);
        assertEquals(newHandler, result.getTypeHandler());
    }

    @Test
    public void testRefine() {
        JavaType refType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType);

        JavaType newSuperClass = TypeFactory.defaultInstance().constructSimpleType(Number.class, null);

        ReferenceType result = referenceType.refine(Integer.class, TypeBindings.emptyBindings(),
                newSuperClass, null);

        assertNotNull(result);
        assertEquals(newSuperClass, result.getSuperClass());
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, TypeFactory.defaultInstance().constructSimpleType(Integer.class, null));

        StringBuilder sb = referenceType.getErasedSignature(new StringBuilder());

        assertNotNull(sb);
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, TypeFactory.defaultInstance().constructSimpleType(Integer.class, null));

        StringBuilder sb = referenceType.getGenericSignature(new StringBuilder());

        assertNotNull(sb);
        assertTrue(sb.length() > 0);
    }

    @Test
    public void testToString() {
        JavaType refType = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType);

        String result = referenceType.toString();

        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType1 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType1);

        JavaType refType2 = TypeFactory.defaultInstance().constructSimpleType(Integer.class, null);
        ReferenceType referenceType2 = ReferenceType.construct(Integer.class, TypeBindings.emptyBindings(),
                null, null, refType2);

        boolean result = referenceType1.equals(referenceType2);

        assertFalse(result);
    }
}