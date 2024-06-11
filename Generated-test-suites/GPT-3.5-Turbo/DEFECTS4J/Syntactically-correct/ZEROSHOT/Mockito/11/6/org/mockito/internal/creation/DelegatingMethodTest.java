import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
  
  private Method mockedMethod;
  private DelegatingMethod delegatingMethod;
  
  @Before
  public void setUp() throws Exception {
    mockedMethod = MyClass.class.getMethod("myMethod", String.class);
    delegatingMethod = new DelegatingMethod(mockedMethod);
  }
  
  @Test
  public void testGetExceptionTypes() {
    Class<?>[] expected = mockedMethod.getExceptionTypes();
    Class<?>[] actual = delegatingMethod.getExceptionTypes();
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetJavaMethod() {
    Method expected = mockedMethod;
    Method actual = delegatingMethod.getJavaMethod();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetName() {
    String expected = mockedMethod.getName();
    String actual = delegatingMethod.getName();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetParameterTypes() {
    Class<?>[] expected = mockedMethod.getParameterTypes();
    Class<?>[] actual = delegatingMethod.getParameterTypes();
    assertArrayEquals(expected, actual);
  }
  
  @Test
  public void testGetReturnType() {
    Class<?> expected = mockedMethod.getReturnType();
    Class<?> actual = delegatingMethod.getReturnType();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsVarArgs() {
    boolean expected = mockedMethod.isVarArgs();
    boolean actual = delegatingMethod.isVarArgs();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testIsAbstract() {
    int modifiers = mockedMethod.getModifiers();
    boolean expected = (modifiers & Modifier.ABSTRACT) != 0;
    boolean actual = delegatingMethod.isAbstract();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testEquals() {
    Object o = mockedMethod;
    boolean expected = mockedMethod.equals(o);
    boolean actual = delegatingMethod.equals(o);
    assertEquals(expected, actual);
  }
  
  @Test
  public void testHashCode() {
    int expected = 1;
    int actual = delegatingMethod.hashCode();
    assertEquals(expected, actual);
  }
  
  private static class MyClass {
    public void myMethod(String arg) {
      // Do nothing
    }
  }
}