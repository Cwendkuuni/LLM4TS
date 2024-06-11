package net.sourceforge.beanbin.reflect;

import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import net.sourceforge.beanbin.query.*;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(ReflectionSearchTest.class);
    }

    @Test
    public void testHasAnnotation_TypicalUseCase() {
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testHasAnnotation_AnnotationNameOnly() {
        assertTrue(reflectionSearch.hasAnnotation("TestAnnotation"));
    }

    @Test
    public void testHasAnnotation_ClassNameOnly() {
        assertTrue(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testHasAnnotation_NoMatchingAnnotations() {
        assertFalse(reflectionSearch.hasAnnotation("NoAnnotation"));
    }

    @Test
    public void testHasAnnotation_WildcardMatch() {
        assertTrue(reflectionSearch.hasAnnotation("*Test*"));
    }

    @Test
    public void testHasAnnotation_WildcardNoMatch() {
        assertFalse(reflectionSearch.hasAnnotation("*NoAnnotation*"));
    }

    @Test
    public void testHasAnnotation_NullTerm() {
        assertFalse(reflectionSearch.hasAnnotation(null));
    }

    @Test
    public void testHasAnnotation_EmptyTerm() {
        assertFalse(reflectionSearch.hasAnnotation(""));
    }
    
    @Test
    public void testMethodsThatHave_TypicalUseCase() {
        ReflectionSearch result = reflectionSearch.methodsThatHave("test");
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_NullTerm() throws BeanBinException {
        reflectionSearch.methodsThatHave(null);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_EmptyTerm() throws BeanBinException {
        reflectionSearch.methodsThatHave("");
    }

    @Test
    public void testMethodsThatDontHave_TypicalUseCase() {
        ReflectionSearch result = reflectionSearch.methodsThatDontHave("test");
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave_NullTerm() throws BeanBinException {
        reflectionSearch.methodsThatDontHave(null);
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave_EmptyTerm() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("");
    }

    @Test
    public void testAnd_TypicalUseCase() {
        ReflectionSearch result = reflectionSearch.and();
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testOr_TypicalUseCase() {
        ReflectionSearch result = reflectionSearch.or();
        assertNotNull(result);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testGetMethods_TypicalUseCase() throws BeanBinException {
        Method[] methods = ReflectionSearchTest.class.getMethods();
        assertEquals(methods.length, reflectionSearch.getMethods().size());
        for (Method method : methods) {
            assertTrue(reflectionSearch.getMethods().contains(method));
        }
    }

    @Test
    public void testGetMethods_NoMatchingMethods() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(String.class);
        assertTrue(reflectionSearch.getMethods().isEmpty());
    }

    @Test(expected = BeanBinException.class)
    public void testGetMethods_NullQuery() throws BeanBinException {
        reflectionSearch = new ReflectionSearch(null);
        reflectionSearch.getMethods();
    }

    @Test
    public void testIntersect_TypicalUseCase() throws NoSuchMethodException {
        Method method1 = ReflectionSearchTest.class.getMethod("testIntersect_TypicalUseCase");
        Method method2 = ReflectionSearchTest.class.getMethod("testAnd_TypicalUseCase");
        Method method3 = ReflectionSearchTest.class.getMethod("testOr_TypicalUseCase");
        
        List<Method> master = new ArrayList<>();
        master.add(method1);
        master.add(method2);
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(method1);
        toadd.add(method3);
        
        reflectionSearch.intersect(master, toadd);
        
        assertTrue(master.contains(method1));
        assertFalse(master.contains(method2));
        assertFalse(master.contains(method3));
    }

    @Test
    public void testUnion_TypicalUseCase() throws NoSuchMethodException {
        Method method1 = ReflectionSearchTest.class.getMethod("testUnion_TypicalUseCase");
        Method method2 = ReflectionSearchTest.class.getMethod("testGetMethods_TypicalUseCase");
        Method method3 = ReflectionSearchTest.class.getMethod("testIntersect_TypicalUseCase");
        
        List<Method> master = new ArrayList<>();
        master.add(method1);
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(method2);
        toadd.add(method3);
        
        reflectionSearch.union(master, toadd);
        
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
        assertTrue(master.contains(method3));
    }

}