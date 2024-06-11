import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;

public class ReflectionSearchTest {

  @Test
  public void testHasAnnotation_WithWildcard_ReturnsTrue() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    boolean result = reflectionSearch.hasAnnotation("*Controller");
    assertTrue(result);
  }

  @Test
  public void testHasAnnotation_WithoutWildcard_ReturnsFalse() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    boolean result = reflectionSearch.hasAnnotation("Service");
    assertFalse(result);
  }

  @Test
  public void testMethodsThatHave_WithValidTerm_ReturnsCorrectMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatHave("getId").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(1, methods.size());
    assertEquals("getId", methods.get(0).getName());
  }

  @Test
  public void testMethodsThatHave_WithInvalidTerm_ReturnsEmptyList() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatHave("getInvalid").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(0, methods.size());
  }

  @Test
  public void testMethodsThatDontHave_WithValidTerm_ReturnsCorrectMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatDontHave("setId").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(1, methods.size());
    assertEquals("getName", methods.get(0).getName());
  }

  @Test
  public void testMethodsThatDontHave_WithInvalidTerm_ReturnsAllMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatDontHave("getInvalid").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(3, methods.size());
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("getId")));
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("getName")));
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("setName")));
  }

  @Test
  public void testAnd_WithPreviousCriteria_ReturnsCorrectMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatHave("getId").and().methodsThatHave("getName").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(1, methods.size());
    assertEquals("getId", methods.get(0).getName());
  }

  @Test
  public void testOr_WithPreviousCriteria_ReturnsCorrectMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatHave("getId").or().methodsThatHave("getInvalid").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(1, methods.size());
    assertEquals("getId", methods.get(0).getName());
  }

  @Test
  public void testGetMethods_WithNoCriteria_ReturnsAllMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(3, methods.size());
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("getId")));
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("getName")));
    assertTrue(methods.stream().anyMatch(method -> method.getName().equals("setName")));
  }

  @Test
  public void testGetMethods_WithPreviousCriteria_ReturnsCorrectMethods() {
    ReflectionSearch reflectionSearch = new ReflectionSearch(ExampleClass.class);
    List<Method> methods = null;
    try {
      methods = reflectionSearch.methodsThatHave("getId").and().methodsThatHave("getName").getMethods();
    } catch (BeanBinException e) {
      fail("Exception not expected");
    }
    assertEquals(1, methods.size());
    assertEquals("getId", methods.get(0).getName());
  }

  private static class ExampleClass {
    private int id;
    private String name;

    public int getId() {
      return id;
    }

    public void setId(int id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

}