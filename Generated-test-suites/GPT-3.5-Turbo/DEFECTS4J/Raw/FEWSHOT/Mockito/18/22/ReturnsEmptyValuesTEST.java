import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer() {
    ReturnsEmptyValues values = new ReturnsEmptyValues();
    
    // Test toString()
    assertEquals("Mock for Object, hashCode: 0", values.answer(invocation("toString")));
    
    // Test compareTo()
    assertEquals(0, values.answer(invocation("compareTo", values)));
    assertEquals(1, values.answer(invocation("compareTo", null)));
    
    // Test primitive types
    assertEquals(0, values.answer(invocation("byteMethod")));
    assertEquals(0, values.answer(invocation("booleanMethod")));
    assertEquals(0, values.answer(invocation("charMethod")));
    assertEquals(0, values.answer(invocation("shortMethod")));
    assertEquals(0, values.answer(invocation("intMethod")));
    assertEquals(0, values.answer(invocation("longMethod")));
    assertEquals(0.0f, values.answer(invocation("floatMethod")));
    assertEquals(0.0, values.answer(invocation("doubleMethod")));

    // Test primitive wrapper classes
    assertEquals(0, values.answer(invocation("byteWrapperMethod")));
    assertEquals(0, values.answer(invocation("booleanWrapperMethod")));
    assertEquals(0, values.answer(invocation("charWrapperMethod")));
    assertEquals(0, values.answer(invocation("shortWrapperMethod")));
    assertEquals(0, values.answer(invocation("intWrapperMethod")));
    assertEquals(0, values.answer(invocation("longWrapperMethod")));
    assertEquals(0.0f, values.answer(invocation("floatWrapperMethod")));
    assertEquals(0.0, values.answer(invocation("doubleWrapperMethod")));
    
    // Test collections
    assertEquals(new LinkedList<Object>(), values.answer(invocation("collectionMethod")));
    assertEquals(new HashSet<Object>(), values.answer(invocation("setMethod")));
    assertEquals(new HashSet<Object>(), values.answer(invocation("hashSetMethod")));
    assertEquals(new TreeSet<Object>(), values.answer(invocation("sortedSetMethod")));
    assertEquals(new TreeSet<Object>(), values.answer(invocation("treeSetMethod")));
    assertEquals(new LinkedHashSet<Object>(), values.answer(invocation("linkedHashSetMethod")));
    assertEquals(new LinkedList<Object>(), values.answer(invocation("listMethod")));
    assertEquals(new LinkedList<Object>(), values.answer(invocation("linkedListMethod")));
    assertEquals(new ArrayList<Object>(), values.answer(invocation("arrayListMethod")));
    assertEquals(new HashMap<Object, Object>(), values.answer(invocation("mapMethod")));
    assertEquals(new HashMap<Object, Object>(), values.answer(invocation("hashMapMethod")));
    assertEquals(new TreeMap<Object, Object>(), values.answer(invocation("sortedMapMethod")));
    assertEquals(new TreeMap<Object, Object>(), values.answer(invocation("treeMapMethod")));
    assertEquals(new LinkedHashMap<Object, Object>(), values.answer(invocation("linkedHashMapMethod")));
    
    // Test unsupported types
    assertNull(values.answer(invocation("unsupportedMethod")));
  }
  
  private InvocationOnMock invocation(String methodName, Object... args) {
    try {
      return InvocationBuilder.method("org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues", methodName)
          .args(args)
          .toInvocation();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}