import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import net.sourceforge.beanbin.BeanBinException;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        assertTrue(reflection.hasAnnotation("Override"));
        assertFalse(reflection.hasAnnotation("Deprecated"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        assertTrue(reflection.hasWildcard("test*"));
        assertFalse(reflection.hasWildcard("test"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        reflection.methodsThatHave("toString");
        List<Method> methods = reflection.getMethods();
        assertEquals(1, methods.size());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        reflection.methodsThatDontHave("toString");
        List<Method> methods = reflection.getMethods();
        assertEquals(0, methods.size());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        reflection.methodsThatHave("toString").and().methodsThatHave("hashCode");
        List<Method> methods = reflection.getMethods();
        assertEquals(2, methods.size());
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflection = new ReflectionSearch(Object.class);
        reflection.methodsThatHave("toString").or().methodsThatHave("hashCode");
        List<Method> methods = reflection.getMethods();
        assertTrue(methods.size() > 0);
    }
}