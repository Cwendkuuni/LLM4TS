import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.SearchType;
import net.sourceforge.beanbin.query.Conditional;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.reflect.ReflectionCriteria;
import net.sourceforge.beanbin.Shelf;

public class ReflectionSearchTest {

    private Class clazz;
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        clazz = SomeClass.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("SomeAnnotation"));
        assertFalse(reflectionSearch.hasAnnotation("NonExistingAnnotation"));
    }
    
    @Test
    public void testHasAnnotationWildcard() {
        assertTrue(reflectionSearch.hasAnnotation("Some*"));
        assertFalse(reflectionSearch.hasAnnotation("NonExisting*"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("methodName");
        assertNotNull(result);
        assertEquals(result.getMethods().size(), 1);
        assertEquals(result.getMethods().get(0).getName(), "methodName");
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("methodName");
        assertNotNull(result);
        assertEquals(result.getMethods().size(), 0);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
        assertEquals(result, reflectionSearch);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
        assertEquals(result, reflectionSearch);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        reflectionSearch.methodsThatHave("methodName");
        reflectionSearch.or();
        reflectionSearch.methodsThatHave("otherMethodName");
        
        assertEquals(reflectionSearch.getMethods().size(), 2);
        assertEquals(reflectionSearch.getMethods().get(0).getName(), "methodName");
        assertEquals(reflectionSearch.getMethods().get(1).getName(), "otherMethodName");
    }
    
    @Test
    public void testIntersect() throws NoSuchMethodException {
        Method method1 = SomeClass.class.getMethod("methodName");
        Method method2 = SomeClass.class.getMethod("otherMethodName");
        
        reflectionSearch.intersect(reflectionSearch.getMethods(), Arrays.asList(method1, method2));
        
        assertEquals(reflectionSearch.getMethods().size(), 2);
        assertTrue(reflectionSearch.getMethods().contains(method1));
        assertTrue(reflectionSearch.getMethods().contains(method2));
    }
    
    @Test
    public void testUnion() throws NoSuchMethodException {
        Method method1 = SomeClass.class.getMethod("methodName");
        Method method2 = SomeClass.class.getMethod("otherMethodName");
        
        reflectionSearch.union(reflectionSearch.getMethods(), Arrays.asList(method1, method2));
        
        assertEquals(reflectionSearch.getMethods().size(), 2);
        assertTrue(reflectionSearch.getMethods().contains(method1));
        assertTrue(reflectionSearch.getMethods().contains(method2));
    }
}