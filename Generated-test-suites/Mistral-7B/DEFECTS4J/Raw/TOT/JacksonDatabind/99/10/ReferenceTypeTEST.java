import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeCreation() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String.class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, null, null, refType);

        assertEquals("Expected string array as referenced type", refType, referenceType.getReferencedType());
        assertTrue("Expected reference type", referenceType.isReferenceType());
        assertFalse("Expected not static type", referenceType.isStatic());
    }

    @Test
    public void testUpgradingTypeToReferenceType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);

        JavaType upgradedType = ReferenceType.upgradeFrom(simpleType, refType);

        assertEquals("Expected String[] as referenced type", refType, upgradedType.getReferencedType());
        assertTrue("Expected reference type", upgradedType.isReferenceType());
        assertFalse("Expected not static type", upgradedType.isStatic());
    }

    @Test
    public void testErrorHandlingWhenUpgradingToReferenceTypeWithInvalidType() {
        JavaType simpleType = TypeFactory.defaultInstance().constructType(Integer.class);

        try {
            ReferenceType.upgradeFrom(simpleType, null);
            fail("Expected IllegalArgumentException when referencedType is null");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ReferenceType.upgradeFrom(simpleType, new TypeReference<List<String>>() {}.getType());
            fail("Expected IllegalArgumentException when refdType is not a ReferenceType");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ReferenceType.upgradeFrom(simpleType, TypeFactory.defaultInstance().constructType(Object.class));
            fail("Expected IllegalArgumentException when baseType is not a TypeBase or SimpleType");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetContentType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        ReferenceType referenceType = ReferenceType.construct(String[].class, null, null, refType);

        assertEquals("Expected String[] as content type", refType, referenceType.getContentType());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(List.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, anchorType);

        assertEquals("Expected List as anchor type", anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType anchorType = TypeFactory.defaultInstance().constructType(List.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, refType, anchorType);

        assertTrue("Expected true when this is the anchor type", referenceType.isAnchorType());

        ReferenceType superReferenceType = new ReferenceType(null, null, null, null, referenceType, null);
        assertFalse("Expected false when this is not the anchor type", superReferenceType.isAnchorType());
    }
}