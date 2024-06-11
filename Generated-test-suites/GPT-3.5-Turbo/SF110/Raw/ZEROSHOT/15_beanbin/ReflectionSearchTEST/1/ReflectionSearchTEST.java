import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        assertFalse(reflectionSearch.hasAnnotation(""));
        assertFalse(reflectionSearch.hasAnnotation("Test"));
        assertFalse(reflectionSearch.hasAnnotation("NotAnAnnotation"));
    }
    
    @Test
    public void testHasWildcard() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        assertFalse(reflectionSearch.hasWildcard(""));
        assertFalse(reflectionSearch.hasWildcard("NoWildcard"));
        assertTrue(reflectionSearch.hasWildcard("*Wildcard*"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch returned = reflectionSearch.methodsThatHave("term");
        assertEquals(reflectionSearch, returned);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch returned = reflectionSearch.methodsThatDontHave("term");
        assertEquals(reflectionSearch, returned);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch returned = reflectionSearch.and();
        assertEquals(reflectionSearch, returned);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch returned = reflectionSearch.or();
        assertEquals(reflectionSearch, returned);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> methods = reflectionSearch.getMethods();
        
        assertNotNull(methods);
    }
    
    @Test
    public void testIntersect() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<Method>();
        master.add(ReflectionSearch.class.getDeclaredMethod("hasAnnotation", String.class));
        master.add(ReflectionSearch.class.getDeclaredMethod("hasWildcard", String.class));
        
        List<Method> toadd = new ArrayList<Method>();
        toadd.add(ReflectionSearch.class.getDeclaredMethod("testHasAnnotation"));
        toadd.add(ReflectionSearch.class.getDeclaredMethod("testHasWildcard"));
        
        reflectionSearch.intersect(master, toadd);
        
        assertEquals(2, master.size());
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("hasAnnotation", String.class)));
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("hasWildcard", String.class)));
    }
    
    @Test
    public void testUnion() throws NoSuchMethodException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<Method>();
        master.add(ReflectionSearch.class.getDeclaredMethod("hasAnnotation", String.class));
        master.add(ReflectionSearch.class.getDeclaredMethod("hasWildcard", String.class));
        
        List<Method> toadd = new ArrayList<Method>();
        toadd.add(ReflectionSearch.class.getDeclaredMethod("testHasAnnotation"));
        toadd.add(ReflectionSearch.class.getDeclaredMethod("testHasWildcard"));
        
        reflectionSearch.union(master, toadd);
        
        assertEquals(4, master.size());
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("hasAnnotation", String.class)));
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("hasWildcard", String.class)));
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("testHasAnnotation")));
        assertTrue(master.contains(ReflectionSearch.class.getDeclaredMethod("testHasWildcard")));
    }
}