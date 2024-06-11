import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.annotation.*;
import net.sourceforge.beanbin.BeanBinException;

public class ReflectionSearchTest {

    @Test
    public void hasAnnotation_Test() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        assert reflectionSearch.hasAnnotation("Test");
        assert reflectionSearch.hasAnnotation("Test.class");
        assert !reflectionSearch.hasAnnotation("Before");

        // Edge Cases
        assert reflectionSearch.hasAnnotation("SuppressWarnings");
        assert reflectionSearch.hasAnnotation("Target");
        
        // Error Scenarios
        assert !reflectionSearch.hasAnnotation(null);
        assert !reflectionSearch.hasAnnotation("*");
        assert !reflectionSearch.hasAnnotation("");
    }

    @Test
    public void hasWildcard_Test() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        assert reflectionSearch.hasWildcard("TestClass*");
        assert reflectionSearch.hasWildcard("*Annotation");
        assert !reflectionSearch.hasWildcard("Test");
        
        // Edge Cases
        assert reflectionSearch.hasWildcard("*ReflectionSearch");
        assert reflectionSearch.hasWildcard("Reflection*");
        assert reflectionSearch.hasWildcard("*");
        
        // Error Scenarios
        assert !reflectionSearch.hasWildcard(null);
        assert !reflectionSearch.hasWildcard("");
    }

    @Test
    public void methodsThatHave_Test() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatHave("Test");
        assert reflectionSearch.getMethods().size() == 2;

        // Edge Cases
        reflectionSearch.methodsThatHave("hasAnnotation");
        assert reflectionSearch.getMethods().size() == 3;

        // Error Scenarios
        reflectionSearch.methodsThatHave(null);
        assert reflectionSearch.getMethods().size() == 0;
        reflectionSearch.methodsThatHave("");
        assert reflectionSearch.getMethods().size() == 0;
    }

    @Test
    public void methodsThatDontHave_Test() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatDontHave("Test");
        assert reflectionSearch.getMethods().size() == 6;

        // Edge Cases
        reflectionSearch.methodsThatDontHave("hasAnnotation");
        assert reflectionSearch.getMethods().size() == 5;

        // Error Scenarios
        reflectionSearch.methodsThatDontHave(null);
        assert reflectionSearch.getMethods().size() == 0;
        reflectionSearch.methodsThatDontHave("");
        assert reflectionSearch.getMethods().size() == 0;
    }

    @Test
    public void and_Test() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatHave("Test").and().methodsThatDontHave("Before");
        assert reflectionSearch.getMethods().size() == 1;

        // Edge Cases
        reflectionSearch.methodsThatHave("TestClass*").and().methodsThatDontHave("ReflectionSearch");
        assert reflectionSearch.getMethods().size() == 0;

        // Error Scenarios
        reflectionSearch.methodsThatHave(null).and().methodsThatDontHave("Test");
        assert reflectionSearch.getMethods().size() == 0;
        reflectionSearch.methodsThatHave("").and().methodsThatDontHave("Test");
        assert reflectionSearch.getMethods().size() == 0;
    }

    @Test
    public void or_Test() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatHave("Test").or().methodsThatDontHave("Before");
        assert reflectionSearch.getMethods().size() == 7;

        // Edge Cases
        reflectionSearch.methodsThatHave("TestClass*").or().methodsThatDontHave("ReflectionSearch");
        assert reflectionSearch.getMethods().size() == 7;

        // Error Scenarios
        reflectionSearch.methodsThatHave(null).or().methodsThatDontHave("Test");
        assert reflectionSearch.getMethods().size() == 0;
        reflectionSearch.methodsThatHave("").or().methodsThatDontHave("Test");
        assert reflectionSearch.getMethods().size() == 0;
    }

    @Test
    public void getMethods_Test() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        assert reflectionSearch.getMethods().size() == 7;

        // Edge Cases
        ReflectionSearch emptyReflectionSearch = new ReflectionSearch(EmptyTestClass.class);
        assert emptyReflectionSearch.getMethods().size() == 0;

        // Error Scenarios
        ReflectionSearch nullReflectionSearch = new ReflectionSearch(null);
        try {
            nullReflectionSearch.getMethods();
            assert false;
        } catch (BeanBinException e) {
            assert true;
        }
    }

    @Test
    public void intersect_Test() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatHave("Test");
        assert reflectionSearch.getMethods().size() > 0;

        // Edge Cases
        ReflectionSearch emptyReflectionSearch = new ReflectionSearch(EmptyTestClass.class);
        emptyReflectionSearch.methodsThatHave("Test");
        reflectionSearch.intersect(reflectionSearch.getMethods(), emptyReflectionSearch.getMethods());
        assert reflectionSearch.getMethods().size() == 0;

        // Error Scenarios
        ReflectionSearch nullReflectionSearch = new ReflectionSearch(null);
        nullReflectionSearch.methodsThatHave("Test");
        reflectionSearch.intersect(reflectionSearch.getMethods(), nullReflectionSearch.getMethods());
        assert reflectionSearch.getMethods().size() == 0;
    }

    @Test
    public void union_Test() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);

        // Typical Use Cases
        reflectionSearch.methodsThatHave("Test");
        int initialSize = reflectionSearch.getMethods().size();

        // Edge Cases
        reflectionSearch.methodsThatDontHave("EmptyTestClass");
        reflectionSearch.union(reflectionSearch.getMethods(), reflectionSearch.getMethods());
        assert reflectionSearch.getMethods().size() == initialSize;

        // Error Scenarios
        ReflectionSearch nullReflectionSearch = new ReflectionSearch(null);
        nullReflectionSearch.union(reflectionSearch.getMethods(), nullReflectionSearch.getMethods());
        assert reflectionSearch.getMethods().size() == initialSize;
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    @Deprecated
    public class EmptyTestClass {

        @Test
        public void method1() {
        }

        @TestAnnotation
        public void method2() {
        }

        public void method3() {
        }
    }

}