import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.lang.reflect.Method;
import java.lang.annotation.*;

public class ReflectionSearchTest {
    
    // Test case for hasAnnotation method
    @Test
    public void testHasAnnotation() {
        Class clazz = ReflectionSearch.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertFalse(reflectionSearch.hasAnnotation("Test")); // Test a non-existing annotation
    }
    
    // Test case for methodsThatHave method
    @Test
    public void testMethodsThatHave() throws BeanBinException {
        Class clazz = ReflectionSearch.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatHave("Test"); // Test adding a method criteria
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(0, methods.size()); // Assert that no methods match the criteria
    }
    
    // Test case for methodsThatDontHave method
    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        Class clazz = ReflectionSearch.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.methodsThatDontHave("toString"); // Test adding a method criteria
        List<Method> methods = reflectionSearch.getMethods();
        assertEquals(1, methods.size()); // Assert that methods don't have the specified method
    }
    
    // Test case for getMethods method
    @Test
    public void testGetMethods() throws BeanBinException {
        Class clazz = ReflectionSearch.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        List<Method> methods = reflectionSearch.getMethods();
        assertNotNull(methods); // Assert that the list of methods is not null
    }
    
    // Test case for edge case in intersect method
    @Test
    public void testIntersectEdgeCase() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        master.add(ReflectionSearch.class.getMethods()[0]);
        toadd.add(ReflectionSearch.class.getMethods()[1]);
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        reflectionSearch.intersect(master, toadd);
        assertEquals(0, master.size()); // Assert that intersect method removes elements correctly
    }
    
    // Test case for error scenario in union method
    @Test
    public void testUnionErrorCase() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();
        Method method = ReflectionSearch.class.getMethods()[0];
        master.add(method);
        toadd.add(method);
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);
        // Attempting to add a duplicate method to the master list should not throw an error
        reflectionSearch.union(master, toadd);
        assertEquals(1, master.size()); // Assert that union method does not add duplicate elements
    }
}