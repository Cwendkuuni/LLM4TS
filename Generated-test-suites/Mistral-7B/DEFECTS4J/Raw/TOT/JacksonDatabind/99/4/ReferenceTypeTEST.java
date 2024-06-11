import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedRefType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = ReferenceType.upgradeFrom(baseType, expectedRefType);

        assertEquals(expectedRefType, actualRefType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromMissingRefType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testConstruct() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = ReferenceType.construct(clsType.getRawClass(), TypeBindings.emptyBindings(), null, null, refType);

        assertEquals(clsType, actualRefType.getClassComponent());
        assertEquals(refType, actualRefType.getContentType());
    }

    @Test
    public void testConstructWithNullSuperInts() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = ReferenceType.construct(clsType.getRawClass(), TypeBindings.emptyBindings(), null, null, refType);

        assertEquals(clsType, actualRefType.getClassComponent());
        assertEquals(refType, actualRefType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, anchorType);
        JavaType expectedRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        JavaType result = actualRefType.withContentType(null);
        assertEquals(expectedRefType, result);

        JavaType result2 = actualRefType.withContentType(refType);
        assertEquals(actualRefType, result2);

        JavaType expectedResult = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, new JavaType(int[].class), anchorType);
        JavaType result3 = actualRefType.withContentType(new JavaType(int[].class));
        assertEquals(expectedResult, result3);
    }

    @Test
    public void testRefine() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);
        JavaType expectedRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        JavaType result = actualRefType.refine(Integer.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(expectedRefType, result);

        JavaType result2 = actualRefType.refine(clsType.getRawClass(), TypeBindings.emptyBindings(), null, null);
        assertEquals(actualRefType, result2);

        JavaType result3 = actualRefType.refine(clsType.getRawClass(), TypeBindings.emptyBindings(), TypeFactory.defaultInstance().constructType(Long.class), null);
        JavaType expectedResult = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);
        JavaType expectedResult2 = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);
        expectedResult2.withSuperInterfaces(new JavaType[]{TypeFactory.defaultInstance().constructType(Long.class)});
        assertEquals(expectedResult2, result3);
    }

    @Test
    public void testGetContentType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        assertEquals(refType, actualRefType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        assertTrue(actualRefType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        assertTrue(actualRefType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;");
        StringBuilder result = actualRefType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        StringBuilder expected = new StringBuilder("[Ljava/lang/String;<T extends java.lang.Number>Ljava/lang/Class<T>;T>");
        StringBuilder result = actualRefType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, anchorType);

        assertEquals(anchorType, actualRefType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(ReferenceType.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, anchorType);

        assertFalse(actualRefType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType clsType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType actualRefType = new ReferenceType(clsType, TypeBindings.emptyBindings(), null, null, refType, null);

        String expected = "[reference type, class [Ljava.lang.String;<T extends java.lang.Number>Ljava.lang.Class<T>;T>, class java.lang.Integer]";
        String result = actualRefType.toString();
        assertEquals(expected, result);
    }
}