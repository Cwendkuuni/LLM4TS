import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

  @Test
  public void testAnswer_forToStringMethod() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("toString");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof String);
    assertTrue(result.toString().contains("Mock for"));
  }

  @Test
  public void testAnswer_forCompareToMethod_withDifferentReferences() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("compareTo");
    Object[] arguments = { new Object() };
    setInvocationArguments(invocation, arguments);

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertTrue((Integer) result != 0);
  }

  @Test
  public void testAnswer_forCompareToMethod_withSameReferences() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("compareTo");
    Object[] arguments = { invocation.getMock() };
    setInvocationArguments(invocation, arguments);

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (Integer) result);
  }

  @Test
  public void testAnswer_forPrimitiveReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("primitiveMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (Integer) result);
  }

  @Test
  public void testAnswer_forWrapperClassReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("wrapperClassMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Integer);
    assertEquals(0, (Integer) result);
  }

  @Test
  public void testAnswer_forCollectionReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("collectionMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Collection);
    assertTrue(((Collection) result).isEmpty());
  }

  @Test
  public void testAnswer_forSetReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("setMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Set);
    assertTrue(((Set) result).isEmpty());
  }

  @Test
  public void testAnswer_forHashSetReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("hashSetMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof HashSet);
    assertTrue(((HashSet) result).isEmpty());
  }

  @Test
  public void testAnswer_forSortedSetReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("sortedSetMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof SortedSet);
    assertTrue(((SortedSet) result).isEmpty());
  }

  @Test
  public void testAnswer_forTreeSetReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("treeSetMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof TreeSet);
    assertTrue(((TreeSet) result).isEmpty());
  }

  @Test
  public void testAnswer_forLinkedHashSetReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("linkedHashSetMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof LinkedHashSet);
    assertTrue(((LinkedHashSet) result).isEmpty());
  }

  @Test
  public void testAnswer_forListReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("listMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof List);
    assertTrue(((List) result).isEmpty());
  }

  @Test
  public void testAnswer_forLinkedListReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("linkedListMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof LinkedList);
    assertTrue(((LinkedList) result).isEmpty());
  }

  @Test
  public void testAnswer_forArrayListReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("arrayListMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof ArrayList);
    assertTrue(((ArrayList) result).isEmpty());
  }

  @Test
  public void testAnswer_forMapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("mapMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof Map);
    assertTrue(((Map) result).isEmpty());
  }

  @Test
  public void testAnswer_forHashMapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("hashMapMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof HashMap);
    assertTrue(((HashMap) result).isEmpty());
  }

  @Test
  public void testAnswer_forSortedMapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("sortedMapMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof SortedMap);
    assertTrue(((SortedMap) result).isEmpty());
  }

  @Test
  public void testAnswer_forTreeMapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("treeMapMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof TreeMap);
    assertTrue(((TreeMap) result).isEmpty());
  }

  @Test
  public void testAnswer_forLinkedHashMapReturnType() {
    ReturnsEmptyValues answer = new ReturnsEmptyValues();
    InvocationOnMock invocation = createMockInvocation("linkedHashMapMethod");

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof LinkedHashMap);
    assertTrue(((LinkedHashMap) result).isEmpty());
  }

  private InvocationOnMock createMockInvocation(String methodName) {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getName()).thenReturn(methodName);
    return invocation;
  }

  private void setInvocationArguments(InvocationOnMock invocation, Object[] arguments) {
    when(invocation.getArguments()).thenReturn(arguments);
  }
}