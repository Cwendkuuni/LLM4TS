import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.VariableMap;

public class RenameVarsTest {
    
    private RenameVars renameVars;
    
    @Before
    public void setUp() {
        renameVars = new RenameVars(null, "prefix", false, false, false, null, null, null);
    }
    
    @Test
    public void testProcess() {
        renameVars.process(null, null); 
    }
    
    @Test
    public void testGetVariableMap() {
        VariableMap variableMap = renameVars.getVariableMap();
        assertNotNull(variableMap);
    }
}