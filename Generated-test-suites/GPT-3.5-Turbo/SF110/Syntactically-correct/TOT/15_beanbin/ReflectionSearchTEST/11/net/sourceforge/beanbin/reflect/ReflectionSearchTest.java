import static org.junit.Assert.*;
import org.junit.*;

public class ReflectionSearchTest {
    
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(Object.class);
    }
    
    @Test
    public void testHasAnnotation_typicalCase() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }
    
    @Test
    public void testHasAnnotation_wildcardCase() {
        assertTrue(reflectionSearch.hasAnnotation("java.lang.annotation.*"));
    }
    
    @Test
    public void testHasAnnotation_doesNotExist() {
        assertFalse(reflectionSearch.hasAnnotation("NonExistAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave_typicalCase() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("method");
        assertEquals(reflectionSearch, result);
    }
    
    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_exceptionCase() throws BeanBinException {
        reflectionSearch.methodsThatHave(null);
    }
    
    @Test
    public void testMethodsThatDontHave_typicalCase() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("method");
        assertEquals(reflectionSearch, result);
    }
    
    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave_exceptionCase() throws BeanBinException {
        reflectionSearch.methodsThatDontHave(null);
    }
    
    @Test
    public void testAnd_typicalCase() {
        ReflectionSearch result = reflectionSearch.and();
        assertEquals(reflectionSearch, result);
    }
    
    @Test
    public void testOr_typicalCase() {
        ReflectionSearch result = reflectionSearch.or();
        assertEquals(reflectionSearch, result);
    }
    
    @Test
    public void testGetMethods_typicalCase() throws BeanBinException {
        assertFalse(reflectionSearch.getMethods().isEmpty());
    }
    
    @Test
    public void testGetMethods_emptyCase() throws BeanBinException {
        ReflectionSearch emptySearch = new ReflectionSearch(ReflectionSearch.class);
        assertTrue(emptySearch.getMethods().isEmpty());
    }
    
    @Test(expected = BeanBinException.class)
    public void testGetMethods_exceptionCase() throws BeanBinException {
        ReflectionSearch nullSearch = new ReflectionSearch(null);
        nullSearch.getMethods();
    }
}