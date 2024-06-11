import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ValuedEnumTest {
    
    private JavaVersionEnum javaVersion;

    @Before
    public void setUp() {
        // Initialize JavaVersionEnum object
        javaVersion = JavaVersionEnum.getEnum(100);
    }

    @Test
    public void testGetValue() {
        // Test getValue() method
        int value = javaVersion.getValue();
        Assert.assertEquals(100, value);
    }

    @Test
    public void testCompareTo() {
        // Test compareTo() method with equal value
        JavaVersionEnum equalVersion = JavaVersionEnum.getEnum(100);
        int result = javaVersion.compareTo(equalVersion);
        Assert.assertEquals(0, result);
        
        // Test compareTo() method with greater value
        JavaVersionEnum greaterVersion = JavaVersionEnum.getEnum(120);
        result = javaVersion.compareTo(greaterVersion);
        Assert.assertTrue(result < 0);
        
        // Test compareTo() method with lower value
        JavaVersionEnum lowerVersion = JavaVersionEnum.getEnum(90);
        result = javaVersion.compareTo(lowerVersion);
        Assert.assertTrue(result > 0);
    }

    @Test
    public void testToString() {
        // Test toString() method
        String str = javaVersion.toString();
        Assert.assertEquals("JavaVersion[Java 1.0=100]", str);
    }

    @Test
    public void testGetEnum() {
        // Test getEnum() method with valid value
        JavaVersionEnum version = JavaVersionEnum.getEnum(100);
        Assert.assertNotNull(version);
        Assert.assertEquals(100, version.getValue());
        
        // Test getEnum() method with invalid value
        JavaVersionEnum nullVersion = JavaVersionEnum.getEnum(150);
        Assert.assertNull(nullVersion);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        // Test getEnum() method with null enumClass
        JavaVersionEnum version = JavaVersionEnum.getEnum(null, 100);
    }

    @Test
    public void testGetEnumMap() {
        // Test getEnumMap() method
        Map enumMap = JavaVersionEnum.getEnumMap();
        Assert.assertNotNull(enumMap);
        Assert.assertEquals(4, enumMap.size());
    }

    @Test
    public void testGetEnumList() {
        // Test getEnumList() method
        List enumList = JavaVersionEnum.getEnumList();
        Assert.assertNotNull(enumList);
        Assert.assertEquals(4, enumList.size());
    }

    @Test
    public void testIterator() {
        // Test iterator() method
        Iterator iter = JavaVersionEnum.iterator();
        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }
        Assert.assertEquals(4, count);
    }
}