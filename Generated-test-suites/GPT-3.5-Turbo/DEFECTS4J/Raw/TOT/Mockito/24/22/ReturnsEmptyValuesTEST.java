import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_toStringMethodWithDefaultName_shouldReturnMockDescription() {
        // Setup
        InvocationOnMock invocation = createInvocation("toString");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        String expected = "Mock for Object, hashCode: " + invocation.getMock().hashCode();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_toStringMethodWithName_shouldReturnMockName() {
        // Setup
        InvocationOnMock invocation = createInvocation("toString");
        MockUtil mockUtil = new MockUtil();
        mockUtil.putMockName(invocation.getMock(), "MyMock");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertEquals("MyMock", result);
    }

    @Test
    public void testAnswer_compareToMethod_shouldReturnNonZeroValue() {
        // Setup
        InvocationOnMock invocation = createInvocation("compareTo");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertEquals(1, result);
    }

    @Test
    public void testAnswer_integerReturnType_shouldReturnDefaultValueZero() {
        // Setup
        InvocationOnMock invocation = createInvocation("getInteger");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAnswer_booleanReturnType_shouldReturnDefaultValueFalse() {
        // Setup
        InvocationOnMock invocation = createInvocation("getBoolean");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testAnswer_collectionReturnType_shouldReturnEmptyLinkedList() {
        // Setup
        InvocationOnMock invocation = createInvocation("getCollection");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType_shouldReturnEmptyHashSet() {
        // Setup
        InvocationOnMock invocation = createInvocation("getSet");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof HashSet);
        Assert.assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_hashSetReturnType_shouldReturnEmptyHashSet() {
        // Setup
        InvocationOnMock invocation = createInvocation("getHashSet");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof HashSet);
        Assert.assertTrue(((HashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType_shouldReturnEmptyTreeSet() {
        // Setup
        InvocationOnMock invocation = createInvocation("getSortedSet");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof TreeSet);
        Assert.assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_treeSetReturnType_shouldReturnEmptyTreeSet() {
        // Setup
        InvocationOnMock invocation = createInvocation("getTreeSet");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof TreeSet);
        Assert.assertTrue(((TreeSet) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType_shouldReturnEmptyLinkedHashSet() {
        // Setup
        InvocationOnMock invocation = createInvocation("getLinkedHashSet");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof LinkedHashSet);
        Assert.assertTrue(((LinkedHashSet) result).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType_shouldReturnEmptyLinkedList() {
        // Setup
        InvocationOnMock invocation = createInvocation("getList");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturnType_shouldReturnEmptyLinkedList() {
        // Setup
        InvocationOnMock invocation = createInvocation("getLinkedList");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType_shouldReturnEmptyArrayList() {
        // Setup
        InvocationOnMock invocation = createInvocation("getArrayList");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof ArrayList);
        Assert.assertTrue(((ArrayList) result).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType_shouldReturnEmptyHashMap() {
        // Setup
        InvocationOnMock invocation = createInvocation("getMap");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof HashMap);
        Assert.assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturnType_shouldReturnEmptyHashMap() {
        // Setup
        InvocationOnMock invocation = createInvocation("getHashMap");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof HashMap);
        Assert.assertTrue(((HashMap) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType_shouldReturnEmptyTreeMap() {
        // Setup
        InvocationOnMock invocation = createInvocation("getSortedMap");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof TreeMap);
        Assert.assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturnType_shouldReturnEmptyTreeMap() {
        // Setup
        InvocationOnMock invocation = createInvocation("getTreeMap");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof TreeMap);
        Assert.assertTrue(((TreeMap) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType_shouldReturnEmptyLinkedHashMap() {
        // Setup
        InvocationOnMock invocation = createInvocation("getLinkedHashMap");

        // Execute
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        Assert.assertTrue(result instanceof LinkedHashMap);
        Assert.assertTrue(((LinkedHashMap) result).isEmpty());
    }

    private InvocationOnMock createInvocation(String methodName) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(getClass().getMethod(methodName));
        Mockito.when(invocation.getMock()).thenReturn(Mockito.mock(Object.class));
        return invocation;
    }
}