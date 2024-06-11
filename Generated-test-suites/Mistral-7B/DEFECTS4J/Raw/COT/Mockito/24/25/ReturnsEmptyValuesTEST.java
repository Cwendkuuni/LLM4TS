import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = new Object();

        // When
        String result = (String) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "toString", mock));

        // Then
        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        int result = (Integer) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "compareTo", new Object()));

        // Then
        Assert.assertNotEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReturnEmptyCollectionThrowsExceptionWhenCollectionIsModified() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Collection<Integer> collection = (Collection<Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "collectionMethod", null));
        collection.add(1);

        // Then
        // An exception should be thrown because the returned collection is not modifiable
    }

    @Test
    public void testReturnEmptyArrayList() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Integer> result = (List<Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "listMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyHashSet() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Integer> result = (Set<Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "setMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyTreeSet() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<Integer> result = (SortedSet<Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "sortedSetMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyLinkedHashSet() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Integer> result = (Set<Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "linkedHashSetMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyHashMap() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "mapMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyTreeMap() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedMap<String, Integer> result = (SortedMap<String, Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "sortedMapMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnEmptyLinkedHashMap() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(new InvocationOnMock(ReturnsEmptyValuesTest.class, "linkedHashMapMethod", null));

        // Then
        Assert.assertTrue(result.isEmpty());
    }

}