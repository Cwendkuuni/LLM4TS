import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.DefinitionsRemover;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DevirtualizePrototypeMethodsTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private Node externs;

    @Mock
    private Node root;

    @Mock
    private SimpleDefinitionFinder defFinder;

    @Mock
    private DefinitionSite defSite;

    @Mock
    private Definition definition;

    @Mock
    private JSModule definitionModule;

    @Mock
    private JSModuleGraph moduleGraph;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(compiler.getModuleGraph()).thenReturn(moduleGraph);
        when(defSite.definition).thenReturn(definition);
        when(defSite.module).thenReturn(definitionModule);
    }

    @Test
    public void process_callsDefFinderProcess() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);

        devirtualizePrototypeMethods.process(externs, root);

        verify(defFinder).process(externs, root);
    }

    @Test
    public void rewriteDefinitionIfEligible_callsIsEligibleDefinition() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);

        devirtualizePrototypeMethods.rewriteDefinitionIfEligible(defSite, defFinder);

        verify(devirtualizePrototypeMethods).isEligibleDefinition(defFinder, defSite);
    }

    @Test
    public void isEligibleDefinition_returnsFalseForInvalidDefinition() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);

        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, defSite);

        assertFalse(result);
    }

    @Test
    public void isEligibleDefinition_returnsFalseForUsedMethod() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);
        when(defFinder.getUseSites(definition)).thenReturn(Lists.newArrayList(new UseSite()));

        boolean result = devirtualizePrototypeMethods.isEligibleDefinition(defFinder, defSite);

        assertFalse(result);
    }

    @Test
    public void rewriteCallSites_replacesCallSiteNode() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);
        Collection<UseSite> useSites = Lists.newArrayList(new UseSite());
        when(defFinder.getUseSites(definition)).thenReturn(useSites);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        UseSite site = mock(UseSite.class);
        site.node = node;

        devirtualizePrototypeMethods.rewriteCallSites(defFinder, definition, "newMethodName");

        verify(parent).replaceChild(node, node.getFirstChild());
        verify(parent).addChildToFront(Node.newString(Token.NAME, "newMethodName"));
        verify(compiler).reportCodeChange();
    }

    @Test
    public void rewriteDefinition_replacesDefinitionNode() {
        DevirtualizePrototypeMethods devirtualizePrototypeMethods =
                new DevirtualizePrototypeMethods(compiler);
        Node node = mock(Node.class);
        Node parent = mock(Node.class);
        Node expr = mock(Node.class);
        Node block = mock(Node.class);
        Node functionNode = mock(Node.class);
        Node argList = mock(Node.class);
        Node body = mock(Node.class);
        Node newNameNode = mock(Node.class);
        when(parent.getLastChild()).thenReturn(functionNode);
        when(expr.getParent()).thenReturn(block);
        when(functionNode.getFirstChild().getNext()).thenReturn(argList);
        when(functionNode.getLastChild()).thenReturn(body);

        devirtualizePrototypeMethods.rewriteDefinition(node, "newMethodName");

        verify(parent).removeChild(functionNode);
        verify(newNameNode).addChildToFront(functionNode);
        verify(block).replaceChild(expr, new Node(Token.VAR, newNameNode));
        verify(argList).addChildToFront(Node.newString(Token.NAME, "newMethodName$self"));
        verify(compiler).reportCodeChange();
    }
}