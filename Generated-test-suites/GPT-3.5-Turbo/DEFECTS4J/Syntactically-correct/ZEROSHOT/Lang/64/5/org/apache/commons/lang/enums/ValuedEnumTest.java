import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ValuedEnumTest {

    @Test
    public void testGetValue() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;
        
        assertEquals(100, java10.getValue());
        assertEquals(110, java11.getValue());
        assertEquals(120, java12.getValue());
        assertEquals(130, java13.getValue());
    }
    
    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java10 = (JavaVersionEnum) JavaVersionEnum.getEnum(100);
        JavaVersionEnum java11 = (JavaVersionEnum) JavaVersionEnum.getEnum(110);
        JavaVersionEnum java12 = (JavaVersionEnum) JavaVersionEnum.getEnum(120);
        JavaVersionEnum java13 = (JavaVersionEnum) JavaVersionEnum.getEnum(130);
        
        assertEquals(JavaVersionEnum.JAVA1_0, java10);
        assertEquals(JavaVersionEnum.JAVA1_1, java11);
        assertEquals(JavaVersionEnum.JAVA1_2, java12);
        assertEquals(JavaVersionEnum.JAVA1_3, java13);
    }
    
    @Test
    public void testToString() {
        JavaVersionEnum java10 = JavaVersionEnum.JAVA1_0;
        JavaVersionEnum java11 = JavaVersionEnum.JAVA1_1;
        JavaVersionEnum java12 = JavaVersionEnum.JAVA1_2;
        JavaVersionEnum java13 = JavaVersionEnum.JAVA1_3;
        
        assertEquals("JavaVersion[Java 1.0=100]", java10.toString());
        assertEquals("JavaVersion[Java 1.1=110]", java11.toString());
        assertEquals("JavaVersion[Java 1.2=120]", java12.toString());
        assertEquals("JavaVersion[Java 1.3=130]", java13.toString());
    }
}