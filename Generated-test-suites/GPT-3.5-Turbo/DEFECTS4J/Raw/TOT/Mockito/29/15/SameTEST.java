import org.junit.Before;
import org.junit.Test;
import org.mockito.Description;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SameTest {

    private Same testSame;
    private Description mockedDescription;

    @Before
    public void setup() {
        testSame = new Same("test");
        mockedDescription = mock(Description.class);
    }

    @Test
    public void testMatches_WhenSameObject_ReturnsTrue() {
        assertTrue(testSame.matches("test"));
    }

    @Test
    public void testMatches_WhenDifferentObject_ReturnsFalse() {
        assertFalse(testSame.matches("different"));
    }

    @Test
    public void testMatches_WhenNullObject_ReturnsFalse() {
        assertFalse(testSame.matches(null));
    }

    @Test
    public void testDescribeTo_AppendTextCalledOnceWithExpectedString() {
        testSame.describeTo(mockedDescription);
        verify(mockedDescription, times(1)).appendText("same(\"test\")");
    }

    @Test
    public void testDescribeTo_AppendQuotingCalledTwice_WhenWantedIsString() {
        testSame.describeTo(mockedDescription);
        verify(mockedDescription, times(2)).appendText("\"");
    }

    @Test
    public void testDescribeTo_AppendQuotingCalledOnce_WhenWantedIsCharacter() {
        Same testSame = new Same('c');
        testSame.describeTo(mockedDescription);
        verify(mockedDescription, times(1)).appendText("'");
    }

    @Test
    public void testDescribeTo_AppendQuotingNotCalled_WhenWantedIsNotStringOrCharacter() {
        Same testSame = new Same(123);
        testSame.describeTo(mockedDescription);
        verify(mockedDescription, never()).appendText("\"");
        verify(mockedDescription, never()).appendText("'");
    }

}