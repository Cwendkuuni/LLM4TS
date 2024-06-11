import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructSafety() {
        try {
            SimpleType.construct(Map.class);
            fail("Should have thrown IllegalArgumentException for Map class");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType narrowedType = simpleType._narrow(String.class);
        assertNotEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType typeWithHandler = simpleType.withTypeHandler(new Object());
        assertNotNull(typeWithHandler);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.constructUnsafe(Object.class);
        SimpleType typedType = simpleType.withStaticTyping();
        assertTrue(typedType.isFinal());
    }
}