import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {
    
    @Test
    public void testGetValue() {
        // Typical use case
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        int expectedValue1 = 100;
        int actualValue1 = javaVersion1.getValue();
        Assert.assertEquals(expectedValue1, actualValue1);
        
        // Edge case
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_3;
        int expectedValue2 = 130;
        int actualValue2 = javaVersion2.getValue();
        Assert.assertEquals(expectedValue2, actualValue2);
    }
    
    @Test
    public void testGetEnum_String() {
        // Typical use case
        String javaVersion1 = "Java 1.0";
        JavaVersionEnum expectedEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum actualEnum1 = JavaVersionEnum.getEnum(javaVersion1);
        Assert.assertEquals(expectedEnum1, actualEnum1);
        
        // Error scenario
        String javaVersion2 = "Not a Java version";
        JavaVersionEnum expectedEnum2 = null;
        JavaVersionEnum actualEnum2 = JavaVersionEnum.getEnum(javaVersion2);
        Assert.assertEquals(expectedEnum2, actualEnum2);
    }
    
    @Test
    public void testGetEnum_int() {
        // Typical use case
        int javaVersion1 = 100;
        JavaVersionEnum expectedEnum1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum actualEnum1 = JavaVersionEnum.getEnum(javaVersion1);
        Assert.assertEquals(expectedEnum1, actualEnum1);
        
        // Error scenario
        int javaVersion2 = 999;
        JavaVersionEnum expectedEnum2 = null;
        JavaVersionEnum actualEnum2 = JavaVersionEnum.getEnum(javaVersion2);
        Assert.assertEquals(expectedEnum2, actualEnum2);
    }
    
    @Test
    public void testCompareTo() {
        // Typical use case
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_1;
        int expectedComparison1 = -1;
        int actualComparison1 = javaVersion1.compareTo(javaVersion2);
        Assert.assertEquals(expectedComparison1, actualComparison1);
        
        // Edge case
        JavaVersionEnum javaVersion3 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum javaVersion4 = JavaVersionEnum.JAVA1_2;
        int expectedComparison2 = 0;
        int actualComparison2 = javaVersion3.compareTo(javaVersion4);
        Assert.assertEquals(expectedComparison2, actualComparison2);
    }
    
    @Test
    public void testToString() {
        // Typical use case
        JavaVersionEnum javaVersion1 = JavaVersionEnum.JAVA1_0;
        String expectedToString1 = "JavaVersion[Java 1.0=100]";
        String actualToString1 = javaVersion1.toString();
        Assert.assertEquals(expectedToString1, actualToString1);
        
        // Edge case
        JavaVersionEnum javaVersion2 = JavaVersionEnum.JAVA1_3;
        String expectedToString2 = "JavaVersion[Java 1.3=130]";
        String actualToString2 = javaVersion2.toString();
        Assert.assertEquals(expectedToString2, actualToString2);
    }
}