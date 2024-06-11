import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(String.class);
        assertNotNull(simpleType);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Integer.class);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType._narrow(String.class);
        assertEquals(simpleType, narrowedType);
    }

    @Test
    public void testWithContentType() {
    	SimpleType simpleType = new SimpleType(String.class);
    	JavaType contentType = new JavaType(String.class);
    	assertThrows(IllegalArgumentException.class, () -> simpleType.withContentType(contentType));
    }

    @Test
    public void testWithValueHandler() {
    	SimpleType simpleType = new SimpleType(String.class);
    	Object handler = new Object();
    	SimpleType updatedSimpleType = simpleType.withValueHandler(handler);
    	assertEquals(handler, updatedSimpleType._valueHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertNotNull(erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertNotNull(genericSignature);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}