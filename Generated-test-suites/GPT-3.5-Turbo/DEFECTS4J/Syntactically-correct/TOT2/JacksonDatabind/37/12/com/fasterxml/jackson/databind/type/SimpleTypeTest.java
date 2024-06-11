import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
    }

    @Test
    public void testGenericSignature() {
        StringBuilder sb = new StringBuilder();
        String expectedSignature = "Lcom/fasterxml/jackson/databind/type/SimpleType<Lcom/fasterxml/jackson/databind/JavaType;>;";
        
        String actualSignature = new SimpleType(JavaType.class).getGenericSignature(sb).toString();
        
        assertEquals(expectedSignature, actualSignature);
    }

    // Add more test cases for other public methods as needed

}