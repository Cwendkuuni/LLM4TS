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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Object mock = mock(String.class);
        InvocationOnMock invitation = invocation(returnsEmptyValues, Mockito.method(String.class, "toString"));
        String result = (String) returnsEmptyValues.answer(invitation);
        Mockito.verify(returnsEmptyValues).answer(invitation);
        Mockito.verify(returnsEmptyValues).toString();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues, mock);
        Mockito.verify(mock).toString();
        assert result.contains("Mock for ");
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Object obj1 = new Object();
        Object obj2 = new Object();

        InvocationOnMock invitation = invocation(returnsEmptyValues, Mockito.method(Object.class, "compareTo", Object.class), obj1, obj2);
        int result = (int) returnsEmptyValues.answer(invitation);
        Mockito.verify(returnsEmptyValues).answer(invitation);
        Mockito.verify(returnsEmptyValues).compareTo(obj1, obj2);
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(obj1).compareTo(obj2);
        Mockito.verify(obj2).compareTo(obj1);
        Mockito.verify(obj1).equals(obj2);
        Mockito.verify(obj2).equals(obj1);

        Mockito.verify(returnsEmptyValues, Mockito.times(2)).compareTo(any(Object.class), any(Object.class));
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        assert result != 0;
    }

    @Test(expected = AssertionError.class)
    public void testPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        IntAnswer intAnswer = mock(Answer.class);
        Mockito.when(intAnswer.answer(any(InvocationOnMock.class))).thenReturn(1);
        ReturnsEmptyValues spy = Mockito.spy(returnsEmptyValues);
        Mockito.doAnswer(intAnswer).when(spy).answer(Mockito.method(byte.class, "byteValue"));

        byte result = (byte) spy.answer(invocation(spy, Mockito.method(byte.class, "byteValue")));
        assert result == 0;

        Mockito.verify(intAnswer).answer(any(InvocationOnMock.class));
        Mockito.verifyNoMoreInteractions(intAnswer);
    }

    @Test(expected = AssertionError.class)
    public void testSimpleCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<String> expected = new ArrayList<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(Collection.class, "newInstance"))).thenReturn(expected);
        Collection<String> result = (Collection<String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testComplexCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedList<String> expected = new LinkedList<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(List.class, "newInstance"))).thenReturn(expected);
        List<String> result = (List<String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(List.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(List.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(List.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(List.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testEmptyMapTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashMap<String, String> expected = new HashMap<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(Map.class, "newInstance"))).thenReturn(expected);
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(Map.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(Map.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Map.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Map.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testEmptySetTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashSet<String> expected = new HashSet<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(Set.class, "newInstance"))).thenReturn(expected);
        Set<String> result = (Set<String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(Set.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(Set.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Set.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Set.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testEmptySortedSetTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeSet<String> expected = new TreeSet<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(SortedSet.class, "newInstance"))).thenReturn(expected);
        SortedSet<String> result = (SortedSet<String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(SortedSet.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(SortedSet.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(SortedSet.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(SortedSet.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testEmptySortedMapTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeMap<String, String> expected = new TreeMap<>();
        Mockito.when(returnsEmptyValues.answer(Mockito.method(SortedMap.class, "newInstance"))).thenReturn(expected);
        SortedMap<String, String> result = (SortedMap<String, String>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(SortedMap.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(SortedMap.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(SortedMap.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(SortedMap.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testCustomCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedList<Person> expected = new LinkedList<>();
        Person p1 = new Person("John");
        Person p2 = new Person("Mary");
        expected.add(p1);
        expected.add(p2);
        Mockito.when(returnsEmptyValues.answer(Mockito.method(Collection.class, "newInstance"))).thenReturn(expected);
        Collection<Person> result = (Collection<Person>) returnsEmptyValues.answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));

        assert result.isEmpty();
        assert result.getClass().equals(expected.getClass());
        assert result.getClass().equals(Collection.class);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);

        Mockito.verify(returnsEmptyValues).answer(invocation(returnsEmptyValues, Mockito.method(Collection.class, "newInstance")));
        Mockito.verify(returnsEmptyValues).newInstance();
        Mockito.verifyNoMoreInteractions(returnsEmptyValues);
    }

    @Test
    public void testNullReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer<Object> answer = Mockito.mock(Answer.class);
        Mockito.when(returnsEmptyValues.answer(any(InvocationOnMock.class))).thenAnswer(answer);
        Object result = returnsEmptyValues.answer(Mockito.mock(Object.class));
        assert result == null;

        Mockito.verify(answer).answer(any(InvocationOnMock.class));
        Mockito.verifyNoMoreInteractions(answer);
    }
}