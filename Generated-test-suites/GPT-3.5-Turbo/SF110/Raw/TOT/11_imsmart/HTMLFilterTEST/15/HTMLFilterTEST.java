import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HTMLFilterTest {
  
  private HTMLFilter htmlFilter;
  
  @Before
  public void setUp() {
    htmlFilter = new HTMLFilter();
  }
  
  @After
  public void tearDown() {
    htmlFilter = null;
  }
  
  @Test
  public void testFilter_withNullMessage_returnsNull() {
    String filteredMessage = htmlFilter.filter(null);
    assertNull(filteredMessage);
  }
  
  @Test
  public void testFilter_withEmptyMessage_returnsEmptyString() {
    String filteredMessage = htmlFilter.filter("");
    assertEquals("", filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageHavingNoSpecialCharacters_returnsSameMessage() {
    String message = "This is a normal message without any special characters.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(message, filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageContainingLessThan_returnsFilteredMessage() {
    String message = "This message contains a < sign.";
    String expectedFilteredMessage = "This message contains a &lt; sign.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(expectedFilteredMessage, filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageContainingGreaterThan_returnsFilteredMessage() {
    String message = "This message contains a > sign.";
    String expectedFilteredMessage = "This message contains a &gt; sign.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(expectedFilteredMessage, filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageContainingAmpersand_returnsFilteredMessage() {
    String message = "This message contains an & character.";
    String expectedFilteredMessage = "This message contains an &amp; character.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(expectedFilteredMessage, filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageContainingQuote_returnsFilteredMessage() {
    String message = "This message contains a \" character.";
    String expectedFilteredMessage = "This message contains a &quot; character.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(expectedFilteredMessage, filteredMessage);
  }
  
  @Test
  public void testFilter_withMessageContainingMultipleSpecialCharacters_returnsFilteredMessage() {
    String message = "This message contains <, >, &, and \" characters.";
    String expectedFilteredMessage = "This message contains &lt;, &gt;, &amp;, and &quot; characters.";
    String filteredMessage = htmlFilter.filter(message);
    assertEquals(expectedFilteredMessage, filteredMessage);
  }
  
}