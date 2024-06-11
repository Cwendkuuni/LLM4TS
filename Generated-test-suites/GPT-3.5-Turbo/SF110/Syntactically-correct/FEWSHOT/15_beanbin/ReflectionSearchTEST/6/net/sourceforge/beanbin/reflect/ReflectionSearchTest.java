package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.search.SearchType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {
    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        reflectionSearch = new ReflectionSearch(Object.class);
    }

    @Test
    public void testHasAnnotation() {
        assertFalse(reflectionSearch.hasAnnotation("test"));
    }

    @Test
    public void testHasAnnotationWildcard() {
        assertTrue(reflectionSearch.hasAnnotation("*Test"));
    }

    @Test
    public void testMethodsThatHave() throws BeanBinException {
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(Object.class.getMethod("equals", Object.class));
        expectedMethods.add(Object.class.getMethod("hashCode"));
        expectedMethods.add(Object.class.getMethod("toString"));

        reflectionSearch.methodsThatHave("equals");
        List<Method> methods = reflectionSearch.getMethods();

        assertEquals(expectedMethods.size(), methods.size());
        for (Method method : expectedMethods) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testMethodsThatDontHave() throws BeanBinException {
        List<Method> expectedMethods = new ArrayList<>();
        expectedMethods.add(Object.class.getMethod("wait"));
        expectedMethods.add(Object.class.getMethod("notify"));

        reflectionSearch.methodsThatDontHave("equals");
        List<Method> methods = reflectionSearch.getMethods();

        assertEquals(expectedMethods.size(), methods.size());
        for (Method method : expectedMethods) {
            assertTrue(methods.contains(method));
        }
    }

    @Test
    public void testAnd() {
        reflectionSearch.methodsThatHave("equals").and().methodsThatDontHave("wait");
        ReflectionCriteria criteria = reflectionSearch.getQuery().getCriterias().get(0);

        assertEquals(SearchType.EQUALS, ((MethodReflectionCriteria) criteria).getSearchType());
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void testOr() {
        reflectionSearch.methodsThatHave("equals").or().methodsThatDontHave("wait");
        ReflectionCriteria criteria = reflectionSearch.getQuery().getCriterias().get(0);

        assertEquals(SearchType.EQUALS, ((MethodReflectionCriteria) criteria).getSearchType());
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }
}