package net.sourceforge.beanbin.reflect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import net.sourceforge.beanbin.BeanBinException;
import net.sourceforge.beanbin.search.MethodReflectionCriteria;
import net.sourceforge.beanbin.search.ReflectionCriteria;
import net.sourceforge.beanbin.query.SearchType;
import net.sourceforge.beanbin.reflect.ReflectionSearch;
import net.sourceforge.beanbin.search.ReflectionShelf;

public class ReflectionSearchTest {
	
	@Test
	public void testHasAnnotation() {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		assertTrue(reflectionSearch.hasAnnotation("java.lang.Deprecated"));
		assertFalse(reflectionSearch.hasAnnotation("java.lang.Override"));
	}

	@Test
	public void testMethodsThatHave() throws BeanBinException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		reflectionSearch.methodsThatHave("length");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(1, methods.size());
		Method method = methods.get(0);
		assertEquals("length", method.getName());
	}

	@Test
	public void testMethodsThatDontHave() throws BeanBinException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		reflectionSearch.methodsThatDontHave("length");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(115, methods.size());
	}

	@Test
	public void testAnd() throws BeanBinException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		reflectionSearch.methodsThatHave("length").and().methodsThatHave("charAt");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(2, methods.size());
	}

	@Test
	public void testOr() throws BeanBinException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		reflectionSearch.methodsThatHave("length").or().methodsThatHave("charAt");
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(116, methods.size());
	}

	@Test
	public void testGetMethods() throws BeanBinException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		List<Method> methods = reflectionSearch.getMethods();
		assertEquals(116, methods.size());
	}

	@Test
	public void testIntersect() throws BeanBinException, NoSuchMethodException, SecurityException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		MethodReflectionCriteria criteria1 = new MethodReflectionCriteria(clazz, "length", SearchType.EQUALS);
		MethodReflectionCriteria criteria2 = new MethodReflectionCriteria(clazz, "charAt", SearchType.EQUALS);
		ReflectionCriteria reflectionCriteria = new ReflectionCriteria();
		reflectionCriteria.setPreviousCondition(Conditional.AND);
		reflectionCriteria.add(criteria1);
		reflectionCriteria.add(criteria2);
		List<Method> master = reflectionCriteria.getResults();
		List<Method> toadd = new ArrayList<>();
		Method lengthMethod = clazz.getMethod("length");
		Method charAtMethod = clazz.getMethod("charAt", int.class);
		toadd.add(lengthMethod);
		toadd.add(charAtMethod);
		reflectionSearch.intersect(master, toadd);
		assertEquals(2, master.size());
	}

	@Test
	public void testUnion() throws BeanBinException, NoSuchMethodException, SecurityException {
		Class clazz = String.class;
		ReflectionSearch reflectionSearch = new ReflectionSearch(clazz);
		MethodReflectionCriteria criteria1 = new MethodReflectionCriteria(clazz, "length", SearchType.EQUALS);
		MethodReflectionCriteria criteria2 = new MethodReflectionCriteria(clazz, "charAt", SearchType.EQUALS);
		ReflectionCriteria reflectionCriteria = new ReflectionCriteria();
		reflectionCriteria.setPreviousCondition(Conditional.OR);
		reflectionCriteria.add(criteria1);
		reflectionCriteria.add(criteria2);
		List<Method> master = reflectionCriteria.getResults();
		List<Method> toadd = new ArrayList<>();
		Method charAtMethod = clazz.getMethod("charAt", int.class);
		toadd.add(charAtMethod);
		reflectionSearch.union(master, toadd);
		assertEquals(2, master.size());
	}

}