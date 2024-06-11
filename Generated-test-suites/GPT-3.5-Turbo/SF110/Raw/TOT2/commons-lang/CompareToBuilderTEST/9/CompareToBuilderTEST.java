import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_NullObjects() {
        Object obj1 = null;
        Object obj2 = null;
        int result = CompareToBuilder.reflectionCompare(obj1, obj2);
        assertEquals(0, result);
    }

    @Test
    public void testAppendSuper() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(5);
        int result = builder.toComparison();
        assertEquals(5, result);
    }

    @Test
    public void testAppend_Comparator() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(10, 5, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        int result = builder.toComparison();
        assertEquals(1, result);
    }

    // Add more test cases for other methods as needed

}