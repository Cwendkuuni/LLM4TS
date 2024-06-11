import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class ScopedAliasesTest {
    @Test
    public void testScopedAliases() {
        // Basic test case
        AbstractCompiler compiler = new AbstractCompiler();
        PreprocessorSymbolTable preprocessorSymbolTable = null;
        AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
        HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

        Node root = createTestNode("function test() { var x = goog.x; goog.scope(function() { var y = goog.y; }); x = y; }");
        scopedAliases.process(null, root);

        Node testNode = root.getFirstChild();
        Node scopeNode = testNode.getChildAtIndex(1);
        Node functionNode = scopeNode.getLastChild();

        assertNull(findVariableByName("x", testNode));
        assertNull(findVariableByName("y", testNode));

        assertSame(testNode.getChildAtIndex(1), findVariableByName("goog", testNode).getNode());
        assertSame(functionNode.getFirstChild(), findVariableByName("goog", functionNode).getNode());
        assertSame(findVariableByName("y", functionNode), findVariableByName("y", testNode));

        // Test for improper use of goog.scope
        Node improperUsageNode = createTestNode("function test() { goog.scope(function() { goog.scope(function() {}); }); }");
        scopedAliases.process(null, improperUsageNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_USED_IMPROPERLY", compiler.getDiagnostics().get(0).getType().name());

        // Test for google.scope with wrong number of parameters
        Node wrongParameterNode = createTestNode("function test() { goog.scope(); }");
        scopedAliases.process(null, wrongParameterNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS", compiler.getDiagnostics().get(0).getType().name());

        // Test for google.scope referencing 'this'
        Node thisReferenceNode = createTestNode("function test() { goog.scope(function() { this.x = 1; }); }");
        scopedAliases.process(null, thisReferenceNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_REFERENCES_THIS", compiler.getDiagnostics().get(0).getType().name());

        // Test for google.scope using 'return' statement
        Node returnStatementNode = createTestNode("function test() { goog.scope(function() { return 1; }); }");
        scopedAliases.process(null, returnStatementNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_USES_RETURN", compiler.getDiagnostics().get(0).getType().name());

        // Test for google.scope using 'throw' statement
        Node throwStatementNode = createTestNode("function test() { goog.scope(function() { throw new Error(); }); }");
        scopedAliases.process(null, throwStatementNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_USES_THROW", compiler.getDiagnostics().get(0).getType().name());

        // Test for alias redefinition
        Node aliasRedefinitionNode = createTestNode("function test() { goog.scope(function() { var x = goog.x; var x = goog.y; }); }");
        scopedAliases.process(null, aliasRedefinitionNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", compiler.getDiagnostics().get(0).getType().name());

        // Test if local variables are not in goog.scope and not aliases
        Node nonAliasLocalNode = createTestNode("function test() { goog.scope(function() { var x = 1; }); var y = x; }");
        scopedAliases.process(null, nonAliasLocalNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_NON_ALIAS_LOCAL", compiler.getDiagnostics().get(0).getType().name());

        // Test if alias is assigned a value twice
        Node aliasAssignmentTwiceNode = createTestNode("function test() { goog.scope(function() { var x = goog.x; goog.x = goog.y; }); }");
        scopedAliases.process(null, aliasAssignmentTwiceNode);
        assertEquals(1, compiler.getDiagnostics().size());
        assertEquals("JSC_GOOG_SCOPE_ALIAS_REDEFINED", compiler.getDiagnostics().get(0).getType().name());
    }

    private Node createTestNode(String code) {
        SourcePosition sourcePosition = new SourcePosition();
        Node root = new Node_Script(code, null, sourcePosition, null);
        return root;
    }

    private Node findVariableByName(String name, Node parent) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
            if (child.isVar()) {
                Var variable = child.asVar();
                if (name.equals(variable.getName())) {
                    return variable.getNode();
                }
            }
            Node foundNode = findVariableByName(name, child);
            if (foundNode != null) {
                return foundNode;
            }
        }
        return null;
    }
}