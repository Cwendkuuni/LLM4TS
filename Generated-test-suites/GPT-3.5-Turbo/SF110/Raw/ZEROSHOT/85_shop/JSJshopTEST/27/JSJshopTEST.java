import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {

	@Test
	public void testMain() {
		String[] args = {"domainDef-file-name", "problemDef-file-name"};
		JSJshop.main(args);
	}
	
	@Test
	public void testJSJshop() {
		JSJshop shop = new JSJshop();
		assertNull(shop.getTree());
		assertNull(shop.getSolution());
		assertNull(shop.getAddList());
		assertNull(shop.getDeleteList());
	}
	
	@Test
	public void testJSJshopStringString() {
		JSJshop shop = new JSJshop("nameDomainFile", "nameProblemFile");
		assertNotNull(shop.getTree());
		assertNotNull(shop.getSolution());
		assertNotNull(shop.getAddList());
		assertNotNull(shop.getDeleteList());
	}
	
	@Test
	public void testJSJshopStringJSTaskAtom() {
		JSTaskAtom pred = new JSTaskAtom();
		JSJshop shop = new JSJshop("nameFile", pred);
		assertNotNull(shop.getTree());
		assertNotNull(shop.getSolution());
		assertNotNull(shop.getAddList());
		assertNotNull(shop.getDeleteList());
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
		JSJshop shop = new JSJshop();
		shop.setFile("nameFile", new JSTaskAtom());
		assertNotNull(shop.getTree());
		assertNotNull(shop.getSolution());
	}
	
	@Test
	public void testTestParser() {
		JSJshop shop = new JSJshop();
		shop.testParser();
	}
	
	@Test
	public void testParserFile() {
		JSJshop shop = new JSJshop();
		assertTrue(shop.parserFile("libraryFile"));
	}
	
	@Test
	public void testGetBufferedReader() {
		JSJshop shop = new JSJshop();
		assertNull(shop.getBufferedReader("dir", "file"));
		assertNull(shop.getBufferedReader("dir", "file", new JApplet()));
	}
	
	@Test
	public void testGetAppletURL() {
		JSJshop shop = new JSJshop();
		assertNotNull(shop.getAppletURL("file", new JApplet()));
	}
	
	@Test
	public void testProcessToken() {
		JSJshop shop = new JSJshop();
		shop.processToken(new StreamTokenizer());
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