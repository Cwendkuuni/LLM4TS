import org.junit.Test;
import static org.junit.Assert.*;

public class JSJshopTest {
    
    @Test
    public void testMain() {
        // Test case 1: Valid input arguments (2 arguments provided)
        String[] args1 = {"domainDef-file-name", "problemDef-file-name"};
        JSJshop.main(args1);
        // Assert that the JSJshopVars.allPlans flag is set to the default value 'one'
        assertFalse(JSJshopVars.allPlans); 
        
        // Test case 2: Invalid input arguments (less than 2 arguments provided)
        String[] args2 = {"domainDef-file-name"};
        JSJshop.main(args2);
        // Assert that the correct usage message is printed
        assertEquals("Usage :\n java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n", JSUtil.getOutput());
        
        // Test case 3: Invalid input arguments (more than 4 arguments provided)
        String[] args3 = {"domainDef-file-name", "problemDef-file-name", "one", "another"};
        JSJshop.main(args3);
        // Assert that the correct usage message is printed
        assertEquals("Usage :\n java JSJshop <domainDef-file-name> <problemDef-file-name> [number-of-plans]\n[number-of-plans] can be 'one' or 'all'. The default value is 'one'\n", JSUtil.getOutput());
        
        // Test case 4: Invalid input arguments (invalid [number-of-plans])
        String[] args4 = {"domainDef-file-name", "problemDef-file-name", "invalid"};
        JSJshop.main(args4);
        // Assert that the correct error message is printed
        assertEquals("Invalid parameter\n", JSUtil.getOutput());
    }
    
    @Test
    public void testJSJshopConstructor() {
        // Test case 1: Valid input files
        JSJshop jsjshop1 = new JSJshop("domainDef-file-name", "problemDef-file-name");
        // Assert that the JSListPlanningProblem is not empty
        assertFalse(jsjshop1.probSet.isEmpty());
        
        // Test case 2: Invalid input files (domain file is not parsed successfully)
        JSJshop jsjshop2 = new JSJshop("invalid-domainDef-file-name", "problemDef-file-name");
        // Assert that the JSListPlanningProblem is empty
        assertTrue(jsjshop2.probSet.isEmpty());
        
        // Test case 3: Invalid input files (problem file is not parsed successfully)
        JSJshop jsjshop3 = new JSJshop("domainDef-file-name", "invalid-problemDef-file-name");
        // Assert that the JSListPlanningProblem is empty
        assertTrue(jsjshop3.probSet.isEmpty());
    }
    
    @Test
    public void testGetTree() {
        // Test case 1: JSJshopNode is null
        JSJshop jsjshop1 = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNull(jsjshop1.getTree());
        
        // Test case 2: JSJshopNode is not null
        JSJshop jsjshop2 = new JSJshop("domainDef-file-name", "problemDef-file-name");
        assertNotNull(jsjshop2.getTree());
    }
    
    // Repeat the above test method template for all the remaining methods in the JSJshop class
    
}