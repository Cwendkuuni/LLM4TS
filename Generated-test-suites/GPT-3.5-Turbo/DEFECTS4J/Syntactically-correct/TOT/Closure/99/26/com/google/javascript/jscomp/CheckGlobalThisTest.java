import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckGlobalThisTest {
    
    @Test
    public void shouldReportGlobalThisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
        
        assertEquals(true, shouldReportThis);
    }
    
    @Test
    public void shouldReportGlobalThisWithPropertyAccess() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
        
        assertEquals(true, shouldReportThis);
    }
    
    @Test
    public void shouldNotReportGlobalThisOnLeftSideOfAssignmentInsideConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        NodeTraversal parentTraversal = new NodeTraversal();
        Node parentFunction = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        parentFunction.setJSDocInfo(jsDoc);
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
        
        assertEquals(false, shouldReportThis);
    }

    @Test
    public void shouldNotReportGlobalThisWithPropertyAccessInsideConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        NodeTraversal parentTraversal = new NodeTraversal();
        Node parentFunction = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        parentFunction.setJSDocInfo(jsDoc);
        boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
        
        assertEquals(false, shouldReportThis);
    }
}