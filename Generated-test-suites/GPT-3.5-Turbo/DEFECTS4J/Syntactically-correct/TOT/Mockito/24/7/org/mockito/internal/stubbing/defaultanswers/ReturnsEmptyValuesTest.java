import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
  
  private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
  
  @Test
  public void testAnswer_toStringMethod_DefaultMock_ReturnsDescriptionOfMock(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("toString");
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
  }
  
  @Test
  public void testAnswer_toStringMethod_CustomMock_ReturnsCustomDescriptionOfMock(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("toString");
    
    // MockName and MockSettings can be mocked, but they are not accessible.
    // To test with a custom mock name we can create a custom class extending MockUtil where we override getMockName()
    // method to return a custom name string.
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertEquals("custom-mock-name", result);
  }
  
  @Test
  public void testAnswer_compareToMethod_ReturnsNonZero(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("compareTo");
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertNotEquals(0, result);
  }
  
  @Test
  public void testAnswer_primitiveReturnTypeMethod_ReturnsAppropriatePrimitive(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("primitiveReturnTypeMethod");
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertEquals(0, result);
  }
  
  @Test
  public void testAnswer_wrapperClassReturnTypeMethod_ReturnsConsistentValue(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("wrapperClassReturnTypeMethod");
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertEquals(0, result);
  }
  
  @Test
  public void testAnswer_collectionReturnTypeMethod_ReturnsEmptyCollection(){
    // Arrange
    InvocationOnMock invocation = prepareInvocation("collectionReturnTypeMethod");
    
    // Act
    Object result = returnsEmptyValues.answer(invocation);
    
    // Assert
    assertTrue(result instanceof Collection);
    assertTrue(((Collection<?>) result).isEmpty());
  }
  
  private InvocationOnMock prepareInvocation(String methodName){
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Method method = Foo.class.getMethod(methodName);
    when(invocation.getMethod()).thenReturn(method);
    when(invocation.getMock()).thenReturn(new Foo());
    return invocation;
  }
  
  private static class Foo {
    public int primitiveReturnTypeMethod(){
      return 0;
    }
    
    public Integer wrapperClassReturnTypeMethod(){
      return 0;
    }
    
    public Collection<?> collectionReturnTypeMethod(){
      return null;
    }
    
    // Other methods
  }
}