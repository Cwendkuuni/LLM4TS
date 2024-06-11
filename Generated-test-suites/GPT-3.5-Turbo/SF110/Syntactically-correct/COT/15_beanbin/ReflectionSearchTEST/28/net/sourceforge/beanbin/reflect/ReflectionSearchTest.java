import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertTrue(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasAnnotationWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertTrue(reflectionSearch.hasAnnotation("org.junit.*"));
    }

    @Test
    public void testHasAnnotationFalse() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertFalse(reflectionSearch.hasAnnotation("Ignore"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatDontHave("hasWildcard");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").and().methodsThatDontHave("hasWildcard");
        assertEquals(1, reflectionSearch.getMethods().size());
    }

    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").or().methodsThatDontHave("hasWildcard");
        assertEquals(2, reflectionSearch.getMethods().size());
    }

    @Test
    public void testIntersection() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").and().methodsThatDontHave("hasWildcard");
        reflectionSearch.methodsThatHave("getMethods");
        assertEquals(0, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testUnion() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.methodsThatHave("hasAnnotation").or().methodsThatDontHave("hasWildcard");
        reflectionSearch.methodsThatHave("getMethods");
        assertEquals(2, reflectionSearch.getMethods().size());
    }

}