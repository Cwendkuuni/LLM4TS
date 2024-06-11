import org.junit.Test;
import org.apache.commons.lang.enums.ValuedEnum;
import static org.junit.Assert.*;

public class ValuedEnumTest {
    
    // Define a subclass of ValuedEnum for testing
    public static final class TestEnum extends ValuedEnum {
        public static final int VALUE1 = 1;
        public static final int VALUE2 = 2;
        public static final TestEnum ENUM1 = new TestEnum("ENUM1", VALUE1);
        public static final TestEnum ENUM2 = new TestEnum("ENUM2", VALUE2);
        
        private TestEnum(String name, int value) {
            super(name, value);
        }
    }
    
    @Test
    public void testGetValue() {
        TestEnum enum1 = TestEnum.ENUM1;
        TestEnum enum2 = TestEnum.ENUM2;
        
        assertEquals(TestEnum.VALUE1, enum1.getValue());
        assertEquals(TestEnum.VALUE2, enum2.getValue());
    }
    
    @Test
    public void testCompareTo() {
        TestEnum enum1 = TestEnum.ENUM1;
        TestEnum enum2 = TestEnum.ENUM2;
        
        assertTrue(enum1.compareTo(enum2) < 0);
        assertTrue(enum2.compareTo(enum1) > 0);
        assertEquals(0, enum1.compareTo(enum1));
    }
    
    @Test
    public void testGetEnum() {
        TestEnum enum1 = TestEnum.ENUM1;
        TestEnum enum2 = TestEnum.ENUM2;
        
        assertEquals(enum1, TestEnum.getEnum(TestEnum.class, TestEnum.VALUE1));
        assertEquals(enum2, TestEnum.getEnum(TestEnum.class, TestEnum.VALUE2));
    }
    
    @Test
    public void testToString() {
        TestEnum enum1 = TestEnum.ENUM1;
        TestEnum enum2 = TestEnum.ENUM2;
        
        assertEquals("TestEnum[ENUM1=1]", enum1.toString());
        assertEquals("TestEnum[ENUM2=2]", enum2.toString());
    }
}

