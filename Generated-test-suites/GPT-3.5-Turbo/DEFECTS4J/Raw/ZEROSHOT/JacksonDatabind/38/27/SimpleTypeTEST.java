import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertTrue(simpleType.isContainerType() == false);
    }

    @Test
    public void testConstruct() {
        try {
            SimpleType constructType = SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
        
        try {
            SimpleType constructType = SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
        
        try {
            SimpleType constructType = SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
        
        SimpleType simpleType = SimpleType.construct(String.class);
        assertTrue(simpleType.isContainerType() == false);
    }

    @Test
    public void testWithContentType() {
        try {
            SimpleType simpleType = new SimpleType(Object.class);
            simpleType.withContentType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        SimpleType newType = simpleType.withValueHandler(valueHandler);
        assertTrue(newType.getValueHandler().equals(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        try {
            SimpleType simpleType = new SimpleType(Object.class);
            simpleType.withContentValueHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {}
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertTrue(newType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType refinedType = simpleType.refine(Object.class, null, null, null);
        assertTrue(refinedType == null);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.isContainerType() == false);
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertTrue(erasedSignature.toString().equals("Ljava/lang/Object;"));
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertTrue(genericSignature.toString().equals("Ljava/lang/Object;;"));
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType1));
        assertTrue(simpleType1.equals(simpleType2));
        assertFalse(simpleType1.equals(null));
        assertFalse(simpleType1.equals(new Object()));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertTrue(simpleType.toString().equals("[simple type, class java.lang.Object]"));
    }

}