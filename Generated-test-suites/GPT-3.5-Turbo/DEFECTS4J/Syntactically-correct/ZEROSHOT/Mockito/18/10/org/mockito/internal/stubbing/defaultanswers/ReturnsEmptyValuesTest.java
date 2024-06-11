import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import java.util.*;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer() {
    ReturnsEmptyValues re = new ReturnsEmptyValues();
    // Test for toString() method
    assertEquals("Mock for Object, hashCode: 0", re.answer(null));
    
    // Test for compareTo() method
    Object mockObject = new Object();
    InvocationOnMock invocationMock = new InvocationOnMock(mockObject, new Object[]{mockObject});
    assertEquals(0, re.answer(invocationMock));
    
    // Test for primitive and wrapper classes
    assertEquals(false, re.answer(new InvocationOnMock(null, null)));
    assertEquals(0, re.answer(new InvocationOnMock(null, null)));
    assertEquals(0L, re.answer(new InvocationOnMock(null, null)));
    assertEquals((short)0, re.answer(new InvocationOnMock(null, null)));
    assertEquals((byte)0, re.answer(new InvocationOnMock(null, null)));
    assertEquals(' ', re.answer(new InvocationOnMock(null, null)));
    assertEquals(0.0f, re.answer(new InvocationOnMock(null, null)));
    assertEquals(0.0, re.answer(new InvocationOnMock(null, null)));
    
    // Test for Collection classes
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof LinkedList<?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof HashSet<?>);
    
    // Test for Set classes
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof HashSet<?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof TreeSet<?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof LinkedHashSet<?>);
    
    // Test for List classes
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof LinkedList<?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof ArrayList<?>);
    
    // Test for Map classes
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof HashMap<?,?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof TreeMap<?,?>);
    assertTrue(re.answer(new InvocationOnMock(null, null)) instanceof LinkedHashMap<?,?>);
  }

}