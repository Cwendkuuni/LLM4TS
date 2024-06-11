import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testMatches_withSameObject_shouldReturnTrue() {
        Object obj = new Object();
        Same same = new Same(obj);
        assertTrue(same.matches(obj));
    }

    @Test
    public void testMatches_withDifferentObject_shouldReturnFalse() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Same same = new Same(obj1);
        assertFalse(same.matches(obj2));
    }

    @Test
    public void testDescribeTo_shouldAppendTextWithQuoting() {
        Same same = new Same("test");
        DescriptionMock description = new DescriptionMock();
        same.describeTo(description);
        assertEquals("same(\"test\")", description.toString());
    }

    private class DescriptionMock implements Description {
        private String description;

        public void appendText(String text) {
            description += text;
        }

        public String toString() {
            return description;
        }

        // Other Description methods can be implemented if necessary
    }

}