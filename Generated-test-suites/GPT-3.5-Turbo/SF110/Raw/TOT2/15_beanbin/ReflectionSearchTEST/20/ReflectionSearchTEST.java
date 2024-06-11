package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.*;
import org.junit.Test;

public class ReflectionSearchTest {

    @Test
    public void testHasAnnotation() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
    }

    @Test
    public void testMethodsThatHave() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        try {
            reflectionSearch.methodsThatHave("method");
        } catch (BeanBinException e) {
            fail("Exception thrown for valid criteria");
        }
    }

    @Test
    public void testMethodsThatDontHave() {
        Class clazz = MyClass.class;
        ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
        try {
            reflectionSearch.methodsThatDontHave("method");
        } catch (BeanBinException e) {
            fail("Exception thrown for valid criteria");
        }
    }

    @Test
    public void testIntersect() {
        List<Method> master = new ArrayList<>();
        master.add(MyClass.class.getMethod("method1"));
        master.add(MyClass.class.getMethod("method2"));
        List<Method> toadd = new ArrayList<>();
        toadd.add(MyClass.class.getMethod("method2"));
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.intersect(master, toadd);
        assertEquals(1, master.size());
        assertEquals("method2", master.get(0).getName());
    }

    @Test
    public void testUnion() {
        List<Method> master = new ArrayList<>();
        master.add(MyClass.class.getMethod("method1"));
        List<Method> toadd = new ArrayList<>();
        toadd.add(MyClass.class.getMethod("method2"));
        ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
        reflectionSearch.union(master, toadd);
        assertEquals(2, master.size());
    }

    // Additional test cases for edge cases and error scenarios should be added here

}