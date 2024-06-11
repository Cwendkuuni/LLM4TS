
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
    
    private AbstractCompiler compiler;
    private CheckLevel level;
    private CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }
    
    @After
    public void tearDown() {
        compiler = null;
        level = null;
        checkGlobalThis = null;
    }
    
    // Test cases go here
    
}
