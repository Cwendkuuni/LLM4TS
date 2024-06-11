package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.ReflectionCriteria;
import net.sourceforge.beanbin.ReflectionShelf;
import net.sourceforge.beanbin.query.Criteria;
import net.sourceforge.beanbin.query.SearchType;

public class ReflectionSearchTest {
    
    @Mock
    private Class clazz;
    
    @Mock
    private Query query;

    private ReflectionSearch reflectionSearch;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reflectionSearch = new ReflectionSearch(clazz);
        reflectionSearch.query = query;
    }

    @Test
    public void testHasAnnotation_withWildcardMatch_shouldReturnTrue() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.*");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertTrue(reflectionSearch.hasAnnotation("org.mockito*"));
    }

    @Test
    public void testHasAnnotation_withExactMatch_shouldReturnTrue() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.Mock");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertTrue(reflectionSearch.hasAnnotation("org.mockito.Mock"));
    }

    @Test
    public void testHasAnnotation_withTermNameSubstring_shouldReturnTrue() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.Mock");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertTrue(reflectionSearch.hasAnnotation(".Mock"));
    }

    @Test
    public void testHasAnnotation_withTermNameSubstringAndWildcard_shouldReturnTrue() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.Mock");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertTrue(reflectionSearch.hasAnnotation("org.mockito.Mo*"));
    }

    @Test
    public void testHasAnnotation_withNoMatch_shouldReturnFalse() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.Mock");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertFalse(reflectionSearch.hasAnnotation("org.mockito.MyAnnotation"));
    }

    @Test
    public void testHasAnnotation_withWildcardInTermName_shouldReturnFalse() {
        Annotation annotation = mock(Annotation.class);
        when(annotation.annotationType().getName()).thenReturn("org.mockito.Mock");

        when(clazz.getAnnotations()).thenReturn(new Annotation[] {annotation});

        assertFalse(reflectionSearch.hasAnnotation("org.mockito*MyAnnotation"));
    }

    @Test
    public void testHasAnnotation_withEmptyAnnotations_shouldReturnFalse() {
        when(clazz.getAnnotations()).thenReturn(new Annotation[] {});

        assertFalse(reflectionSearch.hasAnnotation("org.mockito.Mock"));
    }

    @Test
    public void testHasAnnotation_withNullAnnotations_shouldReturnFalse() {
        when(clazz.getAnnotations()).thenReturn(null);

        assertFalse(reflectionSearch.hasAnnotation("org.mockito.Mock"));
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatHave_shouldThrowBeanBinException() throws BeanBinException {
        reflectionSearch.methodsThatHave("test");

        verify(query).add(any(MethodReflectionCriteria.class));
    }

    @Test(expected = BeanBinException.class)
    public void testMethodsThatDontHave_shouldThrowBeanBinException() throws BeanBinException {
        reflectionSearch.methodsThatDontHave("test");

        verify(query).add(any(MethodReflectionCriteria.class));
    }

    @Test
    public void testAnd_shouldSetNextConditionalToAND() {
        ReflectionSearch result = reflectionSearch.and();

        verify(query).setNextConditional(Conditional.AND);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testOr_shouldSetNextConditionalToOR() {
        ReflectionSearch result = reflectionSearch.or();

        verify(query).setNextConditional(Conditional.OR);
        assertEquals(reflectionSearch, result);
    }

    @Test
    public void testGetMethods_withNullListInReflectionShelf_shouldRetrieveMethods() throws BeanBinException {
        Method method = mock(Method.class);
        List<Method> methodList = new ArrayList<>();
        methodList.add(method);

        when(query.getCriterias()).thenReturn(new Criteria[] {mock(ReflectionCriteria.class)});

        when(query.get()).thenReturn(null).thenReturn(methodList);

        List<Method> result = reflectionSearch.getMethods();

        verify(query, times(2)).getCriterias();
        verify(query).get();
        verify(query).set(anyListOf(Method.class));

        assertEquals(methodList, result);
    }

    @Test
    public void testGetMethods_withEmptyListInReflectionShelf_shouldRetrieveMethods() throws BeanBinException {
        Method method = mock(Method.class);
        List<Method> methodList = new ArrayList<>();
        methodList.add(method);

        when(query.getCriterias()).thenReturn(new Criteria[] {mock(ReflectionCriteria.class)});

        when(query.get()).thenReturn(new ArrayList<>()).thenReturn(methodList);

        List<Method> result = reflectionSearch.getMethods();

        verify(query, times(2)).getCriterias();
        verify(query).get();
        verify(query).set(anyListOf(Method.class));

        assertEquals(methodList, result);
    }

    @Test
    public void testGetMethods_withExistingListInReflectionShelf_shouldReturnMethods() throws BeanBinException {
        Method method = mock(Method.class);
        List<Method> methodList = new ArrayList<>();
        methodList.add(method);

        when(query.getCriterias()).thenReturn(new Criteria[] {mock(ReflectionCriteria.class)});

        when(query.get()).thenReturn(methodList);

        List<Method> result = reflectionSearch.getMethods();

        verify(query).getCriterias();
        verify(query).get();
        verify(query, never()).set(anyListOf(Method.class));

        assertEquals(methodList, result);
    }

    @Test
    public void testIntersect_shouldRemoveMethodsNotInToaddListFromMasterList() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        List<Method> master = new ArrayList<>();
        master.add(method1);
        master.add(method2);

        List<Method> toadd = new ArrayList<>();
        toadd.add(method1);

        reflectionSearch.intersect(master, toadd);

        verify(method2, times(2)).equals(any(Method.class));
        verify(master).remove(1);
    }

    @Test
    public void testUnion_shouldAddMethodsNotInMasterListToMasterList() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        List<Method> master = new ArrayList<>();
        master.add(method1);

        List<Method> toadd = new ArrayList<>();
        toadd.add(method2);

        reflectionSearch.union(master, toadd);

        verify(master).contains(method2);
        verify(master).add(method2);
    }
}