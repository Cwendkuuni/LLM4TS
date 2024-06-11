import org.junit.Test;
import org.mockito.internal.matchers.Same;
import static org.junit.Assert.assertEquals;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");

        boolean result = same.matches("test");

        assertEquals(true, result);
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        DescriptionMock descriptionMock = new DescriptionMock();

        same.describeTo(descriptionMock);

        assertEquals("same(\"test\")", descriptionMock.getDescription());
    }

    private class DescriptionMock extends Description {
        private String description;

        public void appendText(String text) {
            description += text;
        }

        public void appendQuoting(String text) {
            description += text;
        }

        public String getDescription() {
            return description;
        }
    }

}