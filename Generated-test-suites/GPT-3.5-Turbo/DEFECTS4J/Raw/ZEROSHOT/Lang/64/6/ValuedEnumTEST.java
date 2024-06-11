import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals("Java 1.3", java1_3.getName());
        assertEquals(130, java1_3.getValue());
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
        assertEquals("Java 1.3", java1_3.getName());
        assertEquals(130, java1_3.getValue());
    }
    
    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());
        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        assertEquals("Java 1.3", java1_3.getName());
        assertEquals(130, java1_3.getValue());
    }
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        assertNotNull(enumMap);
        assertEquals(4, enumMap.size());
        assertTrue(enumMap.containsKey("Java 1.0"));
        assertTrue(enumMap.containsKey("Java 1.1"));
        assertTrue(enumMap.containsKey("Java 1.2"));
        assertTrue(enumMap.containsKey("Java 1.3"));
        assertEquals(100, ((JavaVersionEnum)enumMap.get("Java 1.0")).getValue());
        assertEquals(110, ((JavaVersionEnum)enumMap.get("Java 1.1")).getValue());
        assertEquals(120, ((JavaVersionEnum)enumMap.get("Java 1.2")).getValue());
        assertEquals(130, ((JavaVersionEnum)enumMap.get("Java 1.3")).getValue());
    }
    
    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        assertNotNull(enumList);
        assertEquals(4, enumList.size());
        assertEquals("Java 1.0", ((JavaVersionEnum)enumList.get(0)).getName());
        assertEquals(100, ((JavaVersionEnum)enumList.get(0)).getValue());
        assertEquals("Java 1.1", ((JavaVersionEnum)enumList.get(1)).getName());
        assertEquals(110, ((JavaVersionEnum)enumList.get(1)).getValue());
        assertEquals("Java 1.2", ((JavaVersionEnum)enumList.get(2)).getName());
        assertEquals(120, ((JavaVersionEnum)enumList.get(2)).getValue());
        assertEquals("Java 1.3", ((JavaVersionEnum)enumList.get(3)).getName());
        assertEquals(130, ((JavaVersionEnum)enumList.get(3)).getValue());
    }
    
    @Test
    public void testIterator() {
        Iterator iterator = JavaVersionEnum.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals("Java 1.0", ((JavaVersionEnum)iterator.next()).getName());
        assertTrue(iterator.hasNext());
        assertEquals("Java 1.1", ((JavaVersionEnum)iterator.next()).getName());
        assertTrue(iterator.hasNext());
        assertEquals("Java 1.2", ((JavaVersionEnum)iterator.next()).getName());
        assertTrue(iterator.hasNext());
        assertEquals("Java 1.3", ((JavaVersionEnum)iterator.next()).getName());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testEquals() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_0_2 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals(java1_0, java1_0);
        assertEquals(java1_0, java1_0_2);
        assertNotEquals(java1_0, java1_1);
        assertNotEquals(java1_0, null);
        assertNotEquals(java1_0, "Java 1.0");
    }
    
    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertTrue(java1_0.compareTo(java1_1) < 0);
        assertTrue(java1_1.compareTo(java1_0) > 0);
        assertTrue(java1_0.compareTo(java1_0) == 0);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        assertEquals("JavaVersion[Java 1.1=110]", java1_1.toString());
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        assertEquals("JavaVersion[Java 1.2=120]", java1_2.toString());
        JavaVersionEnum java1_3 = JavaVersionEnum.JAVA1_3;
        assertEquals("JavaVersion[Java 1.3=130]", java1_3.toString());
    }
}