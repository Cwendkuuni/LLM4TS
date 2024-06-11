import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito; 
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

   @Test
   public void testAnswer() {
      ReturnsEmptyValues rev = new ReturnsEmptyValues();
      
      // Testing toString() method
      Object mock1 = Mockito.mock(Object.class);
      assertEquals("Mock for Object, hashCode: " + mock1.hashCode(), rev.answer(new InvocationOnMock(mock1, Object.class.getMethod("toString"), null)));
      
      // Testing compareTo() method
      Object mock2 = Mockito.mock(Object.class);
      assertEquals(1, rev.answer(new InvocationOnMock(mock2, Comparable.class.getMethod("compareTo", Object.class), null)));
      
      // Testing primitive-returning methods
      Object mock3 = Mockito.mock(Object.class);
      assertEquals(false, rev.answer(new InvocationOnMock(mock3, Object.class.getMethod("equals", Object.class), null)));
      
      // Testing primitive wrapper class-returning methods
      Object mock4 = Mockito.mock(Object.class);
      assertEquals(null, rev.answer(new InvocationOnMock(mock4, Object.class.getMethod("hashCode"), null)));
      
      // Testing collection-returning methods
      Object mock5 = Mockito.mock(Object.class);
      assertEquals(new LinkedList<Object>(), rev.answer(new InvocationOnMock(mock5, Object.class.getMethod("getClass"), null)));
      
      // Testing other methods
      Object mock6 = Mockito.mock(Object.class);
      assertEquals(null, rev.answer(new InvocationOnMock(mock6, Object.class.getMethod("wait"), null)));
   }
 
}