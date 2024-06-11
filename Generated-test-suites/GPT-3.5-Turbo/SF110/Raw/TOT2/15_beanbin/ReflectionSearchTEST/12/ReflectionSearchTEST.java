import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.lang.reflect.Method;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = TestClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }

    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        assertTrue(reflectionSearch.hasWildcard("wild*card"));
        assertFalse(reflectionSearch.hasWildcard("noWildcard"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatHave("testMethod");
        List<Method> methods = reflectionSearch.getMethods();
        assertTrue(methods.size() > 0);
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        reflectionSearch.methodsThatDontHave("nonExistentMethod");
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size());
    }

    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        List<Method> master = new ArrayList<>();
        List<Method> toAdd = new ArrayList<>();
        master.add(TestClass.class.getMethod("testMethod"));
        toAdd.add(TestClass.class.getMethod("testMethod"));
        reflectionSearch.intersect(master, toAdd);
        assertEquals(1, master.size());
    }

    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(TestClass.class);
        List<Method> master = new ArrayList<>();
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(TestClass.class.getMethod("testMethod"));
        reflectionSearch.union(master, toAdd);
        assertEquals(1, master.size());
    }

}

class TestClass {
    @TestAnnotation
    public void testMethod() {}
    public void otherMethod() {}
}