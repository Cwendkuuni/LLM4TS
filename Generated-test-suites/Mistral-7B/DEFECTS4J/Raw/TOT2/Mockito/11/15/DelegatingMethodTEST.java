import org.junit.Test;import static org.mockito.Mockito.*;import static org.junit.Assert.assertEquals;

public class DelegatingMethodTest {

 @Test
 public void testGetReturnType() {
  Method method = mock(Method.class);
  DelegatingMethod delegatingMethod = new DelegatingMethod(method);
  Class<?> expectedReturnType = String.class;
  when(method.getReturnType()).thenReturn(expectedReturnType);

  Class<?> actualReturnType = delegatingMethod.getReturnType();

  assertEquals(expectedReturnType, actualReturnType);
 }

}