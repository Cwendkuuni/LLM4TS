import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer_toString() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("toString"));
    when(invocation.getMock()).thenReturn(new Object());
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
  }

  @Test
  public void testAnswer_compareTo_sameReferences() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethod("compareTo", Object.class));
    when(invocation.getMock()).thenReturn(new ComparableObject());
    when(invocation.getArguments()).thenReturn(new Object[]{invocation.getMock()});
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, result);
  }

  @Test
  public void testAnswer_compareTo_differentReferences() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethod("compareTo", Object.class));
    when(invocation.getMock()).thenReturn(new ComparableObject());
    when(invocation.getArguments()).thenReturn(new Object[]{new Object()});
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertNotEquals(0, result);
  }

  @Test
  public void testAnswer_primitiveReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getInt"));
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, result);
  }
  
  @Test
  public void testAnswer_primitiveWrapperReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getInteger"));
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, result);
  }
  
  @Test
  public void testAnswer_collectionReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getList"));
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof List);
    assertTrue(((List) result).isEmpty());
  }
  
  @Test
  public void testAnswer_setReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getSet"));
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Set);
    assertTrue(((Set) result).isEmpty());
  }
  
  @Test
  public void testAnswer_mapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getMap"));
    
    Object result = answer.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof Map);
    assertTrue(((Map) result).isEmpty());
  }
  
  @Test
  public void testAnswer_nullReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(answer.answer(invocation)).thenCallRealMethod();
    
    when(invocation.getMethod()).thenReturn(SampleClass.class.getDeclaredMethod("getObject"));
    
    Object result = answer.answer(invocation);
    
    assertNull(result);
  }

  class ComparableObject implements Comparable<Object> {
    public int compareTo(Object o) {
      return 0;
    }
  }

  class SampleClass {
    public int getInt() {
      return 0;
    }
    public Integer getInteger() {
      return 0;
    }
    public List getList() {
      return null;
    }
    public Set getSet() {
      return null;
    }
    public Map getMap() {
      return null;
    }
    public Object getObject() {
      return null;
    }
  }

}