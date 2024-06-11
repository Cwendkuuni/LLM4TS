import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.*;
import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.query.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        assertFalse(search.hasAnnotation("Test"));
        assertTrue(search.hasAnnotation("java.lang.annotation.Retention"));
        assertFalse(search.hasAnnotation("NonExistentAnnotation"));
        assertTrue(search.hasAnnotation("*Retention"));
        assertFalse(search.hasAnnotation("*NonExistent"));
    }
    
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        search.methodsThatHave("test").and().methodsThatHave("search");
        assertEquals(1, search.getMethods().size());
        
        search.methodsThatHave("*test").and().methodsThatHave("search");
        assertEquals(1, search.getMethods().size());
        
        search.methodsThatHave("NonExistent");
        assertEquals(0, search.getMethods().size());
    }
    
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        search.methodsThatDontHave("test").and().methodsThatDontHave("search");
        assertEquals(0, search.getMethods().size());
        
        search.methodsThatDontHave("*test").or().methodsThatDontHave("search");
        assertEquals(0, search.getMethods().size());
        
        search.methodsThatDontHave("NonExistent");
        assertEquals(1, search.getMethods().size());
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        assertSame(search, search.and());
    }
    
    @Test
    public void testOr() {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        assertSame(search, search.or());
    }
    
    @Test
    public void testGetMethods() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        assertEquals(0, search.getMethods().size());
        
        search.methodsThatHave("test").and().methodsThatHave("search");
        assertEquals(1, search.getMethods().size());
    }
    
    @Test
    public void testIntersect() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        Method method1 = ReflectionSearch.class.getDeclaredMethod("testIntersect", List.class, List.class);
        Method method2 = ReflectionSearch.class.getDeclaredMethod("testUnion", List.class, List.class);
        master.add(method1);
        master.add(method2);
        toadd.add(method1);
        
        search.intersect(master, toadd);
        assertEquals(1, master.size());
        assertTrue(master.contains(method1));
        
        search.intersect(toadd, toadd);
        assertEquals(1, toadd.size());
        assertTrue(toadd.contains(method1));
    }
    
    @Test
    public void testUnion() throws BeanBinException {
        ReflectionSearch search = new ReflectionSearch(ReflectionSearch.class);
        
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        Method method1 = ReflectionSearch.class.getDeclaredMethod("testIntersect", List.class, List.class);
        Method method2 = ReflectionSearch.class.getDeclaredMethod("testUnion", List.class, List.class);
        master.add(method1);
        toadd.add(method2);
        
        search.union(master, toadd);
        assertEquals(2, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
        
        search.union(toadd, toadd);
        assertEquals(1, toadd.size());
        assertTrue(toadd.contains(method2));
    }
}