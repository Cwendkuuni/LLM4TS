import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.search.*;

public class ReflectionSearchTest {
    
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        Class clazz = ReflectionSearchTest.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }
    
    @Test
    public void testHasAnnotation_Typical() {
        assertTrue(reflectionSearch.hasAnnotation("Test"));
    }
    
    @Test
    public void testHasAnnotation_WildcardMatch() {
        assertTrue(reflectionSearch.hasAnnotation("j*"));
    }
    
    @Test
    public void testHasAnnotation_ClassNameMatch() {
        assertTrue(reflectionSearch.hasAnnotation("ReflectionSearchTest"));
    }
    
    @Test
    public void testHasAnnotation_NoMatch() {
        assertFalse(reflectionSearch.hasAnnotation("NoAnnotation"));
    }
    
    @Test
    public void testHasAnnotation_EmptyTerm() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }
    
    @Test
    public void testMethodsThatHave_Typical() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("testHasAnnotation_Typical");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatHave_WildCardMatch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("testHas*");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatHave_NoMatch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("NoMatchMethod");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatHave_EmptyTerm() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave_Typical() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("setUp");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave_WildCardMatch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("te*t");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave_NoMatch() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("testHasAnnotation_Typical");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave_EmptyTerm() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("");
        assertNotNull(result);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethods_Typical() throws BeanBinException {
        methodList = reflectionSearch.methodsThatHave("testHasAnnotation_Typical").getMethods();
        assertNotNull(methodList);
        assertFalse(methodList.isEmpty());
    }
    
    @Test
    public void testGetMethods_NoMatch() throws BeanBinException {
        methodList = reflectionSearch.methodsThatHave("NoMatchMethod").getMethods();
        assertNotNull(methodList);
        assertTrue(methodList.isEmpty());
    }
    
    @Test
    public void testGetMethods_EmptyQuery() throws BeanBinException {
        methodList = reflectionSearch.getMethods();
        assertNotNull(methodList);
        assertTrue(methodList.isEmpty());
    }
    
    @Test
    public void testIntersect() throws NoSuchMethodException {
        Method method1 = ReflectionSearchTest.class.getDeclaredMethod("testHasAnnotation_Typical");
        Method method2 = ReflectionSearchTest.class.getDeclaredMethod("testMethodsThatHave_Typical");
        
        List<Method> master = Arrays.asList(method1, method2);
        List<Method> toadd = Arrays.asList(method1);
        
        reflectionSearch.intersect(master, toadd);
        
        assertTrue(master.contains(method1));
        assertFalse(master.contains(method2));
    }
    
    @Test
    public void testUnion() throws NoSuchMethodException {
        Method method1 = ReflectionSearchTest.class.getDeclaredMethod("testHasAnnotation_Typical");
        Method method2 = ReflectionSearchTest.class.getDeclaredMethod("testMethodsThatHave_Typical");
        
        List<Method> master = Arrays.asList(method1);
        List<Method> toadd = Arrays.asList(method1, method2);
        
        reflectionSearch.union(master, toadd);
        
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
    }
    
}