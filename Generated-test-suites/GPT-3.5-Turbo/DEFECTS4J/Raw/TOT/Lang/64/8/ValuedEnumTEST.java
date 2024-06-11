import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testGetEnumByName() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        Assert.assertEquals(100, java1_0.getValue());
        
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        Assert.assertEquals(110, java1_1.getValue());
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        Assert.assertEquals("Java 1.0", java1_0.getName());
        
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        Assert.assertEquals("Java 1.1", java1_1.getName());
    }

    @Test
    public void testGetEnumMap() {
        Map enumMap = JavaVersionEnum.getEnumMap();
        Assert.assertEquals(4, enumMap.size());
        
        JavaVersionEnum java1_2 = (JavaVersionEnum) enumMap.get(120);
        Assert.assertEquals("Java 1.2", java1_2.getName());
    }

    @Test
    public void testGetEnumList() {
        List enumList = JavaVersionEnum.getEnumList();
        Assert.assertEquals(4, enumList.size());
        
        JavaVersionEnum java1_3 = (JavaVersionEnum) enumList.get(3);
        Assert.assertEquals("Java 1.3", java1_3.getName());
    }

    @Test
    public void testIterator() {
        Iterator enumIterator = JavaVersionEnum.iterator();
        int count = 0;
        while (enumIterator.hasNext()) {
            enumIterator.next();
            count++;
        }
        Assert.assertEquals(4, count);
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_1 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;
        
        Assert.assertEquals(-10, java1_0.compareTo(java1_1));
        Assert.assertEquals(-20, java1_0.compareTo(java1_2));
        Assert.assertEquals(0, java1_0.compareTo(java1_0));
    }

    @Test(expected = ClassCastException.class)
    public void testCompareToWithDifferentEnumClass() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        OtherEnum otherEnum = OtherEnum.FIRST_VALUE;
        
        java1_0.compareTo(otherEnum);
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
    }
}