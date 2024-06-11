
import org.junit.Test;
import java.lang.reflect.Method;
import java.util.List;
import static org.junit.Assert.*;

public class ReflectionSearchTest {
  
  @Test
  public void testHasAnnotation() {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    assertTrue(reflection.hasAnnotation("net.sourceforge.beanbin.reflect"));
    assertFalse(reflection.hasAnnotation("net.sourceforge.beanbin.notexists"));
  }
  
  @Test
  public void testMethodsThatHave() throws BeanBinException {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    List<Method> methods = reflection.methodsThatHave("term").getMethods();
    assertEquals(2, methods.size());
  }
  
  @Test
  public void testMethodsThatDontHave() throws BeanBinException {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    List<Method> methods = reflection.methodsThatDontHave("term").getMethods();
    assertEquals(1, methods.size());
  }
  
  @Test
  public void testAnd() {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    assertSame(reflection, reflection.and());
  }
  
  @Test
  public void testOr() {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    assertSame(reflection, reflection.or());
  }
  
  @Test
  public void testGetMethods() throws BeanBinException {
    ReflectionSearch reflection = new ReflectionSearch(ReflectionSearch.class);
    List<Method> methods = reflection.getMethods();
    assertNotNull(methods);
  }
  
}
