import org.junit.Test;
import static org.junit.Assert.*;

public class TokenQueueTest {
  
  @Test
  public void testIsEmpty() {
    TokenQueue queue = new TokenQueue("");
    assertTrue(queue.isEmpty());
    
    queue = new TokenQueue("data");
    assertFalse(queue.isEmpty());
  }
  
  @Test
  public void testPeek() {
    TokenQueue queue = new TokenQueue("data");
    assertEquals('d', queue.peek());
  }
  
  @Test
  public void testAddFirst() {
    TokenQueue queue = new TokenQueue("ata");
    queue.addFirst('d');
    assertEquals("data", queue.toString());
  }
  
  @Test
  public void testMatches() {
    TokenQueue queue = new TokenQueue("data");
    assertTrue(queue.matches("dat"));
    assertFalse(queue.matches("ata"));
  }
  
  @Test
  public void testMatchesCS() {
    TokenQueue queue = new TokenQueue("data");
    assertTrue(queue.matchesCS("Dat"));
    assertFalse(queue.matchesCS("dat"));
  }
  
  @Test
  public void testMatchesAny() {
    TokenQueue queue = new TokenQueue("data");
    assertTrue(queue.matchesAny("dat", "abc"));
    assertFalse(queue.matchesAny("abc", "def"));
  }
  
  @Test
  public void testMatchesStartTag() {
    TokenQueue queue = new TokenQueue("<p>");
    assertTrue(queue.matchesStartTag());
    assertFalse(queue.matchesStartTag());
  }
  
  @Test
  public void testMatchChomp() {
    TokenQueue queue = new TokenQueue("data");
    assertTrue(queue.matchChomp("dat"));
    assertFalse(queue.matchChomp("ata"));
  }
  
  @Test
  public void testMatchesWhitespace() {
    TokenQueue queue = new TokenQueue(" ");
    assertTrue(queue.matchesWhitespace());
    assertFalse(queue.matchesWhitespace());
  }
  
  @Test
  public void testMatchesWord() {
    TokenQueue queue = new TokenQueue("data");
    assertTrue(queue.matchesWord());
    assertFalse(queue.matchesWord());
  }
  
  @Test
  public void testAdvance() {
    TokenQueue queue = new TokenQueue("data");
    queue.advance();
    assertEquals('a', queue.peek());
  }
  
  @Test
  public void testConsume() {
    TokenQueue queue = new TokenQueue("data");
    assertEquals('d', queue.consume());
  }
  
  @Test
  public void testConsumeString() {
    TokenQueue queue = new TokenQueue("data");
    queue.consume("dat");
    assertEquals("a", queue.toString());
  }
  
  @Test
  public void testConsumeTo() {
    TokenQueue queue = new TokenQueue("some data");
    assertEquals("some ", queue.consumeTo(" "));
  }
  
  @Test
  public void testConsumeToIgnoreCase() {
    TokenQueue queue = new TokenQueue("some data");
    assertEquals("some ", queue.consumeToIgnoreCase("DaTa"));
  }
  
  @Test
  public void testConsumeToAny() {
    TokenQueue queue = new TokenQueue("some data");
    assertEquals("some", queue.consumeToAny("d", " "));
  }
  
  @Test
  public void testChompTo() {
    TokenQueue queue = new TokenQueue("some data");
    assertEquals("some ", queue.chompTo(" "));
  }
  
  @Test
  public void testChompToIgnoreCase() {
    TokenQueue queue = new TokenQueue("some data");
    assertEquals("some ", queue.chompToIgnoreCase("DaTa"));
  }
  
  @Test
  public void testChompBalanced() {
    TokenQueue queue = new TokenQueue("(one (two) three) four");
    assertEquals("(one (two) three)", queue.chompBalanced('(', ')'));
  }
  
  @Test
  public void testUnescape() {
    assertEquals("foo\\bar", TokenQueue.unescape("foo\\\\bar"));
    assertEquals("foo\\'bar", TokenQueue.unescape("foo\\'bar"));
  }
  
  @Test
  public void testConsumeWhitespace() {
    TokenQueue queue = new TokenQueue("  data");
    assertTrue(queue.consumeWhitespace());
    assertFalse(queue.consumeWhitespace());
  }
  
  @Test
  public void testConsumeWord() {
    TokenQueue queue = new TokenQueue("word");
    assertEquals("word", queue.consumeWord());
  }
  
  @Test
  public void testConsumeTagName() {
    TokenQueue queue = new TokenQueue("tag-name");
    assertEquals("tag-name", queue.consumeTagName());
  }
  
  @Test
  public void testConsumeElementSelector() {
    TokenQueue queue = new TokenQueue("element-selector");
    assertEquals("element-selector", queue.consumeElementSelector());
  }
  
  @Test
  public void testConsumeCssIdentifier() {
    TokenQueue queue = new TokenQueue("css-identifier");
    assertEquals("css-identifier", queue.consumeCssIdentifier());
  }
  
  @Test
  public void testConsumeAttributeKey() {
    TokenQueue queue = new TokenQueue("attribute:key");
    assertEquals("attribute:key", queue.consumeAttributeKey());
  }
  
  @Test
  public void testRemainder() {
    TokenQueue queue = new TokenQueue("data");
    assertEquals("data", queue.remainder());
  }
}