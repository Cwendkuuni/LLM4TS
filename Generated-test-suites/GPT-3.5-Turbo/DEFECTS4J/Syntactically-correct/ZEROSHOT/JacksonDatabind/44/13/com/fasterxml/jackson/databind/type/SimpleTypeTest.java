import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {

    private SimpleType simpleType;

    @Before
    public void setup() {
        simpleType = new SimpleType(Object.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType constructed = SimpleType.constructUnsafe(Object.class);
        Assert.assertEquals(simpleType, constructed);
    }

    @Test
    public void testConstruct() {
        SimpleType constructed = SimpleType.construct(Object.class);
        Assert.assertEquals(simpleType, constructed);
    }

    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
        Assert.assertEquals(handler, typeWithHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType typeWithValueHandler = simpleType.withValueHandler(handler);
        Assert.assertEquals(handler, typeWithValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(null);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType staticType = simpleType.withStaticTyping();
        Assert.assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        try {
            simpleType.refine(Object.class, null, null, null);
            Assert.fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testIsContainerType() {
        Assert.assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        Assert.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testEquals() {
        SimpleType sameType = new SimpleType(Object.class);
        Assert.assertEquals(simpleType, sameType);

        SimpleType differentType = new SimpleType(String.class);
        Assert.assertNotEquals(simpleType, differentType);

        Assert.assertNotEquals(simpleType, null);
        Assert.assertNotEquals(simpleType, new Object());
    }
}