import org.junit.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        // Test case 1: Annotation exists
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        boolean hasAnnotation = reflectionSearch.hasAnnotation("ExampleAnnotation");
        Assert.assertTrue(hasAnnotation);

        // Test case 2: Annotation does not exist
        reflectionSearch = new ReflectionSearch(ExampleClass.class);
        hasAnnotation = reflectionSearch.hasAnnotation("NonExistentAnnotation");
        Assert.assertFalse(hasAnnotation);
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        // Test case 1: Method with specified term exists
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.methodsThatHave("exampleTerm");
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(1, methods.size());
        Assert.assertEquals("exampleMethod", methods.get(0).getName());

        // Test case 2: Method with specified term does not exist
        reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.methodsThatHave("nonExistentTerm");
        methods = reflectionSearch.getMethods();
        Assert.assertEquals(0, methods.size());
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        // Test case 1: Method without specified term exists
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.methodsThatDontHave("exampleTerm");
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(0, methods.size());

        // Test case 2: Method without specified term does not exist
        reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.methodsThatDontHave("nonExistentTerm");
        methods = reflectionSearch.getMethods();
        Assert.assertEquals(1, methods.size());
        Assert.assertEquals("exampleMethod", methods.get(0).getName());
    }

    @Test
    public void testAnd() {
        // Test case 1: Next conditional is set to AND
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.and();
        Assert.assertEquals(Conditional.AND, reflectionSearch.query.getNextConditional());
    }

    @Test
    public void testOr() {
        // Test case 1: Next conditional is set to OR
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        reflectionSearch.or();
        Assert.assertEquals(Conditional.OR, reflectionSearch.query.getNextConditional());
    }

    @Test
    public void testGetMethods() throws BeanBinException {
        // Test case 1: Methods are retrieved correctly
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        List<Method> methods = reflectionSearch.getMethods();
        Assert.assertEquals(1, methods.size());
        Assert.assertEquals("exampleMethod", methods.get(0).getName());
    }

    @Test
    public void testIntersect() {
        // Test case 1: Intersecting two lists correctly
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        List<Method> master = new ArrayList<>();
        master.add(ExampleClass.class.getDeclaredMethods()[0]);
        List<Method> toadd = new ArrayList<>();
        toadd.add(ExampleClass.class.getDeclaredMethods()[0]);
        reflectionSearch.intersect(master, toadd);
        Assert.assertEquals(1, master.size());
    }

    @Test
    public void testUnion() {
        // Test case 1: Combining two lists correctly
        ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
        List<Method> master = new ArrayList<>();
        master.add(ExampleClass.class.getDeclaredMethods()[0]);
        List<Method> toadd = new ArrayList<>();
        toadd.add(ExampleClass.class.getDeclaredMethods()[1]);
        reflectionSearch.union(master, toadd);
        Assert.assertEquals(2, master.size());
    }

    private static class ExampleClass {

        @ExampleAnnotation
        public void exampleMethod(String exampleParam) {
            // Method body
        }

        public void anotherMethod() {
            // Method body
        }
    }
}