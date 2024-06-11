import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.stubbing.defaultanswers.ObjectMethodsGuru;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
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

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testAnswer_toString() {
        // Given
        String expected = "Mock for Class, hashCode: 123";

        InvocationOnMock invocation = mockUtil.getInvocation(mockUtil.createMock(Clas.class), methodsGuru.getToStringMethod());

        // When
        Object result = returnsEmptyValues.answer(invocation);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testAnswer_compareTo() {
        // Given
        int expected = 1;

        InvocationOnMock invocation = mockUtil.getInvocation(mockUtil.createMock(Comparable.class), methodsGuru.getCompareToMethod());

        // When
        Object result = returnsEmptyValues.answer(invocation);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_primitive() {
        // Given
        int expected = 0;

        // When
        Object result = returnsEmptyValues.returnValueFor(int.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_wrapperClass() {
        // Given
        Integer expected = 0;

        // When
        Object result = returnsEmptyValues.returnValueFor(Integer.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_Collection() {
        // Given
        Collection<Object> expected = new LinkedList<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(Collection.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_Set() {
        // Given
        Set<Object> expected = new HashSet<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(Set.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_HashSet() {
        // Given
        Set<Object> expected = new HashSet<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(HashSet.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        // Given
        SortedSet<Object> expected = new TreeSet<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(SortedSet.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        // Given
        SortedSet<Object> expected = new TreeSet<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(TreeSet.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        // Given
        Set<Object> expected = new LinkedHashSet<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_List() {
        // Given
        List<Object> expected = new LinkedList<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(List.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        // Given
        List<Object> expected = new LinkedList<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(LinkedList.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        // Given
        List<Object> expected = new ArrayList<Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(ArrayList.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_Map() {
        // Given
        Map<Object, Object> expected = new HashMap<Object, Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(Map.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_HashMap() {
        // Given
        Map<Object, Object> expected = new HashMap<Object, Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(HashMap.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        // Given
        SortedMap<Object, Object> expected = new TreeMap<Object, Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        // Given
        SortedMap<Object, Object> expected = new TreeMap<Object, Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(TreeMap.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        // Given
        Map<Object, Object> expected = new LinkedHashMap<Object, Object>();

        // When
        Object result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        // Then
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnValueFor_otherClass() {
        // Given
        Object expected = null;

        // When
        Object result = returnsEmptyValues.returnValueFor(Object.class);

        // Then
        Assert.assertEquals(expected, result);
    }

}