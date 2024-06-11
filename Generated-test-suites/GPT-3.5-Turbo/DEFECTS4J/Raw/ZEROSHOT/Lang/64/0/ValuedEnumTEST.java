import org.junit.Test;
import org.apache.commons.lang.enums.ValuedEnum;
import org.apache.commons.lang.enums.JavaVersionEnum;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testGetEnumWithInvalidName() {
        ValuedEnum invalidEnum = JavaVersionEnum.getEnum("Invalid");
        assertNull(invalidEnum);
    }
    
    @Test
    public void testGetEnumWithValidValue() {
        ValuedEnum enum1 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = JavaVersionEnum.getEnum(110);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = JavaVersionEnum.getEnum(130);
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testGetEnumWithInvalidValue() {
        ValuedEnum invalidEnum = JavaVersionEnum.getEnum(999);
        assertNull(invalidEnum);
    }
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertFalse(enumMap.isEmpty());
        assertEquals(4, enumMap.size());
        
        ValuedEnum enum1 = (ValuedEnum) enumMap.get("Java 1.0");
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = (ValuedEnum) enumMap.get("Java 1.1");
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = (ValuedEnum) enumMap.get("Java 1.2");
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = (ValuedEnum) enumMap.get("Java 1.3");
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        assertFalse(enumList.isEmpty());
        assertEquals(4, enumList.size());
        
        ValuedEnum enum1 = (ValuedEnum) enumList.get(0);
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = (ValuedEnum) enumList.get(1);
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = (ValuedEnum) enumList.get(2);
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = (ValuedEnum) enumList.get(3);
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testIterator() {
        Iterator enumIterator = JavaVersionEnum.iterator();
        assertNotNull(enumIterator);
        assertTrue(enumIterator.hasNext());
        
        ValuedEnum enum1 = (ValuedEnum) enumIterator.next();
        assertEquals("Java 1.0", enum1.getName());
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = (ValuedEnum) enumIterator.next();
        assertEquals("Java 1.1", enum2.getName());
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = (ValuedEnum) enumIterator.next();
        assertEquals("Java 1.2", enum3.getName());
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = (ValuedEnum) enumIterator.next();
        assertEquals("Java 1.3", enum4.getName());
        assertEquals(130, enum4.getValue());
        
        assertFalse(enumIterator.hasNext());
    }
    
    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals(100, enum1.getValue());
        
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals(110, enum2.getValue());
        
        ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
        assertEquals(120, enum3.getValue());
        
        ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
        assertEquals(130, enum4.getValue());
    }
    
    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
        assertTrue(enum1.compareTo(enum1) == 0);
    }
    
    @Test
    public void testToString() {
        ValuedEnum enum1 = new JavaVersionEnum("Java 1.0", 100);
        assertEquals("JavaVersionEnum[Java 1.0=100]", enum1.toString());
        
        ValuedEnum enum2 = new JavaVersionEnum("Java 1.1", 110);
        assertEquals("JavaVersionEnum[Java 1.1=110]", enum2.toString());
        
        ValuedEnum enum3 = new JavaVersionEnum("Java 1.2", 120);
        assertEquals("JavaVersionEnum[Java 1.2=120]", enum3.toString());
        
        ValuedEnum enum4 = new JavaVersionEnum("Java 1.3", 130);
        assertEquals("JavaVersionEnum[Java 1.3=130]", enum4.toString());
    }

}