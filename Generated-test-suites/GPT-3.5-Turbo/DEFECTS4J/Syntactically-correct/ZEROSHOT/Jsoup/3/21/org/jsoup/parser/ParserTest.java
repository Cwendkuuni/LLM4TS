import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {
  
  private static final String TEST_HTML = "<html><head><title>Test</title></head><body><h1>Heading 1</h1><p>Paragraph 1</p></body></html>";
  private Document doc;
  
  @Before
  public void setUp() {
    doc = Jsoup.parse(TEST_HTML);
  }
  
  @Test
  public void testParse() {
    String expectedTitle = "Test";
    String actualTitle = doc.title();
    Assert.assertEquals(expectedTitle, actualTitle);
    
    String expectedBodyText = "Heading 1 Paragraph 1";
    String actualBodyText = doc.body().text();
    Assert.assertEquals(expectedBodyText, actualBodyText);
  }
  
  @Test
  public void testParseBodyFragment() {
    String expectedBodyText = "Heading 1 Paragraph 1";
    String actualBodyText = doc.body().text();
    Assert.assertEquals(expectedBodyText, actualBodyText);
  }

}