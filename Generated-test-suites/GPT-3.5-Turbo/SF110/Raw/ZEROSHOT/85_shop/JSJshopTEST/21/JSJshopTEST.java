import junit.framework.TestCase;

public class JSJshopTest extends TestCase {
	
	public void testMain() {
		String[] args = {"domainDef-file-name", "problemDef-file-name"};
		JSJshop.main(args);
	}
	
	public void testJSJshop() {
		JSJshop jshop = new JSJshop();
	}
	
	public void testJSJshopStringString() {
		String nameDomainFile = "domainDef-file-name";
		String nameProblemFile = "problemDef-file-name";
		JSJshop jshop = new JSJshop(nameDomainFile, nameProblemFile);
	}
	
	public void testJSJshopStringJSTaskAtom() {
		String nameFile = "nameFile";
		JSTaskAtom pred = new JSTaskAtom();
		JSJshop jshop = new JSJshop(nameFile, pred);
	}
	
	public void testGetTree() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getTree());
	}
	
	public void testGetSolution() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getSolution());
	}
	
	public void testGetAddList() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getAddList());
	}
	
	public void testGetDeleteList() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getDeleteList());
	}
	
	public void testSetFile() {
		String nameFile = "nameFile";
		JSTaskAtom pred = new JSTaskAtom();
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.setFile(nameFile, pred));
	}
	
	public void testTestParser() {
		JSJshop jshop = new JSJshop();
		jshop.testParser();
	}
	
	public void testParserFile() {
		String libraryFile = "libraryFile";
		JSJshop jshop = new JSJshop();
		assertFalse(jshop.parserFile(libraryFile));
	}
	
	public void testGetBufferedReader() {
		String dir = "dir";
		String file = "file";
		JApplet applet = new JApplet();
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getBufferedReader(dir, file));
		assertNotNull(jshop.getBufferedReader(dir, file, applet));
	}
	
	public void testGetAppletURL() {
		String file = "file";
		JApplet applet = new JApplet();
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.getAppletURL(file, applet));
	}
	
	public void testProcessToken() {
		JSJshop jshop = new JSJshop();
		StreamTokenizer tokenizer = new StreamTokenizer();
		jshop.processToken(tokenizer);
	}
	
	public void testDom() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.dom());
	}
	
	public void testProb() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.prob());
	}
	
	public void testSol() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.sol());
	}
	
	public void testTree() {
		JSJshop jshop = new JSJshop();
		assertNotNull(jshop.tree());
	}
}