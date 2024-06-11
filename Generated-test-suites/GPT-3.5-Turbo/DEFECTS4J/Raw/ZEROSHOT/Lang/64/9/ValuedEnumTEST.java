import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ValuedEnumTest {
    
    @Test
    public void testGetEnum() {
        Class enumClass = JavaVersionEnum.class;
        List list = mock(List.class);
        Iterator iterator = mock(Iterator.class);
        ValuedEnum enumValue = mock(ValuedEnum.class);
        
        when(Enum.getEnumList(enumClass)).thenReturn(list);
        when(list.iterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(enumValue);
        when(enumValue.getValue()).thenReturn(100);
        
        Enum result = ValuedEnum.getEnum(enumClass, 100);
        
        verify(Enum.class).getEnumList(enumClass);
        verify(list).iterator();
        verify(iterator, times(2)).hasNext();
        verify(iterator).next();
        verify(enumValue).getValue();
        
        assertEquals(enumValue, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        Class enumClass = null;
        int value = 100;
        
        ValuedEnum.getEnum(enumClass, value);
    }
    
    @Test
    public void testGetValue() {
        ValuedEnum enumValue = new JavaVersionEnum("Java 1.0", 100);
        
        int result = enumValue.getValue();
        
        assertEquals(100, result);
    }
    
    @Test
    public void testCompareToWhenThisIsLessThanOther() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enumValue2 = new JavaVersionEnum("Java 1.1", 110);
        
        int result = enumValue1.compareTo(enumValue2);
        
        assertEquals(-10, result);
    }
    
    @Test
    public void testCompareToWhenThisIsGreaterThanOther() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.1", 110);
        ValuedEnum enumValue2 = new JavaVersionEnum("Java 1.0", 100);
        
        int result = enumValue1.compareTo(enumValue2);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testCompareToWhenThisIsEqualToOther() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enumValue2 = new JavaVersionEnum("Java 1.0", 100);
        
        int result = enumValue1.compareTo(enumValue2);
        
        assertEquals(0, result);
    }
    
    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentType() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enumValue2 = new AnotherVersionEnum("Another 1.0", 100);
        
        enumValue1.compareTo(enumValue2);
    }
    
    @Test(expected = NullPointerException.class)
    public void testCompareToWithNullOther() {
        ValuedEnum enumValue1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enumValue2 = null;
        
        enumValue1.compareTo(enumValue2);
    }
    
    @Test
    public void testToString() {
        ValuedEnum enumValue = new JavaVersionEnum("Java 1.0", 100);
        
        String result = enumValue.toString();
        
        assertEquals("JavaVersion[Java 1.0=100]", result);
    }
    
    @Test
    public void testToStringWhenToStringAlreadySet() {
        ValuedEnum enumValue = new JavaVersionEnum("Java 1.0", 100);
        String toString = "TestToString";
        enumValue.iToString = toString;
        
        String result = enumValue.toString();
        
        assertEquals(toString, result);
    }
}