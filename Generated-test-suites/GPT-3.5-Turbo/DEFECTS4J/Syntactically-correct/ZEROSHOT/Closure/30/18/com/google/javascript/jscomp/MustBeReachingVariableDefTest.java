import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.parsing.parser.ParserFactory;
import com.google.javascript.jscomp.parsing.parser.Tok;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

    private MustBeReachingVariableDef mustBeReachingVariableDef;

    @Test
    public void testGetDef() throws IOException {
        // Arrange
        String code = "a = 5;";
        Node root = parseCode(code);
        AbstractCompiler compiler = getCompiler();
        Scope scope = new Scope(root);
        ControlFlowGraph<Node> cfg = ControlFlowAnalysis.build(cfg);
        mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, scope, compiler);
        
        // Act
        Node def = mustBeReachingVariableDef.getDef("a", root);
        
        // Assert
        Assert.assertNotNull(def);
    }

    @Test
    public void testDependsOnOuterScopeVars() throws IOException {
        // Arrange
        String code = "a = 5;";
        Node root = parseCode(code);
        AbstractCompiler compiler = getCompiler();
        Scope scope = new Scope(root);
        ControlFlowGraph<Node> cfg = ControlFlowAnalysis.build(cfg);
        mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, scope, compiler);
        
        // Act
        boolean result = mustBeReachingVariableDef.dependsOnOuterScopeVars("a", root);
        
        // Assert
        Assert.assertFalse(result);
    }

    private Node parseCode(String code) {
        ParserFactory parserFactory = new ParserFactory();
        Parser parser = parserFactory.create(Parser.Config.Strict());
        TokenStream tokenStream = parser.parse(ParserInput.fromString(code));
        return parser.parseStatement(tokenStream);
    }

    private AbstractCompiler getCompiler() {
        return new AbstractCompiler();
    }

}