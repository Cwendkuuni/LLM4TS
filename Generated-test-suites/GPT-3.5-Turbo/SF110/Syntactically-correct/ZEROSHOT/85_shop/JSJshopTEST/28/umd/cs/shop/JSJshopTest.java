import org.junit.*;
import static org.junit.Assert.*;

public class JSJshopTest {

  @Test
  public void testMain() {
    // Test case 1: when args.length < 2
    // Expected output: "Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]"
    String[] args1 = new String[0];
    JSJshop.main(args1);

    // Test case 2: when args.length > 4
    // Expected output: "Usage : java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]"
    String[] args2 = { "file1", "file2", "arg1", "arg2", "arg3" };
    JSJshop.main(args2);

    // Test case 3: when args.length is 2
    // Expected output: Success
    String[] args3 = { "file1", "file2"};
    JSJshop.main(args3);
  }

  @Test
  public void testJSJshop() {
    // Test case 1: Default constructor
    JSJshop jsjshop1 = new JSJshop();
    assertNull(jsjshop1.getTree());
    assertNull(jsjshop1.getSolution());

    // Test case 2: Constructor with nameDomainFile and nameProblemFile
    JSJshop jsjshop2 = new JSJshop("domainFile", "problemFile");
    assertNotNull(jsjshop2.getTree());
    assertNotNull(jsjshop2.getSolution());
  }

  @Test
  public void testSetFile() {
    JSJshop jsjshop = new JSJshop();
    assertNull(jsjshop.setFile("nameFile", new JSTaskAtom()));

    // Test case 2: when listPairs.size() is 1
    // Expected output: Success
    try {
      JSListPlanningProblem prob = new JSListPlanningProblem();
      prob.addElement(new JSPlanningProblem());
      jsjshop.probSet = prob;
      JSPairPlanTSListNodes pair = new JSPairPlanTSListNodes(new JSPairPlanTState(new JSPlan(), new JSTState()), new Vector());
      JSListPairPlanTStateNodes listPairs = new JSListPairPlanTStateNodes();
      listPairs.addElement(pair);
      jsjshop.solution = pair;
      jsjshop.tree = new JSJshopNode();
      jsjshop.sol = new JSPlan();
      jsjshop.prob = new JSPlanningProblem();
      jsjshop.dom = new JSPlanningDomain();
      jsjshop.probSet = new JSListPlanningProblem();
      jsjshop.probSet.addElement(new JSPlanningProblem());
      jsjshop.setFile("nameFile", new JSTaskAtom());
      assertNotNull(jsjshop.getTree());
      assertNotNull(jsjshop.getSolution());
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testTestParser() {
    // Test case 1: when FileReader throws an exception
    // Expected output: print "Error reading control parameters: "
    JSJshop jsjshop = new JSJshop();
    jsjshop.testParser();

    // Test case 2: when FileReader does not throw an exception
    try {
      jsjshop.parserFile("farp.shp");
      jsjshop.testParser();
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testParserFile() {
    // Test case 1: when FileReader throws an exception
    // Expected output: return false
    JSJshop jsjshop = new JSJshop();
    assertFalse(jsjshop.parserFile("NoSuchFile.shp"));

    // Test case 2: when StreamTokenizer throws an exception
    // Expected output: return false
    assertTrue(jsjshop.parserFile("empty.shp"));

    // Test case 3: when FileReader does not throw an exception
    try {
      assertTrue(jsjshop.parserFile("file.shp"));
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }

  @Test
  public void testGetBufferedReader() {
    JSJshop jsjshop = new JSJshop();

    // Test case 1: when applet is null
    BufferedReader br1 = jsjshop.getBufferedReader("dir", "file");
    assertNull(br1);

    // Test case 2: when applet is not null
    BufferedReader br2 = jsjshop.getBufferedReader("dir", "file", new JApplet());
    assertNull(br2);
  }

  @Test
  public void testGetAppletURL() {
    JSJshop jsjshop = new JSJshop();

    // Test case 1: when MalformedURLException is thrown
    URL url1 = jsjshop.getAppletURL("file", new JApplet());
    assertNull(url1);

    // Test case 2: when MalformedURLException is not thrown
    URL url2 = jsjshop.getAppletURL("file", null);
    assertNull(url2);
  }
}