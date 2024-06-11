import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
  
  private Entities entities;
  
  @Before
  public void setUp() {
    entities = new Entities();
  }
  
  @Test
  public void testIsNamedEntity() {
    assertTrue(entities.isNamedEntity("lt")); // typical use case
    assertTrue(entities.isNamedEntity("gt")); // typical use case
    assertTrue(entities.isNamedEntity("amp")); // typical use case
    assertFalse(entities.isNamedEntity("abcd")); // non-existent entity
    assertFalse(entities.isNamedEntity(null)); // null input
  }
  
  @Test
  public void testIsBaseNamedEntity() {
    assertTrue(entities.isBaseNamedEntity("lt")); // typical use case
    assertTrue(entities.isBaseNamedEntity("gt")); // typical use case
    assertTrue(entities.isBaseNamedEntity("amp")); // typical use case
    assertFalse(entities.isBaseNamedEntity("abcd")); // non-existent entity
    assertFalse(entities.isBaseNamedEntity(null)); // null input
  }
  
  @Test
  public void testGetCharacterByName() {
    assertEquals(Character.valueOf('<'), entities.getCharacterByName("lt")); // typical use case
    assertEquals(Character.valueOf('>'), entities.getCharacterByName("gt")); // typical use case
    assertEquals(Character.valueOf('&'), entities.getCharacterByName("amp")); // typical use case
    assertNull(entities.getCharacterByName("abcd")); // non-existent entity
    assertNull(entities.getCharacterByName(null)); // null input
  }
  
  @Test
  public void testEscape() {
    String input = "This is a <b>bold</b> statement";
    String expectedOutput = "This is a &lt;b&gt;bold&lt;/b&gt; statement";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    
    input = "This is an & example &amp; &gt;a";
    expectedOutput = "This is an &amp; example &amp;amp; &gt;a";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    
    input = "This is a > tag";
    expectedOutput = "This is a &gt; tag";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings()));
  }
  
  @Test
  public void testUnescape() {
    String input = "This is an &lt;b&gt;bold&lt;/b&gt; statement";
    String expectedOutput = "This is an <b>bold</b> statement";
    assertEquals(expectedOutput, Entities.unescape(input));
    
    input = "This is an &amp; example &gt;a";
    expectedOutput = "This is an & example >a";
    assertEquals(expectedOutput, Entities.unescape(input));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.unescape(input));
  }
  
  @Test
  public void testUnescapeStrict() {
    String input = "This is an &lt;b&gt;bold&lt;/b statement"; // missing closing ;
    String expectedOutput = "This is an &lt;b&gt;bold&lt;/b statement"; // should not be unescaped
    assertEquals(expectedOutput, Entities.unescape(input, true));
    
    input = "This is an &amp; example >a"; // amp; wrongly placed
    expectedOutput = "This is an &amp; example &gt;a"; // should not be unescaped
    assertEquals(expectedOutput, Entities.unescape(input, true));
    
    input = null; // null input
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.unescape(input, true));
  }
  
  @Test
  public void testEscapeWithAttributeFlag() {
    String input = "This is a <b>bold</b> statement";
    String expectedOutput = "This is a &lt;b&gt;bold&lt;/b&gt; statement";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
    
    input = "This is an & example &amp; &gt;a"; // amp within attribute
    expectedOutput = "This is an &amp; example &amp;amp; &gt;a";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
    
    input = "This is a > tag";
    expectedOutput = "This is a &gt; tag";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
  }
  
  @Test
  public void testEscapeWithWhiteNormalizationFlag() {
    String input = "This is a <b>bold</b> statement";
    String expectedOutput = "This is a <b>bold</b> statement"; // no white space normalization
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, true, false));
    
    input = "This is an & example &gt;a"; // amp within attribute
    expectedOutput = "This is an & example &gt;a"; // no white space normalization
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, true, false));
    
    input = "This is a > tag";
    expectedOutput = "This is a > tag"; // no white space normalization
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, true, false));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, true, false));
  }
  
  @Test
  public void testEscapeWithStripLeadingWhiteFlag() {
    String input = "   This is a <b>bold</b> statement";
    String expectedOutput = "   This is a <b>bold</b> statement"; // leading white space not stripped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, false, true));
    
    input = "   This is an & example &gt;a"; // amp within attribute
    expectedOutput = "   This is an & example &gt;a"; // leading white space not stripped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, false, true));
    
    input = "   This is a > tag";
    expectedOutput = "   This is a > tag"; // leading white space not stripped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, false, true));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), false, false, true));
  }
  
  @Test
  public void testEscapeWithMultipleFlags() {
    String input = "   This is a <b>bold</b> statement";
    String expectedOutput = " This is a &lt;b&gt;bold&lt;/b&gt; statement"; // leading white space stripped, tags escaped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, true, false));
    
    input = "   This is an & example &gt;a"; // amp within attribute
    expectedOutput = " This is an &amp; example &gt;a"; // leading white space stripped, amp within attribute escaped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, true, false));
    
    input = "   This is a > tag";
    expectedOutput = "   This is a &gt; tag"; // leading white space not stripped, tag escaped
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, false, false));
    
    input = null;
    expectedOutput = "";
    assertEquals(expectedOutput, Entities.escape(input, new Document.OutputSettings(), true, true, true)); // all flags set
  }
}