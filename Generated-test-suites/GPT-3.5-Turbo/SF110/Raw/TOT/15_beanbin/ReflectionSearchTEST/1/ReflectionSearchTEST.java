import org.junit.Test;
import static org.junit.Assert.*;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation_typicalUseCase() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        assertTrue(reflectionSearch.hasAnnotation("Entity"));
    }

    @Test
    public void testHasAnnotation_edgeCase_emptyTerm() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        assertFalse(reflectionSearch.hasAnnotation(""));
    }

    @Test
    public void testHasAnnotation_errorScenario_nullTerm() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(AnnotationTestClass.class);

        assertFalse(reflectionSearch.hasAnnotation(null));
    }

    @Test
    public void testMethodsThatHave_typicalUseCase() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodsTestClass.class);

        reflectionSearch.methodsThatHave("get").and().methodsThatHave("set");

        assertEquals(2, reflectionSearch.getMethods().size());
    }

    @Test
    public void testMethodsThatDontHave_typicalUseCase() throws BeanBinException {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodsTestClass.class);

        reflectionSearch.methodsThatDontHave("get").or().methodsThatDontHave("set");

        assertEquals(2, reflectionSearch.getMethods().size());
    }

    @Test
    public void testGetMethods_errorScenario_BeanBinException() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(MethodsTestClass.class);

        try {
            reflectionSearch.getMethods();
            fail("Exception should have been thrown.");
        } catch (BeanBinException e) {
            assertEquals("Some error message", e.getMessage());
        }
    }

    @Test
    public void testIntersect_typicalUseCase() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(IntersectTestClass.class);
        List<Method> master = new ArrayList<>();
        master.add(IntersectTestClass.class.getMethod("method1"));
        master.add(IntersectTestClass.class.getMethod("method2"));
        master.add(IntersectTestClass.class.getMethod("method3"));

        List<Method> toadd = new ArrayList<>();
        toadd.add(IntersectTestClass.class.getMethod("method2"));
        toadd.add(IntersectTestClass.class.getMethod("method3"));

        reflectionSearch.intersect(master, toadd);

        assertEquals(2, master.size());
        assertTrue(master.contains(IntersectTestClass.class.getMethod("method2")));
        assertTrue(master.contains(IntersectTestClass.class.getMethod("method3")));
    }

    @Test
    public void testUnion_typicalUseCase() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(UnionTestClass.class);
        List<Method> master = new ArrayList<>();
        master.add(UnionTestClass.class.getMethod("method1"));
        
        List<Method> toadd = new ArrayList<>();
        toadd.add(UnionTestClass.class.getMethod("method2"));
        toadd.add(UnionTestClass.class.getMethod("method3"));

        reflectionSearch.union(master, toadd);

        assertEquals(3, master.size());
        assertTrue(master.contains(UnionTestClass.class.getMethod("method1")));
        assertTrue(master.contains(UnionTestClass.class.getMethod("method2")));
        assertTrue(master.contains(UnionTestClass.class.getMethod("method3")));
    }
}