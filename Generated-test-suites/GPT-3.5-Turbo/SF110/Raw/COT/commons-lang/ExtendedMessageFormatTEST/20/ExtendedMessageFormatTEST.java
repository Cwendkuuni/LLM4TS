
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedMessageFormatTest {
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        assertEquals("Hello {0}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hi {0}");
        assertEquals("Hi {0}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalid() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hi {0");
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");
        assertEquals(format1, format2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        assertEquals(format.hashCode(), format.hashCode());
    }

    // Add more test cases for other methods as needed

}
