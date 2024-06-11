import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.*;
import net.sourceforge.beanbin.reflect.*;

public class ReflectionSearchTest {

    private Class testClass;
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        testClass = String.class;
        reflectionSearch = new ReflectionSearch(testClass);
    }

    @Test
    public void testHasAnnotation_whenAnnotationExists_returnTrue() {
        assertTrue(reflectionSearch.hasAnnotation("java.lang.Deprecated"));
    }
    
    @Test
    public void testHasAnnotation_whenClassNameExists_returnTrue() {
        assertTrue(reflectionSearch.hasAnnotation("Deprecated"));
    }
    
    @Test
    public void testHasAnnotation_whenAnnotationDoesNotExist_returnFalse() {
        assertFalse(reflectionSearch.hasAnnotation("java.lang.Override"));
    }
    
    @Test
    public void testHasAnnotation_whenWildcardExists_returnTrue() {
        assertTrue(reflectionSearch.hasAnnotation("*Deprecated"));
    }
    
    @Test
    public void testHasAnnotation_whenWildcardDoesNotMatch_returnFalse() {
        assertFalse(reflectionSearch.hasAnnotation("*Override"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("toString");
        assertTrue(result instanceof ReflectionSearch);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("hashCode");
        assertTrue(result instanceof ReflectionSearch);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch result = reflectionSearch.and();
        assertTrue(result instanceof ReflectionSearch);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch result = reflectionSearch.or();
        assertTrue(result instanceof ReflectionSearch);
    }

    @Test
    public void testGetMethods_whenNoMethodsMatchCriteria_returnEmptyList() throws BeanBinException {
        reflectionSearch.methodsThatHave("nonExistentMethod");
        assertEquals(0, reflectionSearch.getMethods().size());
    }
    
    @Test
    public void testGetMethods_whenMethodsMatchCriteria_returnMethodList() throws BeanBinException {
        reflectionSearch.methodsThatHave("toString");
        assertFalse(reflectionSearch.getMethods().isEmpty());
    }

    @After
    public void tearDown() {
        testClass = null;
        reflectionSearch = null;
    }
}