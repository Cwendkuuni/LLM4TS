import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        boolean result = reflectionSearch.hasAnnotation("Test");
        assertFalse(result);
    }
    
    @Test
    public void testHasWildcardAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        boolean result = reflectionSearch.hasAnnotation("*");
        assertFalse(result);
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatHave("test");
        assertNotNull(result);
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("test");
        assertNotNull(result);
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> result = reflectionSearch.getMethods();
        assertNotNull(result);
    }
    
    @Test
    public void testIntersect() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<>();
        master.add(ReflectionSearch.class.getDeclaredMethods()[0]);
        master.add(ReflectionSearch.class.getDeclaredMethods()[1]);
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(ReflectionSearch.class.getDeclaredMethods()[0]);
        
        reflectionSearch.intersect(master, toadd);
        
        assertEquals(1, master.size());
        assertEquals(ReflectionSearch.class.getDeclaredMethods()[0], master.get(0));
    }
    
    @Test
    public void testUnion() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<>();
        master.add(ReflectionSearch.class.getDeclaredMethods()[0]);
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(ReflectionSearch.class.getDeclaredMethods()[1]);
        
        reflectionSearch.union(master, toadd);
        
        assertEquals(2, master.size());
        assertEquals(ReflectionSearch.class.getDeclaredMethods()[0], master.get(0));
        assertEquals(ReflectionSearch.class.getDeclaredMethods()[1], master.get(1));
    }
}