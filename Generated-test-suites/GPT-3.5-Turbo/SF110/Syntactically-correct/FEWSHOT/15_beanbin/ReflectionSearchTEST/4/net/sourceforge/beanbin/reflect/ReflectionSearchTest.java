package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.MethodReflectionCriteria;
import net.sourceforge.beanbin.query.ReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectionSearchTest {

    private ReflectionSearch reflectionSearch;

    @Before
    public void setup() {
        Class clazz = ReflectionSearch.class;
        reflectionSearch = new ReflectionSearch(clazz);
    }

    @Test
    public void testHasAnnotation_shouldReturnTrueIfClassHasAnnotation() {
        assertTrue(reflectionSearch.hasAnnotation("Override"));
        assertTrue(reflectionSearch.hasAnnotation("Deprecated"));
        assertFalse(reflectionSearch.hasAnnotation("SuppressWarnings"));
        assertFalse(reflectionSearch.hasAnnotation("Test"));
    }

    @Test
    public void testMethodsThatHave_shouldAddMethodReflectionCriteriaToList() {
        try {
            reflectionSearch.methodsThatHave("test");
            List<Criteria> criterias = reflectionSearch.getQuery().getCriterias();
            boolean found = false;
            for (Criteria criteria : criterias) {
                if (criteria instanceof MethodReflectionCriteria) {
                    MethodReflectionCriteria methodCriteria = (MethodReflectionCriteria) criteria;
                    if (methodCriteria.getMethodName().equals("test") && methodCriteria.getSearchType() == SearchType.EQUALS) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                fail("MethodReflectionCriteria not added to list");
            }
        } catch (BeanBinException e) {
            fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testMethodsThatDontHave_shouldAddMethodReflectionCriteriaToList() {
        try {
            reflectionSearch.methodsThatDontHave("test");
            List<Criteria> criterias = reflectionSearch.getQuery().getCriterias();
            boolean found = false;
            for (Criteria criteria : criterias) {
                if (criteria instanceof MethodReflectionCriteria) {
                    MethodReflectionCriteria methodCriteria = (MethodReflectionCriteria) criteria;
                    if (methodCriteria.getMethodName().equals("test") && methodCriteria.getSearchType() == SearchType.DOESNOTEQUAL) {
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                fail("MethodReflectionCriteria not added to list");
            }
        } catch (BeanBinException e) {
            fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testAnd_shouldSetNextConditionalToAND() {
        reflectionSearch.and();
        assertEquals(Conditional.AND, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void testOr_shouldSetNextConditionalToOR() {
        reflectionSearch.or();
        assertEquals(Conditional.OR, reflectionSearch.getQuery().getNextConditional());
    }

    @Test
    public void testGetMethods_shouldReturnListOfMethods() {
        try {
            List<Method> methods = reflectionSearch.getMethods();
            assertEquals(27, methods.size());
        } catch (BeanBinException e) {
            fail("Exception thrown: " + e);
        }
    }

    @Test
    public void testIntersect_shouldRemoveMethodsNotPresentInToadd() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();

        Method method1 = getMethod("method1");
        Method method2 = getMethod("method2");
        Method method3 = getMethod("method3");

        master.add(method1);
        master.add(method2);
        master.add(method3);

        toadd.add(method1);

        reflectionSearch.intersect(master, toadd);

        assertEquals(1, master.size());
        assertTrue(master.contains(method1));
        assertFalse(master.contains(method2));
        assertFalse(master.contains(method3));
    }

    @Test
    public void testUnion_shouldAddMethodsNotAlreadyPresentInMaster() {
        List<Method> master = new ArrayList<>();
        List<Method> toadd = new ArrayList<>();

        Method method1 = getMethod("method1");
        Method method2 = getMethod("method2");
        Method method3 = getMethod("method3");

        master.add(method1);
        toadd.add(method2);
        toadd.add(method3);

        reflectionSearch.union(master, toadd);

        assertEquals(3, master.size());
        assertTrue(master.contains(method1));
        assertTrue(master.contains(method2));
        assertTrue(master.contains(method3));
    }

    private Method getMethod(String name) {
        try {
            return ReflectionSearch.class.getMethod(name);
        } catch (NoSuchMethodException e) {
            fail("Exception thrown: " + e);
            return null;
        }
    }

}