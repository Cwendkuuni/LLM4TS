import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DevirtualizePrototypeMethodsTest {

    private AbstractCompiler compiler;
    private DevirtualizePrototypeMethods devirtualize;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        devirtualize = new DevirtualizePrototypeMethods(compiler);
    }

    @Test
    public void testIsCall() {
        Node node = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.CALL, node);

        UseSite site = new UseSite(node, parent);
        boolean result = DevirtualizePrototypeMethods.isCall(site);

        assertTrue(result);
    }

    @Test
    public void testIsCall_returnsFalse() {
        Node node = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.GETPROP, node);

        UseSite site = new UseSite(node, parent);
        boolean result = DevirtualizePrototypeMethods.isCall(site);

        assertFalse(result);
    }

    @Test
    public void testIsPrototypeMethodDefinition() {
        Node node = new Node(Token.GETPROP, Node.newString("prototype"), Node.newString("method"));
        Node parent = new Node(Token.GETELEM, Node.newString("foo"), node);

        boolean result = DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node);

        assertTrue(result);
    }

    @Test
    public void testIsPrototypeMethodDefinition_returnsFalse() {
        Node node = new Node(Token.GETPROP, Node.newString("bar"), Node.newString("method"));
        Node parent = new Node(Token.GETELEM, Node.newString("foo"), node);

        boolean result = DevirtualizePrototypeMethods.isPrototypeMethodDefinition(node);

        assertFalse(result);
    }

    @Test
    public void testGetRewrittenMethodName() {
        String originalMethodName = "method";
        String expectedResult = "JSCompiler_StaticMethods_method";

        String result = DevirtualizePrototypeMethods.getRewrittenMethodName(originalMethodName);

        assertEquals(expectedResult, result);
    }

    @Test
    public void testRewriteDefinitionIfEligible() {
        DefinitionSite definitionSite = new DefinitionSite();
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);

        DevirtualizePrototypeMethods.rewriteDefinitionIfEligible(definitionSite, defFinder);

        // verify the behavior of the method when the definition is not eligible
    }

    @Test
    public void testRewriteCallSites() {
        SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
        Definition definition = new Definition();
        String newMethodName = "JSCompiler_StaticMethods_method";
        
        DevirtualizePrototypeMethods.rewriteCallSites(defFinder, definition, newMethodName);

        // verify the behavior of the method
    }

    @Test
    public void testRewriteDefinition() {
        Node node = new Node(Token.GETPROP, Node.newString("prototype"), Node.newString("method"));
        String newMethodName = "JSCompiler_StaticMethods_method";

        DevirtualizePrototypeMethods.rewriteDefinition(node, newMethodName);

        // verify the behavior of the method
    }

    @Test
    public void testFixFunctionType() {
        Node functionNode = new Node(Token.FUNCTION);
        CompilerInput input = new CompilerInput("input.js");
        CompilerOptions options = new CompilerOptions();
        AbstractCompiler compiler = new AbstractCompiler(new PrintStream(new ByteArrayOutputStream()));

        DevirtualizePrototypeMethods.fixFunctionType(functionNode, input, options, compiler);

        // verify the behavior of the method
    }

    @Test
    public void testReplaceReferencesToThis() {
        Node node = new Node(Token.NAME, "this");
        String name = "self";

        DevirtualizePrototypeMethods.replaceReferencesToThis(node, name);

        // verify the behavior of the method
    }
}