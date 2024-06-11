package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReflectionSearchTest {
    
    @Test
    public void testHasAnnotation() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertFalse(reflectionSearch.hasAnnotation("Deprecated"));
        assertFalse(reflectionSearch.hasAnnotation("NonexistentAnnotation"));
    }
    
    @Test
    public void testMethodsThatHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

        try {
            reflectionSearch.methodsThatHave("testHasAnnotation");
            assertEquals(1, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testMethodsThatDontHave() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

        try {
            reflectionSearch.methodsThatDontHave("testMethodsThatDontHave");
            assertEquals(1, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAnd() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

        try {
            reflectionSearch.methodsThatHave("testHasAnnotation").and().methodsThatDontHave("testMethodsThatDontHave");
            assertEquals(0, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testOr() {
        ReflectionSearch reflectionSearch = new ReflectionSearch(ReflectionSearch.class);

        try {
            reflectionSearch.methodsThatHave("testHasAnnotation").or().methodsThatDontHave("testMethodsThatDontHave");
            assertEquals(2, reflectionSearch.getMethods().size());
        } catch (BeanBinException e) {
            e.printStackTrace();
        }
    }
}