import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetName() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals("Java 1.0", javaVersion.getName());
    }

    @Test
    public void testGetEnum_String() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum("Java 1.1");
        Assert.assertEquals(JavaVersionEnum.JAVA1_1, javaVersion);
    }

    @Test
    public void testGetEnum_int() {
        JavaVersionEnum javaVersion = JavaVersionEnum.getEnum(120);
        Assert.assertEquals(JavaVersionEnum.JAVA1_2, javaVersion);
    }

    @Test
    public void testGetEnumMap() {
        Map<String, JavaVersionEnum> enumMap = JavaVersionEnum.getEnumMap();
        Assert.assertNotNull(enumMap);
        Assert.assertEquals(4, enumMap.size());
    }

    @Test
    public void testGetEnumList() {
        List<JavaVersionEnum> enumList = JavaVersionEnum.getEnumList();
        Assert.assertNotNull(enumList);
        Assert.assertEquals(4, enumList.size());
    }

    @Test
    public void testIterator() {
        Iterator<JavaVersionEnum> iterator = JavaVersionEnum.iterator();
        Assert.assertNotNull(iterator);
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        Assert.assertEquals(4, count);
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals(100, javaVersion.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        Assert.assertTrue(javaVersion1.compareTo(javaVersion2) < 0);
    }

    @Test
    public void testToString() {
        JavaVersionEnum javaVersion = JavaVersionEnum.JAVA1_0;
        Assert.assertEquals("JavaVersion[Java 1.0=100]", javaVersion.toString());
    }
}