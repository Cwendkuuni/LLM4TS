import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Mockito<String> mock = Mockito.mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        String result = (String) answer.answer(Mockito.argThat(invocation -> invocation.getMethod().getName().equals("toString")));

        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mockObject = Mockito.mock(Comparable.class);
        Object anotherObject = Mockito.mock(Comparable.class);
        Mockito.when(mockObject.compareTo(anotherObject)).then(new ReturnsEmptyValues());

        int result = (Integer) mockObject.compareTo(anotherObject);

        Assert.assertNotSame(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerCollectionMethodModification() {
        Mockito<Collection<String>> mockCollection = Mockito.mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();
        List<String> list = new ArrayList<>();
        Mockito.when(mockCollection.answer(Mockito.any())).thenReturn(list);
        mockCollection.answer(Mockito.any()).add("Test");
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        Answer answer = new ReturnsEmptyValues();

        byte resultByte = (byte) answer.returnValueFor(Byte.TYPE);
        short resultShort = (short) answer.returnValueFor(Short.TYPE);
        int resultInt = (int) answer.returnValueFor(Integer.TYPE);
        long resultLong = (long) answer.returnValueFor(Long.TYPE);
        float resultFloat = (float) answer.returnValueFor(Float.TYPE);
        double resultDouble = (double) answer.returnValueFor(Double.TYPE);
        char resultChar = (char) answer.returnValueFor(Character.TYPE);
        boolean resultBoolean = (boolean) answer.returnValueFor(Boolean.TYPE);

        Assert.assertEquals(0, resultByte);
        Assert.assertEquals(0, resultShort);
        Assert.assertEquals(0, resultInt);
        Assert.assertEquals(0L, resultLong);
        Assert.assertEquals(0.0f, resultFloat, 0.001f);
        Assert.assertEquals(0.0d, resultDouble, 0.001d);
        Assert.assertEquals((char) 0, resultChar);
        Assert.assertEquals(false, resultBoolean);
    }

    @Test
    public void testReturnValueForCollectionType() {
        Answer answer = new ReturnsEmptyValues();

        Collection<String> result = (Collection<String>) answer.returnValueFor(Collection.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSetType() {
        Answer answer = new ReturnsEmptyValues();

        Set<String> result = (Set<String>) answer.returnValueFor(Set.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForHashSetType() {
        Answer answer = new ReturnsEmptyValues();

        Set<String> result = (Set<String>) answer.returnValueFor(HashSet.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSortedSetType() {
        Answer answer = new ReturnsEmptyValues();

        SortedSet<String> result = (SortedSet<String>) answer.returnValueFor(SortedSet.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForTreeSetType() {
        Answer answer = new ReturnsEmptyValues();

        SortedSet<String> result = (SortedSet<String>) answer.returnValueFor(TreeSet.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedHashSetType() {
        Answer answer = new ReturnsEmptyValues();

        Set<String> result = (Set<String>) answer.returnValueFor(LinkedHashSet.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForListType() {
        Answer answer = new ReturnsEmptyValues();

        List<String> result = (List<String>) answer.returnValueFor(List.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedListType() {
        Answer answer = new ReturnsEmptyValues();

        List<String> result = (List<String>) answer.returnValueFor(LinkedList.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForArrayListType() {
        Answer answer = new ReturnsEmptyValues();

        List<String> result = (List<String>) answer.returnValueFor(ArrayList.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForMapType() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, String> result = (Map<String, String>) answer.returnValueFor(Map.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForHashMapType() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, String> result = (Map<String, String>) answer.returnValueFor(HashMap.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSortedMapType() {
        Answer answer = new ReturnsEmptyValues();

        SortedMap<String, String> result = (SortedMap<String, String>) answer.returnValueFor(SortedMap.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForTreeMapType() {
        Answer answer = new ReturnsEmptyValues();

        SortedMap<String, String> result = (SortedMap<String, String>) answer.returnValueFor(TreeMap.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedHashMapType() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, String> result = (Map<String, String>) answer.returnValueFor(LinkedHashMap.class);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());
    }
}