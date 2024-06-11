import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {

  private JSJshop jshop = new JSJshop();

  @Test
  public void testMain() {
    String[] args = {"domainFile", "problemFile"};
    JSJshop.main(args);
    // TODO: Add assertions
  }

  @Test
  public void testJSJshop() {
    JSJshop jshop = new JSJshop();
    // TODO: Add assertions
  }

  @Test
  public void testJSJshopStringString() {
    String nameDomainFile = "domainFile";
    String nameProblemFile = "problemFile";
    JSJshop jshop = new JSJshop(nameDomainFile, nameProblemFile);
    // TODO: Add assertions
  }

  @Test
  public void testJSJshopStringJSTaskAtom() {
    String nameFile = "file";
    JSTaskAtom pred = new JSTaskAtom();
    JSJshop jshop = new JSJshop(nameFile, pred);
    // TODO: Add assertions
  }

  @Test
  public void testGetTree() {
    JSJshopNode tree = jshop.getTree();
    // TODO: Add assertions
  }

  @Test
  public void testGetSolution() {
    JSPairPlanTSListNodes solution = jshop.getSolution();
    // TODO: Add assertions
  }

  @Test
  public void testGetAddList() {
    JSListLogicalAtoms addList = jshop.getAddList();
    // TODO: Add assertions
  }

  @Test
  public void testGetDeleteList() {
    JSListLogicalAtoms deleteList = jshop.getDeleteList();
    // TODO: Add assertions
  }

  @Test
  public void testSetFile() {
    String nameFile = "file";
    JSTaskAtom pred = new JSTaskAtom();
    JSPairPlanTSListNodes result = jshop.setFile(nameFile, pred);
    // TODO: Add assertions
  }

  @Test
  public void testTestParser() {
    jshop.testParser();
    // TODO: Add assertions
  }

  @Test
  public void testParserFile() {
    String libraryFile = "file";
    boolean result = jshop.parserFile(libraryFile);
    // TODO: Add assertions
  }

  @Test
  public void testGetBufferedReader() {
    String dir = ".";
    String file = "file";
    BufferedReader reader = jshop.getBufferedReader(dir, file);
    // TODO: Add assertions
  }

  @Test
  public void testGetBufferedReader2() {
    String dir = ".";
    String file = "file";
    JApplet applet = new JApplet();
    BufferedReader reader = jshop.getBufferedReader(dir, file, applet);
    // TODO: Add assertions
  }

  @Test
  public void testGetAppletURL() {
    String file = "file";
    JApplet applet = new JApplet();
    URL url = jshop.getAppletURL(file, applet);
    // TODO: Add assertions
  }

  @Test
  public void testProcessToken() {
    StreamTokenizer tokenizer = new StreamTokenizer();
    jshop.processToken(tokenizer);
    // TODO: Add assertions
  }

  @Test
  public void testDom() {
    JSPlanningDomain domain = jshop.dom();
    // TODO: Add assertions
  }

  @Test
  public void testProb() {
    JSPlanningProblem problem = jshop.prob();
    // TODO: Add assertions
  }

  @Test
  public void testSol() {
    JSPlan plan = jshop.sol();
    // TODO: Add assertions
  }

  @Test
  public void testTree() {
    JSJshopNode node = jshop.tree();
    // TODO: Add assertions
  }

}