import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;

public class ReflectionSearchTest {

  @Test
  public void testHasAnnotation() {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    assertFalse(search.hasAnnotation("NotAnno"));
    assertFalse(search.hasAnnotation("*NotAnno*"));
    assertTrue(search.hasAnnotation("TestAnno"));
    assertTrue(search.hasAnnotation("*TestAnno*"));
  }

  @Test
  public void testMethodsThatHave() throws BeanBinException {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    search.methodsThatHave("method1");
    List<Method> methods = search.getMethods();
    assertEquals(1, methods.size());
    assertEquals("method1", methods.get(0).getName());
    assertEquals(0, methods.get(0).getParameterCount());

    search.methodsThatHave("method2");
    methods = search.getMethods();
    assertEquals(2, methods.size());
    assertEquals("method1", methods.get(0).getName());
    assertEquals(0, methods.get(0).getParameterCount());
    assertEquals("method2", methods.get(1).getName());
    assertEquals(1, methods.get(1).getParameterCount());
  }

  @Test
  public void testMethodsThatDontHave() throws BeanBinException {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    search.methodsThatDontHave("method1");
    List<Method> methods = search.getMethods();
    assertEquals(1, methods.size());
    assertEquals("method2", methods.get(0).getName());
    assertEquals(1, methods.get(0).getParameterCount());

    search.methodsThatDontHave("method2");
    methods = search.getMethods();
    assertEquals(0, methods.size());
  }

  @Test
  public void testAnd() throws BeanBinException {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    search.methodsThatHave("method1").and().methodsThatHave("method2");
    List<Method> methods = search.getMethods();
    assertEquals(1, methods.size());
    assertEquals("method2", methods.get(0).getName());
    assertEquals(1, methods.get(0).getParameterCount());
  }

  @Test
  public void testOr() throws BeanBinException {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    search.methodsThatHave("method1").or().methodsThatHave("method2");
    List<Method> methods = search.getMethods();
    assertEquals(2, methods.size());
    assertEquals("method1", methods.get(0).getName());
    assertEquals(0, methods.get(0).getParameterCount());
    assertEquals("method2", methods.get(1).getName());
    assertEquals(1, methods.get(1).getParameterCount());
  }

  @Test
  public void testGetMethods() throws BeanBinException {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    List<Method> methods = search.getMethods();
    assertEquals(2, methods.size());
    assertEquals("method1", methods.get(0).getName());
    assertEquals(0, methods.get(0).getParameterCount());
    assertEquals("method2", methods.get(1).getName());
    assertEquals(1, methods.get(1).getParameterCount());
  }

  @Test
  public void testIntersect() {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    List<Method> master = TestUtils.getMethods("method1", "method2");
    List<Method> toadd = TestUtils.getMethods("method2");
    search.intersect(master, toadd);
    assertEquals(1, master.size());
    assertEquals("method2", master.get(0).getName());
  }

  @Test
  public void testUnion() {
    ReflectionSearch search = new ReflectionSearch(TestClass.class);
    List<Method> master = TestUtils.getMethods("method1");
    List<Method> toadd = TestUtils.getMethods("method2");
    search.union(master, toadd);
    assertEquals(2, master.size());
    assertEquals("method1", master.get(0).getName());
    assertEquals("method2", master.get(1).getName());
  }

}

class TestClass {
  @TestAnno
  public void method1() {}

  @TestAnno
  public void method2(String param) {}
}

class TestUtils {
  public static List<Method> getMethods(String... methodNames) {
    List<Method> methods = new ArrayList<>();
    for (String methodName : methodNames) {
      try {
        methods.add(TestClass.class.getMethod(methodName));
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }
    }
    return methods;
  }
}