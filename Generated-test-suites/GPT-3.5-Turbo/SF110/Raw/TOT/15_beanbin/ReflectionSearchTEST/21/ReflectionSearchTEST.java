
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import java.util.*;

public class ReflectionSearchTest {
  
  @Test
  public void testHasAnnotation() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    assertTrue(reflectionSearch.hasAnnotation("MyAnnotation"));
  }
  
  @Test
  public void testHasAnnotation_withWildcard() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    assertTrue(reflectionSearch.hasAnnotation("com.example.*"));
  }
  
  @Test
  public void testHasAnnotation_notFound() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    assertFalse(reflectionSearch.hasAnnotation("OtherAnnotation"));
  }
  
  @Test
  public void testMethodsThatHave() throws BeanBinException {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    List<Method> methods = reflectionSearch.methodsThatHave("test").getMethods();
    assertEquals(2, methods.size());
    assertTrue(methods.contains(MyClass.class.getMethod("testMethod1")));
    assertTrue(methods.contains(MyClass.class.getMethod("testMethod2")));
  }
  
  @Test
  public void testMethodsThatDontHave() throws BeanBinException {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    List<Method> methods = reflectionSearch.methodsThatDontHave("test").getMethods();
    assertEquals(1, methods.size());
    assertTrue(methods.contains(MyClass.class.getMethod("otherMethod")));
  }
  
  @Test
  public void testAnd() throws BeanBinException {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    List<Method> methods = reflectionSearch.methodsThatHave("test").and().methodsThatHave("other").getMethods();
    assertEquals(1, methods.size());
    assertTrue(methods.contains(MyClass.class.getMethod("testMethod2")));
  }
  
  @Test
  public void testOr() throws BeanBinException {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    List<Method> methods = reflectionSearch.methodsThatHave("test").or().methodsThatHave("other").getMethods();
    assertEquals(2, methods.size());
    assertTrue(methods.contains(MyClass.class.getMethod("testMethod1")));
    assertTrue(methods.contains(MyClass.class.getMethod("testMethod2")));
  }
  
  @Test(expected = BeanBinException.class)
  public void testGetMethods_noCriteria() throws BeanBinException {
    ReflectionSearch reflectionSearch = new ReflectionSearch(MyClass.class);
    reflectionSearch.getMethods();
  }
  
  private static class MyClass {
    @MyAnnotation
    public void testMethod1() {}
    @MyAnnotation
    public void testMethod2() {}
    public void otherMethod() {}
  }
  
  private static @interface MyAnnotation {}
}
