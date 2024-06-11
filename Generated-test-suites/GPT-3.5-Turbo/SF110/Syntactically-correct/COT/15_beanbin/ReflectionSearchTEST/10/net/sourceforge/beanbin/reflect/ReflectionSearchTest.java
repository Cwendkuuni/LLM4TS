
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(SomeClass.class);
        assertTrue(reflectionSearch.hasAnnotation("SomeAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistentAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(SomeClass.class);
        reflectionSearch.methodsThatHave("someTerm");
        assertTrue(reflectionSearch.getMethods().size() > 0);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(SomeClass.class);
        reflectionSearch.methodsThatDontHave("someTerm");
        assertTrue(reflectionSearch.getMethods().isEmpty());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(SomeClass.class);
        reflectionSearch.methodsThatHave("term1").and().methodsThatHave("term2");
        assertTrue(reflectionSearch.getMethods().size() > 0);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(SomeClass.class);
        reflectionSearch.methodsThatHave("term1").or().methodsThatHave("term2");
        assertTrue(reflectionSearch.getMethods().size() > 0);
    }
}
