import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class ValuedEnumTest {
    
    // Test getValue()
    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.JAVA1_0;
        assertEquals(100, javaVersionEnum.getValue());
        
        SomeEnum someEnum = SomeEnum.VALUE1;
        assertEquals(1, someEnum.getValue());
    }
    
    // Test getEnum()
    @Test
    public void testGetEnum() {
        JavaVersionEnum javaVersionEnum = JavaVersionEnum.getEnum(100);
        assertEquals(JavaVersionEnum.JAVA1_0, javaVersionEnum);
        
        SomeEnum someEnum = SomeEnum.getEnum(1);
        assertEquals(SomeEnum.VALUE1, someEnum);
        
        SomeEnum invalidEnum = SomeEnum.getEnum(10);
        assertEquals(null, invalidEnum);
    }
    
    // Test getEnumMap()
    @Test
    public void testGetEnumMap() {
        List<JavaVersionEnum> enumList = new ArrayList<>();
        enumList.add(JavaVersionEnum.JAVA1_0);
        enumList.add(JavaVersionEnum.JAVA1_1);
        enumList.add(JavaVersionEnum.JAVA1_2);
        enumList.add(JavaVersionEnum.JAVA1_3);
        
        List<JavaVersionEnum> enumMap = JavaVersionEnum.getEnumMap();
        
        assertEquals(enumList, enumMap);
    }
    
    // Test getEnumList()
    @Test
    public void testGetEnumList() {
        List<JavaVersionEnum> enumList = new ArrayList<>();
        enumList.add(JavaVersionEnum.JAVA1_0);
        enumList.add(JavaVersionEnum.JAVA1_1);
        enumList.add(JavaVersionEnum.JAVA1_2);
        enumList.add(JavaVersionEnum.JAVA1_3);
        
        List<JavaVersionEnum> returnedList = JavaVersionEnum.getEnumList();
        
        assertEquals(enumList, returnedList);
    }
    
    // Test iterator()
    @Test
    public void testIterator() {
        List<JavaVersionEnum> enumList = new ArrayList<>();
        enumList.add(JavaVersionEnum.JAVA1_0);
        enumList.add(JavaVersionEnum.JAVA1_1);
        enumList.add(JavaVersionEnum.JAVA1_2);
        enumList.add(JavaVersionEnum.JAVA1_3);
        
        Iterator<JavaVersionEnum> iterator = JavaVersionEnum.iterator();
        
        List<JavaVersionEnum> returnedList = new ArrayList<>();
        while (iterator.hasNext()) {
            returnedList.add(iterator.next());
        }
        
        assertEquals(enumList, returnedList);
    }
}