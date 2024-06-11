
package umd.cs.shop;

import static org.junit.Assert.*;
import org.junit.Test;

public class JSJshopTest {
    
    @Test
    public void testMain() {
        // Test case 1
        String[] args1 = {"domainFile", "problemFile"};
        JSJshop.main(args1);
        
        // Test case 2
        String[] args2 = {"domainFile", "problemFile", "one"};
        JSJshop.main(args2);
        
        // Test case 3
        String[] args3 = {"domainFile", "problemFile", "all"};
        JSJshop.main(args3);
        
        // Test case 4
        String[] args4 = {"domainFile"};
        JSJshop.main(args4);
        
        // Test case 5
        String[] args5 = {"domainFile", "problemFile", "invalid"};
        JSJshop.main(args5);
        
        // Test case 6
        String[] args6 = {};
        JSJshop.main(args6);
    }
    
    @Test
    public void testConstructor() {
        // Test case 1
        JSJshop j = new JSJshop();
        assertNull(j.probSet);
        assertNull(j.tree);
        
        // Test case 2
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.probSet);
        assertNotNull(j2.tree);
        
        // Test case 3
        JSJshop j3 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j3.probSet);
        assertNotNull(j3.tree);
        
        // Test case 4
        JSJshop j4 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j4.probSet);
        assertNotNull(j4.tree);
    }
    
    @Test
    public void testGetTree() {
        JSJshop j = new JSJshop();
        assertNull(j.getTree());
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.getTree());
    }
    
    @Test
    public void testGetSolution() {
        JSJshop j = new JSJshop();
        assertNull(j.getSolution());
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.getSolution());
    }
    
    @Test
    public void testGetAddList() {
        JSJshop j = new JSJshop();
        assertNull(j.getAddList());
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.getAddList());
    }
    
    @Test
    public void testGetDeleteList() {
        JSJshop j = new JSJshop();
        assertNull(j.getDeleteList());
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.getDeleteList());
    }
    
    @Test
    public void testSetFile() {
        JSJshop j = new JSJshop();
        assertNull(j.setFile("nameFile", new JSTaskAtom()));
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertNotNull(j2.setFile("nameFile", new JSTaskAtom()));
    }
    
    @Test
    public void testParserFile() {
        JSJshop j = new JSJshop();
        assertFalse(j.parserFile("libraryFile"));
        
        JSJshop j2 = new JSJshop("domainFile", "problemFile");
        assertTrue(j2.parserFile("libraryFile"));
    }
}
