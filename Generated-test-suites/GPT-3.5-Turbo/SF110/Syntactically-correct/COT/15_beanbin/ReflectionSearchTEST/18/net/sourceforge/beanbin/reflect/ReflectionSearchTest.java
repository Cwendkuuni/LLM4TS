import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.Retention"));
        assertFalse(reflectionSearch.hasAnnotation("SomeAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatDontHave("hasAnnotation");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(6, methods.size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").and().methodsThatDontHave("methodsThatHave");
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").or().methodsThatDontHave("methodsThatHave");
        assertNotNull(reflectionSearch.getMethods());
    }
}


