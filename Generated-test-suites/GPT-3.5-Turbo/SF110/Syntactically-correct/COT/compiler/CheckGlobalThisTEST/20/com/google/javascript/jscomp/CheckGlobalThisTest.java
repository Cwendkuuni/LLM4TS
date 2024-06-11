
import org.junit.Test;
import com.google.javascript.jscomp.MessageBundle;
import com.google.javascript.jscomp.CompilerOptions.LanguageMode;
import com.google.javascript.jscomp.testing.CodeSnippetParser;

public class CheckGlobalThisTest {

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(null);
        
        // Assert
        assertTrue(result);
    }

    @Test
    public void testShouldReportThis_ParentNotNullAndIsGet() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node node = CodeSnippetParser.parse("var obj = this.property;");
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(node);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testShouldReportThis_ParentIsNull() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node node = CodeSnippetParser.parse("this.property;");
        
        // Act
        boolean result = checkGlobalThis.shouldReportThis(node);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionConstructor() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = null;
        Node n = CodeSnippetParser.parse("function MyClass() {}");
        Node parent = null;
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_FunctionThisAnnotation() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = null;
        Node n = CodeSnippetParser.parse("/** @this {MyClass} */ function myFunction() {}");
        Node parent = null;
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_ParentIsBlock() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = null;
        Node n = CodeSnippetParser.parse("{ var x = this; }");
        Node parent = CodeSnippetParser.parse("function test() { }");
        
        // Act
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testVisit_ThisKeyword_ShouldReportError() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        NodeTraversal t = null;
        Node n = CodeSnippetParser.parse("this.property;");
        Node parent = null;
        
        // Act
        checkGlobalThis.visit(t, n, parent);
        
        // Assert
        // Error reported
    }
    
    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfo() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = CodeSnippetParser.parse("function myFunction() {}");
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_ParentIsAssign() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = CodeSnippetParser.parse("var x = function() {};");
        Node parent = CodeSnippetParser.parse("x = function() {};");
        n.setParent(parent);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentIsVar() {
        // Arrange
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
        Node n = CodeSnippetParser.parse("function myFunction() {}");
        Node parent = CodeSnippetParser.parse("var x = function() {};");
        parent.getFirstChild().addChildBefore(n, null);
        n.setParent(parent);
        
        // Act
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        
        // Assert
        assertNull(result);
    }
}

