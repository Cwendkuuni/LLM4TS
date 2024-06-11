
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testApplyPattern_ValidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0}!");
        assertEquals("Hello, World!", emf.format(new Object[]{"World"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_InvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello, {0!");
    }

    @Test
    public void testApplyPattern_ChoiceFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("I have {0, choice, 1#one apple| 2#two apples| 0#no apples}.");
        assertEquals("I have no apples.", emf.format(new Object[]{0}));
        assertEquals("I have one apple.", emf.format(new Object[]{1}));
        assertEquals("I have two apples.", emf.format(new Object[]{2}));
    }

    @Test
    public void testApplyPattern_CustomFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("The date is {0, mydate, yyyy-MM-dd}.");
        emf.setFormat(0, new SimpleDateFormat("yyyy/MM/dd"));
        assertEquals("The date is 2022/04/20.", emf.format(new Object[]{new Date(122, 3, 20)}));
    }

    @Test
    public void testApplyPattern_NoFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("The number is {0}.");
        assertEquals("The number is 100.", emf.format(new Object[]{100}));
    }

    @Test
    public void testApplyPattern_EscapedQuote() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("It''s {0} o''clock.");
        assertEquals("It's 10 o'clock.", emf.format(new Object[]{10}));
    }
}
