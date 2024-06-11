import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("toString");
        String expected = "Mock for ReturnsEmptyValues, hashCode: " + answer.hashCode();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("compareTo");
        int expected = 1;
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_primitiveReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("primitiveReturn");
        int expected = 0;
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_wrapperClassReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("wrapperClassReturn");
        Integer expected = 0;
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_collectionReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("collectionReturn");
        Collection<Object> expected = new LinkedList<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_setReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("setReturn");
        Set<Object> expected = new HashSet<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_sortedSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("sortedSetReturn");
        SortedSet<Object> expected = new TreeSet<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_linkedHashSetReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("linkedHashSetReturn");
        LinkedHashSet<Object> expected = new LinkedHashSet<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_listReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("listReturn");
        List<Object> expected = new LinkedList<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_linkedListReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("linkedListReturn");
        LinkedList<Object> expected = new LinkedList<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_arrayListReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("arrayListReturn");
        ArrayList<Object> expected = new ArrayList<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_mapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("mapReturn");
        Map<Object, Object> expected = new HashMap<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_hashMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("hashMapReturn");
        HashMap<Object, Object> expected = new HashMap<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_sortedMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("sortedMapReturn");
        SortedMap<Object, Object> expected = new TreeMap<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_treeMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("treeMapReturn");
        TreeMap<Object, Object> expected = new TreeMap<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_linkedHashMapReturn() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = prepareInvocation("linkedHashMapReturn");
        LinkedHashMap<Object, Object> expected = new LinkedHashMap<>();
        Object result = answer.answer(invocation);
        Assert.assertEquals(expected, result);
    }

    // Helper method to prepare the InvocationOnMock object
    private InvocationOnMock prepareInvocation(String methodName) {
        return new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                try {
                    return ReturnsEmptyValues.class.getMethod(methodName);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }
        };
    }
}