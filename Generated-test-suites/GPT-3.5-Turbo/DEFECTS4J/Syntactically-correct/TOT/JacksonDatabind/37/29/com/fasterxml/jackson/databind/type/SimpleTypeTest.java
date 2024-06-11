
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

    @Test
    public void testConstructSimpleType() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithMap() {
        // Error scenario: Constructing SimpleType with a Map class
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithCollection() {
        // Error scenario: Constructing SimpleType with a Collection class
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructSimpleTypeWithArray() {
        // Error scenario: Constructing SimpleType with an Array class
        SimpleType.construct(Integer[].class);
    }

    @Test
    public void testConstructUnsafe() {
        // Typical use case
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        Assert.assertNotNull(simpleType);
    }

    @Test
    public void testWithContentType() {
        // Error scenario: WithContentType is not applicable for SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentType(String.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        // Error scenario: WithContentTypeHandler is not applicable for SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentTypeHandler(String.class);
    }

    @Test
    public void testWithContentValueHandler() {
        // Error scenario: WithContentValueHandler is not applicable for SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.withContentValueHandler(String.class);
    }

    @Test
    public void testWithStaticTyping() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        SimpleType staticType = simpleType.withStaticTyping();
        Assert.assertNotEquals(simpleType, staticType);
        Assert.assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        // Error scenario: Refine is not applicable for SimpleType
        SimpleType simpleType = SimpleType.construct(String.class);
        simpleType.refine(SimpleType.class, null, null, null);
    }

    @Test
    public void testIsContainerType() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertNotNull(simpleType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertNotNull(simpleType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testToString() {
        // Typical use case
        SimpleType simpleType = SimpleType.construct(String.class);
        Assert.assertNotNull(simpleType.toString());
    }

    @Test
    public void testEquals() {
        // Typical use case
        SimpleType simpleType1 = SimpleType.construct(String.class);
        SimpleType simpleType2 = SimpleType.construct(String.class);
        Assert.assertEquals(simpleType1, simpleType2);
    }
}
