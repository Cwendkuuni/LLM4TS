
import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

  @Test
  public void testMain() {
    JSJshop.main(new String[]{"domainDef-file-name", "problemDef-file-name"});
  }

  @Test
  public void testJSJshop() {
    JSJshop shop = new JSJshop();
    assertNotNull(shop.probSet);
    assertNull(shop.tree);
  }

  @Test
  public void testJSJshopStringString() {
    JSJshop shop = new JSJshop("domainDef-file-name", "problemDef-file-name");
    assertNotNull(shop.probSet);
    assertNotNull(shop.tree);
  }

  @Test
  public void testJSJshopStringJSTaskAtom() {
    JSTaskAtom pred = new JSTaskAtom(); // You may need to create a dummy class for this constructor
    JSJshop shop = new JSJshop("nameFile", pred);
    assertNotNull(shop.probSet);
    assertNotNull(shop.tree);
  }

  @Test
  public void testGetTree() {
    JSJshop shop = new JSJshop();
    assertNull(shop.getTree());
  }

  @Test
  public void testGetSolution() {
    JSJshop shop = new JSJshop();
    assertNull(shop.getSolution());
  }

  @Test
  public void testGetAddList() {
    JSJshop shop = new JSJshop();
    assertNull(shop.getAddList());
  }

  @Test
  public void testGetDeleteList() {
    JSJshop shop = new JSJshop();
    assertNull(shop.getDeleteList());
  }

  @Test
  public void testSetFile() {
    JSTaskAtom pred = new JSTaskAtom(); // You may need to create a dummy class for this constructor
    JSJshop shop = new JSJshop();
    JSPairPlanTSListNodes result = shop.setFile("nameFile", pred);
    assertNotNull(result);
    assertNotNull(shop.solution);
    assertNotNull(shop.tree);
  }

  @Test
  public void testTestParser() {
    JSJshop shop = new JSJshop();
    shop.testParser();
  }

  @Test
  public void testParserFile() {
    JSJshop shop = new JSJshop();
    boolean result = shop.parserFile("libraryFile");
    assertFalse(result);
  }

  @Test
  public void testGetBufferedReader() {
    JSJshop shop = new JSJshop();
    BufferedReader br = shop.getBufferedReader("dir", "file");
    assertNotNull(br);
  }

  @Test
  public void testGetBufferedReaderWithApplet() {
    JApplet applet = new JApplet(); // You may need to create a dummy class for this constructor
    JSJshop shop = new JSJshop();
    BufferedReader br = shop.getBufferedReader("dir", "file", applet);
    assertNotNull(br);
  }

  @Test
  public void testGetAppletURL() {
    JApplet applet = new JApplet(); // You may need to create a dummy class for this constructor
    JSJshop shop = new JSJshop();
    URL url = shop.getAppletURL("file", applet);
    assertNotNull(url);
  }

  @Test
  public void testProcessToken() {
    JSJshop shop = new JSJshop();
    StreamTokenizer tokenizer = new StreamTokenizer();
    tokenizer.wordChars('%', '%');
    tokenizer.wordChars('/', '/');
    tokenizer.wordChars('-', '-');
    tokenizer.wordChars('<', '>');
    tokenizer.wordChars('=', '=');
    tokenizer.wordChars('.', '.');
    tokenizer.wordChars('?', '?');
    tokenizer.wordChars('+', '+');
    tokenizer.wordChars('&', '&');
    tokenizer.wordChars('*', '*');
    tokenizer.wordChars('|', '|');
    tokenizer.wordChars('(', ')');
    tokenizer.wordChars('[', ']');
    tokenizer.wordChars('_', '_');
    tokenizer.wordChars('!', '!');
    tokenizer.wordChars('`', '`');
    tokenizer.wordChars('<', '<');
    tokenizer.quoteChar('"');
    tokenizer.eolIsSignificant(true);
    tokenizer.ordinaryChar(':');
    tokenizer.ordinaryChar(';');
    tokenizer.ordinaryChar(',');
    tokenizer.ordinaryChar('(');
    tokenizer.ordinaryChar(')');
    tokenizer.ordinaryChar('[');
    tokenizer.ordinaryChar(']');
    tokenizer.slashSlashComments(true);
    tokenizer.slashStarComments(true);
    tokenizer.ordinaryChars(0, ' ');
    tokenizer.ordinaryChar('\'');
    tokenizer.whitespaceChars(0, ' ');
    tokenizer.commentChar('/');
    tokenizer.commentChar('%');
    shop.processToken(tokenizer);
  }

  @Test
  public void testDom() {
    JSJshop shop = new JSJshop();
    assertNull(shop.dom());
  }

  @Test
  public void testProb() {
    JSJshop shop = new JSJshop();
    assertNull(shop.prob());
  }

  @Test
  public void testSol() {
    JSJshop shop = new JSJshop();
    assertNull(shop.sol());
  }

  @Test
  public void testTree() {
    JSJshop shop = new JSJshop();
    assertNull(shop.tree());
  }
}

