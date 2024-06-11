import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);

        Assert.assertEquals(Object.class, simpleType.getRawClass());
        Assert.assertFalse(simpleType.isContainerType());
        Assert.assertEquals("java.lang.Object", simpleType.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(String[].class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType withContentType = simpleType.withContentType(SimpleType.constructUnsafe(String.class));

        Assert.assertFalse(withContentType.isContainerType());
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            Assert.fail("IllegalArgumentException is expected, but not thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType withTypeHandler = simpleType.withTypeHandler(new Object());

        Assert.assertFalse(withTypeHandler.isContainerType());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        try {
            simpleType.withContentValueHandler(new Object());
            Assert.fail("IllegalArgumentException is expected, but not thrown");
        } catch (IllegalArgumentException e) {
            // Exception is expected
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType withStaticTyping = simpleType.withStaticTyping();

        Assert.assertFalse(withStaticTyping.isContainerType());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        JavaType refine = simpleType.refine(Object.class, TypeBindings.emptyBindings(), null, null);

        Assert.assertNull(refine);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder erasedSignature = simpleType.getErasedSignature(new StringBuilder());

        Assert.assertEquals("Ljava/lang/Object;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        StringBuilder genericSignature = simpleType.getGenericSignature(new StringBuilder());

        Assert.assertEquals("Ljava/lang/Object;", genericSignature.toString());
    }

    @Test
    public void testEqualsWithSameSimpleType() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(Object.class);

        Assert.assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testEqualsWithDifferentSimpleType() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);
        SimpleType simpleType2 = SimpleType.constructUnsafe(String.class);

        Assert.assertNotEquals(simpleType1, simpleType2);
    }

    @Test
    public void testEqualsWithObject() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);

        Assert.assertNotEquals(simpleType1, new Object());
    }

    @Test
    public void testEqualsWithNull() {
        SimpleType simpleType1 = SimpleType.constructUnsafe(Object.class);

        Assert.assertNotEquals(simpleType1, null);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);

        Assert.assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

}