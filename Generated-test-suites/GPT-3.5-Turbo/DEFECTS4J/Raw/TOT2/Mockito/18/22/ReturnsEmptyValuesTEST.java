import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import java.util.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock(mock(Object.class), ReturnsEmptyValues.class.getDeclaredMethod("answer", InvocationOnMock.class), null);
        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswerCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock(mock(Object.class), ReturnsEmptyValues.class.getDeclaredMethod("answer", InvocationOnMock.class), null);
        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForPrimitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
    }

    @Test
    public void testReturnValueForCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertTrue(returnsEmptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(returnsEmptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(returnsEmptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(returnsEmptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(returnsEmptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(returnsEmptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(returnsEmptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }

}