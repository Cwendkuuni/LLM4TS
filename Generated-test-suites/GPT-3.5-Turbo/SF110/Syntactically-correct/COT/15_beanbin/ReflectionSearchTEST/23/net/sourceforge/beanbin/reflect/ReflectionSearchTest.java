
import org.junit.Test;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        assertTrue(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("NonexistentAnnotation"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
        assertEquals(2, methods.size());
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testHasAnnotation")));
        assertTrue(methods.contains(ReflectionSearchTest.class.getMethod("testMethodsThatHave")));
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test").and().methodsThatDontHave("Annotation");
        // assert something here
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("test").or().methodsThatDontHave("Annotation");
        // assert something here
    }

    // TODO: Add test cases for intersect() and union() methods
}
