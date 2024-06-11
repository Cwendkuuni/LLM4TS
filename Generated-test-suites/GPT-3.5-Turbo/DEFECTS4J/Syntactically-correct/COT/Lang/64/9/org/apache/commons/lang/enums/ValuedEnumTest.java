import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetValue() {
        JavaVersionEnum java1 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(100, java1.getValue());
        
        JavaVersionEnum java2 = JavaVersionEnum.JAVA1_1;
        Assert.assertEquals(110, java2.getValue());
        
        JavaVersionEnum java3 = JavaVersionEnum.JAVA1_2;
        Assert.assertEquals(120, java3.getValue());
        
        JavaVersionEnum java4 = JavaVersionEnum.JAVA1_3;
        Assert.assertEquals(130, java4.getValue());
    }
    
    @Test
    public void testGetEnum() {
        JavaVersionEnum java1 = JavaVersionEnum.getEnum(100);
        Assert.assertEquals(JavaVersionEnum.JAVA1_0, java1);
        
        JavaVersionEnum java2 = JavaVersionEnum.getEnum(110);
        Assert.assertEquals(JavaVersionEnum.JAVA1_1, java2);
        
        JavaVersionEnum java3 = JavaVersionEnum.getEnum(120);
        Assert.assertEquals(JavaVersionEnum.JAVA1_2, java3);
        
        JavaVersionEnum java4 = JavaVersionEnum.getEnum(130);
        Assert.assertEquals(JavaVersionEnum.JAVA1_3, java4);
    }
    
    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        Assert.assertNotNull(enumMap);
        Assert.assertFalse(enumMap.isEmpty());
        Assert.assertEquals(4, enumMap.size());
    }
    
    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        Assert.assertNotNull(enumList);
        Assert.assertFalse(enumList.isEmpty());
        Assert.assertEquals(4, enumList.size());
    }
    
    @Test
    public void testIterator() {
        Iterator enumIterator = JavaVersionEnum.iterator();
        Assert.assertNotNull(enumIterator);
        Assert.assertTrue(enumIterator.hasNext());
        Assert.assertEquals(JavaVersionEnum.JAVA1_0, enumIterator.next());
        Assert.assertEquals(JavaVersionEnum.JAVA1_1, enumIterator.next());
        Assert.assertEquals(JavaVersionEnum.JAVA1_2, enumIterator.next());
        Assert.assertEquals(JavaVersionEnum.JAVA1_3, enumIterator.next());
    }
}