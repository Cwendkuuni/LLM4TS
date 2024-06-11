import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ConstructorInstantiatorTest {

  @Test
  public void testNoArgConstructor() {
    ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
    MyClass obj = constructorInstantiator.newInstance(MyClass.class);
    assertNotNull(obj);
    assertTrue(obj instanceof MyClass);
  }

  @Test
  public void testWithOuterClass() {
    OuterClass outerClass = mock(OuterClass.class);
    ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
    MyClass obj = constructorInstantiator.newInstance(MyClass.class);
    assertNotNull(obj);
    assertTrue(obj instanceof MyClass);
    verify(outerClass, times(1)).getClass();
  }

  @Test
  public void testWithOuterClassNoConstructor() {
    OuterClass outerClass = mock(OuterClass.class);
    ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
    MyClassNoConstructor obj = constructorInstantiator.newInstance(MyClassNoConstructor.class);
    assertNotNull(obj);
    assertTrue(obj instanceof MyClassNoConstructor);
    verify(outerClass, times(1)).getClass();
  }

  @Test(expected = InstantationException.class)
  public void testWithOuterClassError() {
    OuterClass outerClass = new OuterClass();
    ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(outerClass);
    constructorInstantiator.newInstance(MyClass.class);
  }

  @Test(expected = InstantationException.class)
  public void testNoArgConstructorError() {
    ConstructorInstantiator constructorInstantiator = new ConstructorInstantiator(null);
    constructorInstantiator.newInstance(MyClassNoConstructor.class);
  }
}