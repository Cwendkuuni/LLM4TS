import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CompareToBuilderTest {

    private CompareToBuilder compareToBuilder;

    @Before
    public void setUp() {
        compareToBuilder = new CompareToBuilder();
    }

    @Test
    public void testReflectionCompare_sameObject() {
        Object lhs = new Object();
        Object rhs = lhs;
        int expected = 0;
        int actual = compareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(expected, actual);
    }

    @Test
    public void testReflectionCompare_nullObject() {
        Object lhs = null;
        Object rhs = new Object();
        try {
            compareToBuilder.reflectionCompare(lhs, rhs);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReflectionCompare_classCastException() {
        class A {}
        class B {}
        Object lhs = new A();
        Object rhs = new B();
        try {
            compareToBuilder.reflectionCompare(lhs, rhs);
            fail("ClassCastException should be thrown");
        } catch (ClassCastException e) {
            // expected
        }
    }

    // ... add more test cases as needed

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        int expected = 1;
        int actual = builder.toComparison();
        assertEquals(expected, actual);
    }

    // ... add more test cases as needed

}