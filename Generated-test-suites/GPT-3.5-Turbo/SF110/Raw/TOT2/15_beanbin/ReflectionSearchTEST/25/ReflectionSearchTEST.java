import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ReflectionSearchTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface CustomAnnotation {
    }

    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertFalse(reflectionSearch.hasAnnotation("CustomAnnotation"));
        assertTrue(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertTrue(reflectionSearch.hasWildcard("Search*"));
        assertFalse(reflectionSearch.hasWildcard("Search"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.methodsThatHave("test").getMethods());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.methodsThatDontHave("xyz").getMethods());
    }

    @Test
    public void testAndGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        assertNotNull(reflectionSearch.getMethods());
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        Method method1 = ReflectionSearch.class.getMethods()[0];
        Method method2 = ReflectionSearch.class.getMethods()[1];
        Method method3 = ReflectionSearchTest.class.getMethods()[0];
        List<Method> master = new ArrayList<>();
        master.add(method1);
        master.add(method2);
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(method2);
        toAdd.add(method3);
        reflectionSearch.intersect(master, toAdd);
        assertEquals(1, master.size());
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
        Method method1 = ReflectionSearch.class.getMethods()[0];
        Method method2 = ReflectionSearch.class.getMethods()[1];
        Method method3 = ReflectionSearchTest.class.getMethods()[0];
        List<Method> master = new ArrayList<>();
        master.add(method1);
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(method2);
        toAdd.add(method3);
        reflectionSearch.union(master, toAdd);
        assertEquals(3, master.size());
    }
}