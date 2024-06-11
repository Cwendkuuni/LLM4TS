import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
    
    private ReflectionSearch reflectionSearch;
    
    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
    }
    
    @Test
    public void testHasAnnotation_WithValidTerm_ReturnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
    }
    
    @Test
    public void testHasAnnotation_WithInvalidTerm_ReturnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation("InvalidAnnotation"));
    }
    
    @Test
    public void testHasAnnotation_WithWildcard_ReturnsTrue() {
        assertTrue(reflectionSearch.hasAnnotation("net.sourceforge.*"));
        assertTrue(reflectionSearch.hasAnnotation("Search*"));
    }
    
    @Test
    public void testHasAnnotation_WithEmptyTerm_ReturnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }
    
    @Test
    public void testHasAnnotation_WithNullTerm_ReturnsFalse() {
        assertFalse(reflectionSearch.hasAnnotation(null));
    }
    
    @Test
    public void testMethodsThatHave_WithValidTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("testMethod");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatHave_WithInvalidTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("invalidMethod");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatHave_WithEmptyTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave("");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatHave_WithNullTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatHave(null);
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatDontHave_WithValidTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("clone");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatDontHave_WithInvalidTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("invalidMethod");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatDontHave_WithEmptyTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("");
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testMethodsThatDontHave_WithNullTerm_ReturnsValidReflectionSearchObject() throws BeanBinException {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave(null);
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testAnd_ReturnsValidReflectionSearchObject() {
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testOr_ReturnsValidReflectionSearchObject() {
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
        assertTrue(result instanceof ReflectionSearch);
        assertSame(result, reflectionSearch);
    }
    
    @Test
    public void testGetMethods_WithValidQuery_ReturnsMethodList() throws BeanBinException {
        reflectionSearch.methodsThatHave("testMethod");
        reflectionSearch.and().methodsThatDontHave("clone");
        
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(2, methods.size());
    }
    
    @Test
    public void testGetMethods_WithEmptyQuery_ReturnsEmptyMethodList() throws BeanBinException {
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods);
        assertEquals(0, methods.size());
    }
    
    @Test(expected = BeanBinException.class)
    public void testGetMethods_WithInvalidQuery_ThrowsBeanBinException() throws BeanBinException {
        reflectionSearch.methodsThatHave("invalidMethod");
        reflectionSearch.and().methodsThatDontHave("notFoundMethod");
        
        reflectionSearch.getMethods();
    }
    
    @Test
    public void testIntersect_WithMatchingMethods_ReturnsIntersectionList() {
        List<Method> master = new ArrayList<>();
        master.add(getMethod("methodA"));
        master.add(getMethod("methodB"));
        master.add(getMethod("methodC"));
        
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(getMethod("methodA"));
        toAdd.add(getMethod("methodC"));
        toAdd.add(getMethod("methodD"));
        
        reflectionSearch.intersect(master, toAdd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(getMethod("methodA")));
        assertTrue(master.contains(getMethod("methodC")));
    }
    
    @Test
    public void testIntersect_WithNoMatchingMethods_ReturnsEmptyList() {
        List<Method> master = new ArrayList<>();
        master.add(getMethod("methodA"));
        master.add(getMethod("methodB"));
        master.add(getMethod("methodC"));
        
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(getMethod("methodD"));
        toAdd.add(getMethod("methodE"));
        toAdd.add(getMethod("methodF"));
        
        reflectionSearch.intersect(master, toAdd);
        
        assertEquals(0, master.size());
    }
    
    @Test
    public void testUnion_WithMatchingMethods_ReturnsUnionList() {
        List<Method> master = new ArrayList<>();
        master.add(getMethod("methodA"));
        master.add(getMethod("methodB"));
        
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(getMethod("methodB"));
        toAdd.add(getMethod("methodC"));
        
        reflectionSearch.union(master, toAdd);
        
        assertEquals(3, master.size());
        assertTrue(master.contains(getMethod("methodA")));
        assertTrue(master.contains(getMethod("methodB")));
        assertTrue(master.contains(getMethod("methodC")));
    }
    
    @Test
    public void testUnion_WithNoMatchingMethods_ReturnsExpandedList() {
        List<Method> master = new ArrayList<>();
        master.add(getMethod("methodA"));
        master.add(getMethod("methodB"));
        
        List<Method> toAdd = new ArrayList<>();
        toAdd.add(getMethod("methodC"));
        toAdd.add(getMethod("methodD"));
        
        reflectionSearch.union(master, toAdd);
        
        assertEquals(4, master.size());
        assertTrue(master.contains(getMethod("methodA")));
        assertTrue(master.contains(getMethod("methodB")));
        assertTrue(master.contains(getMethod("methodC")));
        assertTrue(master.contains(getMethod("methodD")));
    }
    
    private Method getMethod(String methodName) {
        try {
            return ReflectionSearch.class.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("Method not found: " + methodName);
        }
    }
}