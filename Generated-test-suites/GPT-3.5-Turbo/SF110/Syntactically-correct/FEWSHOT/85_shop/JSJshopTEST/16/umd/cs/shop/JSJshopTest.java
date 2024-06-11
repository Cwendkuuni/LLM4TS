package umd.cs.shop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class JSJshopTest {
    private JSJshop jsjShop;

    @Before
    public void setUp() {
        jsjShop = new JSJshop();
    }

    @Test
    public void testConstructor() {
        assertNotNull(jsjShop.probSet);
        assertNull(jsjShop.tree);
    }

    @Test
    public void testMain() {
        String[] args1 = {"domainDef-file-name", "problemDef-file-name"};
        // Test when args.length < 2
        JSJshop.main(args1);
        // Test when args.length > 2
        String[] args2 = {"domainDef-file-name", "problemDef-file-name", "number-of-plans"};
        JSJshop.main(args2);
        // Test when args.length > 4
        String[] args3 = {"domainDef-file-name", "problemDef-file-name", "number-of-plans", "extra-arg"};
        JSJshop.main(args3);
    }

    @Test
    public void testSetFile() {
        String nameFile = "nameFile";
        JSTaskAtom pred = new JSTaskAtom();
        
        jsjShop.setFile(nameFile, pred);
        
        assertNotNull(jsjShop.tree);
        assertNotNull(jsjShop.solution);
    }

    @Test
    public void testGetTree() {
        JSJshopNode tree = jsjShop.getTree();
        assertNull(tree);
        
        jsjShop.tree = new JSJshopNode();
        tree = jsjShop.getTree();
        assertNotNull(tree);
    }

    @Test
    public void testGetSolution() {
        JSPairPlanTSListNodes solution = jsjShop.getSolution();
        assertNull(solution);
        
        jsjShop.solution = new JSPairPlanTSListNodes();
        solution = jsjShop.getSolution();
        assertNotNull(solution);
    }

    @Test
    public void testGetAddList() {
        JSListLogicalAtoms addList = jsjShop.getAddList();
        assertNull(addList);
        
        JSPlan plan = new JSPlan();
        plan.assignFailure();
        JSPairPlanTState pair = new JSPairPlanTState();
        JSTState state = new JSTState();
        pair.planS(plan).tState(state);
        jsjShop.solution = new JSPairPlanTSListNodes(pair, new JSListLogicalAtoms());
        addList = jsjShop.getAddList();
        assertNull(addList);
    }

    @Test
    public void testGetDeleteList() {
        JSListLogicalAtoms deleteList = jsjShop.getDeleteList();
        assertNull(deleteList);
        
        JSPlan plan = new JSPlan();
        plan.assignFailure();
        JSPairPlanTState pair = new JSPairPlanTState();
        JSTState state = new JSTState();
        pair.planS(plan).tState(state);
        jsjShop.solution = new JSPairPlanTSListNodes(pair, new JSListLogicalAtoms());
        deleteList = jsjShop.getDeleteList();
        assertNull(deleteList);
    }

    @Test
    public void testParserFile() {
        String libraryFile = "libraryFile";

        // Test when file is parsed successfully
        assertEquals(true, jsjShop.parserFile(libraryFile));
    }
    
    @Test
    public void testProcessToken() {
        StreamTokenizer tokenizer = new StreamTokenizer();
        
        try {
            tokenizer.pushBack();
            tokenizer.sval = "defDomain";
            jsjShop.processToken(tokenizer);
            
            tokenizer.pushBack();
            tokenizer.sval = "defProblem";
            jsjShop.processToken(tokenizer);

            tokenizer.pushBack();
            tokenizer.sval = "invalidToken";
            try {
                jsjShop.processToken(tokenizer);
            } catch (JSParserError e) {
                // Expected error
            }
        } catch (JSParserError e) {
            // Test failed
        }
    }
}