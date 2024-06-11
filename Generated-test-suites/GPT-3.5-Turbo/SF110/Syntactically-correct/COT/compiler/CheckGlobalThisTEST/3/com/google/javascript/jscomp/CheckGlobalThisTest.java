import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testCheckGlobalThisConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
        assertNotNull(checkGlobalThis);
    }
    
    @Test
    public void testShouldTraverse() {
        // test case for shouldTraverse method
        // write your test case here
    }

    @Test
    public void testVisit() {
        // test case for visit method
        // write your test case here
    }

    @Test
    public void testShouldReportThis() {
        // test case for shouldReportThis method
        // write your test case here
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // test case for getFunctionJsDocInfo method
        // write your test case here
    }
}