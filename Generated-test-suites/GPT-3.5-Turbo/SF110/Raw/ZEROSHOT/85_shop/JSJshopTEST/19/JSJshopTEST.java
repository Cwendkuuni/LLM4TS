import static org.junit.Assert.*;
import org.junit.Test;
import umd.cs.shop.JSJshop;

public class JSJshopTest {

  @Test
  public void testMain() {
    String[] args1 = {"domainDef-file-name", "problemDef-file-name"};
    JSJshop.main(args1);
    String[] args2 = {"domainDef-file-name", "problemDef-file-name", "one"};
    JSJshop.main(args2);
    String[] args3 = {"domainDef-file-name", "problemDef-file-name", "all"};
    JSJshop.main(args3);
    String[] args4 = {"domainDef-file-name", "problemDef-file-name", "2"};
    JSJshop.main(args4);
  }

  @Test
  public void testConstructor1() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getTree());
  }

  @Test
  public void testConstructor2() {
    String nameDomainFile = "domainDef-file-name";
    String nameProblemFile = "problemDef-file-name";
    JSJshop jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
    assertNotNull(jsjshop.getTree());
    assertNotNull(jsjshop.getSolution());
  }

  @Test
  public void testConstructor3() {
    String nameFile = "file-name";
    JSTaskAtom pred = new JSTaskAtom();
    JSJshop jsjshop = new JSJshop(nameFile, pred);
    assertNotNull(jsjshop.getTree());
    assertNotNull(jsjshop.getSolution());
  }

  @Test
  public void testGetTree() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getTree());
  }

  @Test
  public void testGetSolution() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getSolution());
  }

  @Test
  public void testGetAddList() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getAddList());
  }

  @Test
  public void testGetDeleteList() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getDeleteList());
  }

  @Test
  public void testSetFile() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.setFile("nameFile", new JSTaskAtom()));
  }

  @Test
  public void testTestParser() {
    JSJshop jsjshop = new JSJshop();
    jsjshop.testParser();
  }

  @Test
  public void testParserFile() {
    JSJshop jsjshop = new JSJshop();
    assertFalse(jsjshop.parserFile("libraryFile"));
  }

  @Test
  public void testGetBufferedReader() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getBufferedReader("dir", "file"));
    assertNull(jsjshop.getBufferedReader("dir", "file", new JApplet()));
  }

  @Test
  public void testGetAppletURL() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.getAppletURL("file", new JApplet()));
  }

  @Test
  public void testProcessToken() {
    JSJshop jsjshop = new JSJshop();
    assertFalse(jsjshop.processToken(null));
  }

  @Test
  public void testDom() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.dom());
  }

  @Test
  public void testProb() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.prob());
  }

  @Test
  public void testSol() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.sol());
  }

  @Test
  public void testTree() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.tree());
  }
}