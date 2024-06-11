
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.*;


public class ReflectionSearchTest {
  
  @Test
  public void testHasAnnotation() {
    Class clazz = ReflectionSearch.class;
    ReflectionSearch rs = new ReflectionSearch(clazz);
    
    // Test with annotation that exists in class
    assertTrue(rs.hasAnnotation("Deprecated"));
    
    // Test with annotation that does not exist in class
    assertFalse(rs.hasAnnotation("TestAnnotation"));
    
    // Test with invalid annotation name
    assertFalse(rs.hasAnnotation("InvalidAnnotation"));
     
    // Test with null annotation name
    assertFalse(rs.hasAnnotation(null));
     
  }
  
  @Test
  public void testMethodsThatHave() throws BeanBinException {
    Class clazz = ReflectionSearch.class;
    ReflectionSearch rs = new ReflectionSearch(clazz);
    
    rs.methodsThatHave("get");
    
    List<Method> methods = rs.getMethods();
    
    // Test if the methods list contains a method with the name "get"
    assertFalse(methods.isEmpty());
    assertTrue(methods.stream().anyMatch(m -> m.getName().equals("get")));
    
    // Test with invalid method name
    rs.methodsThatHave("invalidMethod");
    methods = rs.getMethods();
    assertTrue(methods.isEmpty());
    
    // Test with null method name
    rs.methodsThatHave(null);
    methods = rs.getMethods();
    assertTrue(methods.isEmpty());
    
  }
  
  @Test
  public void testMethodsThatDontHave() throws BeanBinException {
    Class clazz = ReflectionSearch.class;
    ReflectionSearch rs = new ReflectionSearch(clazz);
    
    rs.methodsThatDontHave("get");
    
    List<Method> methods = rs.getMethods();
    
    // Test if the methods list does not contain a method with the name "get"
    assertFalse(methods.isEmpty());
    assertTrue(methods.stream().noneMatch(m -> m.getName().equals("get")));
    
    // Test with invalid method name
    rs.methodsThatDontHave("invalidMethod");
    methods = rs.getMethods();
    assertFalse(methods.isEmpty());
    
    // Test with null method name
    rs.methodsThatDontHave(null);
    methods = rs.getMethods();
    assertFalse(methods.isEmpty());
    
  }
  
  @Test
  public void testAnd() {
    Class clazz = ReflectionSearch.class;
    ReflectionSearch rs = new ReflectionSearch(clazz);
    
    ReflectionSearch rsAnd = rs.and();
    
    // Test if the returned object is of type ReflectionSearch
    assertTrue(rsAnd instanceof ReflectionSearch);
    
  }
  
  @Test
  public void testOr() {
    Class clazz = ReflectionSearch.class;
    ReflectionSearch rs = new ReflectionSearch(clazz);
    
    ReflectionSearch rsOr = rs.or();
    
    // Test if the returned object is of type ReflectionSearch
    assertTrue(rsOr instanceof ReflectionSearch);
    
  }
}
