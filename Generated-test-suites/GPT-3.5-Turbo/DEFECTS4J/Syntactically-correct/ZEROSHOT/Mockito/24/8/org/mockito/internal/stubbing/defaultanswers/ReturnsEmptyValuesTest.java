import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer_toStringReturnsMockDescription() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = returnsEmptyValues.answer(invocation);

        Object mock = invocation.getMock();
        MockUtil mockUtil = new MockUtil();
        MockName name = mockUtil.getMockName(mock);
        
        if (name.isDefault()) {
            assertEquals("Mock for " + mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
        } else {
            assertEquals(name.toString(), result);
        }
    }
    
    @Test
    public void testAnswer_compareToMethodReturnsNonZero() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        
        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }
    
    @Test
    public void testReturnValueFor_primitiveReturnTypeReturnsDefaultValue() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(int.class), returnsEmptyValues.returnValueFor(int.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Integer.class), returnsEmptyValues.returnValueFor(Integer.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(boolean.class), returnsEmptyValues.returnValueFor(boolean.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Boolean.class), returnsEmptyValues.returnValueFor(Boolean.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(byte.class), returnsEmptyValues.returnValueFor(byte.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Byte.class), returnsEmptyValues.returnValueFor(Byte.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(short.class), returnsEmptyValues.returnValueFor(short.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Short.class), returnsEmptyValues.returnValueFor(Short.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(long.class), returnsEmptyValues.returnValueFor(long.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Long.class), returnsEmptyValues.returnValueFor(Long.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(float.class), returnsEmptyValues.returnValueFor(float.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Float.class), returnsEmptyValues.returnValueFor(Float.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(double.class), returnsEmptyValues.returnValueFor(double.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Double.class), returnsEmptyValues.returnValueFor(Double.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(char.class), returnsEmptyValues.returnValueFor(char.class));
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Character.class), returnsEmptyValues.returnValueFor(Character.class));
    }
    
    @Test
    public void testReturnValueFor_collectionReturnTypeReturnsEmptyCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.returnValueFor(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }
}