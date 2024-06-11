import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Object mock = mock(String.class);
        when(mock.toString()).thenReturn("Mock String");

        MockName name = Mockito.mock(MockName.class);
        when(name.isDefault()).thenReturn(true);
        when(name.toString()).thenReturn("MockNameDefault");

        when(returnsEmptyValues.answer(Mockito.argThat(inv -> inv.getTarget().equals(mock) && inv.getMethod().getName().equals("toString"))))
                .thenReturn("Mock for String, hashCode: " + mock.hashCode());

        assertEquals("Mock for String, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(new InvocationOnMock(mock, "toString()")));
        verifyZeroInteractions(name);

        when(name.isDefault()).thenReturn(false);
        assertEquals("MockNameDefault", returnsEmptyValues.answer(new InvocationOnMock(mock, "toString()")));
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());

        assertEquals(1, returnsEmptyValues.answer(new InvocationOnMock(mock(Comparable.class), "compareTo(T other)")));
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());

        assertEquals(0, (byte) returnsEmptyValues.answer(new InvocationOnMock(mock(Byte.class), "byteValue()")));
        assertEquals(0, (short) returnsEmptyValues.answer(new InvocationOnMock(mock(Short.class), "shortValue()")));
        assertEquals(0, (int) returnsEmptyValues.answer(new InvocationOnMock(mock(Integer.class), "intValue()")));
        assertEquals(0L, (long) returnsEmptyValues.answer(new InvocationOnMock(mock(Long.class), "longValue()")));
        assertEquals(0f, (float) returnsEmptyValues.answer(new InvocationOnMock(mock(Float.class), "floatValue()")));
        assertEquals(0d, (double) returnsEmptyValues.answer(new InvocationOnMock(mock(Double.class), "doubleValue()")));
        assertEquals(false, (boolean) returnsEmptyValues.answer(new InvocationOnMock(mock(Boolean.class), "booleanValue()")));
        assertEquals(Character.MIN_VALUE, (char) returnsEmptyValues.answer(new InvocationOnMock(mock(Character.class), "charValue()")));
        assertEquals('\u0000', (char) returnsEmptyValues.answer(new InvocationOnMock(mock(Character.class), "charValue()")));
    }

    @Test
    public void testEmptyCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());

        assertEquals(new LinkedList<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Collection.class), "")));
        assertEquals(new ArrayList<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Collection.class), "size()")));

        assertEquals(new HashSet<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Set.class), "newHashSet()")));
        assertEquals(new LinkedHashSet<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Set.class), "newLinkedHashSet()")));

        assertEquals(new TreeSet<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(SortedSet.class), "newTreeSet()")));
        assertEquals(new TreeSet<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(SortedSet.class), "newTreeSet(Collections.emptyList())")));

        assertEquals(new ArrayList<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(List.class), "newArrayList()")));
        assertEquals(new LinkedList<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(List.class), "newLinkedList()")));

        assertEquals(new HashMap<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Map.class), "newHashMap()")));
        assertEquals(new LinkedHashMap<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(Map.class), "newLinkedHashMap()")));

        assertEquals(new TreeMap<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(SortedMap.class), "newTreeMap()")));
        assertEquals(new TreeMap<>(), returnsEmptyValues.answer(new InvocationOnMock(mock(SortedMap.class), "newTreeMap(Collections.emptyList())")));
    }

    @Test(expected = AssertionError.class)
    public void testUnsupportedCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());

        // TODO: implement test case for Iterable and other collections

        when(mock(Collection.class).iterator()).thenReturn(mock(Iterator.class).mockReturnValue(null));
        assertEquals(null, returnsEmptyValues.answer(new InvocationOnMock(mock(Collection.class), "iterator()")));
    }

    @Test
    public void testOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());

        assertEquals(null, returnsEmptyValues.answer(new InvocationOnMock(mock(Object.class), "")));
        assertEquals(null, returnsEmptyValues.answer(new InvocationOnMock(mock(Object.class), "hashCode()")));
        assertEquals(null, returnsEmptyValues.answer(new InvocationOnMock(mock(Object.class), "getClass()")));
        assertEquals(null, returnsEmptyValues.answer(new InvocationOnMock(mock(Object.class), "toString()")));
    }

}