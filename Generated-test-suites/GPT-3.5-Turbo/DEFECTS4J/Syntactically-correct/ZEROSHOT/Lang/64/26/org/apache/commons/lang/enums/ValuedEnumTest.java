import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testGetEnumString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        Assert.assertEquals(100, java1_0.getValue());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        Assert.assertEquals(110, java1_1.getValue());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");
        Assert.assertEquals(120, java1_2.getValue());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum("Java 1.3");
        Assert.assertEquals(130, java1_3.getValue());
    }

    @Test
    public void testGetEnumInt() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(100);
        Assert.assertEquals("Java 1.0", java1_0.getName());

        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum(110);
        Assert.assertEquals("Java 1.1", java1_1.getName());

        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum(120);
        Assert.assertEquals("Java 1.2", java1_2.getName());

        JavaVersionEnum java1_3 = JavaVersionEnum.getEnum(130);
        Assert.assertEquals("Java 1.3", java1_3.getName());
    }

    @Test
    public void testGetEnumMap() {
        Assert.assertEquals(4, JavaVersionEnum.getEnumMap().size());
    }

    @Test
    public void testGetEnumList() {
        Assert.assertEquals(4, JavaVersionEnum.getEnumList().size());
    }

    @Test
    public void testIterator() {
        Iterator<JavaVersionEnum> iterator = JavaVersionEnum.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        Assert.assertEquals(4, count);
    }

    @Test
    public void testComparable() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java1_2 = JavaVersionEnum.JAVA1_2;

        Assert.assertTrue(java1_0.compareTo(java1_2) < 0);
        Assert.assertTrue(java1_2.compareTo(java1_0) > 0);
        Assert.assertEquals(0, java1_0.compareTo(java1_0));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
    }
}