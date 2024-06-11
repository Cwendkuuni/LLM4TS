import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;

public class JSJshopTest {
    JSJshop jsjshop;
    
    @Before
    public void setup() {
        jsjshop = new JSJshop();
    }
    
    @Test
    public void testMain() {
        String[] args = new String[]{"domainDef-file", "problemDef-file", "one"};
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        JSJshop.main(args);
        
        String expectedOutput = "Usage :\n" +
            " java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n" +
            "[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testJSJshop() {
        assertNotNull(jsjshop);
        assertNull(jsjshop.dom);
        assertNull(jsjshop.prob);
        assertNull(jsjshop.probSet);
        assertNull(jsjshop.sol);
        assertNull(jsjshop.tree);
        assertNull(jsjshop.solution);
    }
    
    @Test
    public void testJSJshopStringString() {
        String nameDomainFile = "domainDef-file";
        String nameProblemFile = "problemDef-file";
        jsjshop = new JSJshop(nameDomainFile, nameProblemFile);
        
        assertNotNull(jsjshop.dom);
        assertNotNull(jsjshop.prob);
        assertNotNull(jsjshop.probSet);
        assertNotNull(jsjshop.sol);
        assertNotNull(jsjshop.tree);
        assertNotNull(jsjshop.solution);
    }
    
    @Test
    public void testJSJshopStringJSTaskAtom() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        jsjshop = new JSJshop(nameFile, pred);
        
        assertNotNull(jsjshop.dom);
        assertNotNull(jsjshop.prob);
        assertNotNull(jsjshop.probSet);
        assertNotNull(jsjshop.sol);
        assertNull(jsjshop.tree);
        assertNotNull(jsjshop.solution);
    }
    
    @Test
    public void testGetTree() {
        assertNull(jsjshop.getTree());
    }
    
    @Test
    public void testGetSolution() {
        assertNull(jsjshop.getSolution());
    }
    
    @Test
    public void testGetAddList() {
        assertNull(jsjshop.getAddList());
    }
    
    @Test
    public void testGetDeleteList() {
        assertNull(jsjshop.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        String nameFile = "file";
        JSTaskAtom pred = new JSTaskAtom();
        assertNotNull(jsjshop.setFile(nameFile, pred));
    }
    
    @Test
    public void testTestParser() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        
        jsjshop.testParser();
        
        String expectedOutput = "( [` \n";
        assertEquals(expectedOutput, outContent.toString());
    }
    
    @Test
    public void testParserFile() {
        String libraryFile = "library-file";
        assertFalse(jsjshop.parserFile(libraryFile));
    }
    
    @Test
    public void testGetBufferedReader() {
        String dir = ".";
        String file = "file";
        assertNull(jsjshop.getBufferedReader(dir, file));
        
        file = null;
        assertNull(jsjshop.getBufferedReader(dir, file));
    }
    
    @Test
    public void testGetBufferedReaderWithApplet() {
        String file = "file";
        JApplet applet = new JApplet();
        assertNull(jsjshop.getBufferedReader(file, applet));
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader("( defdomain domain )"));
        jsjshop.processToken(tokenizer);
        
        assertNotNull(jsjshop.dom);
        assertNull(jsjshop.prob);
        
        tokenizer = new StreamTokenizer(new StringReader("( defproblem problem )"));
        jsjshop.processToken(tokenizer);
        
        assertNotNull(jsjshop.dom);
        assertNotNull(jsjshop.prob);
        assertNotNull(jsjshop.probSet);
        
        tokenizer = new StreamTokenizer(new StringReader("deftask task"));
        try {
            jsjshop.processToken(tokenizer);
            fail("Expected JSParserError");
        } catch (JSParserError e) {
            // Test passed
        }
    }
    
    @Test
    public void testDom() {
        assertNull(jsjshop.dom());
        
        jsjshop.dom = new JSPlanningDomain();
        assertNotNull(jsjshop.dom());
    }
    
    @Test
    public void testProb() {
        assertNull(jsjshop.prob());
        
        jsjshop.prob = new JSPlanningProblem();
        assertNotNull(jsjshop.prob());
    }
    
    @Test
    public void testSol() {
        assertNull(jsjshop.sol());
        
        jsjshop.sol = new JSPlan();
        assertNotNull(jsjshop.sol());
    }
    
    @Test
    public void testTree() {
        assertNull(jsjshop.tree());
        
        jsjshop.tree = new JSJshopNode();
        assertNotNull(jsjshop.tree());
    }
}