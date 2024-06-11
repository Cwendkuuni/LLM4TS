import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class RenameVarsTest {
    
    private RenameVars renameVars;
    
    @Before
    public void setUp() {
        renameVars = new RenameVars(null, "", false, false, false, null, null, null);
    }
    
    @Test
    public void testProcess() {
        //TODO: Implement test case for process method
    }
    
    @Test
    public void testGetNewGlobalName() {
        //TODO: Implement test case for getNewGlobalName method
    }
    
    @Test
    public void testGetNewLocalName() {
        //TODO: Implement test case for getNewLocalName method
    }
    
    @Test
    public void testGetPseudoName() {
        //TODO: Implement test case for getPseudoName method
    }
    
    @Test
    public void testReusePreviouslyUsedVariableMap() {
        //TODO: Implement test case for reusePreviouslyUsedVariableMap method
    }
    
    @Test
    public void testAssignNames() {
        //TODO: Implement test case for assignNames method
    }
    
    @Test
    public void testGetVariableMap() {
        //TODO: Implement test case for getVariableMap method
    }
    
    @Test
    public void testOkToRenameVar() {
        //TODO: Implement test case for okToRenameVar method
    }
    
}