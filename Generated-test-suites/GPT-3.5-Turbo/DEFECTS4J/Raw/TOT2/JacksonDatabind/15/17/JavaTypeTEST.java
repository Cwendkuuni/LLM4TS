import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    @Test
    public void testWithGetTypeHandler() {
        Object mockHandler = new Object();
        JavaType instance = new JavaTypeSubclass(mockHandler, null, false);
        
        JavaType result = instance.withTypeHandler(mockHandler);
        
        assertEquals(mockHandler, result.getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object mockHandler = new Object();
        JavaType instance = new JavaTypeSubclass(null, mockHandler, true);
        
        JavaType result = instance.withContentValueHandler(mockHandler);
        
        assertEquals(mockHandler, result.getValueHandler());
    }

    @Test
    public void testNarrowBy() {
        JavaType instance = new JavaTypeSubclass(null, null, false);
        
        JavaType result = instance.narrowBy(Integer.class);
        
        assertEquals(instance, result);
    }

}